public class Factory {
    public Factor factory(String str) {
        Factor factor;
        if (str.startsWith("sin")) {
            factor = new Sin(str);
        } else if (str.startsWith("cos")) {
            factor = new Cos(str);
        } else if (str.startsWith("x")) {
            factor = new Power(str);
        } else if (str.startsWith("(")) {
            factor = new Poly(str.substring(1,str.length() - 1));
        } else {
            factor = new Constant(str);
        }
        return factor;
    }
}
