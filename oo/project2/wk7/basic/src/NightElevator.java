import com.oocourse.TimableOutput;
import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class NightElevator extends Elevator {
    private NightDispatcher dispatcher;
    private ArrayList<PersonRequest> inRequest = new ArrayList<>();
    private int floor;
    private int state;
    private final int max = 6;
    private int num = 0;
    private final int speed = 400;
    private String id;
    private ArrayList<PersonRequest> waiting = new ArrayList<>();

    public NightElevator(Dispatcher dispatcher,int floor,int state,String id) {
        this.dispatcher = (NightDispatcher) dispatcher;
        this.floor = floor;
        this.state = state;
        this.id = id;
    }

    public void run() {
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
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TimableOutput.println("CLOSE-" + floor + "-" + id);
            }
            if (floor == 1) {
                synchronized (dispatcher) {
                    while (waiting.size() < max && dispatcher.get().size() > 0) {
                        int size = dispatcher.get().size();
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

    public int changeStateNig() {
        synchronized (dispatcher) {
            if (dispatcher.get().size() == 0 && inRequest.size() == 0) {
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
}
