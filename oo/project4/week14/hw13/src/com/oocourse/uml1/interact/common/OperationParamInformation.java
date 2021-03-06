package com.oocourse.uml1.interact.common;

import com.oocourse.uml1.utils.string.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 方法参数信息
 */
public class OperationParamInformation implements Comparable<OperationParamInformation> {

    private final List<String> parameterTypes;
    private final String returnType;

    /**
     * 构造函数
     *
     * @param parameterTypes    属性类型序列
     * @param returnType        返回值类型
     */
    public OperationParamInformation(List<String> parameterTypes, String returnType) {
        this.parameterTypes = new ArrayList<>(parameterTypes);
        Collections.sort(this.parameterTypes);
        this.returnType = returnType;
    }

    /**
     * 获取参数类型序列
     *
     * @return 参数类型序列
     */
    public List<String> getParameterTypes() {
        return parameterTypes;
    }

    /**
     * 获取返回值类型
     *
     * @return 返回值类型
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * 相等性判定
     *
     * @param o 对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationParamInformation that = (OperationParamInformation) o;
        return Objects.equals(parameterTypes, that.parameterTypes) &&
                Objects.equals(returnType, that.returnType);
    }

    /**
     * 哈希值求解
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return Objects.hash(parameterTypes, returnType);
    }

    /**
     * 大小比较
     *
     * @param that 另一个类
     * @return 比较结果
     */
    @Override
    public int compareTo(OperationParamInformation that) {
        int compareLength = this.getParameterTypes().size() - that.getParameterTypes().size();
        if (compareLength != 0) {
            return compareLength;
        }

        for (int i = 0; i < this.getParameterTypes().size(); i++) {
            int compareParam = this.getParameterTypes().get(i).compareTo(that.getParameterTypes().get(i));
            if (compareParam != 0) {
                return compareParam;
            }
        }

        if (this.getReturnType() == null) {
            if (that.getReturnType() == null) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (that.getReturnType() == null) {
                return 1;
            } else {
                return this.getReturnType().compareTo(that.getReturnType());
            }
        }
    }

    /**
     * 字符串形式
     *
     * @return 字符形式
     */
    @Override
    public String toString() {
        if (returnType == null) {
            return String.format("(%s, no return)",
                    StringUtils.joinWithTransform(parameterTypes, String::toString, ", "));
        } else {
            return parameterTypes.isEmpty() ? String.format("(return: %s)", returnType) :
                    String.format("(%s, return: %s)",
                        StringUtils.joinWithTransform(parameterTypes, String::toString, ", "),
                        returnType);
        }
    }
}
