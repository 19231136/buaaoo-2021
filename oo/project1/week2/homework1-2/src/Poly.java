import java.util.ArrayList;

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
                if (i > 1) {
                    ope2 = exp.charAt(i - 1) != '^' && exp.charAt(i - 1) != '*';
                } else {
                    ope2 = true;
                }
                if (i > 0 && depth == 0 && ope1 && ope2) {
                    if (exp.substring(start,i).startsWith("-")) {
                        term = new Term("-1*" + exp.substring(start + 1,i));
                    } else if (exp.substring(start).startsWith("+")) {
                        term = new Term(exp.substring(start + 1,i));
                    } else {
                        term = new Term(exp.substring(start,i));
                    }
                    this.terms.add(term);
                    start = i;
                }
            }
            if (i == exp.length() - 1) {
                if (exp.substring(start).startsWith("-")) {
                    term = new Term("-1*" + exp.substring(start + 1));
                } else if (exp.substring(start).startsWith("+")) {
                    term = new Term(exp.substring(start + 1));
                } else {
                    term = new Term(exp.substring(start));
                }
                this.terms.add(term);
            }

        }
    }

    public String derivation() {
        String out = "";
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
            out = out + terms.get(i).better();
        }
        return "+(" + out + ")";
    }

}
