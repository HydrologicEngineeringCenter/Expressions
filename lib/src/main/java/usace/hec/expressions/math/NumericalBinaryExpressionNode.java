package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionType;

public abstract class  NumericalBinaryExpressionNode extends BinaryExpressionNode<Number,Number,Number> {

    public NumericalBinaryExpressionNode(ExpressionNode<Number> left, ExpressionNode<Number> right) {
        super(left, right);
    }
    @Override
    public ExpressionType resultType() {
        ExpressionType left = leftnode.resultType();
        ExpressionType right = rightnode.resultType();
        if (left == ExpressionType.DOUBLE || right == ExpressionType.DOUBLE)
            return ExpressionType.DOUBLE;
        if (left == ExpressionType.INTEGER && right == ExpressionType.INTEGER)
            return ExpressionType.INTEGER;
        return ExpressionType.DOUBLE;
    }
}
