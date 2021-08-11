import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Poly {
    private Map<BigInteger,BigInteger> map = new HashMap<>();

    public void add(Term term) {
        BigInteger coef = term.getCoef();
        BigInteger degree = term.getDegree();
        if (!coef.equals(BigInteger.ZERO)) {
            if (map.containsKey(degree)) {
                coef = coef.add(map.get(degree));
                if (coef.equals(BigInteger.ZERO)) {
                    map.remove(degree);
                }
                else {
                    map.put(degree,coef);
                }
            }
            else {
                map.put(degree,coef);
            }
        }
    }

    public String derivation() {
        Set<BigInteger> keyset = map.keySet();
        keyset.stream().sorted();
        String out = "";
        for (BigInteger degree : keyset) {
            if (!degree.equals(BigInteger.ZERO)) {
                BigInteger coef = map.get(degree);
                coef = coef.multiply(degree);
                degree = degree.subtract(BigInteger.ONE);
                String out1 = "";   //系数*
                if (coef.equals(BigInteger.ONE)) {
                    out1 = "+";
                }
                else if (coef.equals(BigInteger.valueOf(-1))) {
                    out1 = "-";
                }
                else {
                    if (coef.compareTo(BigInteger.ZERO) == 1) { //>
                        out1 = "+" + String.valueOf(coef) + "*";
                    }
                    else {
                        out1 = String.valueOf(coef) + "*";
                    }
                }
                if (coef.equals(BigInteger.ONE) && degree.equals(BigInteger.ZERO)) {
                    out1 = "+1";
                }
                if (coef.equals(BigInteger.valueOf(-1)) && degree.equals(BigInteger.ZERO)) {
                    out1 = "-1";
                }
                String out2 = "";   //x
                if (!degree.equals(BigInteger.ZERO)) {
                    out2 = "x";
                }
                String out3 = "";   //**指数
                if (!degree.equals(BigInteger.ONE) && !degree.equals(BigInteger.ZERO)) {
                    out3 = "**" + String.valueOf(degree);
                }
                String out4 = out1 + out2 + out3;
                if (out4.length() != 0 && out4.lastIndexOf("*") == out4.length() - 1) {
                    out4 = out4.substring(0,out4.length() - 1);
                }
                out = out4 + out;
            }
        }
        return out;
    }

}
