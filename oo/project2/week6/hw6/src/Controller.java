import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class Controller {
    private ArrayList<Elevator> elevators = new ArrayList<>();
    private String pattern;
    private boolean end = false;
    private int flag = 0;

    public void setStrategy(String pattern) {
        this.pattern = pattern;
    }

    public void allocate(PersonRequest perReq) {
        int size = elevators.size();
        Elevator elevator = elevators.get(flag);
        elevator.getDispatcher().add(perReq);
        flag++;
        if (flag > size - 1) {
            flag = 0;
        }
    }

    public void addElevator(String id) {
        Dispatcher dispatcher = new Dispatcher(pattern);
        Elevator elevator = new Elevator(dispatcher,1,0,id);
        elevators.add(elevator);
        elevator.start();
    }

    public void setEnd() {
        synchronized (this) {
            this.end = true;
            for (int i = 0;i < elevators.size();i++) {
                elevators.get(i).getDispatcher().setEnd();
            }
            notifyAll();
        }
    }

}
