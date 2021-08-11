import com.oocourse.TimableOutput;
import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class RandomElevator extends Elevator {
    private RandomDispatcher dispatcher;
    private ArrayList<PersonRequest> inRequest = new ArrayList<>();
    private int floor;
    private int state;
    private final int max = 6;
    private int num = 0;
    private final int speed = 400;
    private String id;

    public RandomElevator(Dispatcher dispatcher,int floor,int state,String id) {
        this.dispatcher = (RandomDispatcher) dispatcher;
        this.floor = floor;
        this.state = state;
        this.id = id;
    }

    public void run() {
        while (true) {
            move();
            boolean in = hasIn();
            boolean out = hasOut();
            boolean open = in | out;
            if (open) {
                boolean ifOpen;
                synchronized (dispatcher) {
                    ifOpen = open();
                }
                if (ifOpen) {
                    close();
                }
            }
            state = changeState();
            if (state == 3) {
                break;
            }
        }
    }

    public void move() {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (state == 1) {
            floor++;
        }
        if (state == -1) {
            floor--;
        }
        if (state != 0) {
            TimableOutput.println("ARRIVE-" + floor + "-" + id);
        }
    }

    public boolean hasIn() {            //有人能上电梯
        synchronized (dispatcher) {
            int outSize = dispatcher.get().size();
            int inSize = inRequest.size();
            if (outSize == 0) {
                return false;
            } else if (num >= max) {
                return false;
            }
            for (int i = 0;i < dispatcher.get().size();i++) {
                int to = dispatcher.get().get(i).getToFloor();
                int from = dispatcher.get().get(i).getFromFloor();
                if (dispatcher.get().get(i).getFromFloor() == floor) {
                    if (floor == 1) {
                        //TimableOutput.println("floor1 hasin");
                        return true;
                    } else if ((to - from) * state > 0) {
                        //TimableOutput.println("same dir hasin");
                        return true;
                    } else if (inSize == 0) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean hasOut() {           // 有人能下电梯
        if (inRequest.size() == 0) {
            return false;
        }
        for (int i = 0;i < inRequest.size();i++) {
            if (this.floor == inRequest.get(i).getToFloor()) {
                return true;
            }
        }
        return false;
    }

    public boolean open() {
        if (hasIn() | hasOut()) {
            TimableOutput.println("OPEN-" + floor + "-" + id);
            goOut();
            if (hasIn()) {
                goIn();
            }
            //TimableOutput.println("end in");
            return true;
        }
        return false;
    }

    public void goOut() {
        if (inRequest.size() == 0) {
            return;
        }
        for (int i = inRequest.size() - 1;i >= 0;i--) {
            PersonRequest perReq = inRequest.get(i);
            if (floor == perReq.getToFloor()) {
                TimableOutput.println("OUT-" + perReq.getPersonId() + "-" + floor + "-" + id);
                inRequest.remove(perReq);
                num--;
            }
        }
    }

    public void goIn() {
        synchronized (dispatcher) {
            int direction = state;
            int outSize = dispatcher.get().size();
            for (int i = outSize - 1;i >= 0;i--) {
                PersonRequest perReq = dispatcher.get().get(i);
                int from = perReq.getFromFloor();
                int to = perReq.getToFloor();
                if (num < max && from == floor) {
                    if (floor == 1 || (to - from) * direction >= 0 || inRequest.size() == 0) {
                        inRequest.add(perReq);
                        dispatcher.get().remove(perReq);
                        direction = to - from;
                        num++;
                        TimableOutput.println("IN-" + perReq.getPersonId() + "-" +
                                floor + "-" + id);
                    }
                }
            }
        }
    }

    public void close() {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimableOutput.println("CLOSE-" + floor + "-" + id);
    }

    public int changeState() {
        synchronized (dispatcher) {
            int inSize = inRequest.size();
            //TimableOutput.println("in number is" + inSize);
            //TimableOutput.println(dispatcher.read().size() == 0 && inSize == 0);
            while (dispatcher.get().size() == 0 && inSize == 0) {
                if (dispatcher.getEnd()) {
                    return 3;
                } else {
                    try {
                        dispatcher.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (inSize != 0) {
                int begin = inRequest.get(0).getFromFloor();
                int end = inRequest.get(0).getToFloor();
                if (end - begin > 0) {
                    return 1;
                }
                return -1;
            } else {                      //空载
                if (state == 0) {
                    for (int i = 0;i < dispatcher.get().size();i++) {
                        if (dispatcher.get().get(i).getFromFloor() == 1) {
                            return 0;
                        }
                    }
                    return 1;
                } else {
                    int min = 20;
                    int dir = 0;
                    for (int i = 0;i < dispatcher.get().size();i++) {
                        int from = dispatcher.get().get(i).getFromFloor();
                        int distance = from - floor;
                        if (Math.abs(distance) < min) {
                            min = Math.abs(distance);
                            dir = distance;
                        }
                    }
                    if (dir == 0) {
                        return 0;
                    } else if (dir < 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        }
    }
}
