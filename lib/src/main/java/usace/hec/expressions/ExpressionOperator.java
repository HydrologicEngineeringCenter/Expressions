package usace.hec.expressions;

public enum ExpressionOperator {
    PLUS("ADD"),
    MINUS("MINUS"),
    MULTIPLY("MULTI"),
    DIVIDE,
    EXPONENT,
    MAX,
    MIN,
    NEGATE,
    AND,
    IF,
    OR,
    XOR,
    EQ,
    GT,
    GTE,
    LT,
    LTE;

    ExpressionOperator(String name){
    }
}
