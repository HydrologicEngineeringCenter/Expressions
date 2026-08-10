package usace.hec.expressions.comparison;

import usace.hec.expressions.*;

import java.io.Serial;

public class IntegerBetweenNode extends TernaryExpressionNode implements BooleanExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode left;
    private IntegerExpressionNode middle;
    private IntegerExpressionNode right;

    public IntegerBetweenNode(IntegerExpressionNode left, IntegerExpressionNode middle, IntegerExpressionNode right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    @Override
    public boolean evaluate() {
        ee.clear();
        int leftVal = left.evaluate();
        int middleVal = middle.evaluate();
        int rightVal = right.evaluate();
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
