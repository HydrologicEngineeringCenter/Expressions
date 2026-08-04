package usace.hec.expressions.time;

import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;

import java.io.Serial;
import java.time.*;


public class WaterYearNode extends CalendarYearNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode child;
    public WaterYearNode(DateTimeExpressionNode child) {
        super(child);
    }
    @Override
    public int evaluate() {
        int year = super.evaluate();
        if (((DateTimeExpressionNode) super.child()).evaluate().isAfter(LocalDateTime.of(year, Month.SEPTEMBER,30,23,59))){
            return year + 1;
        }
        return year;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.WATERYEAR;
    }

    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.INTEGER;
    }
    @Override
    public ExpressionNode child() {
        return this.child;
    }

}