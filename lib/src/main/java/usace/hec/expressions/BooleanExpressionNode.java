package usace.hec.expressions;

public interface BooleanExpressionNode extends ExpressionNode {
    boolean evaluate();
    @Override
    default ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }
}
