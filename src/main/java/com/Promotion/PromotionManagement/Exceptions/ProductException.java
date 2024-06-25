package com.Promotion.PromotionManagement.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ProductException extends RuntimeException{
  //  private String message;
    public ProductException(String msg){
        super(msg);
    }
}
