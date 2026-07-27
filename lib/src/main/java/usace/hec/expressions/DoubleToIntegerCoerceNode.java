package usace.hec.expressions;


import java.util.List;

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
public class DoubleToIntegerCoerceNode implements IntegerExpressionNode, DisplayNode {

    private final DoubleExpressionNode child;

    public DoubleToIntegerCoerceNode(DoubleExpressionNode child) {
        this.child = child;
    }

    @Override
    public int evaluate() {
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
        return "CONVERTTOINT(" + child.ExcelSyntax() + ")";
    }

    @Override
    public String ExcelSyntax() {
        // Excel doesn't require explicit widening casts, so we just render the child.
        return child.ExcelSyntax();
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.INTCOERSION;
    }
    @Override
    public String displayName(boolean infix) {
        return Operator().getInfixName();

    }
    @Override
    public String category() {
        return "Conversion";
    }
    @Override
    public String defaultSyntax(boolean infix) {
        return Operator().getPrefixName() + "()";

    }
}
