package usace.hec.expressions.math;


import usace.hec.expressions.*;

public abstract class  IntegerBinaryExpressionNode implements BinaryExpressionNode, IntegerExpressionNode {

    @Override
    public ExpressionType resultType() {
        return ExpressionType.INTEGER;
    }
}
