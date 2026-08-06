package usace.hec.expressions.time;

import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;
import java.time.*;


public class WaterYearNode extends CalendarYearNode {
    @Serial
    private static final long serialVersionUID = 1L;
    public WaterYearNode(DateTimeExpressionNode child) {
        super(child);
    }
    @Override
    public int evaluate() {
        int year = super.evaluate();
        if (super.hasError()) {
            if (((DateTimeExpressionNode) super.child()).evaluate().isAfter(LocalDateTime.of(year, Month.SEPTEMBER, 30, 23, 59))) {
                return year + 1;
            }
            return year;
        }
        return 0;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.WATERYEAR;
    }
    @Override
    public ExpressionNode child() {
        return super.child();
    }

}