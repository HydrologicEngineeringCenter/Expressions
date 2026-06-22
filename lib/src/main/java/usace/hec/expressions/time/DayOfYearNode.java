package usace.hec.expressions.time;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;

public class DayOfYearNode extends UnaryExpressionNode<Double> {
    public DayOfYearNode(ExpressionNode<Double> child) {
        super(child);
    }

    @Override
    public Double evaluate() {
        //return the day of year
        return 0.0;
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
