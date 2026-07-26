package usace.hec.expressions.math;

import java.io.Serializable;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.UnaryExpressionNode;

public abstract class  DoubleUnaryExpressionNode<T extends Serializable> extends UnaryExpressionNode<Double, T> {

    public DoubleUnaryExpressionNode(ExpressionNode<T> child) {
        super(child);
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }
}
