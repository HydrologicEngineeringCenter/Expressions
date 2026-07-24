package usace.hec.expressions;

import java.util.EnumMap;
import java.util.Map;

/**
 * Defines precedence and associativity for infix operators used in the shunting-yard parser.
 * Higher precedence values bind tighter.
 */
public final class OperatorPrecedence {

    public enum Associativity { LEFT, RIGHT }

    // Initialized inline to avoid "blank final field" compiler errors
    private static final Map<ExpressionOperator, Integer> PRECEDENCE = new EnumMap<>(ExpressionOperator.class);
    private static final Map<ExpressionOperator, Associativity> ASSOCIATIVITY = new EnumMap<>(ExpressionOperator.class);

    static {
        // Power (right-associative)
        PRECEDENCE.put(ExpressionOperator.POW, 5);
        ASSOCIATIVITY.put(ExpressionOperator.POW, Associativity.RIGHT);

        // Multiplicative
        PRECEDENCE.put(ExpressionOperator.MULTIPLY, 4);
        PRECEDENCE.put(ExpressionOperator.DIVIDE, 4);
        ASSOCIATIVITY.put(ExpressionOperator.MULTIPLY, Associativity.LEFT);
        ASSOCIATIVITY.put(ExpressionOperator.DIVIDE, Associativity.LEFT);

        // Additive
        PRECEDENCE.put(ExpressionOperator.PLUS, 3);
        PRECEDENCE.put(ExpressionOperator.MINUS, 3);
        ASSOCIATIVITY.put(ExpressionOperator.PLUS, Associativity.LEFT);
        ASSOCIATIVITY.put(ExpressionOperator.MINUS, Associativity.LEFT);

        // Comparison
        PRECEDENCE.put(ExpressionOperator.GT, 2);
        PRECEDENCE.put(ExpressionOperator.GTE, 2);
        PRECEDENCE.put(ExpressionOperator.LT, 2);
        PRECEDENCE.put(ExpressionOperator.LTE, 2);
        PRECEDENCE.put(ExpressionOperator.EQ, 2);
        ASSOCIATIVITY.put(ExpressionOperator.GT, Associativity.LEFT);
        ASSOCIATIVITY.put(ExpressionOperator.GTE, Associativity.LEFT);
        ASSOCIATIVITY.put(ExpressionOperator.LT, Associativity.LEFT);
        ASSOCIATIVITY.put(ExpressionOperator.LTE, Associativity.LEFT);
        ASSOCIATIVITY.put(ExpressionOperator.EQ, Associativity.LEFT);

        // Logical
        PRECEDENCE.put(ExpressionOperator.AND, 1);
        PRECEDENCE.put(ExpressionOperator.OR, 1);
        PRECEDENCE.put(ExpressionOperator.XOR, 1);
        ASSOCIATIVITY.put(ExpressionOperator.AND, Associativity.LEFT);
        ASSOCIATIVITY.put(ExpressionOperator.OR, Associativity.LEFT);
        ASSOCIATIVITY.put(ExpressionOperator.XOR, Associativity.LEFT);
    }

    /**
     * Returns the precedence level for an operator.
     * Returns 0 for operators not configured here (e.g., functions).
     */
    public static int getPrecedence(ExpressionOperator op) {
        return PRECEDENCE.getOrDefault(op, 0);
    }

    /**
     * Returns the associativity for an operator.
     * Defaults to LEFT for unconfigured operators.
     */
    public static Associativity getAssociativity(ExpressionOperator op) {
        return ASSOCIATIVITY.getOrDefault(op, Associativity.LEFT);
    }

    /**
     * Convenience check for left-associative operators.
     */
    public static boolean isLeftAssociative(ExpressionOperator op) {
        return ASSOCIATIVITY.getOrDefault(op, Associativity.LEFT) == Associativity.LEFT;
    }
}