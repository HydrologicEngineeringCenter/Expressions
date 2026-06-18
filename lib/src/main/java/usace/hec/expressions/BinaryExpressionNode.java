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
    public String stringify() {
        StringBuilder sb = new StringBuilder();
        prefixAppend(sb);
        return sb.toString();
    }

    public void prefixAppend(StringBuilder sb) {
        sb.append(OpName());
        sb.append('(');
        leftnode.prefixAppend(sb);
        sb.append(',');
        rightnode.prefixAppend(sb);
        sb.append(')');
    }

    @Override
    public String stringifyInFix(){
        StringBuilder sb = new StringBuilder();
        excelAppend(sb);
        return sb.toString();
    }

    public void excelAppend(StringBuilder sb) {
        sb.append('(');
        leftnode.excelAppend(sb);
        sb.append(InfixOpName());
        rightnode.excelAppend(sb);
        sb.append(')');
    }

    public abstract String OpName();
    public abstract String InfixOpName();
    public abstract ExpressionOperator Operator();
}
