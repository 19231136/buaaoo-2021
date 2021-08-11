import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] argv) {
        TimableOutput.initStartTimestamp();
        Dispatcher dispatcher = new Dispatcher();
        InputHandler inputHandler = new InputHandler(dispatcher);
        Thread input = new Thread(inputHandler);
        Elevator ele = new Elevator(dispatcher,1,0);
        Thread elevator = new Thread(ele);
        input.start();
        elevator.start();
    }
}
