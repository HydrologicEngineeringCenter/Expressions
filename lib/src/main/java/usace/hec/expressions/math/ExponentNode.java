package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class ExponentNode extends NumericalBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link ExpressionNode}s) with exponentiation ({@code ^}) returning the value of the
     * first child's value to the power of the second child's value (e.g. {@code x^y})
     */
    public ExponentNode(ExpressionNode<Number> left, ExpressionNode<Number> right) {
        super(left, right);
        
    }
    @Override
    public Double evaluate() {
        Double left = ((Number)leftnode.evaluate()).doubleValue();
        Double right = ((Number)rightnode.evaluate()).doubleValue();
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
