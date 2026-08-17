package usace.hec.expressions;

import usace.hec.expressions.math.DoubleUnaryExpressionNode;

/**
 * Wraps an {@link IntegerExpressionNode} and exposes it as a {@link DoubleExpressionNode}.
 * <p>
 * This node performs widening coercion at parse time, ensuring that mixed-type 
 * arithmetic (e.g., {@code int + double}) resolves to the specialized 
 * {@code DoubleAddNode} without runtime casting or boxing.
 * </p>
 *
 * @see ExpressionType#canWiden(ExpressionType, ExpressionType)
 */
public class IntegerToDoubleCoerceNode extends DoubleUnaryExpressionNode {

    private final IntegerExpressionNode child;

    public IntegerToDoubleCoerceNode(IntegerExpressionNode child) {
        this.child = child;
    }

    @Override
    public double evaluate() {
        ee.clear();
        // Primitive cast, zero allocation, zero boxing
        return (double) child.evaluate();
    }

    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }

    // --- Accessor ---
    public IntegerExpressionNode child() {
        return child;
    }

    // --- Syntax Generation ---
    @Override
    public String PreFixSyntax() {
        // Explicit cast representation for debugging/prefix output
        return Operator().getInfixName() +"(" + child.PreFixSyntax() + ")";
    }

    @Override
    public String ExcelSyntax() {
        // Excel doesn't require explicit widening casts, so we just render the child.
        return child.ExcelSyntax();
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.DOUBLECOERSION;
    }
}
