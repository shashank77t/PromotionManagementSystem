package com.Promotion.PromotionManagement.Service;

import com.Promotion.PromotionManagement.Enum.Role;
import com.Promotion.PromotionManagement.Exceptions.ProductException;
import com.Promotion.PromotionManagement.Exceptions.PromotionException;
import com.Promotion.PromotionManagement.Exceptions.UserInfoException;
import com.Promotion.PromotionManagement.Models.Product;
import com.Promotion.PromotionManagement.Models.Promotion;
import com.Promotion.PromotionManagement.Models.PurchaseHistory;
import com.Promotion.PromotionManagement.Models.UserInfo;
import com.Promotion.PromotionManagement.Repository.PromotionRepository;
import com.Promotion.PromotionManagement.Repository.PurchaseHistoryRepository;
import com.Promotion.PromotionManagement.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class PurchaseHistoryService {
    private static final Logger logger=LoggerFactory.getLogger(PurchaseHistoryService.class);
    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public PurchaseHistory buyTheProduct(UUID customerId,UUID promotionId,int quantity)throws UserInfoException{
        UserInfo userInfo=userInfoRepository.findById(customerId).orElse(null);
        Promotion promotion=promotionRepository.findById(promotionId).orElse(null);
        if(userInfo==null){
            throw  new UserInfoException("User not exists with this customerId");
        }
        if(promotion==null){
            throw new PromotionException("Promotion not exists with this promotionId");
        }
        if(!userInfo.getRole().equals(Role.USER)){
            throw new UserInfoException("He is not a USER");
        }
        PurchaseHistory p=userInfo.getPurchaseHistory();
        logger.info("purchaseHistory object #####################{}",p.getPurchaseId());
        if(p==null){
            p=new PurchaseHistory();
            p.setPurchaseId(UUID.randomUUID());

        }
        if(!p.getProductList().contains(promotion.getProduct())) {
            p.getProductList().add(promotion.getProduct());
        }
        p.setPurchaseDate(LocalDate.now());
        p.setUserInfo(userInfo);
        Double dis=promotion.getDiscountRate();
        Double price=1.0;
        if(dis!=null){
          price=(dis/100)*promotion.getProduct().getPrice();
        }
        Product product=promotion.getProduct();
        if(product.getQuantity()<quantity){
            throw new ProductException("The product quantity is not sufficient");
        }
        product.setQuantity(product.getQuantity()-quantity);
        p.setTotalAmount(p.getTotalAmount()+(int)(price*quantity));
        userInfo.setPurchaseHistory(p);
        purchaseHistoryRepository.save(p);
        return p;

    }

}
