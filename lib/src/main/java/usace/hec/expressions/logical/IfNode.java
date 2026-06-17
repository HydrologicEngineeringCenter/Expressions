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
    public List<DataListener<?>> fetchListeners() {
        List<DataListener<?>> list = conditionNode.fetchListeners();
       list.addAll(thenNode.fetchListeners());
       list.addAll(elseNode.fetchListeners());
       return list;  
    }
    @Override
    public String stringify(){
        return "IF(" + conditionNode.stringify() + "," + thenNode.stringify() + "," + elseNode.stringify() + ")";
    }
    @Override
    public String stringifyInFix(){
        return "IF(" + conditionNode.stringify() + "," + thenNode.stringify() + "," + elseNode.stringify() + ")";
    }
}
