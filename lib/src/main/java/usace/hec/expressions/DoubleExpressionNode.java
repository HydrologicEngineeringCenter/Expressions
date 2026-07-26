package usace.hec.expressions;

public interface DoubleExpressionNode extends ExpressionNode<Double> {
    Double evaluate();
    @Override
    default ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }
}
