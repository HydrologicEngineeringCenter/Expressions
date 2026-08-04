package usace.hec.expressions.strings;

import java.io.Serial;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.StringExpressionNode;
import usace.hec.expressions.ExpressionOperator;

public class StartsWithNode implements BooleanExpressionNode, BinaryExpressionNode {
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
        return source.evaluate().startsWith(search.evaluate());
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
