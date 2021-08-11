import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class RandomDispatcher extends Dispatcher {
    private boolean end = false;
    private ArrayList<PersonRequest> request = new ArrayList();

    public void setEnd() {
        synchronized (this) {
            this.end = true;
            this.notifyAll();
        }
    }

    public void add(PersonRequest personRequest) {
        synchronized (this) {
            request.add(personRequest);
            notifyAll();
        }
    }

    public ArrayList<PersonRequest> get() {
        synchronized (this) {
            return request;
        }
    }

    public synchronized boolean getEnd() {
        return end;
    }
}
