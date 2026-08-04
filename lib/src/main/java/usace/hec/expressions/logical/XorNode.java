package usace.hec.expressions.logical;

import usace.hec.expressions.*;
import usace.hec.expressions.comparison.BooleanBinaryExpressionNode;

import java.io.Serial;


public class XorNode extends BooleanBinaryExpressionNode{
    @Serial
    private static final long serialVersionUID = 1L;
    private BooleanExpressionNode left;
    private BooleanExpressionNode right;
    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children (boolean {@link ExpressionNode}s), returning the XOR of
     * the childs' values (e.g. {@code true} if only one child is true, otherwise {@code false})
     */
    public XorNode(BooleanExpressionNode left, BooleanExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean evaluate() {
        return left.evaluate() ^ right.evaluate();
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.XOR;
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
}

