package usace.hec.expressions.logical;

import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.UnaryExpressionNode;

public abstract class BooleanUnaryExpressionNode implements UnaryExpressionNode, BooleanExpressionNode{
    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }
}
