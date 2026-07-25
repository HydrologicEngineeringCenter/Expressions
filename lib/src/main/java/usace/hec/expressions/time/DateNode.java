package usace.hec.expressions.time;

import java.io.Serial;

import java.time.LocalDateTime;

import usace.hec.expressions.ConstantLeafNode;
import usace.hec.expressions.ExpressionOperator;

public class DateNode extends ConstantLeafNode<LocalDateTime> {
    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDateTime date;

    public DateNode(LocalDateTime date) {
        super(date);
        this.date = date;
    }
    @Override
    public LocalDateTime evaluate() {
        return this.date;
    }
    
    @Override
    public String PreFixSyntax() {
        return "DATE(" + date.getDayOfMonth() + ","
             + date.getMonthValue() + ","
             + date.getDayOfMonth() + ")";
    }

    @Override
    public String ExcelSyntax() {
        return PreFixSyntax();
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.DATE;
    }
}
