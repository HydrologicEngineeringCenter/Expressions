package usace.hec.expressions.math;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;

public class AbsNode extends UnaryExpressionNode<Double> {

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
    public void excelAppend(StringBuilder sb) {
        sb.append(Operator().getInfixName());
        child.excelAppend(sb);
        sb.append(Operator().getInfixName());
    }
}
