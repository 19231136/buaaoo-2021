import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
    private String exp;
    public static final String CHAR = "^[\\(\\)\\d-\\+xcosin\\* \\t]+$";
    public static final String SC = "(s[ \\t]+i)|(i[ \\t]+n)|(c[ \\t]+o)|(o[ \\t]+s)";
    public static final String SYM1 = "[-\\+]([ \\t]+)?[-\\+][ \\t]+\\d";
    public static final String SYM2 = "[\\*\\(]([ \\t]+)?[\\+-][ \\t]+\\d";
    public static final String POWER = "\\*[ \\t]+\\*";
    public static final String CON = "\\d[ \\t]+\\d";

    public Expression(String exp) {
        this.exp = exp.replaceAll("[ \\t]+"," ");
    }

    public String change() throws Exception2 {
        Matcher matcher = matcher(CHAR,exp);
        if (!matcher.find()) {
            throw new Exception2();
        }
        matcher = matcher(SC,exp);
        if (matcher.find()) {
            throw new Exception2();
        }
        matcher = matcher(CON,exp);
        if (matcher.find()) {
            throw new Exception2();
        }
        int i = 0;
        int flag = 1;
        int count = 0;
        while (exp.charAt(i) == '+' || exp.charAt(i) == '-'
                || exp.charAt(i) == ' ' || exp.charAt(i) == '\t') {
            if (exp.charAt(i) == '-') {
                flag = -flag;
            }
            if (exp.charAt(i) == '+' || exp.charAt(i) == '-') {
                count++;
            }
            i++;
        }
        if (flag == 1 && count == 2) {
            exp = exp.substring(i);
        } else if (flag == -1 && count == 2) {
            exp = "-" + exp.substring(i);
        }
        matcher = matcher(SYM1,exp);
        if (matcher.find()) {
            throw new Exception2();
        }
        matcher = matcher(SYM2,exp);
        if (matcher.find()) {
            throw new Exception2();
        }
        matcher = matcher(POWER,exp);
        if (matcher.find()) {
            throw new Exception2();
        }
        else {
            exp = exp.replaceAll("\\*\\*","^");
        }
        exp = exp.replaceAll(" ","");
        if (exp.equals("")) {
            throw new Exception2();
        }
        return exp;
    }

    public Matcher matcher(String re,String exp) {
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(exp);
        return matcher;
    }
}
