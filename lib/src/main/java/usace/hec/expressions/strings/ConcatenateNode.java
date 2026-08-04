package usace.hec.expressions.strings;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.StringExpressionNode;

import java.io.Serial;



public class ConcatenateNode implements StringExpressionNode, BinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringExpressionNode left;
    private final StringExpressionNode right;

    public ConcatenateNode(StringExpressionNode left, StringExpressionNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String evaluate() {
        return left.evaluate().toString() + right.evaluate().toString();
    }

    @Override
    public ExpressionNode left() { return left; }

    @Override
    public ExpressionNode right() { return right; }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.CONCAT;
    }
}