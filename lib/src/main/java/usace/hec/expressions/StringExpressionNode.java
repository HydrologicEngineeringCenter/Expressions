package usace.hec.expressions;


public interface StringExpressionNode extends ExpressionNode {
    String evaluate();
    @Override
    default ExpressionType resultType() {
        return ExpressionType.STRING;
    }
}
