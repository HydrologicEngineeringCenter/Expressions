package usace.hec.expressions.math;

import usace.hec.expressions.*;

import java.io.Serial;

public class IntegerFloorNode extends IntegerUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode child;
    /**
     * A numerical {@link UnaryExpressionNode} that evaluates a child (numerical {@link IntegerExpressionNode}), returning the {@code Math.floor} of the child's value (e.g. {@code Math.floor(6.6) == 6})
     */
    public IntegerFloorNode(IntegerExpressionNode child) {
        this.child = child;
    }

    @Override
    public int evaluate() {
        return (int)Math.floor(child.evaluate());
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.FLOOR;
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
