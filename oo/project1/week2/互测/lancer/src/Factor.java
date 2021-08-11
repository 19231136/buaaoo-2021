import java.math.BigInteger;

public abstract class Factor implements Cloneable {
    private FactorType type;

    public FactorType getType() {
        return type;
    }

    public void setType(FactorType type) {
        this.type = type;
    }

    public Term toTerm() {
        return new Term(this);
    }

    // 只要大小 <= 1 就能解包
    // size = 0 的时候解包出来是 0
    public boolean isUnwrappable() {
        return this.type == FactorType.EXPRESSION &&
                ((FactorExpression) this).getExpression().size() <= 1;
    }

    // 把 Factor(Expression(<Term>)) 解包为 <Term>
    // 使用之前需要判断能够解包，否则可能出错
    public Term unwrap() {
        Expression expr = ((FactorExpression) this).getExpression();
        if (expr.size() == 0) {
            return FactorFactory.getFactor(
                    FactorType.EXPONENT,
                    BigInteger.ZERO,
                    BigInteger.ZERO
            ).toTerm();
        } else {
            return expr.getTerm().clone();
        }
    }

    public abstract boolean isMergeable(Factor o);

    public abstract Factor merge(Factor o);

    public abstract Term getDerivation();

    public abstract String toString();

    public abstract int hashCode();

    public Factor clone() {
        Factor ret = null;
        try {
            ret = (Factor) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
