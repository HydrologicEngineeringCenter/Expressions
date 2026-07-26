package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionType;

public abstract class  IntegerBinaryExpressionNode extends BinaryExpressionNode<Integer,Double,Double> {

    public IntegerBinaryExpressionNode(ExpressionNode<Double> left, ExpressionNode<Double> right) {
        super(left, right);
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }
}
