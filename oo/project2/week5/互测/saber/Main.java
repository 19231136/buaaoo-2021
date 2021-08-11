import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        try {
            TimableOutput.initStartTimestamp();
            // create share obj
            RequestQueue unsReqQue = new RequestQueue();
            RequestQueue reqQueInElev = new RequestQueue();
            RequestQueue reqQueOutElev = new RequestQueue();

            Elevator elev = new Elevator(6, 1, 400, 200, 200, reqQueInElev, reqQueOutElev);
            elev.start();

            InputThread inputThread = new InputThread(unsReqQue);
            inputThread.start();

            Schedule schedule = new Schedule(unsReqQue, reqQueOutElev);
            schedule.start();


        } catch (Exception e) {
            System.exit(0);
        }
    }
}
