import java.math.BigInteger;

public class Sin extends Factor {
    private BigInteger degree = BigInteger.ONE;

    public Sin(String s) {
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
            return "cos(x)";
        } else if (degree.equals(BigInteger.valueOf(2))) {
            return "2*cos(x)*sin(x)";
        } else {
            return degree.toString() + "*cos(x)*sin(x)^" +
                    degree.subtract(BigInteger.ONE).toString();
        }
    }

    @Override
    public String toString() {
        if (degree.equals(BigInteger.ZERO)) {
            return "1";
        } else if (degree.equals(BigInteger.ONE)) {
            return "sin(x)";
        } else {
            return "sin(x)^" + degree.toString();
        }
    }

    public BigInteger getDegree() {
        return degree;
    }
}
