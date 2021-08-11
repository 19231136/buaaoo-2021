public class Novel extends OtherA {
    private boolean finish;

    public Novel(String name,double price,long num,long age,boolean finish) {
        super(name,price,num,age);
        this.finish = finish;
    }

    public String get_information() {
        String information = super.get_information();
        return information + " " + String.valueOf(finish);
    }

    public int get_type() {
        return 4;
    }
}
