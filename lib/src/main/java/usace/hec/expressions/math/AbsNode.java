package usace.hec.expressions.math;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class AbsNode extends DoubleUnaryExpressionNode<Double> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A numerical {@link DoubleUnaryExpressionNode} that evaluates a child (numerical {@link ExpressionNode}), returning the absolute value {@code Math.abs} of the child's value (e.g. {@code |-2| == 2})
     */
    public AbsNode(ExpressionNode<Double> child) {
        super(child);
    }

    @Override
    public Double evaluate() {
        Double value = child.evaluate();
        Double result = Math.abs(value);
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
        return ExpressionOperator.ABS;
    }

    @Override
    public String ExcelSyntax() {
        return Operator().getInfixName() +  child.ExcelSyntax() + Operator().getInfixName();
    }
}
