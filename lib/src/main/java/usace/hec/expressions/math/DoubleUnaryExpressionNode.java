package usace.hec.expressions.math;


import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.UnaryExpressionNode;

public abstract class  DoubleUnaryExpressionNode implements UnaryExpressionNode, DoubleExpressionNode {
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }
}
