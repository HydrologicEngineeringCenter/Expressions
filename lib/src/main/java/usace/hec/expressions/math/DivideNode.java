package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

public class DivideNode extends BinaryExpressionNode<Double,Double,Double> {
    public DivideNode(ExpressionNode<Double> left, ExpressionNode<Double> right) {
        super(left, right);
        
    }
    @Override
    public Double evaluate() {
        Double left = leftnode.evaluate();
        Double right = rightnode.evaluate();
        Double result = left.doubleValue() / right.doubleValue();
        return result;
    }
    @Override
    public String OpName() {
        return Operator().getPrefixName();
    }
    @Override
    public String InfixOpName() {
        return Operator().getInfixName();
    }
    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.DIVIDE;
    }
}
