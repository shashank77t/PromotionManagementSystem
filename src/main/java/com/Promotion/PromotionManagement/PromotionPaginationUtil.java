package com.Promotion.PromotionManagement;

import com.Promotion.PromotionManagement.Models.Promotion;

import java.util.List;

public class PromotionPaginationUtil {

    public static List<Promotion> getPaginatedList(List<Promotion> fullList, int page, int size) {

        if (fullList == null || page < 0 || size <= 0) {
            return List.of();
        }

        int fromIndex = page * size;
        if (fromIndex >= fullList.size()) {
            return List.of();
        }
        // toIndex exclusive
        return fullList.subList(fromIndex, Math.min(fromIndex + size, fullList.size()));
    }
}
