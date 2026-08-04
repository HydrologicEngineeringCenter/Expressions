package usace.hec.expressions.math;


import usace.hec.expressions.*;

public abstract class  DoubleUnaryExpressionNode implements UnaryExpressionNode, DoubleExpressionNode {
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }
}
