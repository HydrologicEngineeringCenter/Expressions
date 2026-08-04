package usace.hec.expressions.math;


import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.BinaryExpressionNode;

public abstract class  IntegerBinaryExpressionNode implements BinaryExpressionNode, IntegerExpressionNode {

    @Override
    public ExpressionType resultType() {
        return ExpressionType.INTEGER;
    }
}
