package com.Promotion.PromotionManagement.Repository;

import com.Promotion.PromotionManagement.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product,UUID> {
}
