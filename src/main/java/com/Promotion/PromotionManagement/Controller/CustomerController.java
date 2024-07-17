package com.Promotion.PromotionManagement.Controller;

import com.Promotion.PromotionManagement.Dto.GeneralMsgDto;
import com.Promotion.PromotionManagement.Dto.RequestDto.JwtRequest;
import com.Promotion.PromotionManagement.Enum.CategoryType;
import com.Promotion.PromotionManagement.Enum.PromotionType;
import com.Promotion.PromotionManagement.Exceptions.PromotionException;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Promotion;
import com.Promotion.PromotionManagement.Models.PurchaseHistory;
import com.Promotion.PromotionManagement.Models.UserBehaviour;
import com.Promotion.PromotionManagement.Models.UserInfo;
//import com.Promotion.PromotionManagement.Service.PurchaseHistoryService;
import com.Promotion.PromotionManagement.Service.PurchaseHistoryService;
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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/User")
public class CustomerController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private PurchaseHistoryService purchaseHistoryService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtility jwtUtility;


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

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());



        return new ResponseEntity<>(new GeneralMsgDto(jwtToken),HttpStatus.OK);
    }


    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody UserInfo customer){
        try{
            String ab = userInfoService.CustomerSignUp(customer);
            return new ResponseEntity<>(new GeneralMsgDto(ab),HttpStatus.OK);
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
    @GetMapping("/userBehaviourOnId")
    public ResponseEntity  userBehaviourOnId(@RequestParam UUID CustomerId){
        try{
            UserBehaviour u=userInfoService.userBehaviourOnId(CustomerId);
            return new ResponseEntity<>(u,HttpStatus.OK);
        }catch (UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }
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

    @PostMapping("/buyTheProduct")
    public ResponseEntity buyTheProductS(@RequestParam UUID customerID,@RequestParam UUID promotionID,@RequestParam int quantity){
        try{
            PurchaseHistory pu=purchaseHistoryService.buyTheProduct(customerID,promotionID,quantity);
            return new ResponseEntity<>(pu,HttpStatus.OK);
        }catch(UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }catch(PromotionException p){
            return new ResponseEntity<>(new GeneralMsgDto(p.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getAllUsers")
    public List<UserInfo> getAllUsers(){
        return userInfoService.getAllUSers();
    }

}