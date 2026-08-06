package usace.hec.expressions.time;

import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.logical.BooleanUnaryExpressionNode;

import java.io.Serial;
import java.time.LocalDateTime;

public class LeapYearNode extends BooleanUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode child;
    public LeapYearNode(DateTimeExpressionNode child) {
        this.child = child;
    }
    @Override
    public boolean evaluate() {
        ee.clear();
        LocalDateTime c = child.evaluate();
        checkErrors();
        if (c.getYear() % 100 == 0){
            return c.getYear() % 400 == 0;
        }
        return c.getYear() % 4 == 0;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.LEAPYEAR;
    }

    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }
    @Override
    public ExpressionNode child() {
        return this.child;
    }

}
