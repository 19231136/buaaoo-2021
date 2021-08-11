import java.math.BigDecimal;

public class Bookset {
    private String name;
    private BigDecimal price;
    private int num;

    public Bookset(String name, BigDecimal price, int num) {
        this.name = name;
        this.price = price;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getNum() {
        return num;
    }

    public BigDecimal sum() {
        BigDecimal num1 = BigDecimal.valueOf(num);
        return num1.multiply(price);
    }
}
