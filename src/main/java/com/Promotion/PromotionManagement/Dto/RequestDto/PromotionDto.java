package com.Promotion.PromotionManagement.Dto.RequestDto;

import com.Promotion.PromotionManagement.Enum.PromotionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDto {
    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private Double discountRate;
}
