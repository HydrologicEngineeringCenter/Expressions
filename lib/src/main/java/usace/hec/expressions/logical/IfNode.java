package usace.hec.expressions.logical;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;


public abstract class IfNode implements ExpressionNode{
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
    public List<DataListener> fetchListeners() {
        List<DataListener> list = conditionNode.fetchListeners();
        list.addAll(thenNode.fetchListeners());
        list.addAll(elseNode.fetchListeners());
        return list;  
    }

    @Override
    public void setProvider(DataProvider dp) {
        thenNode.setProvider(dp);
        elseNode.setProvider(dp);
        conditionNode.setProvider(dp);
    }

    @Override
    public String PreFixSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append("IF(");
        sb.append(conditionNode.PreFixSyntax());
        sb.append(',');
        sb.append(thenNode.PreFixSyntax());
        sb.append(',');
        sb.append(elseNode.PreFixSyntax());
        sb.append(')');
        return sb.toString();
    }

    @Override
    public String ExcelSyntax(){
       StringBuilder sb = new StringBuilder();
        sb.append("IF(");
        sb.append(conditionNode.ExcelSyntax());
        sb.append(',');
        sb.append(thenNode.ExcelSyntax());
        sb.append(',');
        sb.append(elseNode.ExcelSyntax());
        sb.append(')');
        return sb.toString();
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.IF;
    }
}
