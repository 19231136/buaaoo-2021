import java.util.Scanner;

public class MainClass {
    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        Expression expression = new Expression(scanner.nextLine());
        Poly poly = new Poly(expression.change());
        String out = poly.derivation();
        out = out.replaceAll("\\^","**");
        //System.out.println(out);
        expression = new Expression(out);
        out = expression.change();
        String out1 = new Better(out).better();
        System.out.println(out1);
    }
}
