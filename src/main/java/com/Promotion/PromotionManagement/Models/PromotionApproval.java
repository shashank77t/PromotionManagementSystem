package com.Promotion.PromotionManagement.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PromotionApproval {
         @Id
      //   @GeneratedValue(strategy = GenerationType.AUTO)
         private UUID promotionApprovalId;
         @OneToOne(mappedBy = "promotionApproval",cascade = CascadeType.ALL)
         @JsonIgnore
         private Promotion promotion;
         @ManyToMany
         private List<UserInfo>userInfos=new ArrayList<>();
         private Boolean approved;
}
