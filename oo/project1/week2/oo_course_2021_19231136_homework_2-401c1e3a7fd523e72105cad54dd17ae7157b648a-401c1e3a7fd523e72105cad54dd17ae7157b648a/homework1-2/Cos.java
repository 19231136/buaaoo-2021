import java.math.BigInteger;

public class Cos extends Factor {
    private BigInteger degree = BigInteger.ONE;

    public Cos(String s) {
        int index = s.indexOf("^");
        if (index != -1) {
            degree = new BigInteger(s.substring(index + 1));
        }
    }

    @Override
    public String derivation() {
        if (degree.equals((BigInteger.ZERO))) {
            return "0";
        } else if (degree.equals(BigInteger.ONE)) {
            return "-sin(x)";
        } else if (degree.equals(BigInteger.valueOf(2))) {
            return "-2*cos(x)*sin(x)";
        } else {
            return "-" + degree.toString() + "*sin(x)*cos(x)^" +
                    degree.subtract(BigInteger.ONE).toString();
        }
    }

    public String toString() {
        if (degree.equals(BigInteger.ZERO)) {
            return "1";
        } else if (degree.equals(BigInteger.ONE)) {
            return "cos(x)";
        } else {
            return "cos(x)^" + degree.toString();
        }
    }

    public BigInteger getDegree() {
        return degree;
    }
}
