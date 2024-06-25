package com.Promotion.PromotionManagement.Controller;

import com.Promotion.PromotionManagement.Dto.GeneralMsgDto;
import com.Promotion.PromotionManagement.Enum.PromotionType;
import com.Promotion.PromotionManagement.Exceptions.ProductException;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Product;
import com.Promotion.PromotionManagement.Models.UserInfo;
import com.Promotion.PromotionManagement.Repository.ProductRepository;
import com.Promotion.PromotionManagement.Repository.UserInfoRepository;
import com.Promotion.PromotionManagement.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/Product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @PostMapping("Business_User/addProduct")
    public ResponseEntity addProduct(@RequestBody Product product, @RequestParam UUID business_userId){
        try{
            Product product1 = productService.addProduct(product, business_userId);
            return new ResponseEntity<>(product1,HttpStatus.OK);
        }catch(UserInfoException e){
            return new ResponseEntity<>(new GeneralMsgDto(e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (ProductException p){
            return new ResponseEntity<>(new GeneralMsgDto(p.getMessage()),HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("Business_User/deleteProductById")
    public ResponseEntity deleteProductById(@RequestParam UUID business_userId,@RequestParam UUID productID){
        try{
            String deleted= productService.deleteProductById(business_userId, productID);
            return new ResponseEntity<>(new GeneralMsgDto(deleted),HttpStatus.OK);
        }catch(UserInfoException e){
            return new ResponseEntity<>(new GeneralMsgDto(e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (ProductException p){
            return new ResponseEntity<>(new GeneralMsgDto(p.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/viewAllProducts")
    public ResponseEntity viewAllProducts(){
       List<Product> productList=productRepository.findAll();
        return new ResponseEntity<>(productList,HttpStatus.OK);
    }
    @PutMapping("/Business_User/updateProduct")
    public ResponseEntity updateProduct(@RequestBody Product product,@RequestParam UUID business_userId){
        try{
            Product product1=productService.updateProduct(product,business_userId);
            return new ResponseEntity<>(product1,HttpStatus.OK);
        }catch (UserInfoException e){
            return new ResponseEntity<>(new GeneralMsgDto(e.getMessage()),HttpStatus.OK);
        }catch (ProductException p){
            return new ResponseEntity<>(new GeneralMsgDto(p.getMessage()),HttpStatus.OK);
        }
    }
    @GetMapping("/Business_User/getAllProducts")
    public ResponseEntity getAllProducts(@RequestParam UUID Business_UserId){
        try{
            List<Product> productList = productService.getAllProducts(Business_UserId);
            return new ResponseEntity<>(productList,HttpStatus.OK);
        }catch (UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/Business_User/getTotalAmount")
    public ResponseEntity getTotalAmount(@RequestParam UUID Business_UserId){
        try{
            String ab=productService.getTotalAmount(Business_UserId);
            return new ResponseEntity<>(new GeneralMsgDto(ab),HttpStatus.OK);
        }catch (UserInfoException u){
            return new ResponseEntity<>(new GeneralMsgDto(u.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }




}
