import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    public static final String RE1 = "([+-]?\\d*\\*)*x(\\*{2}[+-]?\\d+)?";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        s = s.replaceAll("[ \\t]*","");
        String re = "[+-]{0,3}" + RE1 + "(\\*[+-]?(" + RE1 + "|\\d+))*|[+-]{1,2}\\d+";
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(s);
        Poly poly = new Poly();
        while (matcher.find()) {
            String part = s.substring(matcher.start(),matcher.end());
            Term term = new Term(part);
            poly.add(term);
        }
        String out = poly.derivation();
        if (out.indexOf("+") == 0) {
            out = out.substring(1);
        }
        if (out.equals("")) {
            out = String.valueOf(0);
        }
        if (out.lastIndexOf("*") == out.length() - 1) {
            out = out.substring(0,out.length() - 1);
        }
        System.out.println(out);
    }
}