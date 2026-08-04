package usace.hec.expressions.math;


import usace.hec.expressions.*;

public abstract class  DoubleBinaryExpressionNode implements BinaryExpressionNode, DoubleExpressionNode {

    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }
}
