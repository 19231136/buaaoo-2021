import java.math.BigInteger;

public class Constant extends Factor {
    private BigInteger num;

    public BigInteger getNum() {
        return num;
    }

    public Constant(String s) {
        num = new BigInteger(s);
    }

    @Override
    public String derivation() {
        return "0";
    }

    public String toString() {
        return this.num.toString();
    }
}
