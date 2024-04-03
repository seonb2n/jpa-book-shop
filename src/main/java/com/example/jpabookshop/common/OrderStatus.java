package com.example.jpabookshop.common;

public enum OrderStatus {

    READY("준비"),
    IN_PROGRESS("진행중"),
    DONE("완료"),
    CANCEL("취소");

    private String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
