package com.oocourse.elevator2;

/**
 * 非法起始楼层异常
 */
class InvalidFromFloorException extends PersonRequestException {
    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    InvalidFromFloorException(String original) {
        super(original);
    }
}
