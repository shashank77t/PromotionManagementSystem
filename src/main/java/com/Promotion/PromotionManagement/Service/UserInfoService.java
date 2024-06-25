package com.Promotion.PromotionManagement.Service;

import com.Promotion.PromotionManagement.Enum.CategoryType;
import com.Promotion.PromotionManagement.Enum.PromotionType;
import com.Promotion.PromotionManagement.Enum.Role;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Promotion;
import com.Promotion.PromotionManagement.Models.UserBehaviour;
import com.Promotion.PromotionManagement.Models.UserInfo;
import com.Promotion.PromotionManagement.Repository.ProductRepository;
import com.Promotion.PromotionManagement.Repository.PromotionRepository;
import com.Promotion.PromotionManagement.Repository.UserBehaviourRepository;
import com.Promotion.PromotionManagement.Repository.UserInfoRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public  class UserInfoService {
    public static final Logger logger=LoggerFactory.getLogger(UserInfoService.class);
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private UserBehaviourRepository userBehaviourRepository;
    public UserInfo BusinessUserSignUp(UserInfo userInfo)throws UserInfoException{
        if(!userInfo.getRole().equals(Role.BUSINESS_USER)){
            throw new UserInfoException("He is not a BUSINESS_USER");
        }
          userInfo.setUserID(UUID.randomUUID());
        return userInfoRepository.save(userInfo);
    }

    public UserInfo CustomerSignUp(UserInfo userInfo){
        if (userInfo.getRole() !=Role.USER) {
            throw new UserInfoException("He is not a Customer");
        }

        // Generate UUID for the userInfo if not already set

            userInfo.setUserID(UUID.randomUUID());


        // Fetch all promotions
        List<Promotion> promotionList = promotionRepository.findAll();

        // Initialize userBehaviourList if it's empty
        if (userInfo.getUserBehaviourList() == null || userInfo.getUserBehaviourList().isEmpty()) {
            List<UserBehaviour> userBehaviourList = new ArrayList<>();
            for (Promotion promotion : promotionList) {

                UserBehaviour userBehaviour = new UserBehaviour();
                userBehaviour.setUserBehaviourId(UUID.randomUUID());
                userBehaviour.setPromotion(promotion);
                userBehaviour.setPurchaseFrequency(0);
                userBehaviour.setVisitedCount(0);
                userBehaviour.setUserInfo(userInfo); // Ensure the relationship is set

                userBehaviourList.add(userBehaviour);
                logger.info("userBehaviour #################Id{}",userBehaviour.getUserBehaviourId());
                logger.info("userInfo  ####################Id{}",userInfo.getUserID());
            }
            userInfo.setUserBehaviourList(userBehaviourList);
        }

        // Save the userInfo along with the associated userBehaviours
        return userInfoRepository.save(userInfo);



    }

    public List<Promotion> viewPromotionsByCustomer(UUID userInfoID,int page){
        UserInfo userInfo=userInfoRepository.findById(userInfoID).orElse(null);
        if(userInfo==null){
            throw new UserInfoException("Here,The UserInfo ID is not valid");
        }
        if(!userInfo.getRole().equals(Role.USER)){
            throw new UserInfoException("He is not a Customer");
        }
        List<Promotion>list=promotionRepository.findAll();
//        List<UserBehaviour>userBehaviourList=new ArrayList<>();
//        if(userInfo.getUserBehaviourList().size()==0){
//            for(int i=0;i<list.size();i++){
//                UserBehaviour userBehaviour=new UserBehaviour();
//                userBehaviour.setUserBehaviourId(UUID.randomUUID());
//                userBehaviour.setPromotion(list.get(i));
//                userBehaviour.setPurchaseFrequency(0);
//                userBehaviour.setVisitedCount(0);
//            }
//        }
//        userInfo.setUserBehaviourList(userBehaviourList);
 //       userInfoRepository.save(userInfo);
        int size=list.size();
        int pagesize=10;
        logger.info("page number ####### {}",size);
        int totp = (int) Math.ceil((double)size / 10);


        if(page-1==totp){
            pagesize=size%10;
        }
        logger.info("page number ####### {}",pagesize);

        if (page-1 > totp) {
            throw new IllegalArgumentException("Invalid page number. Maximum available page: " + totp);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, pagesize, Sort.by(Sort.Direction.DESC, "visitedCount"));
        Page<UserBehaviour> pagingUser = userBehaviourRepository.findAll(pageRequest);
        list.clear();
        List<Promotion>newPromotions=new ArrayList<>();
        List<UserBehaviour>newUserBehaviour=pagingUser.getContent();
        logger.info("loading promotions in page {}",newPromotions.size());
        for(UserBehaviour u:newUserBehaviour){
            u.setVisitedCount(u.getVisitedCount()+1);
            logger.info("visited count is{}",u.getVisitedCount());
            newPromotions.add(u.getPromotion());
        }

        userBehaviourRepository.saveAll(newUserBehaviour);
        return newPromotions;
    }
    public List<UserBehaviour> getAllUserBehaviourList(UUID id){
        return userBehaviourRepository.findAll();
    }
    public UserInfo getUserBehaviourOnID(UUID id){
        UserInfo u=userInfoRepository.findById(id).orElse(null);
        logger.info("user behaviour{}",u.getUserID());
        return u;
    }
    public List<Promotion> viewPromotionsOnPromotionType(UUID customerID, PromotionType promotionType)throws UserInfoException{
        UserInfo userInfo=userInfoRepository.findById(customerID).orElse(null);
        if(userInfo==null){
            throw new UserInfoException("Here,The UserInfo ID is not valid");
        }
        if(!userInfo.getRole().equals(Role.USER)){
            throw new UserInfoException("He is not a Customer");
        }
        List<Promotion>promotionList=promotionRepository.findAll();
        List<Promotion>categoryPromotions=new ArrayList<>();
        for(Promotion p:promotionList){
            if(p.getPromotionType().equals(promotionType)){
                categoryPromotions.add(p);
            }
        }
        return categoryPromotions;
    }

    public List<Promotion> viewPromotionsOnCategory(UUID customerID,CategoryType categoryType){
        UserInfo userInfo=userInfoRepository.findById(customerID).orElse(null);
        if(userInfo==null){
            throw new UserInfoException("Here,The UserInfo ID is not valid");
        }
        if(!userInfo.getRole().equals(Role.USER)){
            throw new UserInfoException("He is not a Customer");
        }
        List<Promotion>promotionList=promotionRepository.findAll();
        List<Promotion>PromotionsOnProductType=new ArrayList<>();
        for(Promotion p:promotionList){
            if(p.getPromotionType().equals(p.getProduct().getCategoryType())){
                PromotionsOnProductType.add(p);
                logger.info("show product category ###########{}",p.getProduct().getCategoryType());
            }
        }
        return PromotionsOnProductType;
    }






}
