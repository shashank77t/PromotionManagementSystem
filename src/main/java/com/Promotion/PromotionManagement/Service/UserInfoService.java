package com.Promotion.PromotionManagement.Service;

import com.Promotion.PromotionManagement.Enum.CategoryType;
import com.Promotion.PromotionManagement.Enum.PromotionType;
import com.Promotion.PromotionManagement.Enum.Role;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Promotion;
import com.Promotion.PromotionManagement.Models.UserBehaviour;
import com.Promotion.PromotionManagement.Models.UserInfo;
import com.Promotion.PromotionManagement.Repository.PromotionRepository;
import com.Promotion.PromotionManagement.Repository.UserBehaviourRepository;
import com.Promotion.PromotionManagement.Repository.UserInfoRepository;
import com.Promotion.PromotionManagement.PromotionPaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

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

    public String CustomerSignUp(UserInfo userInfo){
        if (userInfo.getRole() !=Role.USER) {
            throw new UserInfoException("He is not a Customer");
        }


            userInfo.setUserID(UUID.randomUUID());
            UserBehaviour userBehaviour=new UserBehaviour();
            userBehaviour.setUserBehaviourId(UUID.randomUUID());
            userInfo.setUserBehaviour(userBehaviour);
            userBehaviour.setUserInfo(userInfo);
            logger.info("userid #################### {}",userInfo.getUserID());
         userBehaviourRepository.save(userBehaviour);



        return "user registered successfully";

    }
    public UserInfo signUp(UserInfo approver)throws UserInfoException{
        if (approver.getRole() !=Role.APPROVER) {
            throw new UserInfoException("He is not a Customer");
        }


        approver.setUserID(UUID.randomUUID());

        logger.info("userid {}",approver.getUserID());
        userInfoRepository.save(approver);



        return approver;

    }

    public List<Promotion> viewPromotionsByCustomer(UUID userInfoID,int page){
        UserInfo userInfo=userInfoRepository.findById(userInfoID).orElse(null);
        UserBehaviour u = userInfo.getUserBehaviour();
        if(userInfo==null){
            throw new UserInfoException("Here,The UserInfo ID is not valid");
        }
        if(!userInfo.getRole().equals(Role.USER)){
            throw new UserInfoException("He is not a Customer");
        }
        List<Promotion>ans=new ArrayList<>();
        Set<CategoryType>st=new HashSet<>();
        List<Promotion> list =promotionRepository.findActivePromotions();

        int size = list.size();
        int pagesize = 10;
        logger.info("page number ####### {}", size);
        int totp = (int) Math.ceil((double) size / 10);


        if (page - 1 == totp) {
            pagesize = size % 10;
        }
        logger.info("page number ####### {}", pagesize);

        if (page - 1 > totp) {
            throw new IllegalArgumentException("Invalid page number. Maximum available page: " + totp);
        }
        if(!userInfo.getUserBehaviour().getPromotionList().isEmpty()){
            for(Promotion p:userInfo.getUserBehaviour().getPromotionList()){
                CategoryType cate=p.getProduct().getCategoryType();
                if(!st.contains(cate)){
                    st.add(cate);
                }
            }
            for (CategoryType category : CategoryType.values()) {
                if(!st.contains(category)){
                    st.add(category);
                }
            }
            for(CategoryType category:st){
                List<Promotion>temp=promotionRepository.findPromotionsByCategoryType(category.toString());
                ans.addAll(temp);
            }
             List<Promotion>requiredPromotions= PromotionPaginationUtil.getPaginatedList(ans, page-1, pagesize);
            for(Promotion p:requiredPromotions){
                if (!u.getPromotionList().contains(p)) {
                    u.getPromotionList().add(p);
                    p.setUserBehaviour(u);
                }
                u.setVisitCount(u.getVisitCount()+1);
                p.setLikes(p.getLikes() + 1);
            }

            userBehaviourRepository.save(u);
            return requiredPromotions;

        } else {

//            PageRequest pageRequest = PageRequest.of(page - 1, pagesize, Sort.by(Sort.Direction.DESC, "likes"));
//            Page<Promotion> pagingUser = promotionRepository.findAll(pageRequest);
//
            List<Promotion>viewPromotions= PromotionPaginationUtil.getPaginatedList(list, page-1, pagesize);
          //  List<Promotion> viewPromotions = pagingUser.getContent();
            logger.info("loading promotions in page {}", viewPromotions.size());

            for (Promotion promotion : viewPromotions) {
                if (!u.getPromotionList().contains(promotion)) {
                    u.getPromotionList().add(promotion);
                    promotion.setUserBehaviour(u);
                }
                promotion.setLikes(promotion.getLikes() + 1);
                u.setVisitCount(u.getVisitCount()+1);
            }

            userBehaviourRepository.save(u);

            return viewPromotions;
        }
    }
    public UserBehaviour userBehaviourOnId(UUID id)throws UserInfoException{
        UserInfo userInfo=userInfoRepository.findById(id).orElse(null);
        UserBehaviour userBehaviour=userInfo.getUserBehaviour();
        if(userBehaviour==null){
            throw new UserInfoException("UserBehaviour is not found");
        }
        return userBehaviour;
    }

    public List<Promotion> viewPromotionsOnPromotionType(UUID customerID, PromotionType promotionType)throws UserInfoException{
        UserInfo userInfo=userInfoRepository.findById(customerID).orElse(null);
        if(userInfo==null){
            throw new UserInfoException("Here,The UserInfo ID is not valid");
        }
        if(!userInfo.getRole().equals(Role.USER)){
            throw new UserInfoException("He is not a Customer");
        }




        List<Promotion>promotionList=promotionRepository.findPromotionsOnPromotionType(promotionType.toString());
        List<Promotion>ans=new ArrayList<>();

        UserBehaviour u=userInfo.getUserBehaviour();
        List<Promotion>categoryPromotions=new ArrayList<>();

        for(Promotion p:promotionList){

                categoryPromotions.add(p);
                if (!u.getPromotionList().contains(p)) {
                    u.getPromotionList().add(p);

                    p.setUserBehaviour(u);
                }
                u.setVisitCount(u.getVisitCount()+1);
                p.setLikes(p.getLikes()+1);

        }
        userBehaviourRepository.save(u);
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

        List<Promotion>promotionList=promotionRepository.promotionOnCategoryType(categoryType.toString());
        UserBehaviour u=userInfo.getUserBehaviour();
        List<Promotion>PromotionsOnProductType=new ArrayList<>();
        for(Promotion p:promotionList){

                p.setLikes(p.getLikes()+1);
                PromotionsOnProductType.add(p);
                if (!u.getPromotionList().contains(p)) {
                    u.getPromotionList().add(p);

                    p.setUserBehaviour(u);
                }
                u.setVisitCount(u.getVisitCount()+1);
              //  logger.info("show product category ###########{}",p.getProduct().getCategoryType());
            }

        userBehaviourRepository.save(u);
        return PromotionsOnProductType;
    }

    public List<UserInfo> getAllUSers(){
        return userInfoRepository.findAll();
    }




}
