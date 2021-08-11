import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] argv) {
        TimableOutput.initStartTimestamp();
        InputHandler inputHandler = new InputHandler();
        inputHandler.start();
    }
}
