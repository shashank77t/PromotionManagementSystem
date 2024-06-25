package com.Promotion.PromotionManagement.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PurchaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID purchaseId;
    @OneToOne(mappedBy = "purchaseHistory",cascade = CascadeType.ALL)
    private UserInfo userInfo;
    private LocalDate purchaseDate;
    private int totalAmount;
    @OneToMany
    private List<Product>productList;




}
