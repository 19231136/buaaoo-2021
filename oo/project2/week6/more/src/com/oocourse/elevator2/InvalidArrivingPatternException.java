package com.oocourse.elevator2;

/**
 * 非法到达模式异常
 */
public class InvalidArrivingPatternException extends Throwable {
    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    public InvalidArrivingPatternException(String original) {
        super(original);
    }
}
