package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.demo.config.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "storeName", "result"})
public class MenuResponse<T, String> {//BaseResponse 객체를 사용할때 성공, 실패 경우
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    private final int code;
    private String storeName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 요청에 성공한 경우
    public MenuResponse(T result, String storeName) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = (String) SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.storeName = storeName;
        this.result = result;
    }

    // 요청에 실패한 경우
    public MenuResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = (String) status.getMessage();
        this.code = status.getCode();
    }
}

