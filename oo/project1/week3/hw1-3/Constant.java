import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant extends Factor {
    private BigInteger num;

    public BigInteger getNum() {
        return num;
    }

    public Constant(String s) {
        String reCostant = "^[\\+-]?\\d+$";
        Pattern pattern = Pattern.compile(reCostant);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            num = new BigInteger(s);
        } else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
    }

    @Override
    public String derivation() {
        return "0";
    }

    public String toString() {
        return this.num.toString();
    }
}
