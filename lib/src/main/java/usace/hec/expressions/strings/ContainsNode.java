package usace.hec.expressions.strings;

import java.io.Serial;

import usace.hec.expressions.*;

public class ContainsNode implements BooleanExpressionNode, BinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringExpressionNode source;
    private final StringExpressionNode search;

    public ContainsNode(StringExpressionNode source, StringExpressionNode search) {
        this.source = source;
        this.search = search;
    }

    @Override
    public boolean evaluate() {
        return source.evaluate().contains(search.evaluate());
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
        return ExpressionOperator.CONTAINS;
    }
    @Override
    public void setProvider(DataProvider dp) {
        source.setProvider(dp);
        search.setProvider(dp);
    }
}
