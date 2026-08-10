package usace.hec.expressions.comparison;

import usace.hec.expressions.*;

import java.io.Serial;

public class DoubleBetweenNode extends TernaryExpressionNode implements BooleanExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode left;
    private DoubleExpressionNode middle;
    private DoubleExpressionNode right;

    public DoubleBetweenNode(DoubleExpressionNode left, DoubleExpressionNode middle, DoubleExpressionNode right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    @Override
    public boolean evaluate() {
        ee.clear();
        double leftVal = left.evaluate();
        double middleVal = middle.evaluate();
        double rightVal = right.evaluate();
        checkErrors();
        return leftVal < middleVal && middleVal < rightVal;
    }

    @Override
    protected ExpressionNode left() {
        return left;
    }

    @Override
    protected ExpressionNode middle() {
        return middle;
    }

    @Override
    protected ExpressionNode right() {
        return right;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.BETWEEN;
    }
}
