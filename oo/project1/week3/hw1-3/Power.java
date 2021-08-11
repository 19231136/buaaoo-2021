import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Power extends Factor {
    private BigInteger degree = BigInteger.ZERO;

    public Power(String s) {
        String rePower = "^x(\\^[+-]?\\d+)?$";
        Pattern pattern = Pattern.compile(rePower);
        Matcher matcher = pattern.matcher(s);
        if (!matcher.find()) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        int index = s.indexOf("^");
        if (index != -1) {
            degree = new BigInteger(s.substring(index + 1));
            if (degree.compareTo(BigInteger.valueOf(50)) > 0 ||
                    degree.compareTo(BigInteger.valueOf(-50)) < 0) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
        } else {
            degree = BigInteger.ONE;
        }
    }

    public BigInteger getDegree() {
        return degree;
    }

    @Override
    public String derivation() {
        if (degree.equals((BigInteger.ZERO))) {
            return "0";
        } else if (degree.equals(BigInteger.ONE)) {
            return "1";
        } else if (degree.equals(BigInteger.valueOf(2))) {
            return "2*x";
        } else {
            return degree.toString() + "*x^" + degree.subtract(BigInteger.ONE).toString();
        }
    }

    @Override
    public String toString() {
        if (degree.equals(BigInteger.ZERO)) {
            return "1";
        } else if (degree.equals(BigInteger.ONE)) {
            return "x";
        } else {
            return "x^" + degree.toString();
        }
    }
}
