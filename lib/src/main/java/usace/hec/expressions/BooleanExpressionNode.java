package usace.hec.expressions;

public interface BooleanExpressionNode extends ExpressionNode<Boolean> {
    Boolean evaluate();
    @Override
    default ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }
}
