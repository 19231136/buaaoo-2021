import java.util.ArrayList;
import com.oocourse.TimableOutput;

public class RandomElevator extends Elevator {
    private RandomDispatcher dispatcher;
    private ArrayList<Person> inRequest = new ArrayList<>();
    private int floor;
    private int state;
    private int max;
    private int num = 0;
    private int speed;
    private String id;
    private String type;
    private RandomController randomController;
    private static final int[] FLOORB = {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0};
    private static final int[] FLOORC = {1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1};

    public RandomElevator(Dispatcher dispatcher,String id,int max,int speed,
                          String type,RandomController randomController) {
        this.dispatcher = (RandomDispatcher) dispatcher;
        this.id = id;
        this.floor = 1;
        this.state = 0;
        this.max = max;
        this.speed = speed;
        this.type = type;
        this.randomController = randomController;
    }

    public int[] getFloor() {
        if (type.equals("B")) {
            return FLOORB;
        } else if (type.equals("C")) {
            return FLOORC;
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
        //System.out.println(id + "+" + state);
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
        //System.out.println(floor);
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
                if (from == floor && getFloor()[to - 1] == 1) {
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
                    synchronized (randomController) {
                        randomController.remove(person);
                    }
                }
            }
        }
    }

    public void goIn() {
        synchronized (dispatcher) {
            int direction = state;
            int outSize = dispatcher.get(type).size();
            for (int i = 0;i < outSize;i++) {
                Person person = dispatcher.get(type).get(i);
                //System.out.println(outSize + id + getName());
                int from = person.getFrom();
                int to = person.getTempDes();
                if (num < max && from == floor && getFloor()[to - 1] == 1) {
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
            while (dispatcher.isEmpty(type) && inSize == 0) {
                //System.out.println("out and in is zero" + id);
                if (dispatcher.getEnd()) {
                    //System.out.println("input is zero" + id);
                    //System.out.println(randomController.getPeople().size());
                    synchronized (randomController.getPeople()) {
                        if (randomController.getPeople().size() == 0) {
                            return 3;
                        }
                    }
                }
                try {
                    //System.out.println(getName() + "wait");
                    dispatcher.wait();
                    //System.out.println(getName() + "wake");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (inSize != 0) {
                int begin = inRequest.get(0).getFrom();
                int end = inRequest.get(0).getTempDes();
                if (end - begin > 0) {
                    //System.out.println(id + getName() + "up");
                    return 1;
                }
                //System.out.println(id + getName() + "down");
                //System.out.println(end + " " + begin);
                return -1;
            } else {                      //空载
                if (state == 0) {
                    for (int i = 0;i < dispatcher.get(type).size();i++) {
                        if (dispatcher.get(type).get(i).getFrom() == 1
                                && getFloor()[floor - 1] == 1) {
                            return 0;
                        }
                    }
                    return 1;
                } else {
                    int min = 20;
                    int dir = 100;
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
                    //System.out.println(id + "+" + dir);
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
