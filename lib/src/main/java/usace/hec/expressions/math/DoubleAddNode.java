package usace.hec.expressions.math;

import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.BinaryExpressionNode;


import java.io.Serial;



public class DoubleAddNode extends DoubleBinaryExpressionNode{
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode left;
    private DoubleExpressionNode right;
    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link DoubleExpressionNode}s), returning the summation {@code +} of the childs' values (e.g. {@code 2 + 2 == 4})
     */
    public DoubleAddNode(DoubleExpressionNode left, DoubleExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public double evaluate() {
        ee.clear();
        double leftVal = left.evaluate();
        double rightVal = right.evaluate();
        checkErrors();
        return leftVal + rightVal;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.PLUS;
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
