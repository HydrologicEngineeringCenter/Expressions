package usace.hec.expressions.strings;

import java.io.Serial;
import usace.hec.expressions.*;

public class TrimNode implements StringExpressionNode, UnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringExpressionNode source;

    public TrimNode(StringExpressionNode source) { this.source = source; }

    @Override
    public String evaluate() { return source.evaluate().trim(); }

    @Override
    public String PreFixSyntax() { return Operator().getPrefixName() + "(" + source.PreFixSyntax() + ")"; }

    @Override
    public String ExcelSyntax() { return Operator().getPrefixName() + "(" + source.ExcelSyntax() + ")"; }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.TRIM;
    }

    @Override
    public ExpressionNode child() {
        return source;
    }
}
