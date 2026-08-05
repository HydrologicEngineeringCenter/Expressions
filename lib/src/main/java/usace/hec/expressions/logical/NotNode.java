package usace.hec.expressions.logical;


import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.UnaryExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class NotNode extends BooleanUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private BooleanExpressionNode child;
    /**
     * A boolean {@link UnaryExpressionNode} that evaluates two children (boolean {@link ExpressionNode}s), returning the AND of
     * the childs' values (e.g. {@code true} if both children are true, otherwise {@code false})
     */
    public NotNode(BooleanExpressionNode child) {
        this.child = child;
    }
    @Override
    public boolean evaluate() {
        boolean childVal = !child.evaluate();
        checkErrors();
        return childVal;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.NOT;
    }
    @Override
    public ExpressionNode child() {
        return this.child;
    }
}

