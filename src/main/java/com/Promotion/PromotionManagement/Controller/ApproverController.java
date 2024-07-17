package com.Promotion.PromotionManagement.Controller;

import com.Promotion.PromotionManagement.Dto.GeneralMsgDto;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.UserInfo;
import com.Promotion.PromotionManagement.Service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/Approver")
public class ApproverController {
    @Autowired
    private UserInfoService userInfoService;
    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody UserInfo approver){
        try{
            UserInfo u=userInfoService.signUp(approver);
            return new ResponseEntity(u, HttpStatus.OK);
        }catch(UserInfoException e){
            return new ResponseEntity<>(new GeneralMsgDto(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

}
