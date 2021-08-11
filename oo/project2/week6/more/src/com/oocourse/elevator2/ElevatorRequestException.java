package com.oocourse.elevator2;

public class ElevatorRequestException extends Exception {
    private final String original;

    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    ElevatorRequestException(String original) {
        super(String.format("Elevator request parse failed! - \"%s\"", original));
        this.original = original;
    }

    /**
     * 获取原字符串
     *
     * @return 原字符串
     */
    public String getOriginal() {
        return original;
    }
}
