import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Term {
    private BigInteger coef = BigInteger.ONE;
    private BigInteger degree = BigInteger.ZERO;

    public Term(String s) {
        String re = "([+-]{0,3}(\\d+\\*)?x(\\*\\*[+|-]?\\d+)?)|[+-]{0,3}\\d+";
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(s);
        long symbol = 1;
        while (matcher.find()) {
            String sub = s.substring(matcher.start(),matcher.end());    //因子
            if (sub.startsWith("---") || sub.startsWith("-++")) {
                symbol = -symbol;
                sub = sub.substring(3);
            } else if (sub.startsWith("+-+") || sub.startsWith("++-")) {
                symbol = -symbol;
                sub = sub.substring(3);
            } else if (sub.startsWith("+--") || sub.startsWith("-+-") || sub.startsWith("--+")) {
                sub = sub.substring(3);
            } else if (sub.startsWith("++") || sub.startsWith("--")) {
                sub = sub.substring(2);
            } else if (sub.startsWith("+-") || sub.startsWith("-+")) {
                symbol = -symbol;
                sub = sub.substring(2);
            } else if (sub.startsWith("+")) {
                sub = sub.substring(1);
            } else if (sub.startsWith("-")) {
                symbol = -symbol;
                sub = sub.substring(1);
            }
            if (sub.indexOf("x") == -1) {
                coef = coef.multiply(new BigInteger(sub));
            }
            else if (sub.indexOf("x") == 0) {
                if (sub.indexOf("**") == -1) {
                    degree = degree.add(BigInteger.ONE);
                }
                else {
                    String[] out = sub.split("\\*\\*");
                    degree = degree.add(new BigInteger(out[1]));
                }
            }
            else {
                int index = sub.indexOf("*");
                String num = sub.substring(0,index);
                coef = coef.multiply(new BigInteger(num));
                if (sub.indexOf("**") == -1) {
                    degree = degree.add(BigInteger.ONE);
                }
                else {
                    int i = sub.lastIndexOf("*");
                    degree = degree.add(new BigInteger(sub.substring(i + 1)));
                }
            }
        }
        coef = coef.multiply(BigInteger.valueOf(symbol));
    }

    public BigInteger getCoef() {
        return coef;
    }

    public BigInteger getDegree() {
        return degree;
    }
}