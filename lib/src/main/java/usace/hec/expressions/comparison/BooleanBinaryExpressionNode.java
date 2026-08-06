package usace.hec.expressions.comparison;


import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.EvaluationError;
import usace.hec.expressions.ExpressionType;

public abstract class  BooleanBinaryExpressionNode implements BinaryExpressionNode, BooleanExpressionNode {
    protected EvaluationError ee = new EvaluationError();
    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }
    @Override
    public EvaluationError getEvaluationError(){
        return this.ee;
    }
    @Override
    public void checkErrors(){
        if (left().hasError()) {
            ee = left().getEvaluationError();
        } else if (right().hasError()) {
            ee = right().getEvaluationError();
        }
    }
}
