package com.example.demo.src.likeStore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetLikeStoreRes {
    private int storeIdx;
    private String storeName;
    private int minOrderPrice;
    private float avgRate;
    private int deliveryTip;
    private String menuNames;
}
