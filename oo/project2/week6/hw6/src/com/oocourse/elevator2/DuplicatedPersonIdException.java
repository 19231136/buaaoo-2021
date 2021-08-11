package com.oocourse.elevator2;

/**
 * 重复人员编号异常
 */
class DuplicatedPersonIdException extends PersonRequestException {
    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    DuplicatedPersonIdException(String original) {
        super(original);
    }
}
