package com.oocourse.elevator2;

/**
 * 基本格式非法异常
 */
class InvalidPatternException extends Exception {
    private final String original;

    /**
     * 构造函数
     *
     * @param original 原字符串
     */
    InvalidPatternException(String original) {
        super(String.format("Request parse failed! - \"%s\"", original));
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
