package usace.hec.expressions.comparison;

import usace.hec.expressions.*;

import java.io.Serial;

public class BooleanEqualToNode extends BooleanBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private BooleanExpressionNode left;
    private BooleanExpressionNode right;
    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children ( {@link BooleanExpressionNode}s), returning whether the two values are equal
     * (e.g. {@code true} if both children are the same value, otherwise {@code false})
     */
    public BooleanEqualToNode(BooleanExpressionNode left, BooleanExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean evaluate() {
        return left.evaluate() == right.evaluate();
    }
    @Override
    public ExpressionOperator Operator() {
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
