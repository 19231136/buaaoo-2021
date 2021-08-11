import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] argv) {
        TimableOutput.initStartTimestamp();
        Dispatcher dispatcher = new Dispatcher();
        InputHandler inputHandler = new InputHandler(dispatcher);
        inputHandler.start();
    }
}
