package com.Promotion.PromotionManagement.Models;

import com.Promotion.PromotionManagement.Enum.Gender;
import com.Promotion.PromotionManagement.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    private UUID userID;
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String Location;

    @OneToOne
    private PurchaseHistory purchaseHistory;
    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Promotion> promotions=new ArrayList<>();
    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL)
    private List<Product>productList=new ArrayList<>();
    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL)
    private List<UserBehaviour> userBehaviourList=new ArrayList<>();


}
