package usace.hec.expressions;

import usace.hec.expressions.math.IntegerUnaryExpressionNode;

/**
 * Wraps an {@link DoubleExpressionNode} and exposes it as a {@link IntegerExpressionNode}.
 * <p>
 * This node performs widening coercion at parse time, ensuring that mixed-type 
 * arithmetic (e.g., {@code int + double}) resolves to the specialized 
 * {@code DoubleAddNode} without runtime casting or boxing.
 * </p>
 *
 * @see ExpressionType#canWiden(ExpressionType, ExpressionType)
 */
public class DoubleToIntegerCoerceNode extends IntegerUnaryExpressionNode {

    private final DoubleExpressionNode child;

    public DoubleToIntegerCoerceNode(DoubleExpressionNode child) {
        this.child = child;
    }

    @Override
    public int evaluate() {
        ee.clear();
        // Primitive cast, zero allocation, zero boxing
        return (int) child.evaluate();
    }

    @Override
    public ExpressionType resultType() {
        return ExpressionType.INTEGER;
    }

    // --- Accessor ---
    public DoubleExpressionNode child() {
        return child;
    }

    // --- Syntax Generation ---
    @Override
    public String PreFixSyntax() {
        // Explicit cast representation for debugging/prefix output
        return Operator().getPrefixName() +"(" + child.PreFixSyntax() + ")";
    }

    @Override
    public String ExcelSyntax() {
        // Excel doesn't require explicit widening casts, so we just render the child.
        return Operator().getInfixName() +"(" + child.ExcelSyntax() + ")";
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.INTCOERSION;
    }
}
