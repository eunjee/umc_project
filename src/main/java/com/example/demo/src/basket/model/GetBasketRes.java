package com.example.demo.src.basket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class GetBasketRes {
    private int basketIdx;
    private int userIdx;
    private int storeIdx;
    private String storeName;
    private List<String> menuName;
    private int amount;
    private int totalPrice;
}
