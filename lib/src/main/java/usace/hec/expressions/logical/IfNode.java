package usace.hec.expressions.logical;

import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.TernaryExpressionNode;

import java.io.Serial;




public abstract class IfNode implements ExpressionNode, TernaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    protected ExpressionNode thenNode;
    protected ExpressionNode elseNode;
    protected BooleanExpressionNode conditionNode;

    /**
     * A generic {@link ExpressionNode} with three children {@link ExpressionNode}s. The first child is a boolean {@code ExpressionNode}, in which if computed to be {@code true}, then the second child is computed
     * for it's numerical value and the third child uncomputed, otherwise the third child is computed and the second child is left uncomputed.
     */
    public IfNode(BooleanExpressionNode condition, ExpressionNode thenn, ExpressionNode elsee){
        conditionNode = condition;
        thenNode = thenn;
        elseNode = elsee;
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.IF;
    }

    public ExpressionNode left(){
        return conditionNode;
    };
    public ExpressionNode middle(){
        return thenNode;
    };
    public ExpressionNode right(){
        return elseNode;
    };
}
