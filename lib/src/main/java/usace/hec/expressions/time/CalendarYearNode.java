package usace.hec.expressions.time;

import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.math.IntegerUnaryExpressionNode;


import java.io.Serial;
import java.time.LocalDateTime;


public class CalendarYearNode extends IntegerUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode child;
    public CalendarYearNode(DateTimeExpressionNode child) {
        this.child = child;
    }
    @Override
    public int evaluate() {
        ee.clear();
        LocalDateTime c = child.evaluate();
        checkErrors();
        return c.getYear();
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.YEAR;
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