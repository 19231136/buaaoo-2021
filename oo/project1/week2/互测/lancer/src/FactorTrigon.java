import java.math.BigInteger;
import java.util.Objects;

public class FactorTrigon extends Factor {
    private Factor base;
    private BigInteger deg;

    public FactorTrigon(Factor base, BigInteger deg, FactorType type) {
        setType(type);
        this.base = base;
        this.deg = deg;
    }

    public Term getDerivation() {
        Term ret = new Term();

        // deg * (sin | cos)(base) ** (deg - 1) * (cos(base)| -sin(base)) * base'
        ret.add(FactorFactory.getFactor(
                FactorType.EXPONENT,
                deg,
                BigInteger.ZERO
        ));

        Term tmp1 = base.getDerivation();
        Expression tmp2 = tmp1.toExpression();

        ret.add(FactorFactory.getFactor(
                FactorType.EXPRESSION,
                tmp2
        ));

        if (getType() == FactorType.SIN) {
            ret.add(FactorFactory.getFactor(
                    FactorType.SIN,
                    base,
                    deg.subtract(BigInteger.ONE)
            ));
            ret.add(FactorFactory.getFactor(
                    FactorType.COS,
                    base,
                    BigInteger.valueOf(1)
            ));
        }

        if (getType() == FactorType.COS) {
            ret.add(FactorFactory.getFactor(
                    FactorType.COS,
                    base,
                    deg.subtract(BigInteger.ONE)
            ));
            ret.add(FactorFactory.getFactor(
                    FactorType.SIN,
                    base,
                    BigInteger.valueOf(1)
            ));
            ret.add(FactorFactory.getFactor(
                    FactorType.EXPONENT,
                    BigInteger.valueOf(-1),
                    BigInteger.ZERO
            ));
        }

        return ret;
    }

    public boolean simplify() {
        // 1. FactorExpression 拆包
        // 2. FactorTrigon 向下递归 simplify()
        boolean flag = false;
        if (base.isUnwrappable()) {
            Term term = base.unwrap();
            if (term.size() == 1) {
                base = term.getFactor().clone();
                flag = true;
            }
        }
        if (base instanceof FactorTrigon) {
            flag |= ((FactorTrigon) base).simplify();
        }
        return flag;
    }

    public Factor getBase() {
        return base;
    }

    public BigInteger getDeg() {
        return deg;
    }

    @Override
    public String toString() {
        String str = "";
        if (getType() == FactorType.SIN) {
            str = str + "sin";
        }
        if (getType() == FactorType.COS) {
            str = str + "cos";
        }
        str = str + "(" + base.toString() + ")";

        if (!deg.equals(BigInteger.ONE)) {
            str = str + "**" + deg.toString();
        }
        if (deg.equals(BigInteger.ZERO)) {
            str = "1";
        }

        return str;
    }

    // NOTE: 这里直接判定 base = x，因此没有写条件
    // TODO: 对不同的 base 也能判定
    @Override
    public boolean isMergeable(Factor o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FactorTrigon that = (FactorTrigon) o;
        // return Objects.equals(base, that.base) && getType() == that.getType();
        return getType() == that.getType();
    }

    @Override
    // 会改变自身
    // 需要提前判断可合并
    public Factor merge(Factor o) {
        FactorTrigon that = (FactorTrigon) o;
        return new FactorTrigon(
                this.base,
                this.deg.add(that.deg),
                this.getType()
        );
    }

    // NOTE: 这里直接判定 base = x，因此没有写条件
    // TODO: 对不同的 base 也能判定
    @Override
    public int hashCode() {
        return Objects.hash(getType(), deg);
    }

    @Override
    public Factor clone() {
        Factor ret = super.clone();
        ((FactorTrigon) ret).base = base.clone();
        return ret;
    }
}
