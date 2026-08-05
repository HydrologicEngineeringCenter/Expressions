package usace.hec.expressions;

public abstract class TernaryExpressionNode implements ExpressionNode {
    protected abstract ExpressionNode left();
    protected abstract ExpressionNode middle();
    protected abstract ExpressionNode right();
    protected transient EvaluationError ee = new EvaluationError();

    @Override
    public String PreFixSyntax() {
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
    public String ExcelSyntax(){
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
    public void setProvider(DataProvider dp){
        left().setProvider(dp);
        middle().setProvider(dp);
        right().setProvider(dp);
    }
    public void checkErrors(){
        if (left().hasError()) {ee = left().getEvaluationError();}
        else if (middle().hasError()) {ee = middle().getEvaluationError();}
        else if (right().hasError()) {ee = right().getEvaluationError();}
    }

}
