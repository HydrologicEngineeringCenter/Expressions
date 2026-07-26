package usace.hec.expressions;

public interface IntegerExpressionNode extends ExpressionNode<Integer>{
    Integer evaluate();
    @Override
    default ExpressionType resultType() {
        return ExpressionType.INTEGER;
    }
}
