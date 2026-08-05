package usace.hec.expressions;

public interface TernaryExpressionNode extends ExpressionNode {
    ExpressionNode left();
    ExpressionNode middle();
    ExpressionNode right();

    @Override
    public default String PreFixSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append(Operator().getPrefixName());
        sb.append('(');
        sb.append(left().PreFixSyntax());
        sb.append(',');
        sb.append(middle().PreFixSyntax());
        sb.append(',');
        sb.append(right().PreFixSyntax());
        sb.append(')');
        return sb.toString();
    }
    @Override
    public default String ExcelSyntax(){
        StringBuilder sb = new StringBuilder();
        sb.append(Operator().getInfixSyntax());
        sb.append(left().ExcelSyntax());
        sb.append(',');
        sb.append(middle().ExcelSyntax());
        sb.append(',');
        sb.append(right().ExcelSyntax());
        sb.append(')');
        return sb.toString();
    }
    @Override
    public default void setProvider(DataProvider dp){
        left().setProvider(dp);
        middle().setProvider(dp);
        right().setProvider(dp);
    }

    @Override
    default EvaluationError getEvaluationError(){
        EvaluationError leftError = left().getEvaluationError();
        if (leftError != null && leftError.isInvalid()){
            return leftError;
        }
        EvaluationError middleError = middle().getEvaluationError();
        if (middleError != null && middleError.isInvalid()){
            return middleError;
        }
        EvaluationError rightError = right().getEvaluationError();
        if (rightError != null && rightError.isInvalid()){
            return rightError;
        }
        return ownError();
    }
    default EvaluationError ownError() {return null;}
}
