import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

public class InputHandler extends Thread {
    private String pattern;
    private Dispatcher dispatcher;
    private Controller controller;

    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        pattern = elevatorInput.getArrivingPattern();
        pattern = "Random";
        dispatcher = new RandomDispatcher();
        controller = new RandomController((RandomDispatcher) dispatcher);
        Factory factory = new Factory();
        for (int i = 1;i <= 3;i++) {
            String type = "A";
            if (i == 2) {
                type = "B";
            } else if (i == 3) {
                type = "C";
            }
            Elevator elevator = factory.create(pattern,dispatcher,
                    Integer.toString(i),type,controller);
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
                    Person person = new Person(perReq);
                    controller.allocate(person);
                } else {
                    ElevatorRequest elevatorRequest = (ElevatorRequest)request;
                    String id = elevatorRequest.getElevatorId();
                    String type = elevatorRequest.getElevatorType();
                    Elevator elevator = factory.create(pattern,dispatcher,id,type,controller);
                    factory.start(elevator,pattern);
                    dispatcher.addElevator();
                }
            }
        }
    }
}
