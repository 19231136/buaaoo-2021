import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Poly extends Factor {
    private ArrayList<Term> terms = new ArrayList<>();

    public Poly(String exp) {
        int depth = 0;
        int start = 0;
        for (int i = 0;i < exp.length();i++) {
            Term term;
            if (exp.charAt(i) == '(') {
                depth += 1;
            } else if (exp.charAt(i) == ')') {
                depth -= 1;
            } else if (i > 0) {
                boolean ope1 = exp.charAt(i) == '+' || exp.charAt(i) == '-';
                boolean ope2;
                if (i > 0) {
                    ope2 = exp.charAt(i - 1) != '^' && exp.charAt(i - 1) != '*'
                        && exp.charAt(i - 1) != '+' && exp.charAt(i - 1) != '-';
                } else {
                    ope2 = true;
                }
                if (i > 0 && depth == 0 && ope1 && ope2) {
                    String sub = exp.substring(start,i);
                    if (sub.startsWith("--") || sub.startsWith("++")) {
                        term = new Term(exp.substring(start + 2,i));
                    } else if (sub.startsWith("-+") || sub.startsWith("+-")) {
                        term = new Term(exp.substring(start + 2,i) + "*-1");
                    } else if (sub.startsWith("+")) {
                        term = new Term(exp.substring(start + 1,i));
                    } else if (sub.startsWith("-")) {
                        term = new Term(exp.substring(start + 1,i) + "*-1");
                    } else {
                        term = new Term(exp.substring(start,i));
                    }
                    this.terms.add(term);
                    start = i;
                }
            }
            if (i == exp.length() - 1) {
                String sub = exp.substring(start);
                if (sub.startsWith("-+") || sub.startsWith("+-")) {
                    term = new Term("-1*" + exp.substring(start + 2));
                } else if (sub.startsWith("--") || sub.startsWith("++")) {
                    term = new Term(exp.substring(start + 2));
                } else if (sub.startsWith("+")) {
                    term = new Term(exp.substring(start + 1));
                } else if (sub.startsWith("-")) {
                    term = new Term("-1*" + exp.substring(start + 1));
                } else {
                    term = new Term(exp.substring(start));
                }
                this.terms.add(term);
            }

        }
    }

    public String derivation() {
        String out = "";
        int j = 0;
        for (j = 0;j < terms.size();j++) {
            if (!terms.get(j).getFactors().isEmpty()) {
                break;
            }
        }
        if (j == terms.size()) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        for (int i = 0;i < terms.size();i++) {
            if (i == 0) {
                out = out + terms.get(i).derivation();
            } else {
                out = out + "+" + terms.get(i).derivation();
            }
        }
        return "(" + out + ")";
    }

    @Override
    public String toString() {
        String out = "";
        for (int i = 0;i < terms.size();i++) {
            if (i == 0) {
                out += terms.get(i).toString();
            } else {
                out += "+" + terms.get(i).toString();
            }
        }
        return "(" + out + ")";
    }

    public String better() {
        String out = "";
        for (int i = 0;i < terms.size();i++) {
            Term term = terms.get(i);
            HashMap<String, BigInteger> factorMap = term.better();
            if (!factorMap.get("Constant").equals(BigInteger.ZERO)) {
                out += factorMap.get("Constant").toString() + "*";
                factorMap.remove("Constant");
                if (!factorMap.get("Power").equals(BigInteger.ZERO)) {
                    if (factorMap.get("Power").equals(BigInteger.ONE)) {
                        out += "x*";
                    } else {
                        out += "x^" + factorMap.get("Power").toString() + "*";
                    }
                }
                factorMap.remove("Power");
                Set<String> set = factorMap.keySet();
                for (String str : set) {
                    if (str.startsWith("(")) { //表达式因子
                        out += str + "*";
                    } else {
                        BigInteger degree = factorMap.get(str);
                        if (!degree.equals(BigInteger.ZERO)) {
                            if (degree.equals(BigInteger.ONE)) {
                                out += str + "*";
                            } else {
                                out += str + "^" + degree + "*";
                            }
                        }
                    }
                }
            }
            if (!out.equals("") && out.charAt(out.length() - 1) == '*') {
                out = out.substring(0,out.length() - 1);
            }
            out += "+";
            out = out.replaceAll("\\+\\+","+");
            out = out.replaceAll("\\+-","-");
        }
        //HashMap<String, BigInteger> factorMap =;
        while (out.length() != 0 && out.charAt(out.length() - 1) == '+') {
            out = out.substring(0,out.length() - 1);
        }
        while (out.startsWith("+")) {
            out = out.substring(1);
        }
        if (out.equals("")) {
            out = "0";
        }
        return out;
    }

}
