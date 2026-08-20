package usace.hec.expressions.time;

import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.logical.BooleanUnaryExpressionNode;
import usace.hec.expressions.math.IntegerUnaryExpressionNode;

import java.io.Serial;
import java.time.LocalDateTime;


public class IsWeekdayNode extends BooleanUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode child;
    public IsWeekdayNode(DateTimeExpressionNode child) {
        this.child = child;
    }
    @Override
    public boolean evaluate() {
        ee.clear();
        LocalDateTime c = child.evaluate();
        checkErrors();
        return c.getDayOfWeek().getValue() <= 5; // getValue returns 1 = Monday, 7 = Sunday.;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.IS_WEEKDAY;
    }

    @Override
    public String ExcelSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append(Operator().getPrefixName());
        sb.append('(');
        sb.append(child().ExcelSyntax());
        sb.append(')');
        return sb.toString(); //same as Prefix, but must propogate excelSyntax to children
    }
    @Override
    public ExpressionNode child() {
        return this.child;
    }
}
