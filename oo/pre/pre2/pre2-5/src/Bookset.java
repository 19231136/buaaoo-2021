public class Bookset {
    private String name;
    private double price;
    private long num;

    public Bookset(String name, double price, long num) {
        this.name = name;
        this.price = price;
        this.num = num;
    }

    public String get_information() {
        String information = name + " " + String.valueOf(price) + " " + String.valueOf(num);
        return information;
    }

    public String information() {
        String information = name + " " + String.valueOf(price);
        return information;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public long getNum() {
        return num;
    }

    public double sum() {
        return num * price;
    }

    public int get_type() {
        return 1;
    }

    public long getAge() {
        return 0;
    }

    public long getYear() {
        return 0;
    }

    public boolean isFinish() {
        return true;
    }

    public String getAuthor() {
        return "";
    }

    public long getGrade() {
        return 0;
    }

    public String getSpecial() {
        return "";
    }
}
