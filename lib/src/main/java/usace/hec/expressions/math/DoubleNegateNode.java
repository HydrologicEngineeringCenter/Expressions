package usace.hec.expressions.math;

import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;


public class DoubleNegateNode extends DoubleUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode child;
    /**
     * A numerical {@link UnaryExpressionNode} that evaluates a child (numerical {@link DoubleExpressionNode}), returning the negation {@code -} of the child's value (e.g. {@code -x})
     */
    public DoubleNegateNode(DoubleExpressionNode child) {
        this.child = child;
    }

    @Override
    public double evaluate() {
        double childVal = child.evaluate();
        checkErrors();
        return -childVal;
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.NEGATE;
    }
    @Override
    public ExpressionNode child() {
        return this.child;
    }
}
