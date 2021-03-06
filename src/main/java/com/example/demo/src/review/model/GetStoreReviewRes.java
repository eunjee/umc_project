package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class GetStoreReviewRes {
    private int userIdx;
    private String nickName;
    private String content;
    private int rate;
}
