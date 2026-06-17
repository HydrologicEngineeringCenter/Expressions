package usace.hec.expressions;

import java.util.List;

public abstract class BinaryExpressionNode<T,R,L> implements ExpressionNode<T>{
    protected ExpressionNode<L> leftnode;
    protected ExpressionNode<R> rightnode;
    public BinaryExpressionNode(ExpressionNode<L> left, ExpressionNode<R> right){
        leftnode = left;
        rightnode = right;
    }
    @Override
    public List<DataListener<?>> fetchListeners() {
       List<DataListener<?>> list = leftnode.fetchListeners();
       list.addAll(rightnode.fetchListeners());
       return list;
    }
    @Override
    public String stringify(){
        return OpName() +"(" + leftnode.stringify() + "," + rightnode.stringify() + ")";
    }
    @Override
    public String stringifyInFix(){
        return leftnode.stringify() + InfixOpName() + rightnode.stringify();
    }
    public abstract String OpName();
    public abstract String InfixOpName();
}
