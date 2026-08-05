package usace.hec.expressions.math;


import usace.hec.expressions.EvaluationError;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.BinaryExpressionNode;

public abstract class  IntegerBinaryExpressionNode implements BinaryExpressionNode, IntegerExpressionNode {
    protected transient EvaluationError ee = new EvaluationError();
    @Override
    public ExpressionType resultType() {
        return ExpressionType.INTEGER;
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
