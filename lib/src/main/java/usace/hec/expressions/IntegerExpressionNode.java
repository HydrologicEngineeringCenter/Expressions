package usace.hec.expressions;

public interface IntegerExpressionNode extends ExpressionNode{
    int evaluate();
    @Override
    default ExpressionType resultType() {
        return ExpressionType.INTEGER;
    }
}
