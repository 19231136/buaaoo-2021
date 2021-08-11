import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sin extends Factor {
    private BigInteger degree = BigInteger.ONE;
    private Factor factor;

    public Sin(String s) {
        String re = "^sin\\((?<func>.+)\\)(\\^(?<degree>[+-]?\\d+))?$";
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(s);
        matcher.find();
        String str = matcher.group("func");
        factor = new Factory().factory(str);
        if (matcher.group("degree") != null) {
            degree = new BigInteger(matcher.group("degree"));
            if (degree.compareTo(BigInteger.valueOf(50)) > 0 ||
                    degree.compareTo(BigInteger.valueOf(-50)) < 0) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
        }
    }

    public Factor getFactor() {
        return factor;
    }

    @Override
    public String derivation() {
        if (degree.equals((BigInteger.ZERO))) {
            return "0";
        } else if (degree.equals(BigInteger.ONE)) {
            return "cos(" + factor.toString() + ")*" + factor.derivation();
        } else if (degree.equals(BigInteger.valueOf(2))) {
            return "2*sin(" + factor.toString() + ")*cos("
                    + factor.toString() + ")*" + factor.derivation();
        } else {
            return degree.toString() + "*sin(" + factor.toString() + ")^"
                    + degree.subtract(BigInteger.ONE).toString()
                    + "*cos(" + factor.toString() + ")*" + factor.derivation();
        }

    }

    @Override
    public String toString() {
        if (degree.equals(BigInteger.ZERO)) {
            return "1";
        } else if (degree.equals(BigInteger.ONE)) {
            return "sin(" + factor.toString() + ")";
        } else {
            return "sin(" + factor.toString() + ")^" + degree.toString();
        }
    }

    public BigInteger getDegree() {
        return degree;
    }
}
