package com.Promotion.PromotionManagement.Models;

import com.Promotion.PromotionManagement.Enum.PromotionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Promotion {
    private static final Logger logger = LoggerFactory.getLogger(Promotion.class);

    @Id
    private UUID promotionId;
    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private Double discountRate;
    @ManyToOne
    @JsonIgnore
    private UserInfo userInfo;
    @ManyToOne
    private Product product;
    @OneToOne
    @JoinColumn
    private PromotionApproval promotionApproval;
     private int likes;
     @ManyToOne
     @JsonIgnore
     private UserBehaviour userBehaviour;

}
