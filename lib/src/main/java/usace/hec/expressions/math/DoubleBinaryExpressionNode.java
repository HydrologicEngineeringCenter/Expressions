package usace.hec.expressions.math;


import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.EvaluationError;
import usace.hec.expressions.ExpressionType;

public abstract class  DoubleBinaryExpressionNode implements BinaryExpressionNode, DoubleExpressionNode {
    protected transient EvaluationError ee = new EvaluationError();
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
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
