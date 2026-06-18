package usace.hec.expressions;

public enum ExpressionOperator {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    POW("^"),
    MAX("max"),
    MIN("min"),
    NEGATE("-"),
    AND("&&"),
    OR("||"),
    XOR("^^"),
    EQ("=="),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    ABS("|"),
    AFTER,
    BEFORE,
    TODAY("TODAY()");

    private final String op;
    ExpressionOperator(){ //used for time nodes
        this.op = null;
    }

    ExpressionOperator(String op){
        this.op = op;
    }

    public String getPrefixName() {
        return name();
    }
    public String getInfixName() {
        return this.op;
    }
}
