import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputHandler implements Runnable {
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
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                dispatcher.setEnd();
                break;
            } else {
                dispatcher.add(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
