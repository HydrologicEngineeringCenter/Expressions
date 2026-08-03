package usace.hec.expressions.math;

import usace.hec.expressions.*;

import java.io.Serial;


public class DoubleMinusNode extends DoubleBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode left;
    private DoubleExpressionNode right;
    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link DoubleExpressionNode}s), returning the subtraction {@code -} of the left child's value by
     * the right child's value (e.g. {@code x - y})
     */
    public DoubleMinusNode(DoubleExpressionNode left, DoubleExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public double evaluate() {

        return left.evaluate() - right.evaluate();
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.MINUS;
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
