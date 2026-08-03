package usace.hec.expressions.strings;

import java.io.Serial;

import usace.hec.expressions.*;

public class EndsWithNode implements BooleanExpressionNode, BinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final StringExpressionNode source;
    private final StringExpressionNode search;

    public EndsWithNode(StringExpressionNode source, StringExpressionNode search) {
        this.source = source;
        this.search = search;
    }

    @Override
    public boolean evaluate() {
        return source.evaluate().endsWith(search.evaluate());
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
        return ExpressionOperator.ENDSWITH;
    }
    @Override
    public void setProvider(DataProvider dp) {
        source.setProvider(dp);
        search.setProvider(dp);
    }
}
