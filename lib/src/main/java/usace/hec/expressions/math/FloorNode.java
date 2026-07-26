package usace.hec.expressions.math;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;

public class FloorNode extends DoubleUnaryExpressionNode<Double> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A numerical {@link UnaryExpressionNode} that evaluates a child (numerical {@link ExpressionNode}), returning the {@code Math.floor} of the child's value (e.g. {@code Math.floor(6.6) == 6})
     */
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
        return ExpressionOperator.FLOOR;
    }
    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }
}
