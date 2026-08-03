package usace.hec.expressions.comparison;

import usace.hec.expressions.*;

import java.io.Serial;

public class IntegerGreaterThanOrEqualNode extends BooleanBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode left;
    private IntegerExpressionNode right;
    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children (numerical {@link IntegerExpressionNode}s), returning whether the left child's value is greater than or equal to the right child's value
     * (e.g. {@code true} if left value > right value, otherwise {@code false})
     */
    public IntegerGreaterThanOrEqualNode(IntegerExpressionNode left, IntegerExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean evaluate() {
        return left.evaluate() >= right.evaluate();
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.GTE;
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }
    @Override
    public ExpressionNode left() {
        return this.left;
    }
    @Override
    public ExpressionNode right() {
        return this.right;
    }
    @Override
    public void setProvider(DataProvider dp) {
        left.setProvider(dp);
        right.setProvider(dp);
    }
}


