import java.math.BigInteger;
import java.util.Objects;

public class FactorExponent extends Factor {
    private BigInteger coe;
    private BigInteger deg;

    public FactorExponent(BigInteger coe, BigInteger deg) {
        setType(FactorType.EXPONENT);
        this.coe = coe;
        this.deg = deg;
    }

    public Term getDerivation() {
        return new FactorExponent(
                coe.multiply(deg),
                deg.subtract(BigInteger.ONE)
        ).toTerm();
    }

    public BigInteger getCoe() {
        return coe;
    }

    public BigInteger getDeg() {
        return deg;
    }

    @Override
    public boolean isMergeable(Factor o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return true;
    }

    public Factor add(Factor o) {
        FactorExponent that = (FactorExponent) o;
        return new FactorExponent(
                coe.add(that.coe),
                deg
        );
    }

    @Override
    // 会改变自身
    // 需要提前判断可合并
    public Factor merge(Factor o) {
        FactorExponent that = (FactorExponent) o;
        return new FactorExponent(
                coe.multiply(that.coe),
                deg.add(that.deg)
        );
    }

    @Override
    public String toString() {
        String str = "";
        if (coe.equals(BigInteger.ZERO)) {
            str = "0";
        } else {
            if (deg.equals(BigInteger.ZERO)) {
                str = coe.toString();
            } else {
                // 特判一下 x**2 -> x*x 的情况
                if (coe.equals(BigInteger.ONE)) {
                    str = str + "x";
                } else if (coe.equals(BigInteger.ONE.negate())) {
                    str = str + "-x";
                } else {
                    str = coe.toString() + "*x";
                }
                if (!deg.equals(BigInteger.ONE)) {
                    if (deg.equals(new BigInteger("2"))) {
                        str = str + "*x";
                    } else {
                        str = str + "**" + deg.toString();
                    }
                }
            }
        }

        return str;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), deg);
    }
}
