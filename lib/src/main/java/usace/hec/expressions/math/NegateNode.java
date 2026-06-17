package usace.hec.expressions.math;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.UnaryExpressionNode;

public class NegateNode extends UnaryExpressionNode<Double> {
    public NegateNode(ExpressionNode<Double> child) {
        super(child);
    }

    @Override
    public String OpName() {
        return "NEG";
    }

    @Override
    public String InfixOpName() {
        return "-";
    }

    @Override
    public Double evaluate() {
        Double value = child.evaluate();
        Double result = -value;
        return result;
    }
}
