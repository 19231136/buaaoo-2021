import java.util.Objects;

public class FactorExpression extends Factor {
    private Expression expr;

    public FactorExpression(Expression expr) {
        setType(FactorType.EXPRESSION);
        this.expr = expr;
    }

    // NOTE: 暴力一点，直接让 (<Expression>) 不是可合并的
    // 我不管了.jpg
    @Override
    public boolean isMergeable(Factor o) {
        return false;
    }

    @Override
    // NOTE: 暴力一点，直接让 (<Expression>) 不是可合并的
    // 我不管了.jpg
    public Factor merge(Factor o) {
        return this;
    }

    @Override
    public Term getDerivation() {
        return FactorFactory.getFactor(
                FactorType.EXPRESSION,
                expr.getDerivation()
        ).toTerm();
    }

    public boolean simplify() {
        return expr.simplify();
    }

    public Expression getExpression() {
        return expr;
    }

    public String toString() {
        return "(" + expr.toString() + ")";
    }

    // TODO: 重写 Expression 类的 hashCode 方法
    @Override
    public int hashCode() {
        return Objects.hash(getType(), expr);
    }

    @Override
    public Factor clone() {
        Factor ret = super.clone();
        ((FactorExpression) ret).expr = expr.clone();
        return ret;
    }
}
