package com.example.demo.src.menu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetStoreMenuRes {
    private int menuIdx;
    private String menuName;
    private int price;
}
