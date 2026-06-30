package usace.hec.expressions.logical;

import java.util.List;

import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;

public class IfNode<T> implements ExpressionNode<T>{
    private ExpressionNode<T> thenNode;
    private ExpressionNode<T> elseNode;
    private ExpressionNode<Boolean> conditionNode;
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
    public List<DataListener<?, ?>> fetchListeners() {
        List<DataListener<?>> list = conditionNode.fetchListeners();
       list.addAll(thenNode.fetchListeners());
       list.addAll(elseNode.fetchListeners());
       return list;  
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
