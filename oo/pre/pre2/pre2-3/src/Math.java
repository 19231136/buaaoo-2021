public class Math extends OtherS {
    private int grade;

    public Math(String name,double price,int num,int year,int grade) {
        super(name,price,num,year);
        this.grade = grade;
    }

    public String get_information() {
        String information = super.get_information();
        return information + " " + String.valueOf(grade);
    }

    public int get_type() {
        return 6;
    }
}
