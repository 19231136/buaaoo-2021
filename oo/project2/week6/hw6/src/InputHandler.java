import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import com.oocourse.elevator2.ElevatorInput;
import java.io.IOException;

public class InputHandler implements Runnable {
    private String pattern;
    private Controller controller;

    public InputHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        pattern = elevatorInput.getArrivingPattern();
        controller.setStrategy(pattern);
        for (int i = 1; i <= 3; i++) {
            controller.addElevator(Integer.toString(i));
        }
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                controller.setEnd();
                break;
            } else {
                if (request instanceof PersonRequest) {
                    PersonRequest perReq = (PersonRequest)request;
                    controller.allocate(perReq);
                } else {
                    ElevatorRequest elevatorRequest = (ElevatorRequest)request;
                    controller.addElevator(elevatorRequest.getElevatorId());
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
