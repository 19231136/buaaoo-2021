package com.oocourse.elevator3;

public class DuplicatedElevatorIdException extends ElevatorRequestException {
    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    DuplicatedElevatorIdException(String original) {
        super(original);
    }
}
