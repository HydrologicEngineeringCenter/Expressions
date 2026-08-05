package usace.hec.expressions.strings;

import java.io.Serial;

import usace.hec.expressions.TernaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.StringExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;

public class ReplaceNode extends TernaryExpressionNode implements StringExpressionNode {
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
    public ExpressionType resultType() { return ExpressionType.STRING; }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.REPLACE;
    }

    @Override
    public ExpressionNode left() {
        return source;
    }

    @Override
    public ExpressionNode middle() {
        return target;
    }

    @Override
    public ExpressionNode right() {
        return replacement;
    }
}