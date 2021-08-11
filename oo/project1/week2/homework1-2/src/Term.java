import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private ArrayList<Factor> factors = new ArrayList<>();

    public Term(String term) {
        int depth = 0;
        int start = 0;
        Factor factor;
        for (int i = 0;i < term.length();i++) {
            if (term.charAt(i) == '(') {
                depth += 1;
            } else if (term.charAt(i) == ')') {
                depth -= 1;
            } else if (depth == 0 && term.charAt(i) == '*') {
                if (term.substring(start,i).startsWith("-")) {
                    factor = new Factory().factory(term.substring(start + 1,i));
                    Factor factor1 = new Factory().factory("-1");
                    this.factors.add(factor1);
                } else if (term.substring(start,i).startsWith("+")) {
                    factor = new Factory().factory(term.substring(start + 1,i));
                } else {
                    factor = new Factory().factory(term.substring(start,i));
                }
                this.factors.add(factor);
                start = i + 1;
            }
            if (i == term.length() - 1) {
                if (term.substring(start).startsWith("-")) {
                    Factor factor1 = new Factory().factory("-1");
                    this.factors.add(factor1);
                    factor = new Factory().factory(term.substring(start + 1));
                } else if (term.substring(start).startsWith("+")) {
                    factor = new Factory().factory(term.substring(start + 1));
                } else {
                    factor = new Factory().factory(term.substring(start));
                }
                this.factors.add(factor);
            }
        }
    }

    public String derivation() {
        Multiply multiply = new Multiply();
        return multiply.derivation(factors);
    }

    public String toString() {
        String out = "";
        for (int i = 0;i < factors.size();i++) {
            if (i == 0) {
                out += factors.get(i).toString();
            } else {
                out += "*" + factors.get(i).toString();
            }
        }
        return out;
    }

    public String better() {
        String out = "";
        BigInteger constant = BigInteger.ONE;
        BigInteger sindeg = BigInteger.ZERO;
        BigInteger cosdeg = BigInteger.ZERO;
        BigInteger powerdeg = BigInteger.ZERO;
        for (int i = 0;i < factors.size();i++) {
            if (factors.get(i) instanceof Constant) {
                constant = constant.multiply(((Constant) factors.get(i)).getNum());
            } else if (factors.get(i) instanceof Sin) {
                sindeg = sindeg.add(((Sin) factors.get(i)).getDegree());
            } else if (factors.get(i) instanceof Cos) {
                cosdeg = cosdeg.add(((Cos) factors.get(i)).getDegree());
            } else if (factors.get(i) instanceof Power) {
                powerdeg = powerdeg.add(((Power) factors.get(i)).getDegree());
            } else {
                out += "*" + ((Poly) factors.get(i)).better();
            }
        }
        if (constant.equals(BigInteger.ZERO)) {
            return "+0";
        }
        else {
            out += "*+" + constant.toString();
            if (powerdeg.equals(BigInteger.ZERO)) {
                out += "";
            } else if (powerdeg.equals(BigInteger.ONE)) {
                out += "*x";
            } else {
                out += "*x^" + powerdeg.toString();
            }
            if (sindeg.equals(BigInteger.ZERO)) {
                out += "";
            } else if (sindeg.equals(BigInteger.ONE)) {
                out += "*sin(x)";
            } else {
                out += "*sin(x)^" + sindeg.toString();
            }
            if (cosdeg.equals(BigInteger.ZERO)) {
                out += "";
            } else if (cosdeg.equals(BigInteger.ONE)) {
                out += "*cos(x)";
            } else {
                out += "*cos(x)^" + cosdeg.toString();
            }
        }
        if (out.startsWith("*")) {
            return out.substring(1);
        }
        if (out.charAt(out.length() - 1) == '*') {
            return out.substring(0,out.length() - 1);
        }
        return "+" + out;
    }

}
