public class Expression {
    private String exp;

    public Expression(String exp) {
        this.exp = exp;
    }

    public String change() {
        exp = exp.replaceAll("[ \\t]*","");
        exp = exp.replaceAll("\\+{3}","+");
        exp = exp.replaceAll("-{3}","-");
        exp = exp.replaceAll("\\+\\+-","-");
        exp = exp.replaceAll("\\+-\\+","-");
        exp = exp.replaceAll("-\\+\\+","-");
        exp = exp.replaceAll("\\+--","+");
        exp = exp.replaceAll("-\\+-","+");
        exp = exp.replaceAll("--\\+","+");
        exp = exp.replaceAll("\\+\\+","\\+");
        exp = exp.replaceAll("\\+-","-");
        exp = exp.replaceAll("-\\+","-");
        exp = exp.replaceAll("--","\\+");
        exp = exp.replaceAll("\\*\\*","^");
        return exp;
    }
}
