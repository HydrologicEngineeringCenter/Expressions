package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class DivideNode extends NumericalBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link ExpressionNode}s), returning the division {@code /} of
     * the first child's value by the second child's value (e.g. {@code x/y})
     */
    public DivideNode(ExpressionNode<Number> left, ExpressionNode<Number> right) {
        super(left, right);
        
    }
    @Override
    public Double evaluate() {
        Double left = ((Number)leftnode.evaluate()).doubleValue();
        Double right = ((Number)rightnode.evaluate()).doubleValue();
        if (right.doubleValue() == 0.0){
            throw new ArithmeticException("Division by zero");
        }
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
