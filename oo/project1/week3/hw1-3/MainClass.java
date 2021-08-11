import java.util.Scanner;

public class MainClass {
    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        Expression expression = new Expression(scanner.nextLine());
        try {
            Poly poly = new Poly(expression.change());
            String out = poly.derivation();
            out = out.substring(1,out.length() - 1);
            //System.out.println(out);
            poly = new Poly(out);
            out = poly.better();
            out = out.replaceAll("\\^","**");
            System.out.println(out);
        } catch (Exception2 exception2) {
            System.out.println("WRONG FORMAT!");
        }
    }
}
