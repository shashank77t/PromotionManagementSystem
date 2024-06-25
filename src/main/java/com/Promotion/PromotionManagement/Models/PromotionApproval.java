package com.Promotion.PromotionManagement.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PromotionApproval {
         @Id
         @GeneratedValue(strategy = GenerationType.AUTO)
         private UUID promotionApprovalId;
         @ManyToOne
         private Promotion promotion;
         @OneToMany
         private List<UserInfo>userInfos;
         private Boolean approved;
}
