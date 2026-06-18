package usace.hec.expressions;

import java.util.List;

public abstract class UnaryExpressionNode<T> implements ExpressionNode<T>{
    protected ExpressionNode<T> child;
    public UnaryExpressionNode(ExpressionNode<T> child){
        this.child = child;
    }

    @Override
    public List<DataListener<?>> fetchListeners() {
        return child.fetchListeners();
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
        child.prefixAppend(sb);
        sb.append(')');
    }

    @Override
    public String stringifyInFix() {
        StringBuilder sb = new StringBuilder();
        excelAppend(sb);
        return sb.toString();
    }

    public void excelAppend(StringBuilder sb) {
        sb.append(InfixOpName());
        sb.append('(');
        child.excelAppend(sb);
        sb.append(')');
    }
    public abstract String OpName();
    public abstract String InfixOpName();
    public abstract ExpressionOperator Operator();

}
