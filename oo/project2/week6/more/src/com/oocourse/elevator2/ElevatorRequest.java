package com.oocourse.elevator2;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElevatorRequest extends Request {

    private final String elevatorId;

    /**
     * 构造函数
     *
     * @param elevatorId   电梯标识
     */
    public ElevatorRequest(String elevatorId) {
        this.elevatorId = elevatorId;
    }

    /**
     * 获取电梯id
     *
     * @return 电梯id
     */
    public String getElevatorId() {
        return elevatorId;
    }

    /**
     * 转为字符串形式
     *
     * @return 字符串形式
     */
    @Override
    public String toString() {
        return String.format("ADD-%s", elevatorId);
    }

    /**
     * 获取哈希值
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{
                this.elevatorId});
    }

    /**
     * 判断对象是否相等
     *
     * @param obj 对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof ElevatorRequest) {
            return (((ElevatorRequest) obj).elevatorId.equals(this.elevatorId));
        } else {
            return false;
        }
    }

    private static final String PARSE_PATTERN_STRING =
            "^ADD-(?<elevatorId>\\d+)\\s*$";
    private static final Pattern PARSE_PATTERN
            = Pattern.compile(PARSE_PATTERN_STRING);

    /**
     * 解析字符串至ELevatorRequest
     *
     * @param string 字符串
     * @return 解析结果
     * @throws ElevatorRequestException 解析失败
     * @throws InvalidPatternException 解析失败
     */
    static ElevatorRequest parse(String string)
    throws InvalidPatternException {
        Matcher matcher = PARSE_PATTERN.matcher(string);
        if (matcher.matches()) {
            String elevatorId = matcher.group("elevatorId");
            return new ElevatorRequest(elevatorId);
        } else {
            throw new InvalidPatternException(string);
        }
    }
}
