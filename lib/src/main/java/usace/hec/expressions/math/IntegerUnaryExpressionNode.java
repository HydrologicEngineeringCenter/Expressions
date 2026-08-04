package usace.hec.expressions.math;


import usace.hec.expressions.*;

public abstract class  IntegerUnaryExpressionNode implements UnaryExpressionNode, IntegerExpressionNode {
    @Override
    public ExpressionType resultType() {
        return ExpressionType.INTEGER;
    }
}
