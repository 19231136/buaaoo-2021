import java.math.BigInteger;

public class Power extends Factor {
    private BigInteger degree = BigInteger.ZERO;

    public Power(String s) {
        int index = s.indexOf("^");
        if (index != -1) {
            degree = new BigInteger(s.substring(index + 1));
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
