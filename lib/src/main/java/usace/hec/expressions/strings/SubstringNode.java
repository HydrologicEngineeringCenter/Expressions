package usace.hec.expressions.strings;

import java.io.Serial;
import java.util.List;

import usace.hec.expressions.DataProvider;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.StringExpressionNode;

public class SubstringNode implements StringExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringExpressionNode source;
    private final IntegerExpressionNode beginIndex;
    private final IntegerExpressionNode endIndex;

    public SubstringNode(StringExpressionNode source, IntegerExpressionNode beginIndex, IntegerExpressionNode endIndex) {
        this.source = source;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    @Override
    public String evaluate() {
        return source.evaluate().substring(beginIndex.evaluate(), endIndex.evaluate());
    }

    @Override
    public String PreFixSyntax() {
        return Operator().getPrefixName() + "(" + source.PreFixSyntax() + "," + beginIndex.PreFixSyntax() + "," + endIndex.PreFixSyntax() + ")";
    }

    @Override
    public String ExcelSyntax() {
        return "SUBSTRING(" + source.ExcelSyntax() + "," + beginIndex.ExcelSyntax() + "," + endIndex.ExcelSyntax() + ")";
    }

    @Override
    public void setProvider(DataProvider dp) {
        source.setProvider(dp);
        beginIndex.setProvider(dp);
        endIndex.setProvider(dp);
    }



    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.SUBSTRING;
    }
}
