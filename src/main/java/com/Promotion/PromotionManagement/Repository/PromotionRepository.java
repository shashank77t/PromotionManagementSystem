package com.Promotion.PromotionManagement.Repository;

import com.Promotion.PromotionManagement.Enum.PromotionType;
import com.Promotion.PromotionManagement.Models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {


    @Query(value = "SELECT p.* FROM Promotion p JOIN Product pr ON p.product_product_id = pr.product_id WHERE pr.category_type = :categoryType AND p.is_active = true", nativeQuery = true)
    List<Promotion> findPromotionsByCategoryType(@Param("categoryType") String categoryType);
    @Query(value = "SELECT * FROM Promotion WHERE is_active = true", nativeQuery = true)
    List<Promotion> findActivePromotions();

    @Query(value = "SELECT * FROM Promotion p WHERE p.promotion_type = :promotionType AND p.is_active=true", nativeQuery = true)
    List<Promotion> findPromotionsOnPromotionType(@Param("promotionType") String promotionType);
    @Query(value = "SELECT p.* FROM Promotion p JOIN Product pr ON p.product_product_id = pr.product_id WHERE pr.category_type = :categoryType AND p.is_active = true", nativeQuery = true)
    List<Promotion> promotionOnCategoryType(@Param("categoryType") String categoryType);

}
