package usace.hec.expressions.strings;


import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.EvaluationError;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.StringExpressionNode;

public abstract class StringBinaryExpressionNode implements BinaryExpressionNode, StringExpressionNode {
    protected EvaluationError ee;
    @Override
    public ExpressionType resultType() {
        return ExpressionType.STRING;
    }
    @Override
    public EvaluationError getEvaluationError(){
        return this.ee;
    }
    public void checkErrors(){
        if (left().hasError()) {
            ee = left().getEvaluationError();
        } else if (right().hasError()) {
            ee = right().getEvaluationError();
        }
    }
}
