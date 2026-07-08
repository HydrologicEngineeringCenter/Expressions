package usace.hec.expressions.math;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;

/**
 * A {@link UnaryExpressionNode} that evaluates a child (numerical {@link ExpressionNode}), returning the floor of the child's value
 */
public class FloorNode extends UnaryExpressionNode<Double, Double> {
    @Serial
    private static final long serialVersionUID = 1L;
    public FloorNode(ExpressionNode<Double> child) {
        super(child);
    }

    @Override
    public Double evaluate() {
        Double value = child.evaluate();
        Double result = Math.floor(value);
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
    public void excelAppend(StringBuilder sb) {
        sb.append(InfixOpName());
        sb.append('(');
        child.excelAppend(sb);
        sb.append(')');
    }
}
