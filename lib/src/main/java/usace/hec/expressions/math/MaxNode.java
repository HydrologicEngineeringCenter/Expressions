package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;

public class MaxNode extends BinaryExpressionNode<Double, Double, Double>{
    public MaxNode(ExpressionNode<Double> left, ExpressionNode<Double> right) {
        super(left, right);
    }

    @Override
    public String OpName() {
        return "MAX";
    }

    @Override
    public String InfixOpName() {
        return "max";
    }

    @Override
    public Double evaluate() {
        Double left = leftnode.evaluate();
        Double right = rightnode.evaluate();
        Double result =  Math.max(left.doubleValue(), right.doubleValue());
        return result;
    }
}
