package usace.hec.expressions.math;


import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.UnaryExpressionNode;

public abstract class  IntegerUnaryExpressionNode implements UnaryExpressionNode, IntegerExpressionNode {
    @Override
    public ExpressionType resultType() {
        return ExpressionType.INTEGER;
    }
}
