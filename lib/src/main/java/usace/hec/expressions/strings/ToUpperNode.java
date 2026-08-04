package usace.hec.expressions.strings;

import java.io.Serial;
import usace.hec.expressions.*;

public class ToUpperNode implements StringExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringExpressionNode source;

    public ToUpperNode(StringExpressionNode source) { this.source = source; }

    @Override
    public String evaluate() {
        return source.evaluate().toUpperCase();
    }

    @Override
    public String PreFixSyntax() {
        return Operator().getPrefixName() + "(" + source.PreFixSyntax() + ")";
    }

    @Override
    public String ExcelSyntax() {
        return Operator().getPrefixName() + "(" + source.ExcelSyntax() + ")";
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.UPPER;
    }
}