package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
public class GetOrderRes {
    private int orderIdx;
    private String storeName;
    private String menuName;
    private int totalPrice;
    private int totalAmount;
    private String payment;
}
