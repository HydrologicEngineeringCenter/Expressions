package usace.hec.expressions.time;

import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.strings.StringUnaryExpressionNode;

import java.io.Serial;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;


public class DayOfWeekNode extends StringUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode child;
    public DayOfWeekNode(DateTimeExpressionNode child) {
        this.child = child;
    }
    @Override
    public String evaluate() {
        ee.clear();
        LocalDateTime c = child.evaluate();
        checkErrors();
        String weekDay = c.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        return weekDay;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.WEEKDAY;
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
