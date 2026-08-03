package usace.hec.expressions.comparison;

import usace.hec.expressions.*;

import java.io.Serial;

public class DoubleEqualToNode extends BooleanBinaryExpressionNode{
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode left;
    private DoubleExpressionNode right;
    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children ( {@link DoubleExpressionNode}s), returning whether the two values are equal
     * (e.g. {@code true} if both children are the same value, otherwise {@code false})
     */
    public DoubleEqualToNode(DoubleExpressionNode left, DoubleExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean evaluate() {
        return left.evaluate() == right.evaluate();
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.EQ;
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
