package usace.hec.expressions.strings;


import usace.hec.expressions.EvaluationError;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.StringExpressionNode;
import usace.hec.expressions.UnaryExpressionNode;

public abstract class StringUnaryExpressionNode implements UnaryExpressionNode, StringExpressionNode {
    protected EvaluationError ee = new EvaluationError();
    @Override
    public ExpressionType resultType() {
        return ExpressionType.STRING;
    }
    @Override
    public EvaluationError getEvaluationError(){
        return this.ee;
    }
    public void checkErrors(){
        if (child().hasError()) {ee = child().getEvaluationError();}
    }
}
