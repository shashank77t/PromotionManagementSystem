package com.Promotion.PromotionManagement.Controller;

import com.Promotion.PromotionManagement.Dto.GeneralMsgDto;
import com.Promotion.PromotionManagement.Dto.RequestDto.JwtRequest;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Promotion;
import com.Promotion.PromotionManagement.Models.UserBehaviour;
import com.Promotion.PromotionManagement.Models.UserInfo;
import com.Promotion.PromotionManagement.Service.PromotionService;
import com.Promotion.PromotionManagement.Service.UserInfoService;
import com.Promotion.PromotionManagement.Utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Business_User")
public class Business_UserController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody UserInfo userInfo){
        String rawPassword = userInfo.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        userInfo.setPassword(encodedPassword);

        try{
            UserInfo userInfo1 = userInfoService.BusinessUserSignUp(userInfo);
            return new ResponseEntity<>(userInfo1, HttpStatus.OK);
        }catch(UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody JwtRequest jwtRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());

        String jwtToken = jwtUtility.generateTokenFromUsername(userDetails);

//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());

//        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);

        return new ResponseEntity<>(new GeneralMsgDto(jwtToken),HttpStatus.OK);
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
