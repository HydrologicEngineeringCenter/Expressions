package usace.hec.expressions.logical;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import usace.hec.expressions.DataListener;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.ExpressionNode;


public class IfNode<T extends Serializable> implements ExpressionNode<T>{
    @Serial
    private static final long serialVersionUID = 1L;
    private ExpressionNode<T> thenNode;
    private ExpressionNode<T> elseNode;
    private ExpressionNode<Boolean> conditionNode;

    /**
     * A generic {@link ExpressionNode} with three children {@link ExpressionNode}s. The first child is a boolean {@code ExpressionNode}, in which if computed to be {@code true}, then the second child is computed
     * for it's numerical value and the third child uncomputed, otherwise the third child is computed and the second child is left uncomputed.
     */
    public IfNode(ExpressionNode<Boolean> condition, ExpressionNode<T> thenn, ExpressionNode<T> elsee){
        conditionNode = condition;
        thenNode = thenn;
        elseNode = elsee;
    }

    @Override
    public T evaluate() {
       Boolean con = conditionNode.evaluate();
       if (con){
        return thenNode.evaluate();
       }else{
        return elseNode.evaluate();
       }
    }
    @Override
    public List<DataListener<?>> fetchListeners() {
        List<DataListener<?>> list = conditionNode.fetchListeners();
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
        prefixAppend(sb);
        return sb.toString();
    }


    @Override
    public void prefixAppend(StringBuilder sb) {
        sb.append("IF(");
        conditionNode.prefixAppend(sb);
        sb.append(',');
        thenNode.prefixAppend(sb);
        sb.append(',');
        elseNode.prefixAppend(sb);
        sb.append(')');
    }

    @Override
    public void excelAppend(StringBuilder sb) {
        sb.append("IF(");
        conditionNode.excelAppend(sb);
        sb.append(',');
        thenNode.excelAppend(sb);
        sb.append(',');
        elseNode.excelAppend(sb);
        sb.append(')');
    }


    @Override
    public String ExcelSyntax(){
        StringBuilder sb = new StringBuilder();
        excelAppend(sb);
        return sb.toString();
    }
}
