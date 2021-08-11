public class OtherS extends Bookset {
    private long year;

    public OtherS(String name,double price,long num,long year) {
        super(name,price,num);
        this.year = year;
    }

    public String get_information() {
        String information = super.get_information();
        return information + " " + String.valueOf(year);
    }

    public int get_type() {
        return 3;
    }

}
