import java.math.BigInteger;

public class FactorFactory {
    public static Factor getFactor(FactorType type, Object... args) {
        Factor factor = null;
        switch (type) {
            case EXPONENT:
                factor = new FactorExponent(
                        (BigInteger) args[0],
                        (BigInteger) args[1]
                );
                break;

            case EXPRESSION:
                factor = new FactorExpression((Expression) args[0]);
                break;

            case SIN:
                factor = new FactorTrigon(
                        (Factor) args[0],
                        (BigInteger) args[1],
                        FactorType.SIN
                );
                break;

            case COS:
                factor = new FactorTrigon(
                        (Factor) args[0],
                        (BigInteger) args[1],
                        FactorType.COS
                );
                break;

            default:
        }

        return factor;
    }
}
