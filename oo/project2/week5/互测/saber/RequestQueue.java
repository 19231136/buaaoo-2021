import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class RequestQueue {
    private ArrayList<PersonRequest> reqQue;
    private boolean inputEnd;

    RequestQueue() {
        reqQue = new ArrayList<PersonRequest>();
        inputEnd = false;
    }

    public void addReq(PersonRequest req) {
        if (req == null) { // 接受到null，说明不会再有输入了
            inputEnd = true;
        } else {
            reqQue.add(req);
        }
    }

    public void removeReq(PersonRequest req) {
        reqQue.remove(req);
    }

    public void endInput(boolean inputEnd) {
        this.inputEnd = inputEnd;
    }

    public int size() {
        return reqQue.size();
    }

    public PersonRequest get(int i) {
        return reqQue.get(i);
    }

    public void removeByIndex(int i) {
        reqQue.remove(i);
    }

    public boolean isEmpty() {
        return reqQue.isEmpty();
    }

    public PersonRequest getFirst() {
        if (reqQue.size() > 0) {
            PersonRequest pr = reqQue.get(0);
            return pr;
        } else {
            return null;
        }
    }

    public boolean inputEnded() {
        return this.inputEnd;
    }

    public void clearQue() {
        reqQue.clear();
    }

    public void close() {
        inputEnd = true;
    }

    public ArrayList<PersonRequest> getReqQue() {
        return reqQue;
    }

    public boolean containsWannaDir(int dir, int curFloor) {
        /* some one in the que,
        * its destination is on the dir
        * */
        if (dir == 1) {
            for (PersonRequest pr : this.getReqQue()) {
                if (pr.getToFloor() > curFloor) {
                    return true;
                } else {
                    // pass
                }
            }
            return false;
        } else if (dir == -1) {
            for (PersonRequest pr : this.getReqQue()) {
                if (pr.getToFloor() < curFloor) {
                    return true;
                } else {
                    // pass
                }
            }
            return false;
        } else {
            System.out.println("invalid direction");
            return false;
        }
    }
}

