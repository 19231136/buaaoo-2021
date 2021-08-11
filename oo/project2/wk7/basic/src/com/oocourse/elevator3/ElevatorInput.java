package com.oocourse.elevator3;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;

/**
 * 电梯输入类
 */
@SuppressWarnings({"WeakerAccess", "RedundantThrows", "unused"})
public class ElevatorInput implements Closeable {
    private final Scanner scanner;
    private final HashSet<Integer> existedPersonId;
    private final HashSet<String> existedElevatorId;

    /**
     * 构造函数
     *
     * @param inputStream 输入流
     */
    public ElevatorInput(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
        this.existedPersonId = new HashSet<>();
        this.existedElevatorId = new HashSet<>();
        this.existedElevatorId.add("1");
        this.existedElevatorId.add("2");
        this.existedElevatorId.add("3");
    }

    private static final InputStream DEFAULT_INPUT_STREAM = System.in;

    /**
     * 构造函数（默认输入流）
     */
    public ElevatorInput() {
        this(DEFAULT_INPUT_STREAM);
    }

    /**
     * 关闭输入
     *
     * @throws IOException 关闭输入
     */
    @Override
    public void close() throws IOException {
        scanner.close();
    }

    /**
     * 获取到达模式
     *
     * @return String 到达模式名称
     */
    public String getArrivingPattern() {
        final String MORNING = "Morning";
        final String NIGHT = "Night";
        final String RANDOM = "Random";
        final String[] SUPPORTED_PATTERNS = {MORNING, NIGHT, RANDOM};
        while (true) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    String pattern = line;
                    for (String supported_pattern : SUPPORTED_PATTERNS) {
                        if (supported_pattern.equals(pattern)) {
                            return pattern;
                        }
                    }
                    throw new InvalidArrivingPatternException(pattern);
                } catch (InvalidArrivingPatternException exception) {
                    exception.printStackTrace(System.err);
                }
            } else {
                return null;
            }
        }
    }

    /**
     * 读取下一个PersonRequest
     * 返回null表示已经读完
     *
     * @return PersonRequest
     */
    public Request nextRequest() {
        while (true) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    PersonRequest request = PersonRequest.parse(line);
                    if (existedPersonId.contains(request.getPersonId())) {
                        throw new DuplicatedPersonIdException(line);
                    } else {
                        existedPersonId.add(request.getPersonId());
                        return request;
                    }
                } catch (InvalidPatternException exception) {
                    try {
                        ElevatorRequest request = ElevatorRequest.parse(line);
                        if (existedElevatorId.contains(request.getElevatorId())) {
                            throw new DuplicatedElevatorIdException(line);
                        } else {
                            existedElevatorId.add(request.getElevatorId());
                            return request;
                        }
                    } catch (ElevatorRequestException | InvalidPatternException e) {
                        e.printStackTrace(System.err);
                    }
                } catch (PersonRequestException exception) {
                    exception.printStackTrace(System.err);
                }
            } else {
                return null;
            }
        }
    }
}
