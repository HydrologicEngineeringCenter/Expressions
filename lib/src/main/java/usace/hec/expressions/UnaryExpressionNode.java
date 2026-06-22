package usace.hec.expressions;

import java.util.List;

public abstract class UnaryExpressionNode<T, C> implements ExpressionNode<T>{
    protected ExpressionNode<C> child;
    public UnaryExpressionNode(ExpressionNode<C> child){
        this.child = child;
    }

    @Override
    public List<DataListener<?>> fetchListeners() {
        return child.fetchListeners();
    }

    @Override
    public String PreFixSyntax() {
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
    public String ExcelSyntax() {
        StringBuilder sb = new StringBuilder();
        excelAppend(sb);
        return sb.toString();
    }

    public void excelAppend(StringBuilder sb) {
        sb.append('(');
        sb.append(InfixOpName());
        child.excelAppend(sb);
        sb.append(')');
    }
    public abstract String OpName();
    public abstract String InfixOpName();
    public abstract ExpressionOperator Operator();

}
