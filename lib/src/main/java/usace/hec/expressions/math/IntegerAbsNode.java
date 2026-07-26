package usace.hec.expressions.math;

import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class IntegerAbsNode extends IntegerUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode child;
    /**
     * A numerical {@link IntegerUnaryExpressionNode} that evaluates a child (numerical {@link IntegerExpressionNode}), returning the absolute value {@code Math.abs} of the child's value (e.g. {@code |-2| == 2})
     */
    public IntegerAbsNode(IntegerExpressionNode child) {
        this.child = child;
    }

    @Override
    public int evaluate() {
        return Math.abs(child.evaluate());

    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.ABS;
    }

    @Override
    public String ExcelSyntax() {
        return Operator().getInfixName() +  child.ExcelSyntax() + Operator().getInfixName();
    }

    @Override
    public ExpressionNode child() {
        return this.child;
    }
}
