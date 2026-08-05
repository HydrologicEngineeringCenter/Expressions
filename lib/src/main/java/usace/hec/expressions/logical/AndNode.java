package usace.hec.expressions.logical;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.comparison.BooleanBinaryExpressionNode;

import java.io.Serial;


public class AndNode extends BooleanBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private BooleanExpressionNode left;
    private BooleanExpressionNode right;
    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children (boolean {@link ExpressionNode}s), returning the AND of
     * the childs' values (e.g. {@code true} if both children are true, otherwise {@code false})
     */
    public AndNode(BooleanExpressionNode left, BooleanExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean evaluate() {
        boolean leftVal = left.evaluate();
        boolean rightVal = right.evaluate();
        checkErrors();
        return leftVal && rightVal;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.AND;
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

