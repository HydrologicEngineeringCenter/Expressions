package usace.hec.expressions.math;


import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionType;

public abstract class  DoubleBinaryExpressionNode implements BinaryExpressionNode, DoubleExpressionNode {

    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }
}
