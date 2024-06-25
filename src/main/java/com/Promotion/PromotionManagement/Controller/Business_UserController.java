package com.Promotion.PromotionManagement.Controller;

import com.Promotion.PromotionManagement.Dto.GeneralMsgDto;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Promotion;
import com.Promotion.PromotionManagement.Models.UserBehaviour;
import com.Promotion.PromotionManagement.Models.UserInfo;
import com.Promotion.PromotionManagement.Service.PromotionService;
import com.Promotion.PromotionManagement.Service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/Business_User")
public class Business_UserController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private PromotionService promotionService;
    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody UserInfo userInfo){

        try{
            UserInfo userInfo1 = userInfoService.BusinessUserSignUp(userInfo);
            return new ResponseEntity<>(userInfo1, HttpStatus.OK);
        }catch(UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }


    }




    //signin API



//    @PostMapping("/createPromotion")
//    public ResponseEntity createPromotion(@RequestBody Promotion promotion, @RequestParam UUID business_userId,@RequestParam UUID productID){
//
//        try {
//            Promotion promotion1 = promotionService.createPromotion(promotion, business_userId, productID);
//            return new ResponseEntity<>(promotion1,HttpStatus.OK);
//        }catch(UserInfoException e){
//            return new ResponseEntity<>(new GeneralMsgDto(e.getMessage()),HttpStatus.BAD_REQUEST);
//        }
//    }















}
