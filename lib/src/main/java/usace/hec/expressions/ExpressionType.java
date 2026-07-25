package usace.hec.expressions;

public enum ExpressionType {
    INTEGER,
    DOUBLE,
    BOOLEAN,
    DATE,
    STRING,
    VOID;

    public boolean isNumeric() {
        return this == INTEGER || this == DOUBLE;
    }

    /** Widening coercion rules: (from, to) */
    public static boolean canWiden(ExpressionType from, ExpressionType to) {
        return (from == INTEGER && to == DOUBLE);
    }
}
