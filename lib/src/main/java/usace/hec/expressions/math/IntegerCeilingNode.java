package usace.hec.expressions.math;

import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;


public class IntegerCeilingNode extends IntegerUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode child;
    /**
     * A numerical {@link UnaryExpressionNode} that evaluates a child (numerical {@link IntegerExpressionNode}), returning the {@code Math.ceil} of the child's value (e.g. {@code Math.ceil(6.6) == 7})
     */
    public IntegerCeilingNode(IntegerExpressionNode child) {
        this.child = child;
    }

    @Override
    public int evaluate() {
        return (int)Math.ceil(child.evaluate());
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.CEILING;
    }

    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }

    @Override
    public ExpressionNode child() {
        return this.child;
    }
}
