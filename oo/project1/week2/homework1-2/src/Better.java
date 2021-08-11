public class Better {
    private String exp;

    public Better(String exp) {
        this.exp = exp;
    }

    public String better() {
        exp = exp.substring(1,exp.length() - 1);
        Poly poly = new Poly(exp);
        exp = poly.better();
        exp = exp.replaceAll("\\^","**");
        if (exp.startsWith("+")) {
            exp = exp.substring(1);
        }
        if (exp.startsWith("(")) {
            exp = exp.substring(1,exp.length() - 1);
        }
        exp = exp.replaceAll("(?<![\\d-])1\\*","");
        exp = exp.replaceAll("\\+\\+","+");
        exp = exp.replaceAll("\\+-","-");
        exp = exp.replaceAll("-\\+","-");
        exp = exp.replaceAll("--","+");
        exp = exp.replaceAll("\\*\\+","*");
        exp = exp.replaceAll("(?<![\\d])0\\+0","0");
        exp = exp.replaceAll("(?<![\\*])\\*1(?![\\d])","");
        exp = exp.replaceAll("\\+0\\+","+");
        if (exp.startsWith("+")) {
            exp = exp.substring(1);
        }
        return exp;
    }
}
