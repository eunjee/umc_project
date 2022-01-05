package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostOrderReq {
    private int userIdx;
    private int storeIdx;
    private List<MenuDTO> menuList;
    private String ownerComment;
    private String deliveryComment;
    private String payment;
}
