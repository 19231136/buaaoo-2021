import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;

public class InputHandler extends Thread {
    private String pattern;
    private Dispatcher dispatcher;

    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        pattern = elevatorInput.getArrivingPattern();
        if (pattern.equals("Random")) {
            dispatcher = new RandomDispatcher();
        } else if (pattern.equals("Morning")) {
            dispatcher = new MorningDispatcher();
        } else {
            dispatcher = new NightDispatcher();
        }
        Factory factory = new Factory();
        for (int i = 1;i <= 3;i++) {
            Elevator elevator = factory.create(pattern,dispatcher,Integer.toString(i));
            factory.start(elevator,pattern);
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
                    Elevator elevator = factory.create(pattern,dispatcher,id);
                    factory.start(elevator,pattern);
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
