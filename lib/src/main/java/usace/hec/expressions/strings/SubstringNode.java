package usace.hec.expressions.strings;

import java.io.Serial;

import usace.hec.expressions.*;

public class SubstringNode extends TernaryExpressionNode implements StringExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringExpressionNode source;
    private final IntegerExpressionNode beginIndex;
    private final IntegerExpressionNode endIndex;
    private EvaluationError ee = new EvaluationError();

    public SubstringNode(StringExpressionNode source, IntegerExpressionNode beginIndex, IntegerExpressionNode endIndex) {
        this.source = source;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    @Override
    public String evaluate() {
        String sourceString = source.evaluate();
        int startIndex = beginIndex.evaluate();
        int endingIndex = endIndex.evaluate();
        checkErrors();
        if (!source.hasError() && !beginIndex.hasError() && !endIndex.hasError()) {
            if (startIndex < 0 || startIndex > sourceString.length()) {
                ee.report(ErrorState.INVALID, this, "start Index out of bounds");
                return "";
            }
            if (endingIndex < 0 || endingIndex > sourceString.length()) {
                ee.report(ErrorState.INVALID, this, "end Index out of bounds");
                return "";
            }
            if (startIndex > endingIndex) {
                ee.report(ErrorState.INVALID, this, "start Index is greater than end Index");
                return "";
            }
            return sourceString.substring(startIndex, endingIndex);
        }
        return "";
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
