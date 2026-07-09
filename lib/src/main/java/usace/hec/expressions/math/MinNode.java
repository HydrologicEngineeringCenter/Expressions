package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class MinNode extends BinaryExpressionNode<Double, Double, Double> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link ExpressionNode}s) returning the minimum value ({@code Math.min}) between
     * the first child's value and the second child's value (e.g. {@code min(8,16) == 8})
     */
    public MinNode(ExpressionNode<Double> left, ExpressionNode<Double> right) {
        super(left, right);
    }
    @Override
    public Double evaluate() {
        Double left = leftnode.evaluate();
        Double right = rightnode.evaluate();
        Double result = Math.min(left.doubleValue(), right.doubleValue());
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
        return ExpressionOperator.MIN;
    }

    @Override
    public void excelAppend(StringBuilder sb) {
        sb.append(InfixOpName());
        sb.append('(');
        leftnode.excelAppend(sb);
        sb.append(',');
        rightnode.excelAppend(sb);
        sb.append(')');
    }
}
