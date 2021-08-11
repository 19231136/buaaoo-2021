public class OtherS extends Bookset {
    private int year;

    public OtherS(String name,double price,int num,int year) {
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
