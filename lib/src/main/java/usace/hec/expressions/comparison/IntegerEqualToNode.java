package usace.hec.expressions.comparison;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.IntegerExpressionNode;

import java.io.Serial;

public class IntegerEqualToNode extends BooleanBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode left;
    private IntegerExpressionNode right;
    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children ( {@link IntegerExpressionNode}s), returning whether the two values are equal
     * (e.g. {@code true} if both children are the same value, otherwise {@code false})
     */
    public IntegerEqualToNode(IntegerExpressionNode left, IntegerExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean evaluate() {
        ee.clear();
        int leftVal = left.evaluate();
        int rightVal = right.evaluate();
        checkErrors();
        return leftVal == rightVal;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.EQ;
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
