public class Schedule extends Thread {
    private RequestQueue unsReqQue; // 未被分配的人们
    private RequestQueue reqQueOutEle;

    public Schedule(RequestQueue unsReqQue, RequestQueue reqQueOutEle) {
        this.unsReqQue = unsReqQue;
        this.reqQueOutEle = reqQueOutEle;
    }

    @Override
    public void run() {
        /*
        * 1. check is over or not
        * 2.
        * */
        while (true) {
            synchronized (unsReqQue) {
                if (unsReqQue.isEmpty() && unsReqQue.inputEnded()) {
                    synchronized (reqQueOutEle) {
                        reqQueOutEle.close();
                        // elev once wait for a empty & havn't ended reqQue,
                        // now wait it up
                        reqQueOutEle.notifyAll();
                        // no more input & all people assigned, schedule over
                        return;
                    }
                }

                if (unsReqQue.isEmpty()) { // tho empty but input not ended
                    try {
                        unsReqQue.wait(); // wait for InputThread
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // uns not empty,
                // put all req in uns to reqQue,
                // then check should close
                synchronized (reqQueOutEle) {
                    for (int i = 0; i < unsReqQue.size(); i++) {
                        reqQueOutEle.addReq(unsReqQue.get(i));
                    }
                    unsReqQue.clearQue(); // unsigned req have all been put into reqOutElev
                    reqQueOutEle.notifyAll();
                }

                // I think, check close and close not need to lock
                synchronized (reqQueOutEle) {
                    if (unsReqQue.inputEnded()) {
                        reqQueOutEle.close();
                        reqQueOutEle.notifyAll();
                    }
                }
            }
        }
    }
}
