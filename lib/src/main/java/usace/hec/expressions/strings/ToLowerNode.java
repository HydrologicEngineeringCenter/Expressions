package usace.hec.expressions.strings;

import java.io.Serial;
import java.util.List;

import usace.hec.expressions.DataListener;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.StringExpressionNode;

public class ToLowerNode implements StringExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringExpressionNode source;
    private transient DataProvider dp = null;

    public ToLowerNode(StringExpressionNode source) { this.source = source; }

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
    public List<DataListener> fetchListeners() { return source.fetchListeners(); }

    @Override
    public void setProvider(DataProvider dp) {
        this.dp = dp;
        source.setProvider(dp);
    }

    @Override
    public ExpressionOperator Operator() { return ExpressionOperator.LOWER; }
}