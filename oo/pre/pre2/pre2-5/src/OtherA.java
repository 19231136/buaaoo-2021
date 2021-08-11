public class OtherA extends Bookset {
    private long age;

    public OtherA(String name,double price,long num,long age) {
        super(name,price,num);
        this.age = age;
    }

    public String get_information() {
        String information = super.get_information();
        return information + " " + String.valueOf(age);
    }

    public String information() {
        String information = super.information();
        return information + " " + String.valueOf(age);
    }

    public long getAge() {
        return age;
    }

    public int get_type() {
        return 2;
    }
}
