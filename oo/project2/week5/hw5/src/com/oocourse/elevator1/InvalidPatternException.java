package com.oocourse.elevator1;

/**
 * 基本格式非法异常
 */
class InvalidPatternException extends PersonRequestException {
    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    InvalidPatternException(String original) {
        super(original);
    }
}
