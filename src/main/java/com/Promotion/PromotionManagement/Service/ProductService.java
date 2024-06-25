package com.Promotion.PromotionManagement.Service;

import com.Promotion.PromotionManagement.Enum.Role;
import com.Promotion.PromotionManagement.Exceptions.ProductException;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Product;
import com.Promotion.PromotionManagement.Models.UserInfo;
import com.Promotion.PromotionManagement.Repository.ProductRepository;
import com.Promotion.PromotionManagement.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private static final Logger logger=LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    public Product addProduct(Product product,UUID business_userId) throws ProductException,UserInfoException{
        UserInfo userInfo=userInfoRepository.findById(business_userId).orElse(null);
    //    logger.info("user role {}",product);
        if(userInfo==null){
            throw new ProductException("the given id is not valid");
        }
        if(userInfo.getRole()!= Role.BUSINESS_USER){
            throw new UserInfoException("The given id is not a business_user");

        }

            product.setUserInfo(userInfo);
            Product p=productRepository.save(product);
            userInfo.getProductList().add(product);
        return p;
    }
    public String deleteProductById(UUID business_userId,UUID productID){
        UserInfo userInfo=userInfoRepository.findById(business_userId).orElse(null);

        if(userInfo==null){
            throw new ProductException("the given id is not valid");
        }
        if(userInfo.getRole()!= Role.BUSINESS_USER){
            throw new UserInfoException("The given id is not a business_user");

        }
        Product product1=productRepository.findById(productID).orElse(null);
        logger.info("user role {}",product1);
        if(product1==null){
            throw new ProductException("The ProductId is not valid");
        }
        productRepository.deleteById(productID);
        return "Product Deleted Successfully";
    }
     public Product updateProduct(Product product,UUID business_userId) throws ProductException,UserInfoException{
         UserInfo userInfo=userInfoRepository.findById(business_userId).orElse(null);

         if(userInfo==null){
             throw new ProductException("the given id is not valid");
         }
         if(userInfo.getRole()!= Role.BUSINESS_USER){
             throw new UserInfoException("The given id is not a business_user");

         }
         Product product1=productRepository.findById(product.getProductId()).orElse(null);
         logger.info("The gieven product is {}",product);
         if(product1==null){
             throw new ProductException("The ProductId is not valid");
         }
         product1.setProductName(product.getProductName());
         product1.setDescription(product.getDescription());
         product1.setPrice(product.getPrice());
         product1.setQuantity(product.getQuantity());
         product1.setCategoryType(product.getCategoryType());


         return productRepository.save(product1);

     }

     public List<Product> getAllProducts(UUID business_userId) throws UserInfoException{
         UserInfo userInfo=userInfoRepository.findById(business_userId).orElse(null);

         if(userInfo==null){
             throw new UserInfoException("the given id is not valid");
         }
         if(userInfo.getRole()!= Role.BUSINESS_USER){
             throw new UserInfoException("The given id is not a business_user");

         }
         List<Product>productList=userInfo.getProductList();
         return productList;
     }
      public String getTotalAmount(UUID Business_UserId){
          UserInfo userInfo=userInfoRepository.findById(Business_UserId).orElse(null);

          if(userInfo==null){
              throw new UserInfoException("the given id is not valid");
          }
          if(userInfo.getRole()!= Role.BUSINESS_USER){
              throw new UserInfoException("The given id is not a business_user");

          }

          List<Product>productList=userInfo.getProductList();
          int total=0;
          for(Product p:productList){
              total+=p.getPrice()*p.getQuantity();
          }
          return String.valueOf(total);
      }
}
