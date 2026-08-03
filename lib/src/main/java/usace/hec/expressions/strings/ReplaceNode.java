package usace.hec.expressions.strings;

import java.io.Serial;
import java.util.List;

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
    public void setProvider(DataProvider dp) {
        source.setProvider(dp);
        target.setProvider(dp);
        replacement.setProvider(dp);
    }

    @Override
    public ExpressionType resultType() { return ExpressionType.STRING; }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.REPLACE;
    }
}