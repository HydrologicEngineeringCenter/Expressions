package usace.hec.expressions.comparison;

import usace.hec.expressions.*;

public abstract class  BooleanBinaryExpressionNode implements BinaryExpressionNode, BooleanExpressionNode{
    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }


}
