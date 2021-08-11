import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Term implements Cloneable {
    // NOTE: Term 不能为空，即使是 0 也得有一个系数
    private HashMap<FactorType, ArrayList<Factor>> typeToList;

    public Term() {
        typeToList = new HashMap<>();
    }

    public Term(Factor factor) {
        this();
        add(factor);
    }

    // NOTE: 调用后会化简自身
    // 返回值是一个对真实元素的引用，因此可以直接修改
    public Factor getCoe() {
        this.simplify();
        Factor one = FactorFactory.getFactor(
                FactorType.EXPONENT,
                BigInteger.ONE,
                BigInteger.ZERO
        );
        if (!typeToList.containsKey(FactorType.EXPONENT)) {
            return one;
        }
        ArrayList<Factor> list = typeToList.get(FactorType.EXPONENT);
        if (list.isEmpty()) {
            return one;
        } else {
            return list.get(0);
        }
    }

    public boolean isMergeable(Term o) {
        return this.hashCode() == o.hashCode();
    }

    // 不会改变自身
    // NOTE: 调用之前要保证可合并
    public Term merge(Term o) {
        Term ret = this.clone();
        FactorExponent coe = ((FactorExponent) ret.getCoe());
        ret.remove(coe);
        ret.add(coe.add(o.getCoe()));
        return ret;
    }

    // 直接修改本体
    // NOTE: 所有 add 操作都只是 add 了一个 clone 版本
    // DEBUG: 又把这个特性改回去了
    public void add(Factor factor) {
        // 可以针对不同类型进行化简
        FactorType type = factor.getType();
        if (!typeToList.containsKey(type)) {
            typeToList.put(type, new ArrayList<>());
        }
        typeToList.get(type).add(factor);
    }

    // 直接修改本体
    // NOTE: 所有 add 操作都只是 add 了一个 clone 版本
    // DEBUG: 又把这个特性改回去了
    public void add(Term term) {
        for (ArrayList<Factor> list: term.typeToList.values()) {
            for (Factor factor: list) {
                this.add(factor);
            }
        }
    }

    // 直接修改本体
    public void remove(Factor factor) {
        FactorType type = factor.getType();
        if (typeToList.containsKey(type)) {
            typeToList.get(type).remove(factor);
        }
    }

    // 会返回第一个 Factor
    public Factor getFactor() {
        for (ArrayList<Factor> list: typeToList.values()) {
            for (Factor factor: list) {
                return factor.clone();
            }
        }
        return null;
    }

    // 不修改本体，返回一个新值
    // Term * Factor -> Term
    public Term multiply(Factor factor) {
        Term ret = this.clone();
        ret.add(factor);
        return ret;
    }

    // 不修改本体，返回一个新值
    // Term * Term -> Term
    public Term multiply(Term term) {
        Term ret = term.clone();
        for (ArrayList<Factor> list: typeToList.values()) {
            for (Factor factor: list) {
                ret.add(factor);
            }
        }
        return ret;
    }

    public int size() {
        int ret = 0;
        for (ArrayList<Factor> list: typeToList.values()) {
            ret += list.size();
        }
        return ret;
    }

    // TODO: 这个解包有点菜，不能解包 1 * (a + b) 这种形态
    // 先暂时不管了
    public boolean isUnwrappable() {
        return this.size() == 1 &&
                typeToList.containsKey(FactorType.EXPRESSION) &&
                typeToList.get(FactorType.EXPRESSION).size() == 1;
    }

    // 把 Term(Factor(<Expression>)) 解包为 <Expression>
    // 使用之前需要判断能够解包，否则可能出错
    public Expression unwrap() {
        Factor factor = typeToList.get(FactorType.EXPRESSION).get(0);
        return ((FactorExpression) factor).getExpression().clone();
    }

    // 和上面的不一样，这是把 Factor 化简的
    public boolean unwrapFactors() {
        if (!typeToList.containsKey(FactorType.EXPRESSION)) {
            return false;
        }

        ArrayList<Factor> unwrappableFactors = new ArrayList<>();
        ArrayList<Factor> list = typeToList.get(FactorType.EXPRESSION);
        list.forEach(factor -> {
            if (factor.isUnwrappable()) {
                unwrappableFactors.add(factor);
            }
        });
        boolean flag = list.removeIf(factor -> factor.isUnwrappable());
        for (Factor factor: unwrappableFactors) {
            this.add(factor.unwrap());
        }
        return flag;
    }

    public Expression getDeriviation() {
        ArrayList<Factor> factors = new ArrayList<>();
        for (ArrayList<Factor> list: typeToList.values()) {
            for (Factor factor: list) {
                factors.add(factor.clone());
            }
        }

        if (factors.isEmpty()) {
            System.err.println("并不会等于 0");
        }

        if (factors.size() == 1) {
            return factors.get(0).getDerivation().toExpression();
        } else {
            Factor factor1 = factors.get(0);
            Term term1 = factor1.toTerm();
            Term term2 = new Term();
            for (int i = 1; i < factors.size(); i++) {
                term2.add(factors.get(i));
            }
            Expression ret = new Expression();
            ret.add(term2.getDeriviation().multiply(term1));
            ret.add(term1.getDeriviation().multiply(term2));
            return ret;
        }
    }

    public Expression toExpression() {
        return new Expression(this);
    }

    public int getSign() {
        if (this.size() == 0) {
            return 1;
        }
        int sgn = 1;
        if (typeToList.containsKey(FactorType.EXPONENT)) {
            ArrayList<Factor> list = typeToList.get(FactorType.EXPONENT);
            for (Factor factor: list) {
                BigInteger coe = ((FactorExponent) factor).getCoe();
                sgn *= coe.compareTo(BigInteger.ZERO);
            }
        }
        return sgn;
    }

    public boolean hasNonExponentFactor() {
        int siz = 0;
        for (FactorType type: FactorType.values()) {
            if (type != FactorType.EXPONENT &&
                    typeToList.containsKey(type)) {
                siz += typeToList.get(type).size();
            }
        }
        return siz > 0;
    }

    public Term negate() {
        Term ret = (Term) this.clone();
        Factor factor = FactorFactory.getFactor(
                FactorType.EXPONENT,
                BigInteger.valueOf(-1),
                BigInteger.ZERO
        );
        ret.add(factor);
        return ret;
    }

    // 调用之前得保证多项式的系数已经是化简的了，尤其是 EXPONENT 类型
    public String toString() {
        if (this.size() == 0) {
            System.err.println("Term 为空，而这不可能");
        }

        String ret = "";
        boolean isFirst = true;
        for (FactorType type: FactorType.values()) {
            if (!typeToList.containsKey(type)) {
                continue;
            }
            for (Factor factor: typeToList.get(type)) {
                if (!isFirst) {
                    ret = ret + "*";
                }
                if (factor.getType() == FactorType.EXPONENT &&
                        this.hasNonExponentFactor() &&
                        ((FactorExponent) factor).getCoe().equals(BigInteger.valueOf(-1)) &&
                        ((FactorExponent) factor).getDeg().equals(BigInteger.ZERO)) {
                    ret = ret + "-";
                } else if (factor.getType() == FactorType.EXPONENT &&
                        this.hasNonExponentFactor() &&
                        ((FactorExponent) factor).getCoe().equals(BigInteger.valueOf(1)) &&
                        ((FactorExponent) factor).getDeg().equals(BigInteger.ZERO)) {
                    ret = ret;
                } else {
                    ret = ret + factor.toString();
                    isFirst = false;
                }
            }
        }

        return ret;
    }

    public boolean simplify() {
        boolean ret = false;
        while (true) {
            boolean flag = false;
            flag |= unwrapFactors();
            flag |= simplifyExponent();
            flag |= simplifyTrigon();
            flag |= simplifyExpression();
            flag |= mergeFactors();
            if (!flag) {
                break;
            } else {
                ret = true;
            }
        }

        // 如果当前 term 为空，则手动加入系数 1
        if (this.size() == 0) {
            this.add(FactorFactory.getFactor(
                    FactorType.EXPONENT,
                    BigInteger.ONE,
                    BigInteger.ZERO
            ));
        }
        return ret;
    }

    // 合并同类项
    private boolean mergeFactors() {
        boolean flag = false;
        for (FactorType type: FactorType.values()) {
            if (typeToList.containsKey(type)) {
                flag |= mergeFactorsByType(type);
            }
        }
        return flag;
    }

    // NOTE: 这段代码还搬到了 Expression 那边，因此要改的话得一起改
    private boolean mergeFactorsByType(FactorType type) {
        boolean flag = false;
        ArrayList<Factor> list = typeToList.get(type);
        for (Factor factor: list) {
            // 找与 factor 可合并的项
            ArrayList<Factor> mergeList = new ArrayList<>();
            for (Factor factor2: list) {
                if (factor.isMergeable(factor2)) {
                    mergeList.add(factor2);
                }
            }

            // 开始合并
            if (mergeList.size() > 1) {
                flag = true;
                Factor result = mergeList.get(0).clone();
                for (Factor factor2: mergeList) {
                    if (factor2 != factor) {
                        result = result.merge(factor2);
                    }
                    list.remove(factor2);
                }
                list.add(result);
                break;
            }
        }
        return flag;
    }

    private boolean simplifyTrigon() {
        // 1. 将 factor(expr(term(<factor>))) 解包成 <factor>
        // 2. 将 sin/cos(<expr>) ** 0 删去变成 1
        boolean flag = false;
        if (typeToList.containsKey(FactorType.SIN)) {
            for (Factor factor: typeToList.get(FactorType.SIN)) {
                flag |= ((FactorTrigon) factor).simplify();
            }
            flag |= typeToList.get(FactorType.SIN).removeIf(factor -> {
                return ((FactorTrigon) factor).getDeg().equals(BigInteger.ZERO);
            });
        }
        if (typeToList.containsKey(FactorType.COS)) {
            for (Factor factor: typeToList.get(FactorType.COS)) {
                flag |= ((FactorTrigon) factor).simplify();
            }
            flag |= typeToList.get(FactorType.COS).removeIf(factor -> {
                return ((FactorTrigon) factor).getDeg().equals(BigInteger.ZERO);
            });
        }
        return flag;
    }

    // 调用 FactorExpression 的化简
    private boolean simplifyExpression() {
        if (!typeToList.containsKey(FactorType.EXPRESSION)) {
            return false;
        }

        boolean flag = false;
        ArrayList<Factor> factors = typeToList.get(FactorType.EXPRESSION);
        for (Factor factor: factors) {
            flag |= ((FactorExpression) factor).simplify();
        }
        return flag;
    }

    // 如果系数简化过且为 1，那么直接删除
    private boolean simplifyExponent() {
        if (!typeToList.containsKey(FactorType.EXPONENT)) {
            return false;
        }
        ArrayList<Factor> list = typeToList.get(FactorType.EXPONENT);
        if (list.isEmpty()) {
            return false;
        }
        FactorExponent factor = (FactorExponent) list.get(0);

        if (list.size() == 1 &&
                this.hasNonExponentFactor() &&
                factor.getCoe().equals(BigInteger.ONE) &&
                factor.getDeg().equals(BigInteger.ZERO)) {
            typeToList.remove(FactorType.EXPONENT);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        ArrayList<Integer> hash = new ArrayList<>();
        for (ArrayList<Factor> list: typeToList.values()) {
            for (Factor factor: list) {
                hash.add(factor.hashCode());
            }
        }
        hash.sort(Integer::compareTo);
        return hash.hashCode();
    }

    @Override
    public Term clone() {
        Term ret = new Term();
        for (ArrayList<Factor> list: typeToList.values()) {
            for (Factor factor: list) {
                ret.add(factor.clone());
            }
        }
        return ret;
    }
}
