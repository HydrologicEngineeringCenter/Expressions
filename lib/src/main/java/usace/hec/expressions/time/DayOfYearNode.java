package usace.hec.expressions.time;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;
import java.time.LocalDateTime;

public class DayOfYearNode extends UnaryExpressionNode<Integer, LocalDateTime> {
    @Serial
    private static final long serialVersionUID = 1L;
    public DayOfYearNode(ExpressionNode<LocalDateTime> child) {
        super(child);
    }

    @Override
    public Integer evaluate() {
        //return the day of year
        LocalDateTime childDate = child.evaluate();
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
        return ExpressionOperator.DOY;
    }

    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.INTEGER;
    }
}
