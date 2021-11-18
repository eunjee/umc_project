package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostStoreReq {
    private String storeName;
    private String storeInfo;
    private int minOrderPrice;
    private int deliveryTip;
    private String storePassword;
}
