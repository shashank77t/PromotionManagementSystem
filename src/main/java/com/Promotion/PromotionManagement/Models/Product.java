package com.Promotion.PromotionManagement.Models;


import com.Promotion.PromotionManagement.Enum.CategoryType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productId;
    private String productName;
    private String description;
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;
    private int price;
    private int quantity;
    @ManyToOne
    @JsonIgnore
    private UserInfo userInfo;



}
