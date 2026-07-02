package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;

public class ExponentNode extends BinaryExpressionNode<Double,Double,Double> {
    @Serial
    private static final long serialVersionUID = 1L;
    public ExponentNode(ExpressionNode<Double> left, ExpressionNode<Double> right) {
        super(left, right);
        
    }
    @Override
    public Double evaluate() {
        Double left = leftnode.evaluate();
        Double right = rightnode.evaluate();
        if (left.doubleValue() < 0 && right.doubleValue() == 0.5){
            throw new UnsupportedOperationException("Imaginary number unsupported");
        }
        Double result = Math.pow(left.doubleValue(), right.doubleValue());
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
        return ExpressionOperator.POW;
    }
}
