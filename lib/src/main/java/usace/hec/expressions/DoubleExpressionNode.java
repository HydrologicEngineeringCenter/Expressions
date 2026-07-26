package usace.hec.expressions;

public interface DoubleExpressionNode extends ExpressionNode {
    double evaluate();
    @Override
    default ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }
}
