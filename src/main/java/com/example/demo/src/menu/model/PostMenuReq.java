package com.example.demo.src.menu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostMenuReq {
    private String menuName;
    private String menuCategory;
    private int price;
    private String menuFlag;
    private String menuInfo;
}
