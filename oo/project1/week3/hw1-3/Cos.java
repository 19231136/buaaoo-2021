import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cos extends Factor {
    private BigInteger degree = BigInteger.ONE;
    private Factor factor;

    public Cos(String s) {
        String re = "^cos\\((?<func>.+)\\)(\\^(?<degree>[+-]?\\d+))?$";
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
            return "-1*sin(" + factor.toString() + ")*" + factor.derivation();
        } else if (degree.equals(BigInteger.valueOf(2))) {
            return "-2*cos(" + factor.toString() + ")*sin("
                    + factor.toString() + ")*" + factor.derivation();
        } else {
            return this.degree.multiply(BigInteger.valueOf(-1)).toString() + "*cos("
                    + factor.toString() + ")^" + degree.subtract(BigInteger.ONE).toString()
                    + "*sin(" + factor.toString() + ")*" + factor.derivation();
        }
    }

    public String toString() {
        if (degree.equals(BigInteger.ZERO)) {
            return "1";
        } else if (degree.equals(BigInteger.ONE)) {
            return "cos(" + factor.toString() + ")";
        } else {
            return "cos(" + factor.toString() + ")^" + degree.toString();
        }
    }

    public BigInteger getDegree() {
        return degree;
    }
}
