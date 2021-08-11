import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NightDispatcher extends Dispatcher {
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
            sort();
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

    public void sort() {
        Comparator<PersonRequest> comparator = new Comparator<PersonRequest>() {
            @Override
            public int compare(PersonRequest o1, PersonRequest o2) {
                if (o1.getFromFloor() == o2.getFromFloor()) {
                    return 0;
                } else if (o1.getFromFloor() > o2.getFromFloor()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
        Collections.sort(request,comparator);
    }
}
