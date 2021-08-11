import java.math.BigInteger;
import java.util.ArrayList;

public class Expression implements Cloneable {
    // 注意之后需要重写 Term 的 hashCode 和 equals 方法
    private ArrayList<Term> termSet;

    public Expression() {
        this.termSet = new ArrayList<>();
    }

    public Expression(Term term) {
        this();
        add(term);
    }

    public int size() {
        return termSet.size();
    }

    // NOTE: 所有 add 操作都只是 add 了一个 clone 版本
    // DEBUG: 又把这个特性改回去了
    public void add(Term term) {
        termSet.add(term);
    }

    public void add(Expression expression) {
        for (Term term: expression.termSet) {
            add(term);
        }
    }

    // 化简 Expression
    // 1. 解包（可能需要递归）
    // 2. 去掉 0 项
    // 3. 递归调用
    public boolean simplify() {
        boolean ret = false;
        while (true) {
            boolean flag = false;
            flag |= termSet.removeIf(x -> x.getSign() == 0);
            flag |= this.unwrapTerms();
            flag |= this.mergeTerms();
            for (Term term: termSet) {
                flag |= term.simplify();
            }
            if (!flag) {
                break;
            } else {
                ret = true;
            }
        }

        // 如果当前是 0 项，就手动变成 0 项
        if (size() == 0) {
            this.add(FactorFactory.getFactor(
                    FactorType.EXPONENT,
                    BigInteger.ZERO,
                    BigInteger.ZERO
            ).toTerm());
        }

        return ret;
    }

    // 如果成功化简返回 true
    public boolean unwrapTerms() {
        ArrayList<Term> unwrappableTerms = new ArrayList<>();
        termSet.forEach(term -> {
            if (term.isUnwrappable()) {
                unwrappableTerms.add(term);
            }
        });
        boolean flag = termSet.removeIf(term -> term.isUnwrappable());
        for (Term term: unwrappableTerms) {
            this.add(term.unwrap());
        }
        return flag;
    }

    // 会返回第一个 Term
    public Term getTerm() {
        if (termSet.size() >= 1) {
            return termSet.get(0).clone();
        } else {
            return null;
        }
    }

    public Expression getDerivation() {
        Expression ret = new Expression();

        this.simplify();
        for (Term term: termSet) {
            ret.add(term.getDeriviation());
        }
        ret.simplify();

        return ret;
    }

    public Expression negate() {
        Expression ret = new Expression();
        for (Term term: ret.termSet) {
            ret.add(term.negate());
        }
        return ret;
    }

    // 不修改本体，返回一个新值
    // Expression * Term -> Expression
    public Expression multiply(Term term) {
        Expression ret = new Expression();
        for (Term t: termSet) {
            ret.add(t.multiply(term));
        }
        return ret;
    }

    // TODO: 重构下面
    // TODO: 无任何正负优化，需要将 + 提到第一位来减少长度
    @Override
    public String toString() {
        String ret = "";

        // 找一个正数丢前面减少长度
        ArrayList<Term> terms = new ArrayList<>();
        Term posTerm = null;
        for (Term term: termSet) {
            if (term.getSign() > 0) {
                posTerm = term;
                break;
            }
        }
        if (posTerm != null) {
            terms.add(posTerm);
        }
        for (Term term: termSet) {
            if (term != posTerm) {
                terms.add(term);
            }
        }

        // 按顺序输出 terms 中的内容
        boolean isFirst = true;
        for (Term term: terms) {
            if (!isFirst && term.getSign() >= 0) {
                ret = ret + "+";
            }
            ret = ret + term.toString();
            isFirst = false;
        }
        if (isFirst) {
            System.err.println("Expression 为空，而这不可能");
        }

        return ret;
    }

    @Override
    public int hashCode() {
        ArrayList<Integer> hash = new ArrayList<>();
        for (Term term: termSet) {
            hash.add(term.hashCode());
        }
        hash.sort(Integer::compareTo);
        return hash.hashCode();
    }

    @Override
    public Expression clone() {
        Expression ret = new Expression();
        for (Term term: termSet) {
            ret.add(term.clone());
        }
        return ret;
    }

    // 合并同类项
    // NOTE: 这段代码是直接从 Term 那边搬过来的，因此要改的话得一起改
    private boolean mergeTerms() {
        boolean flag = false;
        for (Term term: termSet) {
            // 找与 term 可合并的项
            ArrayList<Term> mergeList = new ArrayList<>();
            for (Term term2: termSet) {
                if (term.isMergeable(term2)) {
                    mergeList.add(term2);
                }
            }

            // 开始合并
            if (mergeList.size() > 1) {
                flag = true;
                Term result = mergeList.get(0).clone();
                for (Term term2: mergeList) {
                    if (term2 != term) {
                        result = result.merge(term2);
                    }
                    termSet.remove(term2);
                }
                termSet.add(result);
                break;
            }
        }
        return flag;
    }
}
