package com.oocourse.elevator3;

/**
 * 起始目标楼层相同
 */
class DuplicatedFromToFloorException extends PersonRequestException {
    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    DuplicatedFromToFloorException(String original) {
        super(original);
    }
}
