package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;

public class ExponentNode extends BinaryExpressionNode<Double,Double,Double> {
    public ExponentNode(ExpressionNode<Double> left, ExpressionNode<Double> right) {
        super(left, right);
        
    }
    @Override
    public Double evaluate() {
        Double left = leftnode.evaluate();
        Double right = rightnode.evaluate();
        Double result = Math.pow(left.doubleValue(), right.doubleValue());
        return result;
    }
    @Override
    public String OpName() {
        return "POW";
    }
    @Override
    public String InfixOpName() {
        return "^";
    }
}
