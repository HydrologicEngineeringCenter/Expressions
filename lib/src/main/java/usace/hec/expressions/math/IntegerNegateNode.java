package usace.hec.expressions.math;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;


public class IntegerNegateNode extends IntegerUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode child;
    /**
     * A numerical {@link UnaryExpressionNode} that evaluates a child (numerical {@link IntegerExpressionNode}), returning the negation {@code -} of the child's value (e.g. {@code -x})
     */
    public IntegerNegateNode(IntegerExpressionNode child) {
        this.child = child;
    }

    @Override
    public int evaluate() {
        int c = child.evaluate();
        checkErrors();
        return -c;
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.NEGATE;
    }

    @Override
    public ExpressionNode child() {
        return this.child;
    }
}
