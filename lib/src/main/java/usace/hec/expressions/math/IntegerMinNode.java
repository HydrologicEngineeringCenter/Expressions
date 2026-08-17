package usace.hec.expressions.math;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.BinaryExpressionNode;

import java.io.Serial;


public class IntegerMinNode extends IntegerBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode left;
    private IntegerExpressionNode right;
    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link IntegerExpressionNode}s) returning the minimum value ({@code Math.min}) between
     * the first child's value and the second child's value (e.g. {@code min(8,16) == 8})
     */
    public IntegerMinNode(IntegerExpressionNode left, IntegerExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public int evaluate() {
        ee.clear();
        int l = left.evaluate();
        int r = right.evaluate();
        checkErrors();
        return Math.min(l,r);
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.MIN;
    }

    @Override
    public String ExcelSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append(Operator().getPrefixName());
        sb.append('(');
        sb.append(left().ExcelSyntax());
        sb.append(',');
        sb.append(right().ExcelSyntax());
        sb.append(')');
        return sb.toString();    }
    @Override
    public ExpressionNode left() {
        return this.left;
    }
    @Override
    public ExpressionNode right() {
        return this.right;
    }
}
