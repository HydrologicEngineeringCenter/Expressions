package usace.hec.expressions.math;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;


public class NegateNode extends DoubleUnaryExpressionNode<Double> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A numerical {@link UnaryExpressionNode} that evaluates a child (numerical {@link ExpressionNode}), returning the negation {@code -} of the child's value (e.g. {@code -x})
     */
    public NegateNode(ExpressionNode<Double> child) {
        super(child);
    }

    @Override
    public Double evaluate() {
        Double value = child.evaluate();
        Double result = -value;
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
        return ExpressionOperator.NEGATE;
    }
}
