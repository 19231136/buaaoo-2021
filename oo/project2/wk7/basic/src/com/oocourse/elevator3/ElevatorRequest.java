package com.oocourse.elevator3;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElevatorRequest extends Request {

    private final String elevatorId;
    private final String elevatorType;

    /**
     * 构造函数
     *
     * @param elevatorId   电梯标识
     * @param elevatorType 电梯类型
     */
    public ElevatorRequest(String elevatorId, String elevatorType) {
        this.elevatorId = elevatorId;
        this.elevatorType = elevatorType;
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
     * 获取电梯类型
     *
     * @return 电梯类型
     */
    public String getElevatorType() {
        return elevatorType;
    }

    /**
     * 转为字符串形式
     *
     * @return 字符串形式
     */
    @Override
    public String toString() {
        return String.format("ADD-%s-%s", elevatorId, elevatorType);
    }

    /**
     * 获取哈希值
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{
            this.elevatorId, this.elevatorType});
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
            return (((ElevatorRequest) obj).elevatorId.equals(this.elevatorId))
                && (((ElevatorRequest) obj).elevatorType.equals(this.elevatorType));
        } else {
            return false;
        }
    }

    private static final String PARSE_PATTERN_STRING =
        "^ADD-(?<elevatorId>\\d+)-" +
            "(?<elevatorType>[ABC])\\s*$";
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
            String elevatorType = matcher.group("elevatorType");
            return new ElevatorRequest(elevatorId, elevatorType);
        } else {
            throw new InvalidPatternException(string);
        }
    }
}
