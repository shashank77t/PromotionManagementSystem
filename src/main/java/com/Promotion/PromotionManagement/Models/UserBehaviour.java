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
     private UUID userBehaviourId;
    @OneToOne(mappedBy = "userBehaviour",cascade = CascadeType.ALL)
    @JsonIgnore
    private UserInfo userInfo;
    private LocalDate lastLoginDate;
    private int purchaseFrequency;
    @OneToMany(mappedBy = "userBehaviour",cascade = CascadeType.ALL)
    private List<Promotion>promotionList=new ArrayList<>();
    private int visitCount;




}
