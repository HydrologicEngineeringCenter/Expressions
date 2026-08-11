package usace.hec.expressions.time;

import usace.hec.expressions.*;

import java.io.Serial;
import java.time.LocalDateTime;

public class DateBetweenNode extends TernaryExpressionNode implements BooleanExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode left;
    private DateTimeExpressionNode middle;
    private DateTimeExpressionNode right;

    public DateBetweenNode(DateTimeExpressionNode left, DateTimeExpressionNode middle, DateTimeExpressionNode right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    @Override
    public boolean evaluate() {
        ee.clear();
        LocalDateTime leftVal = left.evaluate();
        LocalDateTime middleVal = middle.evaluate();
        LocalDateTime rightVal = right.evaluate();
        checkErrors();
        return leftVal.isBefore(middleVal) && middleVal.isBefore(rightVal);
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
