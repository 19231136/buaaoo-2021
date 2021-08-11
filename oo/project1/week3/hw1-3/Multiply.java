import java.util.ArrayList;

public class Multiply {
    public String derivation(ArrayList<Factor> factors) {
        String out = "";
        for (int i = 0;i < factors.size();i++) {
            if (i == 0) {
                out += factors.get(i).derivation();
            } else {
                out += "+" + factors.get(i).derivation();
            }
            for (int j = 0;j < factors.size();j++) {
                if (j != i) {
                    out += "*" + factors.get(j).toString();
                }
            }
        }
        return out;
    }
}
