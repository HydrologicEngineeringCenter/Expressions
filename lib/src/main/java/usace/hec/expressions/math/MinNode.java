package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;

public class MinNode extends BinaryExpressionNode<Double, Double, Double> {

    public MinNode(ExpressionNode<Double> left, ExpressionNode<Double> right) {
        super(left, right);
    }

    @Override
    public String OpName() {
        return "MIN";
    }

    @Override
    public String InfixOpName() {
        return "min";
    }

    @Override
    public Double evaluate() {
        Double left = leftnode.evaluate();
        Double right = rightnode.evaluate();
        Double result = Math.min(left.doubleValue(), right.doubleValue());
        return result;
    }
}
