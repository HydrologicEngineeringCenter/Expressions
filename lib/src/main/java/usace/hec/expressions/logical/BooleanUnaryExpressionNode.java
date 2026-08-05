package usace.hec.expressions.logical;

import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.EvaluationError;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.UnaryExpressionNode;

public abstract class BooleanUnaryExpressionNode implements UnaryExpressionNode, BooleanExpressionNode{
    protected EvaluationError ee;
    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }
    @Override
    public EvaluationError getEvaluationError(){
        return this.ee;
    }
    public void checkErrors(){
        if (child().hasError()) {ee = child().getEvaluationError();}
    }
}
