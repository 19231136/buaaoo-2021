import com.oocourse.elevator1.PersonRequest;
import com.oocourse.TimableOutput;

import java.util.ArrayList;
import java.util.Iterator;

public class Elevator extends Thread {

    // private char type; //A或B或C型

    // private int id; // 电梯的编号
    private final int highestFloor = 20;
    private final int lowestFloor = 1;
    private final int infPos = 10000; // positive infinity
    private final int infNeg = -10000; // negative infinity

    private final int maxNum; //最大容量
    private int curNum; // 当前运载人数
    private final int msPerFloor; // 单层运行时间
    private final int doorCloseTime;
    private final int doorOpenTime;

    private int curFloor; //当前楼层
    private int dir; // -1:down, 1:up
    private int pathGuid;

    private final RequestQueue reqQueInElev;   // requests in elev, people in elev
    private final RequestQueue reqQueOutElev; // requests out of elev, signed tasks
    private RequestQueue reqAll; // all the requests

    private final ArrayList<Integer> upPath = new ArrayList<>();
    private final ArrayList<Integer> downPath = new ArrayList<>();

    private int targetFloor;

    public Elevator(int capacity, int curFloor,
                    int msPerFloor, int doorCloseTime, int doorOpenTime,
                    RequestQueue reqQueInElev, RequestQueue reqQueOutElev) {
        this.maxNum = capacity;
        this.curFloor = curFloor;
        this.msPerFloor = msPerFloor;
        this.doorCloseTime = doorCloseTime;
        this.doorOpenTime = doorOpenTime;
        this.reqQueInElev = reqQueInElev;
        this.reqQueOutElev = reqQueOutElev;
    }

    @Override
    public void run() {
        /*
         * 1. check stop
         * 2. check wait
         * 3. adjust edge dir
         * 4. get target floor
         * 5. move to target floor
         * 6. door open
         * 6. get out people
         * 7. get in people
         * 8. door close
         * 8. back to 1
         * */
        while (true) {
            synchronized (reqQueOutElev) {
                if (reqQueOutElev.inputEnded() &&
                        reqQueOutElev.isEmpty() && reqQueInElev.isEmpty()) {
                    break; // elevator is over
                } else if (reqQueOutElev.isEmpty() && reqQueInElev.isEmpty()) {
                    // both empty, but not closed
                    try {
                        reqQueOutElev.wait(); // wait for assign someone for elev
                        if (reqQueOutElev.inputEnded() && reqQueOutElev.isEmpty()) {
                            break; // after wake up, found (input end & no req), bye
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // make sure there is some in/out elev
            adjustEdgeDirection();
            targetFloor = getTargetFloor();
            move(targetFloor);
            openDoor();
            getPeopleOut();
            getPeopleIn();
            closeDoor();
        }

    }

    public int getTargetFloor() {
        if (!reqQueInElev.isEmpty()) { // some one in elev
            targetFloor = getTargetFloorByInAndOut();
        } else {
            targetFloor = getTargetFloorByOut();
        }
        return targetFloor;
    }

    public int getTargetFloorByInAndOut() {
        // make sure: reqIn is NOT empty, direction adjust
        if (allInWannaUp()) {
            dir = 1;
            if (maxNum == curNum) {
                // full
                targetFloor = getNearestToFloorByDir(dir, curFloor);
            } else {
                // not full
                targetFloor = getNearestToFromFloorByDir(dir, curFloor);
            }
        } else if (allInWannaDown()) {
            dir = -1;
            if (maxNum == curNum) {
                targetFloor = getNearestToFloorByDir(dir, curFloor);
            } else {
                targetFloor = getNearestToFromFloorByDir(dir, curFloor);
            }
        } else { // someone up, someone down
            if (maxNum == curNum) { // full
                if (pathGuid == 1) { // guid up
                    dir = 1;
                    targetFloor = getNearestToFloorByDir(dir, curFloor);
                } else if (pathGuid == -1) { // guid down
                    dir = -1;
                    targetFloor = getNearestToFloorByDir(dir, curFloor);
                } else { // guid got cleared, go along
                    targetFloor = getNearestToFloorByDir(dir, curFloor);
                }
            } else { // not full
                if (pathGuid == 1) {
                    dir = 1;
                    targetFloor = getNearestToFromFloorByDir(dir, curFloor);
                } else if (pathGuid == -1) {
                    dir = -1;
                    targetFloor = getNearestToFromFloorByDir(dir, curFloor);
                } else {
                    targetFloor = getNearestToFromFloorByDir(dir, curFloor);
                }
            }
        }
        pathGuid = 0;
        return targetFloor;
    }

    public int getNearestToFloorByDir(int dir, int curFloor) {
        // 前提：内部有人，方向已调整
        // 得到某一方向上，内部人员最近的toFloor
        // 与 contains/all 同时使用，保证存在性
        if (dir == 1) {
            int tmpFloor = infPos;
            for (PersonRequest pr : reqQueInElev.getReqQue()) {
                if (pr.getToFloor() >= curFloor && pr.getToFloor() < tmpFloor) {
                    tmpFloor = pr.getToFloor();
                }
            }
            return tmpFloor;
        } else {
            int tmpFloor = infNeg;
            for (PersonRequest pr : reqQueInElev.getReqQue()) {
                if (pr.getToFloor() <= curFloor && pr.getToFloor() > tmpFloor) {
                    tmpFloor = pr.getToFloor();
                }
            }
            return tmpFloor;
        }
    }

    public int getNearestToFromFloorByDir(int dir, int curFloor) {
        /*
         * 为实现可稍带，要从
         * {所有人的toFloor} || {潜在的可稍带outQeq的fromFloor}
         * 中，选择一个floor
         * 配合contains/all使用
         * */
        if (dir == 1) {
            int tmpFloor = infPos;
            for (PersonRequest pr : reqQueInElev.getReqQue()) {
                if (pr.getToFloor() > curFloor && pr.getToFloor() < tmpFloor) {
                    tmpFloor = pr.getToFloor();
                }
            }

            for (PersonRequest pr : reqQueOutElev.getReqQue()) {
                if (curFloor < pr.getFromFloor() &&
                        pr.getFromFloor() < pr.getToFloor() &&
                        pr.getFromFloor() < tmpFloor) {
                    tmpFloor = pr.getFromFloor();
                }
            }
            return tmpFloor;
        } else {
            int tmpFloor = infNeg;
            for (PersonRequest pr : reqQueInElev.getReqQue()) {
                if (pr.getToFloor() < curFloor && pr.getToFloor() > tmpFloor) {
                    tmpFloor = pr.getToFloor();
                }
            }

            for (PersonRequest pr : reqQueOutElev.getReqQue()) {
                if (pr.getFromFloor() > pr.getToFloor() &&
                        pr.getFromFloor() < curFloor &&
                        pr.getFromFloor() > tmpFloor) {
                    tmpFloor = pr.getFromFloor();
                }
            }
            return tmpFloor;
        }
    }

    public int getTargetFloorByOut() {
        /* the elev is empty
         * choose a path to solve
         * remember to mark a pathGuid,
         * told the elev to turn if down to get up
         * */
        updatePath();
        if (curFloor == highestFloor) { // at the highest floor
            if (downPath.size() == 0) { // no down path, try to solve the up path
                targetFloor = upPath.get(0);
                pathGuid = 1;
            } else {
                targetFloor = downPath.get(0);
                pathGuid = -1;
            }
        } else if (curFloor == lowestFloor) { // do not care the benefit
            if (upPath.size() != 0) { // upPath exists,
                targetFloor = upPath.get(0);
                pathGuid = 1;
            } else { // no up path
                targetFloor = downPath.get(0);
                pathGuid = -1;
            }
        } else { // at middle floor
            if (upPath.size() == 0) { // no one up
                targetFloor = downPath.get(0);
            } else if (downPath.size() == 0) { // no one down
                targetFloor = upPath.get(0);
            } else { // some one up, some one down, compare the benefit
                int distUp = Math.abs(upPath.get(0) - curFloor) +
                        Math.abs(upPath.get(0) - upPath.get(1));
                int distDown = Math.abs(downPath.get(0) - curFloor) +
                        Math.abs(downPath.get(0) - downPath.get(1));
                double upBnf = 1.0 * upPath.get(2) / distUp;
                double downBnf = 1.0 * downPath.get(2) / distDown;
                if (upBnf >= downBnf) {
                    targetFloor = upPath.get(0);
                    pathGuid = 1;
                } else {
                    targetFloor = downPath.get(0);
                    pathGuid = -1;
                }
            }
        }
        dir = (targetFloor > curFloor) ? 1 : -1;
        return targetFloor;
    }

    private void updatePath() {
        upPath.clear();
        downPath.clear();
        synchronized (reqQueOutElev) {
            for (PersonRequest pr : reqQueOutElev.getReqQue()) {
                if (pr.getFromFloor() < pr.getToFloor()) { // someone up
                    if (upPath.size() == 0) {
                        upPath.add(0, pr.getFromFloor());
                        upPath.add(1, pr.getToFloor());
                        upPath.add(2, 1);
                    } else {
                        upPath.set(2, upPath.get(2) + 1);
                        if (pr.getFromFloor() < upPath.get(0)) {
                            upPath.set(0, pr.getFromFloor());
                        }

                        if (pr.getToFloor() > upPath.get(1)) {
                            upPath.set(1, pr.getToFloor());
                        }
                    }
                } else { // some down
                    if (pr.getFromFloor() > pr.getToFloor()) {
                        if (downPath.size() == 0) {
                            downPath.add(0, pr.getFromFloor());
                            downPath.add(1, pr.getToFloor());
                            downPath.add(2, 1);
                        } else {
                            downPath.set(2, downPath.get(2) + 1);
                            if (pr.getFromFloor() > downPath.get(0)) {
                                downPath.set(0, pr.getFromFloor());
                            }

                            if (pr.getToFloor() < downPath.get(1)) {
                                downPath.set(1, pr.getToFloor());
                            }
                        }
                    }
                }
            }
        }
    }

    public void adjustEdgeDirection() {
        if (curFloor == highestFloor) { // at the highest floor
            dir = -1;
            return;
        } else if (curFloor == lowestFloor) { // at the lowest floor
            dir = 1;
            return;
        } else {
            return;
        }
    }

    public void move(int targetFloor) {
        if (targetFloor == curFloor) {
            // pass
        } else {
            int step = (curFloor < targetFloor) ? 1 : -1;
            while (curFloor != targetFloor) {
                sleep(msPerFloor);
                curFloor += step;
                TimableOutput.println("ARRIVE-" + curFloor);
            }
        }
    }

    public void openDoor() {
        TimableOutput.println("OPEN-" + curFloor);
        sleep(doorOpenTime);
    }

    public void getPeopleOut() {
        /*
         * people in elev, out
         * */
        if (curNum == 0) {
            return;
        } else {
            Iterator it = reqQueInElev.getReqQue().iterator();
            while (it.hasNext()) {
                PersonRequest pr = (PersonRequest) it.next();
                if (pr.getToFloor() == curFloor) {
                    it.remove(); //移除该对象
                    TimableOutput.println("OUT-" + pr.getPersonId() + "-" + curFloor);
                    curNum--;
                }
            }
        }
    }

    public void getPeopleIn() {
        /*
         * for those assigned but not get in
         * plz in
         * */
        synchronized (reqQueOutElev) {
            if (reqQueOutElev.isEmpty()) {
                return;
            } else {
                Iterator it = reqQueOutElev.getReqQue().iterator();
                while (it.hasNext()) {
                    PersonRequest pr = (PersonRequest) it.next();
                    if (pr.getFromFloor() == curFloor) {
                        if (curNum < maxNum) {
                            reqQueInElev.addReq(pr);
                            curNum += 1;
                            it.remove(); //移除该对象
                            TimableOutput.println("IN-" + pr.getPersonId() + "-" + curFloor);
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    public void closeDoor() {
        sleep(doorCloseTime);
        TimableOutput.println("CLOSE-" + curFloor);
    }

    public boolean containsInWannaUp() {
        // 对当前楼层而言，有人想上楼
        for (PersonRequest pr : reqQueInElev.getReqQue()) {
            if (pr.getToFloor() > curFloor) {
                return true;
            }
        }
        return false;
    }

    public boolean containsInWannaDown() {
        // 对当前楼层而言，有人想下楼
        for (PersonRequest pr : reqQueInElev.getReqQue()) {
            if (pr.getToFloor() < curFloor) {
                return true;
            }
        }
        return false;
    }

    public boolean allInWannaUp() {
        // 对于当前楼层而言，电梯中所有人的toFloor都在上面
        for (PersonRequest pr : reqQueInElev.getReqQue()) {
            if (pr.getToFloor() < curFloor) {
                return false;
            }
        }
        return true;
    }

    public boolean allInWannaDown() {
        for (PersonRequest pr : reqQueInElev.getReqQue()) {
            if (pr.getToFloor() > curFloor) {
                return false;
            }
        }
        return true;
    }

    public void endInput() {
        // no more input
        reqQueOutElev.close();
        reqAll.close();
    }

    public void sleep(int time) {
        try {
            Thread.currentThread().sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
