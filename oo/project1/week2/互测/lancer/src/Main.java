import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String strExpr = scanner.nextLine();
        ExpressionParser parser = new ExpressionParser(strExpr);
        Expression expr = parser.parseExpression();
        // System.out.println(expr.toString());
        // expr.simplify();
        // System.out.println(expr.toString());
        expr = expr.getDerivation();
        System.out.println(expr.toString());
    }
}
