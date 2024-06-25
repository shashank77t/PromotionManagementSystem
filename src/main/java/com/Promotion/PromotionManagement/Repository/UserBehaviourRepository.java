package com.Promotion.PromotionManagement.Repository;

import com.Promotion.PromotionManagement.Models.UserBehaviour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserBehaviourRepository extends JpaRepository<UserBehaviour, UUID> {
}
