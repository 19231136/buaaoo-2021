import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {
    private String str;
    // [posBegin, posEnd)
    private int posBegin;
    private int posEnd;

    private static final String MSG_WRONG_FORMAT = "WRONG FORMAT!";
    private static final String PATTERN_CONST =
            "[+-]?\\d+";
    private static final String PATTERN_POWER =
            "x(?:[ \\t]*\\*\\*[ \\t]*([+-]?\\d+))?";
    private static final String PATTERN_TRIGON =
            "sin|cos";
    private static final String PATTERN_DEGREE =
            "[ \\t]*\\*\\*[ \\t]*([+-]?\\d+)";

    ExpressionParser(String str) {
        this.str = str;
        this.posBegin = 0;
        this.posEnd = str.length();
    }

    // 如果在 posEnd 前还有需要处理的字符则返回 true
    private boolean skipWhitespace() {
        while (posBegin < posEnd) {
            char c = str.charAt(posBegin);
            if (c == ' ' || c == '\t') {
                posBegin++;
            } else {
                break;
            }
        }
        return posBegin < posEnd;
    }

    public Expression parseExpression() {
        skipWhitespace();

        Expression expression = new Expression();
        char op;

        op = str.charAt(posBegin);
        if (op == '+' || op == '-') {
            posBegin++;
        }
        expression.add(op == '-' ? parseTerm().negate() : parseTerm());

        while (skipWhitespace()) {
            op = str.charAt(posBegin);
            if (op == '+' || op == '-') {
                posBegin++;
                expression.add(op == '-' ? parseTerm().negate() : parseTerm());
            } else {
                System.err.println("表达式在 Expression 阶段不合法");
                break;
            }
        }

        return expression;
    }

    public Term parseTerm() {
        skipWhitespace();

        Term term = new Term();
        char op;

        op = str.charAt(posBegin);
        if (op == '+' || op == '-') {
            posBegin++;
        }
        term.add(parseFactor());
        if (op == '-') {
            term.add(FactorFactory.getFactor(
                    FactorType.EXPONENT,
                    BigInteger.valueOf(-1),
                    BigInteger.ZERO
            ));
        }

        while (skipWhitespace()) {
            op = str.charAt(posBegin);
            if (op == '*') {
                posBegin++;
                term.add(parseFactor());
            } else {
                if (op != '+' && op != '-') {
                    System.err.println("表达式在 Term 阶段不合法");
                }
                break;
            }
        }

        return term;
    }

    private Factor parseFactorConst(Matcher matcher) {
        String component = matcher.group();
        Factor factor = FactorFactory.getFactor(
                FactorType.EXPONENT,
                new BigInteger(component),
                BigInteger.ZERO
        );
        posBegin += component.length();
        return factor;
    }

    private Factor parseFactorExponent(Matcher matcher) {
        String component = matcher.group();
        String deg = matcher.group(1);
        Factor factor = FactorFactory.getFactor(
                FactorType.EXPONENT,
                BigInteger.ONE,
                new BigInteger(deg == null ? "1" : deg)
        );
        posBegin += component.length();
        return factor;
    }

    private Factor parseFactorTrigon(Matcher matcher) {
        final int tmpPosEnd = posEnd;
        int cnt = 0;
        boolean trigger = false;
        for (int i = posBegin; i < posEnd; i++) {
            if (str.charAt(i) == '(') {
                cnt++;
                if (!trigger) {
                    posBegin = i;
                }
                trigger = true;
            }
            if (str.charAt(i) == ')') {
                cnt--;
                trigger = true;
            }
            if (cnt == 0 && trigger) {
                posEnd = i;
                break;
            }
        }
        posBegin++;
        final Factor base = FactorFactory.getFactor(
                FactorType.EXPRESSION,
                parseExpression()
        );
        posBegin = posEnd + 1;
        posEnd = tmpPosEnd;

        String substr = str.substring(posBegin);
        Matcher matcherDegree = Pattern.compile(PATTERN_DEGREE).matcher(substr);
        String deg = null;
        if (matcherDegree.lookingAt()) {
            deg = matcherDegree.group(1);
            posBegin += matcherDegree.group().length();
        }

        String type = matcher.group().substring(0, 3);
        Factor factor = FactorFactory.getFactor(
                type.equals("sin") ? FactorType.SIN : FactorType.COS,
                base,
                new BigInteger(deg == null ? "1" : deg)
        );

        return factor;
    }

    private Factor parseFactorExpression() {
        final int tmpPosEnd = posEnd;
        int cnt = 0;
        for (int i = posBegin; i < posEnd; i++) {
            if (str.charAt(i) == '(') {
                cnt++;
            }
            if (str.charAt(i) == ')') {
                cnt--;
            }
            if (cnt == 0 && i != posBegin) {
                posEnd = i;
                break;
            }
        }
        posBegin++;
        Factor factor = FactorFactory.getFactor(
                FactorType.EXPRESSION,
                parseExpression()
        );

        posBegin = posEnd + 1;
        posEnd = tmpPosEnd;

        return factor;
    }

    public Factor parseFactor() {
        skipWhitespace();

        String substr = str.substring(posBegin);
        Matcher matcherConst = Pattern.compile(PATTERN_CONST).matcher(substr);
        Matcher matcherPower = Pattern.compile(PATTERN_POWER).matcher(substr);
        Matcher matcherTrigon = Pattern.compile(PATTERN_TRIGON).matcher(substr);

        if (matcherConst.lookingAt()) {             // 常数因子
            return parseFactorConst(matcherConst);
        } else if (matcherPower.lookingAt()) {      // 指数因子
            return parseFactorExponent(matcherPower);
        } else if (matcherTrigon.lookingAt()) {     // 三角因子
            return parseFactorTrigon(matcherTrigon);
        } else {                                    // 表达式因子
            return parseFactorExpression();
        }
    }
}
