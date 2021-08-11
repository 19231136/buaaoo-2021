public class Computer extends OtherS {
    private String special;

    public Computer(String name,double price,long num,long year,String special) {
        super(name,price,num,year);
        this.special = special;
    }

    public String get_information() {
        String information = super.get_information();
        return information + " " + special;
    }

    public String information() {
        String information = super.information();
        return information + " " + special;
    }

    public String getSpecial() {
        return special;
    }

    public int get_type() {
        return 7;
    }
}
