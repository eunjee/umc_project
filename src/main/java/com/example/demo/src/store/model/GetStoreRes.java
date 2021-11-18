package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class GetStoreRes {
    private int storeIdx;
    private String storeName;
    private String storeInfo;
    private int minOrderPrice;
    private float avgRate;
    private int recentCommentCount;
    private int deliveryTip;
    private int recentOrderCount;
    private int totalReviewCount;
}
