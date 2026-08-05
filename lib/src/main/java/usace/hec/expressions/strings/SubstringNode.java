package usace.hec.expressions.strings;

import java.io.Serial;

import usace.hec.expressions.*;

public class SubstringNode implements StringExpressionNode, TernaryExpressionNode {
    EvaluationError ee;
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
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.SUBSTRING;
    }

    @Override
    public ExpressionNode left() {
        return source;
    }

    @Override
    public ExpressionNode middle() {
        return beginIndex;
    }

    @Override
    public ExpressionNode right() {
        return endIndex;
    }
}
