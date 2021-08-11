import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MorningElevator extends Elevator {
    private MorningDispatcher dispatcher;
    private ArrayList<Person> inRequest = new ArrayList<>();
    private int floor;
    private int state;
    private int max;
    private int num = 0;
    private int speed;
    private String id;
    private String type;
    private MorningController morningController;
    private static final int[] floorB = {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0};
    private static final int[] floorC = {1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1};

    public MorningElevator(Dispatcher dispatcher, String id, int max, int speed, String type, MorningController morningController) {
        this.dispatcher = (MorningDispatcher) dispatcher;
        this.id = id;
        this.floor = 1;
        this.state = 0;
        this.max = max;
        this.speed = speed;
        this.type = type;
        this.morningController = morningController;
    }

    public int[] getFloor() {
        if (type.equals("B")) {
            return floorB;
        } else if (type.equals("C")) {
            return floorC;
        } else {
            int[] floorA = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
            return floorA;
        }
    }

    public void run() {
        while (true) {
            move();
            //System.out.println(id + "end move");
            boolean in = hasIn();
            //System.out.println("end judge in");
            boolean out = hasOut();
            boolean open = in | out;
            //System.out.println(id + "end judge open");
            if (open) {
                boolean ifOpen;
                synchronized (dispatcher) {
                    ifOpen = open();
                }
                if (ifOpen) {
                    close();
                }
                //System.out.println(id + "end open");
            }
            state = changeState();
            if (state == 3) {
                //System.out.println(id + "end");
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
        if (getFloor()[floor - 1] == 0) {
            return false;
        }
        synchronized (dispatcher) {
            //System.out.println("start hasin");
            int outSize = dispatcher.get(type).size();
            int inSize = inRequest.size();
            if (outSize == 0) {
                return false;
            } else if (num >= max) {
                return false;
            }
            for (int i = 0;i < dispatcher.get(type).size();i++) {
                int to = dispatcher.get(type).get(i).getTempDes();
                int from = dispatcher.get(type).get(i).getFrom();
                if (from == floor) {
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
        if (getFloor()[floor - 1] == 0) {
            return false;
        }
        if (inRequest.size() == 0) {
            return false;
        }
        for (int i = 0;i < inRequest.size();i++) {
            if (this.floor == inRequest.get(i).getTempDes()) {
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
            Person person = inRequest.get(i);
            if (floor == person.getTempDes()) {
                TimableOutput.println("OUT-" + person.getId() + "-" + floor + "-" + id);
                inRequest.remove(person);
                num--;
            }
            if (person.isTransfer()) {
                person.setTransfer();
                person.setFrom(person.getTempDes());
                person.setTempDes(person.getTo());
                person.setFirst(person.getSecond());
                if (person.getFirst().equals("C")) {
                    dispatcher.addC(person);
                    //System.out.println(person.getId() + person.getFirst());
                } else {
                    dispatcher.addAB(person);
                }
            } else {
                synchronized (morningController) {
                    morningController.remove(person);
                }
            }
        }
    }

    public void goIn() {
        synchronized (dispatcher) {
            int direction = state;
            int outSize = dispatcher.get(type).size();
            //System.out.println(outSize + type);
            for (int i = 0;i < outSize;i++) {
                Person person = dispatcher.get(type).get(i);
                //System.out.println(person.getId() + type);
                int from = person.getFrom();
                int to = person.getTempDes();
                if (num < max && from == floor) {
                    if (floor == 1 || (to - from) * direction >= 0 || inRequest.size() == 0) {
                        inRequest.add(person);
                        dispatcher.get(type).remove(person);
                        direction = to - from;
                        num++;
                        TimableOutput.println("IN-" + person.getId() + "-" +
                                floor + "-" + id);
                        i--;
                        outSize--;
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
            while (dispatcher.isEmpty(type) && inSize == 0) {
                //System.out.println("out and in is zero" + id);
                if (dispatcher.getEnd()) {
                    //System.out.println("input is zero" + id);
                    //System.out.println(randomController.getPeople().size());
                    synchronized (morningController.getPeople()) {
                        if (morningController.getPeople().size() == 0) {
                            return 3;
                        }
                    }
                    //System.out.println(id + "still have people");
                    try {
                        dispatcher.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        //System.out.println(id + "wait");
                        dispatcher.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (inSize != 0) {
                int begin = inRequest.get(0).getFrom();
                int end = inRequest.get(0).getTempDes();
                if (end - begin > 0) {
                    return 1;
                }
                return -1;
            } else {                      //空载
                if (state == 0) {
                    for (int i = 0;i < dispatcher.get(type).size();i++) {
                        if (dispatcher.get(type).get(i).getFrom() == 1 && getFloor()[floor - 1] == 1) {
                            return 0;
                        }
                    }
                    return 1;
                } else {
                    int min = 20;
                    int dir = 0;
                    for (int i = 0;i < dispatcher.get(type).size();i++) {
                        int from = dispatcher.get(type).get(i).getFrom();
                        if (getFloor()[from - 1] == 0) {
                            continue;
                        }
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
