package com.example.demo.src.owner.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Owner {
    private int ownerIdx;
    private String ownerName;
    private String ownerPassword;
    private String email;
    private String phoneNum;
    private String status;
}
