import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Term {
    private ArrayList<Factor> factors = new ArrayList<>();

    public Term(String term) {
        int depth = 0;
        int start = 0;
        Factor factor;
        for (int i = 0; i < term.length(); i++) {
            if (term.charAt(i) == '(') {
                depth += 1;
            } else if (term.charAt(i) == ')') {
                depth -= 1;
            } else if (depth == 0 && term.charAt(i) == '*') {
                factor = new Factory().factory(term.substring(start, i));
                this.factors.add(factor);
                start = i + 1;
            }
            if (i == term.length() - 1) {
                factor = new Factory().factory(term.substring(start));
                this.factors.add(factor);
            }
        }
    }

    public String derivation() {
        Multiply multiply = new Multiply();
        String out = multiply.derivation(factors);
        if (out.charAt(out.length() - 1) == '*') {
            out = out.substring(0,out.length() - 1);
        }
        return out;
    }

    public String toString() {
        String out = "";
        for (int i = 0; i < factors.size(); i++) {
            if (i == 0) {
                out += factors.get(i).toString();
            } else {
                out += "*" + factors.get(i).toString();
            }
        }
        return out;
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public HashMap better() {
        HashMap<String,BigInteger> factorMap = new HashMap<>();
        factorMap.put("Constant",BigInteger.ONE);
        factorMap.put("Power",BigInteger.ZERO);
        String out = "";
        for (int i = 0;i < factors.size();i++) {
            Factor factor = factors.get(i);
            if (factor instanceof Constant) {
                BigInteger constant = factorMap.get("Constant");
                constant = constant.multiply(((Constant) factor).getNum());
                factorMap.put("Constant",constant);
            } else if (factor instanceof Power) {
                BigInteger degree = factorMap.get("Power");
                degree = degree.add(((Power) factor).getDegree());
                factorMap.put("Power",degree);
            } else if (factor instanceof Sin) {
                String str = "sin(" + ((Sin)factor).getFactor().toString() + ")";
                BigInteger degree;
                if (!factorMap.containsKey(str)) {
                    degree = ((Sin) factor).getDegree();
                } else {
                    degree = factorMap.get(str);
                    degree = degree.add(((Sin) factor).getDegree());
                }
                factorMap.put(str,degree);
            } else if (factor instanceof Cos) {
                String str = "cos(" + ((Cos)factor).getFactor().toString() + ")";
                BigInteger degree;
                if (!factorMap.containsKey(str)) {
                    degree = ((Cos) factor).getDegree();
                } else {
                    degree = factorMap.get(str);
                    degree = degree.add(((Cos) factor).getDegree());
                }
                factorMap.put(str,degree);
            } else {
                out += factor.toString() + "*";
            }
        }
        if (!out.equals("") && out.charAt(out.length() - 1) == '*') {
            out = out.substring(0,out.length() - 1);
            factorMap.put(out,BigInteger.ZERO);
        }
        return factorMap;
    }

}
