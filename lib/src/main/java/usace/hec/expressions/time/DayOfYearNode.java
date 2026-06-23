package usace.hec.expressions.time;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;

import java.time.LocalDate;

public class DayOfYearNode extends UnaryExpressionNode<Integer, LocalDate> {
    public DayOfYearNode(ExpressionNode<LocalDate> child) {
        super(child);
    }

    @Override
    public Integer evaluate() {
        //return the day of year
        LocalDate childDate = child.evaluate();
        Integer dayOfTheYear = childDate.getDayOfYear();
        return dayOfTheYear.intValue();
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
        return ExpressionOperator.DAY;
    }

    @Override
    public void excelAppend(StringBuilder sb) {
        sb.append(InfixOpName());
        sb.append('(');
        child.excelAppend(sb);
        sb.append(')');
    }
}
