package usace.hec.expressions.math;

import usace.hec.expressions.*;

import java.io.Serial;


public class IntegerMaxNode extends IntegerBinaryExpressionNode{
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode left;
    private IntegerExpressionNode right;
    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link IntegerExpressionNode}s) returning the maximum value ({@code Math.max}) between
     * the first child's value and the second child's value (e.g. {@code max(8,16) == 16})
     */
    public IntegerMaxNode(IntegerExpressionNode left, IntegerExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public int evaluate() {
        return Math.max(left.evaluate(),right.evaluate());
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.MAX;
    }
    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }
    @Override
    public ExpressionNode left() {
        return this.left;
    }
    @Override
    public ExpressionNode right() {
        return this.right;
    }
    public void setProvider(DataProvider dp) {
        left.setProvider(dp);
        right.setProvider(dp);
    }
}
