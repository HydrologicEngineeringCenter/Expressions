package usace.hec.expressions.strings;

import java.io.Serial;
import java.util.List;

import usace.hec.expressions.DataProvider;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.StringExpressionNode;

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
    public void setProvider(DataProvider dp) {
        source.setProvider(dp);
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.UPPER;
    }
}