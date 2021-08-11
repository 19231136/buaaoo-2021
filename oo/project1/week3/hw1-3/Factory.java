
public class Factory {
    public Factor factory(String str) {
        Factor factor = null;
        if (str.startsWith("sin")) {
            factor = new Sin(str);
        } else if (str.startsWith("cos")) {
            factor = new Cos(str);
        } else if (str.startsWith("x")) {
            factor = new Power(str);
        } else if (str.startsWith("(")) {
            if (str.charAt(str.length() - 1) != ')') {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
            factor = new Poly(str.substring(1,str.length() - 1));
        } else {
            factor = new Constant(str);
        }
        return factor;
    }
}
