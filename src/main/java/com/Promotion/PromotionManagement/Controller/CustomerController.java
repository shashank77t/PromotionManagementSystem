package com.Promotion.PromotionManagement.Controller;

import com.Promotion.PromotionManagement.Dto.GeneralMsgDto;
import com.Promotion.PromotionManagement.Enum.CategoryType;
import com.Promotion.PromotionManagement.Enum.PromotionType;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Promotion;
import com.Promotion.PromotionManagement.Models.UserBehaviour;
import com.Promotion.PromotionManagement.Models.UserInfo;
import com.Promotion.PromotionManagement.Service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/User")
public class CustomerController {
    @Autowired
    private UserInfoService userInfoService;
    @PostMapping("signUp")
    public ResponseEntity signUp(@RequestBody UserInfo customer){
        try{
            UserInfo userInfo1 = userInfoService.CustomerSignUp(customer);
            return new ResponseEntity<>(userInfo1,HttpStatus.OK);
        }catch(UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/viewPromotionsByCustomer/{page}")
    public ResponseEntity viewPromotionsByCustomer(@RequestParam UUID CustomerID, @PathVariable int page){
        try{
            List<Promotion> list=userInfoService.viewPromotionsByCustomer(CustomerID,page);
            return new ResponseEntity<>(list,HttpStatus.OK);
        }catch(UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }catch(IllegalArgumentException i){
            return new ResponseEntity<>(new GeneralMsgDto(i.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/userBehaviourListOnId")
    public List<UserBehaviour> userBehaviourListOnId(@RequestParam UUID CustomerId){
        return userInfoService.getAllUserBehaviourList(CustomerId);
    }
    @GetMapping("/getUserBehaviourOnPromotionOnId")
    public UserInfo getUserBehaviourOnPromotionOnId(@RequestParam UUID id){
        return userInfoService.getUserBehaviourOnID(id);
    }

    @GetMapping("/viewPromotionsOnPromtionType/{promotionType}")
    public ResponseEntity viewPromotionsOnPromtionType(@RequestParam UUID customerID,@PathVariable PromotionType promotionType){
        try{
            List<Promotion>promotionList=userInfoService.viewPromotionsOnPromotionType(customerID,promotionType);
            return new ResponseEntity<>(promotionList,HttpStatus.OK);
        }catch (UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/viewPromotionsOnCategory/{categoryType}")
    public ResponseEntity viewPromotionsOnCategory(@RequestParam UUID customerID,@PathVariable CategoryType categoryType){
        try{
                List<Promotion>PromoOnProductType=userInfoService.viewPromotionsOnCategory(customerID,categoryType);
                return new ResponseEntity<>(PromoOnProductType,HttpStatus.OK);
        }catch(UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
}