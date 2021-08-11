import com.oocourse.elevator3.PersonRequest;

public class Person {
    private int id;
    private int from;
    private int tempDes;
    private int to;
    private boolean transfer;
    private String first;
    private String second;

    public Person(PersonRequest perReq) {
        this.id = perReq.getPersonId();
        this.from = perReq.getFromFloor();
        this.to = perReq.getToFloor();
        this.tempDes = this.to;
        setPerson();
        //System.out.println(id + first + second);
    }

    public void setPerson() {
        if ((from1() && to1()) || (from1() && to5()) ||
                (from5() && to1()) || (from5() && to5())) { //C
            first = "C";
            transfer = false;
        } else if ((from4() && to1()) || (from2() && to5())) { //AB+C
            transfer = true;
            first = "AB";
            second = "C";
            if (from4() && to1()) {
                tempDes = 18;
            } else {
                tempDes = 3;
            }
        } else if ((from5() && to2()) || (from1() && to4())) {  //C+AB
            transfer = true;
            first = "C";
            second = "AB";
            if (from5() && to2()) {
                tempDes = 3;
            } else {
                tempDes = 18;
            }
        } else { //AB
            first = "AB";
            transfer = false;
        }

    }

    public boolean from1() {
        return from >= 1 && from <= 3;
    }

    public boolean from2() {
        return from > 3 && from <= 5;
    }

    public boolean from3() {
        return from > 5 && from < 15;
    }

    public boolean from4() {
        return from >= 15 && from < 18;
    }

    public  boolean from5() {
        return from >= 18 && from <= 20;
    }

    public boolean to1() {
        return to >= 1 && to <= 3;
    }

    public boolean to2() {
        return to > 3 && to <= 5;
    }

    public boolean to3() {
        return to > 5 && to < 15;
    }

    public boolean to4() {
        return to >= 15 && to < 18;
    }

    public  boolean to5() {
        return to >= 18 && to <= 20;
    }

    public int getId() {
        return id;
    }

    public int getFrom() {
        return from;
    }

    public int getTempDes() {
        return tempDes;
    }

    public int getTo() {
        return to;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTempDes(int tempDes) {
        this.tempDes = tempDes;
    }

    public void setTransfer() {
        this.transfer = false;
    }

    public void setFirst(String first) {
        this.first = second;
    }
}
