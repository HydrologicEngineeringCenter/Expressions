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
    AFTER("is after?"),
    BEFORE("is before?"),
    TODAY("TODAY()"),
    DOY("Day"),
    LAG("Lagged"),
    RAND("Random");

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
