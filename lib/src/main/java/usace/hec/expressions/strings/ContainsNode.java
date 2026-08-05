package usace.hec.expressions.strings;




import usace.hec.expressions.EvaluationError;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.StringExpressionNode;
import usace.hec.expressions.comparison.BooleanBinaryExpressionNode;

import java.io.Serial;



public class ContainsNode extends BooleanBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private EvaluationError ee;
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
    public void checkErrors(){
        if (left().hasError()) {
            ee = left().getEvaluationError();
        } else if (right().hasError()) {
            ee = right().getEvaluationError();
        }
    }
}
