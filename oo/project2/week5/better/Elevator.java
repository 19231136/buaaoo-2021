import java.util.ArrayList;

import com.oocourse.elevator1.PersonRequest;
import com.oocourse.TimableOutput;

public class Elevator implements Runnable {
    private ArrayList<PersonRequest> inRequest = new ArrayList<>();
    private int strategy;
    private Dispatcher dispatcher;
    private int floor;
    private int state;
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
            move();
            boolean in = hasIn();
            boolean out = hasOut();
            boolean open = in | out;
            if (open) {
                open();
            }
            state = changeState();
            if (state == 3) {
                break;
            }
        }
    }

    public boolean hasIn() {            //有人能上电梯
        synchronized (dispatcher) {
            int outSize = dispatcher.read().size();
            int inSize = inRequest.size();
            if (outSize == 0) {
                return false;
            } else if (num >= max) {
                return false;
            }
            for (int i = 0;i < dispatcher.read().size();i++) {
                int to = dispatcher.read().get(i).getToFloor();
                int from = dispatcher.read().get(i).getFromFloor();
                if (dispatcher.read().get(i).getFromFloor() == floor) {
                    if (floor == 1) {
                        //TimableOutput.println("floor1 hasin");
                        return true;
                    } else if ((to - from) * state > 0) {
                        //TimableOutput.println("same dir hasin");
                        return true;
                    }
                }
            }
            if (inSize == 0) {
                int flag = 0;//不换方向
                for (int i = 0;i < dispatcher.read().size();i++) {
                    int from = dispatcher.read().get(i).getFromFloor();
                    if ((from - floor) * state > 0) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    for (int i = 0;i < dispatcher.read().size();i++) {
                        if (dispatcher.read().get(i).getFromFloor() == floor) {
                            //TimableOutput.println("change dir hasin");
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    public void open() {
        if (hasIn() | hasOut()) {
            TimableOutput.println("OPEN-" + floor);
            goOut();
            if (hasIn()) {
                goIn();
            }
            //TimableOutput.println("end in");
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TimableOutput.println("CLOSE-" + floor);
        }
    }

    public void goIn() {
        synchronized (dispatcher) {
            int inSize = inRequest.size();
            int outSize = dispatcher.get().size();
            int flag = 0;//换方向
            if (inSize == 0) {
                for (int i = 0;i < dispatcher.read().size();i++) {
                    int from = dispatcher.read().get(i).getFromFloor();
                    if ((from - floor) * state > 0) {
                        flag = 1;//不换方向
                        break;
                    }
                }
            }
            for (int i = outSize - 1;i >= 0;i--) {
                PersonRequest perReq = dispatcher.get().get(i);
                int from = perReq.getFromFloor();
                int to = perReq.getToFloor();
                if (num < max && from == floor) {
                    if (floor == 1 || (to - from) * state > 0) {
                        inRequest.add(perReq);
                        dispatcher.get().remove(perReq);
                        num++;
                        TimableOutput.println("IN-" + perReq.getPersonId() + "-" + floor);
                    }
                }
            }
            inSize = inRequest.size();
            //TimableOutput.println(flag == 0 && inSize == 0);
            if (flag == 0 && inSize == 0) {
                for (int i = outSize - 1;i >= 0;i--) {
                    PersonRequest perReq = dispatcher.get().get(i);
                    if (num < max && perReq.getFromFloor() == floor) {
                        inRequest.add(perReq);
                        dispatcher.get().remove(perReq);
                        num++;
                        TimableOutput.println("IN-" + perReq.getPersonId() + "-" + floor);
                    }
                }
            }
        }
        return;
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
        if (inRequest.size() == 0) {
            return;
        }
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
        synchronized (dispatcher) {
            int inSize = inRequest.size();
            //TimableOutput.println("in number is" + inSize);
            //TimableOutput.println(dispatcher.read().size() == 0 && inSize == 0);
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
            if (inSize != 0) {
                int begin = inRequest.get(0).getFromFloor();
                int end = inRequest.get(0).getToFloor();
                if (end - begin > 0) {
                    return 1;
                }
                return -1;
            } else {                      //空载
                if (state == 0) {
                    for (int i = 0;i < dispatcher.read().size();i++) {
                        if (dispatcher.read().get(i).getFromFloor() == 1) {
                            return 0;
                        }
                    }
                    return 1;
                } else {
                    for (int i = 0;i < dispatcher.read().size();i++) {
                        int from = dispatcher.read().get(i).getFromFloor();
                        if ((from - floor) * state > 0) {
                            return state;
                        }
                    }
                    return -state;
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
        if (state == -1) {
            floor--;
        }
        if (state != 0) {
            TimableOutput.println("ARRIVE-" + floor);
        }
    }

}

