package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class GetStoreCategoryRes {
    private int categoryIdx;
    private String categoryName;
    private int storeIdx;
    private String storeName;
}
