import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;

public class InputHandler extends Thread {
    private String pattern;
    private Dispatcher dispatcher;

    public InputHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        pattern = elevatorInput.getArrivingPattern();
        dispatcher.setStrategy(pattern);
        for (int i = 1;i <= 3;i++) {
            Elevator elevator = new Elevator(dispatcher,1,0,Integer.toString(i));
            elevator.start();
        }
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                dispatcher.setEnd();
                break;
            } else {
                if (request instanceof PersonRequest) {
                    PersonRequest perReq = (PersonRequest)request;
                    dispatcher.add(perReq);
                } else {
                    ElevatorRequest elevatorRequest = (ElevatorRequest)request;
                    String id = elevatorRequest.getElevatorId();
                    Elevator elevator = new Elevator(dispatcher,1,0,id);
                    elevator.start();
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
