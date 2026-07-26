package usace.hec.expressions.comparison;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.ExpressionType;

public abstract class  BooleanBinaryExpressionNode implements BinaryExpressionNode, BooleanExpressionNode {
    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }

}
