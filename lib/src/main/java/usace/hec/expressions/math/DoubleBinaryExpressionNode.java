package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionType;

public abstract class  DoubleBinaryExpressionNode extends BinaryExpressionNode<Double,Double,Double> {

    public DoubleBinaryExpressionNode(ExpressionNode<Double> left, ExpressionNode<Double> right) {
        super(left, right);
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }
}
