package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;

import java.io.Serial;


public class DivideNode extends DoubleBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link ExpressionNode}s), returning the division {@code /} of
     * the first child's value by the second child's value (e.g. {@code x/y})
     */
    public DivideNode(ExpressionNode<Double> left, ExpressionNode<Double> right) {
        super(left, right);
        
    }
    @Override
    public Double evaluate() {
        Double left = leftnode.evaluate();
        Double right = rightnode.evaluate();
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
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;//placeholder
    }
}
