import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class InputThread extends Thread {
    private RequestQueue unsReqQue;
    private String arrivePattern;

    InputThread(RequestQueue reqQue) {
        this.unsReqQue = reqQue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        arrivePattern = elevatorInput.getArrivingPattern();
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest(); // get input
            synchronized (unsReqQue) {
                if (request == null) {
                    unsReqQue.close();
                    unsReqQue.notifyAll(); // wake up every thread
                    return; // terminate the InputThread
                } else {
                    if (arrivePattern.equals("Night")) {
                        unsReqQue.addReq(request); // 不要提醒线程，
                    } else {
                        unsReqQue.addReq(request);
                        unsReqQue.notifyAll();
                    }

                }
            }
        }
    }

    public String getArrivePattern() {
        return arrivePattern;
    }
}
