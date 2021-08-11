import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.oocourse.elevator1.PersonRequest;
import com.oocourse.TimableOutput;

public class Elevator implements Runnable {
    private ArrayList<PersonRequest> inRequest = new ArrayList<>();
    private int strategy;
    private Dispatcher dispatcher;
    private int floor;
    private int state;
    private boolean stop = false;
    private final int max = 6;
    private int num = 0;
    private final int speed = 400;

    public Elevator(Dispatcher dispatcher,int floor,int state) {
        this.dispatcher = dispatcher;
        this.floor = floor;
        this.state = state;
    }

    @Override
    public void run() {
        synchronized (dispatcher) {
            while (dispatcher.getStrategy() == 0) {
                try {
                    dispatcher.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            strategy = dispatcher.getStrategy();
        }
        if (strategy == 1) {
            strategy1();
        } else if (strategy == 2) {
            strategy1();
        } else {
            strategy1();
        }
    }

    public void strategy1() {
        while (true) {
            stop = false;
            boolean in = hasIn();
            boolean out = hasOut();
            stop = in | out;
            if (stop) {
                TimableOutput.println("OPEN-" + floor);
                if (out) {
                    goOut();
                }
                if (in) {
                    goIn();
                }
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TimableOutput.println("CLOSE-" + floor);
            }
            state = changeState();
            //TimableOutput.println(state);
            if (state == 3) {
                break;
            } else if (state != 0) {
                move();
                //TimableOutput.println(floor);
            }
        }
    }


    public boolean hasIn() {            //有人能上电梯
        synchronized (dispatcher) {
            int size = dispatcher.read().size();
            if (size == 0) {
                return false;
            } else if (num >= max) {
                return false;
            } else {
                for (int i = 0;i < size;i++) {
                    if (floor == dispatcher.read().get(i).getFromFloor()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void goIn() {
        synchronized (dispatcher) {
            int size = dispatcher.get().size();
            for (int i = size - 1;i >= 0;i--) {
                PersonRequest perReq = dispatcher.get().get(i);
                if (num < max && perReq.getFromFloor() == floor) {
                    dispatcher.get().remove(perReq);
                    inRequest.add(perReq);
                    num++;
                    TimableOutput.println("IN-" + perReq.getPersonId() + "-" + floor);
                    //TimableOutput.println(inRequest.size());
                }
            }
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

    public void goOut() {
        for (int i = inRequest.size() - 1;i >= 0;i--) {
            PersonRequest perReq = inRequest.get(i);
            if (floor == perReq.getToFloor()) {
                TimableOutput.println("OUT-" + perReq.getPersonId() + "-" + floor);
                inRequest.remove(perReq);
                num--;
            }
        }
    }

    public int changeState() {
        //TimableOutput.println(state);
        //TimableOutput.println(inRequest.size());
        synchronized (dispatcher) {
            int inSize = inRequest.size();
            while (dispatcher.read().size() == 0 && inSize == 0) {
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
            //TimableOutput.println(dispatcher.read().size());
            int min = 20;
            int dis = 0;
            int outSize = dispatcher.read().size();
            inSize = inRequest.size();
            if (inSize >= 6 || outSize == 0) {
                for (int i = inSize - 1;i >= 0;i--) {
                    PersonRequest perReq = inRequest.get(i);
                    int now = Math.abs(perReq.getToFloor() - floor);
                    if (now < min) {
                        min = now;
                        dis = perReq.getToFloor();
                    }
                }
                if (dis == floor) {
                    return 0;
                } else if (dis > floor) {
                    return 1;
                } else {
                    return 2;
                }
            } else {
                for (int i = outSize - 1;i >= 0;i--) {
                    PersonRequest perReq = dispatcher.read().get(i);
                    int now = Math.abs(perReq.getFromFloor() - floor);
                    if (now < min) {
                        min = now;
                        dis = perReq.getFromFloor();
                    }
                }
                if (dis == floor) {
                    return 0;
                } else if (dis > floor) {
                    return 1;
                } else {
                    return 2;
                }
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
        if (state == 2) {
            floor--;
        }
        TimableOutput.println("ARRIVE-" + floor);
    }

}

