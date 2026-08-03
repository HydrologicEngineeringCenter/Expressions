package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class DoubleMultiplyNode extends DoubleBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode left;
    private DoubleExpressionNode right;
    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link DoubleExpressionNode}s), returning the multiplication {@code *} of the first child's value by the second
     * child's value (e.g. {@code x * y})
     */
    public DoubleMultiplyNode(DoubleExpressionNode left, DoubleExpressionNode right) {
        this.left = left;
        this.right= right;
    }

    @Override
    public double evaluate() {
        return left.evaluate()*right.evaluate();
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
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
}