package usace.hec.expressions.strings;

import java.io.Serial;


import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.StringExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.comparison.BooleanBinaryExpressionNode;

public class StartsWithNode extends BooleanBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringExpressionNode source;
    private final StringExpressionNode search;

    public StartsWithNode(StringExpressionNode source, StringExpressionNode search) {
        this.source = source;
        this.search = search;
    }

    @Override
    public boolean evaluate() {
        String sourceVal = source.evaluate();
        String searchVal = search.evaluate();
        checkErrors();
        return sourceVal.startsWith(searchVal);
    }

    @Override
    public ExpressionNode left() { return source; }

    @Override
    public ExpressionNode right() { return search; }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.STARTSWITH;
    }
}
