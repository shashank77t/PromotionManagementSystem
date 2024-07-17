package com.Promotion.PromotionManagement.Service;

import com.Promotion.PromotionManagement.Enum.Role;
import com.Promotion.PromotionManagement.Exceptions.PromotionException;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Promotion;
import com.Promotion.PromotionManagement.Models.PromotionApproval;
import com.Promotion.PromotionManagement.Models.UserInfo;
import com.Promotion.PromotionManagement.Repository.PromotionApprovalRepository;
import com.Promotion.PromotionManagement.Repository.PromotionRepository;
import com.Promotion.PromotionManagement.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.UUID;

@Service
public class PromotionApprovalService {
    public static final Logger logger=LoggerFactory.getLogger(PromotionApprovalService.class);
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private PromotionApprovalRepository promotionApprovalRepository;

    public PromotionApproval createPromoApproval(UUID promotionID) throws PromotionException{
        Promotion p=promotionRepository.findById(promotionID).orElse(null);
        if(p==null){
            throw new PromotionException("promotion id is not valid");
        }
        PromotionApproval promotionApproval=new PromotionApproval();
        promotionApproval.setPromotionApprovalId(UUID.randomUUID());
        promotionApproval.setPromotion(p);
        p.setPromotionApproval(promotionApproval);
        promotionApproval.setApproved(false);
        return promotionApprovalRepository.save(promotionApproval);
    }


    public PromotionApproval promotionApprovalS(UUID promoApprovalId,UUID approverID) throws PromotionException,UserInfoException{
        UserInfo u=userInfoRepository.findById(approverID).orElse(null);
        PromotionApproval pa=promotionApprovalRepository.findById(promoApprovalId).orElse(null);
        if(pa==null){
            throw new PromotionException("promotion id is not valid");
        }
        if(u==null){
            throw new UserInfoException("UserInfo id is not valid");
        }
        if(!u.getRole().equals(Role.APPROVER)){
            throw new UserInfoException("User is not a approver");
        }
        Promotion p=pa.getPromotion();
         pa.getUserInfos().add(u);
         if(pa.getUserInfos().size()>=2){
             pa.setApproved(true);
             p.setIsActive(true);
         }
        return promotionApprovalRepository.save(pa);
    }




















    public PromotionApproval promotionApproval(UUID promotionId,UUID userInfoId) throws UserInfoException,PromotionException{
        UserInfo u=userInfoRepository.findById(userInfoId).orElse(null);
        if(u==null){
            throw new UserInfoException("userId is not valid");
        }
        Promotion p=promotionRepository.findById(promotionId).orElse(null);
        if(p==null){
            throw new PromotionException("promotionId is not valid");
        }

         PromotionApproval promotionApproval=p.getPromotionApproval();
          //  promotionApproval.setPromotionApprovalId(UUID.randomUUID());

        if(u.getRole().equals(Role.APPROVER)){
           logger.info("###############");
            promotionApproval.getUserInfos().add(u);
            promotionApproval.setPromotion(p);
        }
        if(promotionApproval.getUserInfos().size()>=2){
            logger.info("the size of the prmotio###########@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@n approval{}",promotionApproval.getUserInfos().size());
            promotionApproval.setApproved(true);
            p.setIsActive(true);
        }
        promotionApprovalRepository.save(promotionApproval);
        promotionRepository.save(p);
        return promotionApproval;
    }
}
