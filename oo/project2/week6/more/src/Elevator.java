import java.util.ArrayList;

import com.oocourse.elevator2.PersonRequest;
import com.oocourse.TimableOutput;

public class Elevator extends Thread {
    private ArrayList<PersonRequest> inRequest = new ArrayList<>();
    private int strategy;
    private Dispatcher dispatcher;
    private int floor;
    private int state;
    private final int max = 6;
    private int num = 0;
    private final int speed = 400;
    private String id;
    private ArrayList<PersonRequest> waiting = new ArrayList<>();

    public Elevator(Dispatcher dispatcher,int floor,int state,String id) {
        this.dispatcher = dispatcher;
        this.floor = floor;
        this.state = state;
        this.id = id;
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
            strategy2();
        } else {
            strategy3();
        }
    }

    public void strategy1() {
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

    public void strategy2() {
        while (true) {
            move();
            boolean out = hasOut();
            boolean in = hasIn();
            if (in | out) {
                boolean ifOpen;
                synchronized (dispatcher) {
                    ifOpen = openMorning();
                }
                if (ifOpen) {
                    close();
                }
            }
            state = changeStateMor();
            if (state == 3) {
                break;
            }
        }
    }

    public void strategy3() {
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            move();
            boolean out = (floor == 1) && (inRequest.size() != 0);
            boolean in = hasInNight();
            if (in | out) {
                TimableOutput.println("OPEN-" + floor + "-" + id);
                goOut();
                for (int i = waiting.size() - 1;i >= 0;i--) {
                    PersonRequest perReq = waiting.get(i);
                    int from = perReq.getFromFloor();
                    if (from == floor) {
                        waiting.remove(perReq);
                        inRequest.add(perReq);
                        num++;
                        TimableOutput.println("IN-" + perReq.getPersonId() +
                                "-" + floor + "-" + id);
                    }
                }
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TimableOutput.println("CLOSE-" + floor + "-" + id);
            }
            if (floor == 1) {
                synchronized (dispatcher) {
                    while (waiting.size() < max && dispatcher.get().size() > 0) {
                        int size = dispatcher.read().size();
                        PersonRequest perReq = dispatcher.get().get(size - 1);
                        waiting.add(perReq);
                        dispatcher.get().remove(perReq);
                    }
                }
            }
            state = changeStateNig();
            if (state == 3) {
                break;
            }

        }
    }

    public boolean openMorning() {
        if (hasOut() || hasIn()) {
            TimableOutput.println("OPEN-" + floor + "-" + id);
            goOut();
            while (num < max) {
                if (floor != 1) {
                    break;
                }
                while (dispatcher.get().isEmpty() && !dispatcher.getEnd()) {
                    try {
                        dispatcher.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (hasIn()) {
                    goIn();
                } else if (dispatcher.getEnd()) {
                    break;
                }
            }
            return true;
        }
        return false;
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
                    } else if (inSize == 0) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean hasInNight() {
        int i;
        for (i = 0;i < waiting.size();i++) {
            int from = waiting.get(i).getFromFloor();
            if (from == floor) {
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

    public void close() {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimableOutput.println("CLOSE-" + floor + "-" + id);
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
                TimableOutput.println("OUT-" + perReq.getPersonId() + "-" + floor + "-" + id);
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
                    int min = 20;
                    int dir = 0;
                    for (int i = 0;i < dispatcher.read().size();i++) {
                        int from = dispatcher.read().get(i).getFromFloor();
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

    public int changeStateMor() {
        synchronized (dispatcher) {
            if (dispatcher.read().size() == 0 && inRequest.size() == 0) {
                if (dispatcher.getEnd()) {
                    return 3;
                }
            }
            if (floor != 1 && inRequest.size() == 0) {
                return -1;
            } else if (floor != 1) {
                return 1;
            }
            if (inRequest.size() != 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public int changeStateNig() {
        synchronized (dispatcher) {
            if (dispatcher.read().size() == 0 && inRequest.size() == 0) {
                if (dispatcher.getEnd() && waiting.size() == 0) {
                    return 3;
                }
            }
            if (floor == 1 && waiting.size() == 0) {
                return 0;
            } else if (waiting.size() == 0 && floor != 1) {
                return -1;
            }
            return 1;
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

}

