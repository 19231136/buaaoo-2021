import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] argc) {
        TimableOutput.initStartTimestamp();
        Controller controller = new Controller();
        InputHandler inputHandler = new InputHandler(controller);
        Thread thread = new Thread(inputHandler);
        thread.start();
    }
}
