public class Poetry extends OtherA {
    private String author;

    public Poetry(String name,double price,int num,int age,String author) {
        super(name,price,num,age);
        this.author = author;
    }

    public String get_information() {
        String information = super.get_information();
        return information + " " + author;
    }

    public int get_type() {
        return 5;
    }
}
