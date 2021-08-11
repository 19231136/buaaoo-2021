public class OtherA extends Bookset {
    private int age;

    public OtherA(String name,double price,int num,int age) {
        super(name,price,num);
        this.age = age;
    }

    public String get_information() {
        String information = super.get_information();
        return information + " " + String.valueOf(age);
    }

    public int get_type() {
        return 2;
    }
}
