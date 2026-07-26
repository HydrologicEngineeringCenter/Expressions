package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class MaxNode extends NumericalBinaryExpressionNode{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link ExpressionNode}s) returning the maximum value ({@code Math.max}) between
     * the first child's value and the second child's value (e.g. {@code max(8,16) == 16})
     */
    public MaxNode(ExpressionNode<Number> left, ExpressionNode<Number> right) {
        super(left, right);
    }
    @Override
    public Double evaluate() {
        Double left = ((Number)leftnode.evaluate()).doubleValue();
        Double right = ((Number)rightnode.evaluate()).doubleValue();
        return Math.max(left,right);
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
        return ExpressionOperator.MAX;
    }
    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }
}
