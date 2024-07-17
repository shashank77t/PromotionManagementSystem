package com.Promotion.PromotionManagement.Controller;

import com.Promotion.PromotionManagement.Dto.GeneralMsgDto;
import com.Promotion.PromotionManagement.Exceptions.PromotionException;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Promotion;
import com.Promotion.PromotionManagement.Models.PromotionApproval;
import com.Promotion.PromotionManagement.Repository.PromotionRepository;
import com.Promotion.PromotionManagement.Service.PromotionApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/Business_User")
public class PromotionApprovalController {
    @Autowired
    private PromotionApprovalService promotionApprovalService;


    @PostMapping("/createPromoApproval")
    public ResponseEntity createPromoApproval(@RequestParam UUID promotionId){
        try{
             PromotionApproval p=promotionApprovalService.createPromoApproval(promotionId);
            return new ResponseEntity(p, HttpStatus.OK);
        }catch(PromotionException p){
            return new ResponseEntity(new GeneralMsgDto(p.getMessage()),HttpStatus.BAD_REQUEST);
        }catch (UserInfoException u){
            return new ResponseEntity(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/promotionApproval")
    public ResponseEntity promotionApproval(@RequestParam UUID promoAppID,@RequestParam UUID userInfoId){
             try{
                 PromotionApproval p=promotionApprovalService.promotionApprovalS(promoAppID,userInfoId);
                 return new ResponseEntity(p, HttpStatus.OK);
             }catch(PromotionException p){
                 return new ResponseEntity(new GeneralMsgDto(p.getMessage()),HttpStatus.BAD_REQUEST);
             }catch (UserInfoException u){
                 return new ResponseEntity(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
             }
    }


}
