package com.Promotion.PromotionManagement.Service;

import com.Promotion.PromotionManagement.Dto.RequestDto.PromotionDto;
import com.Promotion.PromotionManagement.Enum.PromotionType;
import com.Promotion.PromotionManagement.Enum.Role;
import com.Promotion.PromotionManagement.Exceptions.ProductException;
import com.Promotion.PromotionManagement.Exceptions.PromotionException;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.*;
import com.Promotion.PromotionManagement.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
@Service
public class PromotionService {
    private static final Logger logger=LoggerFactory.getLogger(PromotionService.class);
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserBehaviourRepository userBehaviourRepository;
    @Autowired
    private PromotionApprovalRepository promotionApprovalRepository;
    public Promotion createPromotion(PromotionDto promotionDto, UUID business_userId, UUID productID)throws UserInfoException{
        UserInfo userInfo=userInfoRepository.findById(business_userId).orElse(null);
      //  logger.info("promotion object {}",promotion);
        if(userInfo==null){
            throw new UserInfoException("the given id is not valid");
        }
        if(userInfo.getRole()!= Role.BUSINESS_USER){
            throw new UserInfoException("The given id not belongs to a business_user");
        }
        Promotion promotion=new Promotion();
        promotion.setPromotionId(UUID.randomUUID());
        promotion.setPromotionType(promotionDto.getPromotionType());
        promotion.setStartDate(promotionDto.getStartDate());
        promotion.setEndDate(promotionDto.getEndDate());
        promotion.setDiscountRate(promotionDto.getDiscountRate());
        promotion.setIsActive(promotionDto.getIsActive());
        Product product1=productRepository.findById(productID).orElse(null);
        promotion.setProduct(product1);
        promotion.setUserInfo(userInfo);
//        PromotionApproval promotionApproval=new PromotionApproval();
//        promotionApproval.setPromotionApprovalId(UUID.randomUUID());
//        promotion.setPromotionApproval(promotionApproval);
//        promotionApproval.setPromotion(p);
        userInfo.getPromotions().add(promotion);
     //   promotionApprovalRepository.save(promotionApproval);

         userInfoRepository.save(userInfo);
         return promotion;
    }

    public Promotion getPromotionByID(UUID business_userId,UUID promotionID) throws UserInfoException,PromotionException{
        UserInfo userInfo=userInfoRepository.findById(business_userId).orElse(null);
        //  logger.info("promotion object {}",promotion);
        if(userInfo==null){
            throw new UserInfoException("the given id is not valid");
        }
        if(userInfo.getRole()!= Role.BUSINESS_USER){
            throw new UserInfoException("The given id not belongs to a business_user");
        }
        Promotion promotion1=promotionRepository.findById(promotionID).orElse(null);
        if(promotion1==null){
            throw new PromotionException("promotion is not found with this ID");
        }
        logger.info("is promtion is ###### is getting {} ",promotion1.getPromotionType());

        return promotion1;
    }

    public Promotion updatePromotion(Promotion promotion,UUID business_userId) throws UserInfoException,PromotionException{
        UserInfo userInfo=userInfoRepository.findById(business_userId).orElse(null);
        logger.info("promotion object {}",promotion);
        if(userInfo==null){
            throw new UserInfoException("the given id is not valid");
        }
        if(userInfo.getRole()!= Role.BUSINESS_USER){
            throw new UserInfoException("The given id not belongs to a business_user");
        }
        Promotion promotion1=promotionRepository.findById(promotion.getPromotionId()).orElse(null);
        if(promotion1==null){
            throw new PromotionException("PromotionID is not valid");
        }
        promotion1.setIsActive(promotion.getIsActive());
        promotion1.setPromotionType(promotion.getPromotionType());
       // promotion1.setProduct(promotion.getProduct());
        promotion1.setStartDate(promotion.getStartDate());
        promotion1.setEndDate(promotion.getEndDate());
        promotion1.setDiscountRate(promotion.getDiscountRate());

        return promotionRepository.save(promotion1);
    }


    public String deletePromotionByID(UUID business_userId,UUID promotionID) throws UserInfoException,PromotionException{
        UserInfo userInfo=userInfoRepository.findById(business_userId).orElse(null);
      //  logger.info("promotion object {}",promotion);
        if(userInfo==null){
            throw new UserInfoException("the given id is not valid");
        }
        if(userInfo.getRole()!= Role.BUSINESS_USER){
            throw new UserInfoException("The given id not belongs to a business_user");
        }
        Promotion promotion1=promotionRepository.findById(promotionID).orElse(null);
        if(promotion1==null){
            throw new PromotionException("PromotionID is not valid");
        }
        promotionRepository.deleteById(promotionID);
        logger.info("deleted succesfully");
        return "Deleted SuccessFully";
    }
    public List<Promotion> getAllPromotions(UUID business_userId){
        UserInfo userInfo=userInfoRepository.findById(business_userId).orElse(null);
        if(userInfo==null){
            throw new UserInfoException("the given id is not valid");
        }
        if(userInfo.getRole()!= Role.BUSINESS_USER){
            throw new UserInfoException("The given id not belongs to a business_user");
        }
        return userInfo.getPromotions();
    }


    public List<Promotion> viewPromotionsOnCategory(UUID business_userId,PromotionType promotionType){
        UserInfo userInfo=userInfoRepository.findById(business_userId).orElse(null);
        if(userInfo==null){
            throw new UserInfoException("the given id is not valid");
        }
        if(userInfo.getRole()!= Role.BUSINESS_USER){
            throw new UserInfoException("The given id not belongs to a business_user");
        }
        List<Promotion>promotions=userInfo.getPromotions();
        List<Promotion>requiredPromotions=new ArrayList<>();
        for(Promotion promotion:promotions){
            if(promotion.getPromotionType().equals(promotionType)){
                if(promotion.getIsActive())
                requiredPromotions.add(promotion);
            }
        }
        return requiredPromotions;
    }
    public List<Promotion> viewExpiredPromotions(UUID business_userId){
        UserInfo userInfo=userInfoRepository.findById(business_userId).orElse(null);
        if(userInfo==null){
            throw new UserInfoException("the given id is not valid");
        }
        if(userInfo.getRole()!= Role.BUSINESS_USER){
            throw new UserInfoException("The given id not belongs to a business_user");
        }
        List<Promotion>promotionList=userInfo.getPromotions();
        List<Promotion>expiredPromotions=new ArrayList<>();
        for(Promotion promo:promotionList){
              LocalDate endDate=promo.getEndDate();
            LocalDate currentDate = LocalDate.now();
            // Check if the current date is before the endDate
            //including active and inactive promotions we get in expired promotions
             if(!currentDate.isBefore(endDate)){
                 expiredPromotions.add(promo);
             }
        }
        return expiredPromotions;
    }
    public List<Promotion> deleteExpiredPromotions(UUID business_userId){
        UserInfo userInfo = userInfoRepository.findById(business_userId).orElse(null);
        if (userInfo == null) {
            throw new UserInfoException("The given ID is not valid");
        }
        if (userInfo.getRole() != Role.BUSINESS_USER) {
            throw new UserInfoException("The given ID does not belong to a business user");
        }
        List<Promotion> promotionList = userInfo.getPromotions();
        List<Promotion> deletedPromotions = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();


        Iterator<Promotion> iterator = promotionList.iterator();
        while (iterator.hasNext()) {
            Promotion promo = iterator.next();
            LocalDate endDate = promo.getEndDate();
            if (!currentDate.isBefore(endDate)) {
                deletedPromotions.add(promo);
                logger.info("Promotion with ID {} is being removed", promo.getPromotionId());
                iterator.remove();
            }
        }

        userInfo.setPromotions(promotionList);

        userInfoRepository.save(userInfo);



        return deletedPromotions;
    }


}
