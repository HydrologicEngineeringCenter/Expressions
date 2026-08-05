package usace.hec.expressions.math;


import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.EvaluationError;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.UnaryExpressionNode;

public abstract class  DoubleUnaryExpressionNode implements UnaryExpressionNode, DoubleExpressionNode {
    protected EvaluationError ee;
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }
    @Override
    public EvaluationError getEvaluationError(){
        return this.ee;
    }
    public void checkErrors(){
        if (child().hasError()) {ee = child().getEvaluationError();}
    }
}
