package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    POST_EMPTY_PASSWORD(false,2004,"비밀번호를 입력해주세요"),
    POST_INVALID_PASSWORD(false, 2005, "비밀번호 형식을 확인해주세요."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    USERS_EMPTY_USER_NAME(false,2011,"이름을 입력해주세요"),
    USERS_INVALID_USER_NAME(false,2012,"이름의 형식을 확인해주세요"),
    USERS_EMPTY_USER_NICKNAME(false,2013,"닉네임을 입력해주세요"),
    USERS_INVALID_USER_NICKNAME(false,2014,"닉네임은 한글,영어,숫자만 가능합니다."),
    POST_USERS_EMPTY_PHONE_NUMBER(false, 2015, "핸드폰 번호를 입력해주세요."),
    POST_USERS_INVALID_PHONE_NUMBER(false, 2016, "핸드포 번호 형식을 확인해주세요."),
    POST_USERS_EXISTS_PHONE_NUMBER(false,2017,"중복된 핸드폰 번호입니다."),
    POST_USERS_EMPTY_EMAIL(false, 2018, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2019, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2020,"중복된 이메일입니다."),

    //stores
    INVALID_CATEGORY_NAME(false,2022,"해당 카테고리명이 없습니다."),
    POST_STORE_EMPTY_STORE_NAME(false,2023,"음식점 이름을 입력해주세요"),
    POST_STORE_EXIST_STORE_NAME(false,2024,"이미 있는 음식점이름입니다."),
    NOT_EXIST_STORE_NAME(false,2025,"해당 음식점이 없습니다."),
    NOT_EXIST_CATEGORY_NUM(false,2026,"해당 카테고리가 없습니다."),
    INVALID_STORE_LAST_IDX(false,2027,"가게 인덱스의 범위를 벗어났습니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    // 5000 : 필요시 만들어서 쓰세요
    INVALID_STORE_JWT(false,2003,"권한이 없는 가게의 접근입니다."),
    WITHDRAW_ERROR(false, 5001, "탈퇴한 회원입니다"),
    USER_STATUS_ERROR(false,5002,"활성화된 계정이 아닙니다."),
    DELETE_FAIL_ERROR(false,5003,"회원 탈퇴에 실패하였습니다"),
    PATCH_MENU_PRICE_EMPTY(false,5004,"수정할 메뉴 가격을 입력해주세요");

    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
