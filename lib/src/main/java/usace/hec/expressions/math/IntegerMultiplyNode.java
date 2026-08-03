package usace.hec.expressions.math;

import usace.hec.expressions.*;

import java.io.Serial;


public class IntegerMultiplyNode extends IntegerBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode left;
    private IntegerExpressionNode right;
    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link IntegerExpressionNode}s), returning the multiplication {@code *} of the first child's value by the second
     * child's value (e.g. {@code x * y})
     */
    public IntegerMultiplyNode(IntegerExpressionNode left, IntegerExpressionNode right) {
        this.left = left;
        this.right= right;
    }

    @Override
    public int evaluate() {
        return left.evaluate()*right.evaluate();
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.MULTIPLY;
    }

    @Override
    public ExpressionNode left() {
        return this.left;
    }

    @Override
    public ExpressionNode right() {
        return this.right;
    }
    public void setProvider(DataProvider dp) {
        left.setProvider(dp);
        right.setProvider(dp);
    }
}