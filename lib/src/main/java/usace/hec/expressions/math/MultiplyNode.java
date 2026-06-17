package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;

public class MultiplyNode extends BinaryExpressionNode<Double,Double,Double> {
    public MultiplyNode(ExpressionNode<Double> left, ExpressionNode<Double> right) {
        super(left, right);
        
    }
    @Override
    public Double evaluate() {
        Double left = leftnode.evaluate();
        Double right = rightnode.evaluate();
        Double result = left.doubleValue() * right.doubleValue();
        return result;
    }
    @Override
    public String OpName() {
        return "MULTIPLY";
    }
    @Override
    public String InfixOpName() {
        return "*";
    }
}
