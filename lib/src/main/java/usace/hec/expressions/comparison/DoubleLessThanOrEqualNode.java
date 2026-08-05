package usace.hec.expressions.comparison;



import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;

import java.io.Serial;

public class DoubleLessThanOrEqualNode extends BooleanBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode left;
    private DoubleExpressionNode right;
    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children (numerical {@link DoubleExpressionNode}s), returning whether the left child's value is less than or equal the right child's value
     * (e.g. {@code true} if left value > right value, otherwise {@code false})
     */
    public DoubleLessThanOrEqualNode(DoubleExpressionNode left, DoubleExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean evaluate() {
        double leftVal = left.evaluate();
        double rightVal = right.evaluate();
        checkErrors();
        return leftVal <= rightVal;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.LTE;
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

