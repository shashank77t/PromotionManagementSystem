package com.Promotion.PromotionManagement.Controller;

import com.Promotion.PromotionManagement.Dto.GeneralMsgDto;
import com.Promotion.PromotionManagement.Dto.RequestDto.PromotionDto;
import com.Promotion.PromotionManagement.Enum.PromotionType;
import com.Promotion.PromotionManagement.Exceptions.PromotionException;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Promotion;
import com.Promotion.PromotionManagement.Models.UserInfo;
import com.Promotion.PromotionManagement.Service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PromotionController {
    @Autowired
    private PromotionService promotionService;
    @PostMapping("Business_User/createPromotion")
    public ResponseEntity createPromotion(@RequestBody PromotionDto promotionDto, @RequestParam UUID business_userId, @RequestParam UUID productID){

        try {
            Promotion promotion1 = promotionService.createPromotion(promotionDto, business_userId, productID);
            return new ResponseEntity<>(promotion1, HttpStatus.OK);
        }catch(UserInfoException e){
            return new ResponseEntity<>(new GeneralMsgDto(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

   @PutMapping("/Business_User/updatePromotion")
    public ResponseEntity updatePromotion(@RequestBody Promotion promotion,@RequestParam UUID business_userId){
        try {
            Promotion promotion1 = promotionService.updatePromotion(promotion,business_userId);
            return new ResponseEntity<>(promotion1, HttpStatus.OK);
        }catch(UserInfoException e){
            return new ResponseEntity<>(new GeneralMsgDto(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
   @GetMapping("/Business_User/getPromotionById")
    public ResponseEntity getPromotionById(@RequestParam UUID business_userId,@RequestParam UUID promotionID){
        try {
            Promotion promotion1 = promotionService.getPromotionByID(business_userId, promotionID);
            return new ResponseEntity<>(promotion1, HttpStatus.OK);
        }catch(UserInfoException e){
            return new ResponseEntity<>(new GeneralMsgDto(e.getMessage()),HttpStatus.BAD_REQUEST);
        }catch(PromotionException p){
            return new ResponseEntity<>(new GeneralMsgDto(p.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
     @DeleteMapping("/Business_User/deletePromotionById")
    public ResponseEntity deletePromotionByID(@RequestParam UUID business_userId,@RequestParam UUID promotionID){
        try {
           String deleted= promotionService.deletePromotionByID(business_userId, promotionID);
            return new ResponseEntity<>(new GeneralMsgDto(deleted), HttpStatus.OK);
        }catch(UserInfoException e){
            return new ResponseEntity<>(new GeneralMsgDto(e.getMessage()),HttpStatus.BAD_REQUEST);
        }catch (PromotionException p){
            return new ResponseEntity<>(new GeneralMsgDto(p.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getAllPromotions")
    public List<Promotion> getAllPromotions(@RequestParam UUID business_userId){

        return promotionService.getAllPromotions(business_userId);
    }
    @GetMapping("/Business_User/viewPromotions/{promotionType}")
    public ResponseEntity viewPromotionsOnCategory(@RequestParam UUID Business_UserId, @PathVariable PromotionType promotionType){
        try{
             List<Promotion>promotions=promotionService.viewPromotionsOnCategory(Business_UserId,promotionType);
            return new ResponseEntity<>(promotions,HttpStatus.OK);
        }catch (UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/Business_User/viewExpiredPromotions")
    public ResponseEntity viewExpiredPromotions(@RequestParam UUID Business_UserId){
        try{
            List<Promotion>promotions=promotionService.viewExpiredPromotions(Business_UserId);
            return new ResponseEntity<>(promotions,HttpStatus.OK);
        }catch (UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/Business_User/deleteExpiredPromotions")
    public ResponseEntity deleteExpiredPromotions(@RequestParam UUID Business_UserId){
        try{
            List<Promotion>promotions=promotionService.deleteExpiredPromotions(Business_UserId);
            return new ResponseEntity<>(promotions,HttpStatus.OK);
        }catch (UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

}
