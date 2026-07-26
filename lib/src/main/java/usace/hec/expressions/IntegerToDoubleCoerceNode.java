package usace.hec.expressions;



import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.DataListener;

import java.util.List;

/**
 * Wraps an {@link IntExpressionNode} and exposes it as a {@link DoubleExpressionNode}.
 * <p>
 * This node performs widening coercion at parse time, ensuring that mixed-type 
 * arithmetic (e.g., {@code int + double}) resolves to the specialized 
 * {@code DoubleAddNode} without runtime casting or boxing.
 * </p>
 *
 * @see ExpressionType#canWiden(ExpressionType, ExpressionType)
 */
public class IntegerToDoubleCoerceNode implements DoubleExpressionNode {

    private final IntegerExpressionNode child;

    public IntegerToDoubleCoerceNode(IntegerExpressionNode child) {
        this.child = child;
    }

    @Override
    public double evaluate() {
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

    @Override
    public List<DataListener> fetchListeners() {
        // Coercion nodes are passive; propagate listener collection to the child.
        return child.fetchListeners();
    }

    @Override
    public void setProvider(DataProvider provider) {
        // Coercion nodes don't hold state; propagate provider setup to the child.
        child.setProvider(provider);
    }

    // --- Syntax Generation ---
    @Override
    public String PreFixSyntax() {
        // Explicit cast representation for debugging/prefix output
        return "CONVERTTODOUBLE(" + child.ExcelSyntax() + ")";
    }

    @Override
    public String ExcelSyntax() {
        // Excel doesn't require explicit widening casts, so we just render the child.
        return child.ExcelSyntax();
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.DOUBLECOERSION;
    }
}