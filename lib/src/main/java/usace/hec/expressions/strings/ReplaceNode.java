package usace.hec.expressions.strings;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import usace.hec.expressions.DataListener;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.StringExpressionNode;

public class ReplaceNode implements StringExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringExpressionNode source;
    private final StringExpressionNode target;
    private final StringExpressionNode replacement;
    private transient DataProvider dp = null;

    public ReplaceNode(StringExpressionNode source, StringExpressionNode target, StringExpressionNode replacement) {
        this.source = source;
        this.target = target;
        this.replacement = replacement;
    }

    @Override
    public String evaluate() {
        return source.evaluate()
                .replace(target.evaluate(), replacement.evaluate());
    }

    @Override
    public String PreFixSyntax() {
        return Operator().getPrefixName() + "(" + source.PreFixSyntax() + "," + target.PreFixSyntax() + "," + replacement.PreFixSyntax() + ")";
    }

    @Override
    public String ExcelSyntax() {
        return "SUBSTITUTE(" + source.ExcelSyntax() + "," + target.ExcelSyntax() + "," + replacement.ExcelSyntax() + ")";
    }

    @Override
    public List<DataListener> fetchListeners() {
        List<DataListener> list = source.fetchListeners();
        list.addAll(target.fetchListeners());
        list.addAll(replacement.fetchListeners());
        return list;
    }

    @Override
    public void setProvider(DataProvider dp) {
        this.dp = dp;
        source.setProvider(dp);
        target.setProvider(dp);
        replacement.setProvider(dp);
    }

    @Override
    public ExpressionType resultType() { return ExpressionType.STRING; }

    @Override
    public ExpressionOperator Operator() { return ExpressionOperator.REPLACE; }
}