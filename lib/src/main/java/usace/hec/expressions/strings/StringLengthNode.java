package usace.hec.expressions.strings;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.StringExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;
import usace.hec.expressions.math.IntegerUnaryExpressionNode;

public class StringLengthNode extends IntegerUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringExpressionNode source;


    public StringLengthNode(StringExpressionNode source) {
        this.source = source;
    }

    @Override
    public int evaluate() {
        return source.evaluate().length();
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
        return ExpressionOperator.LENGTH;
    }
    @Override
    public ExpressionNode child() {
        return this.source;
    }
}
