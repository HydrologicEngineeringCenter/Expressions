package usace.hec.expressions;

public interface UnaryExpressionNode  extends ExpressionNode {
    ExpressionNode child();

    @Override
    public default String PreFixSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append(Operator().getPrefixName());
        sb.append('(');
        sb.append(child().PreFixSyntax());
        sb.append(')');
        return sb.toString();
    }

    @Override
    public default String ExcelSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(Operator().getInfixName());
        sb.append(child().ExcelSyntax());
        sb.append(')');
        return sb.toString();
    }

    @Override
    public default void setProvider(DataProvider dp){
        child().setProvider(dp);
    }
    @Override
    default EvaluationError getEvaluationError(){
        EvaluationError childError = child().getEvaluationError();
        if (childError !=null && childError.isInvalid()){
            return childError;
        }
        return ownError();
    }
    default EvaluationError ownError() {return null;}
}
