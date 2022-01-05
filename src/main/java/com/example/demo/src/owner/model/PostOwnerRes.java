package com.example.demo.src.owner.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PostOwnerRes {
    private int ownerIdx;
    private String jwt;
}
