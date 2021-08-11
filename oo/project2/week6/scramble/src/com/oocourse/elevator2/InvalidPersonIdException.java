package com.oocourse.elevator2;

/**
 * 非法人员编号异常
 */
class InvalidPersonIdException extends PersonRequestException {
    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    InvalidPersonIdException(String original) {
        super(original);
    }
}
