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
}
