package usace.hec.expressions.math;


import usace.hec.expressions.EvaluationError;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.UnaryExpressionNode;

public abstract class IntegerUnaryExpressionNode implements UnaryExpressionNode, IntegerExpressionNode {
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
        if (child().hasError()) {ee = child().getEvaluationError();}
    }
}
