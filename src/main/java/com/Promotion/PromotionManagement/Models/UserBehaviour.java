package com.Promotion.PromotionManagement.Models;

import com.Promotion.PromotionManagement.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserBehaviour {
    @Id
 //   @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userBehaviourId;
    @ManyToOne
    @JsonIgnore
    private UserInfo userInfo;
    private LocalDate lastLoginDate;
    private int purchaseFrequency;
    @ManyToOne
    private Promotion promotion;
    private int visitedCount;


}
