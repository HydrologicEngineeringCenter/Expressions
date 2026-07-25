package usace.hec.expressions;

public enum ExpressionOperator {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    POW("^"),
    MAX("MAX"),
    MIN("MIN"),
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
    AFTER("AFTER"),
    BEFORE("BEFORE"),
    TODAY("TODAY"),
    DATE("DATE"),
    CURRENTTIMESTEP("CURRENTTIMESTEP"),
    DOY("DOY"),
    LAG("LAG"),
    RAND("RAND"),
    FLOOR("FLOOR"),
    CEILING("CEILING"),
    CONSTANT(""),
    VARIABLE("["),
    IF("IF");

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
