package usace.hec.expressions;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * A generic {@link ExpressionNode} that evaluates two {@link ExpressionNode}s to evaluate simple mathematical operations between them
 */
public abstract class BinaryExpressionNode<T extends Serializable,R extends Serializable,L extends Serializable> implements ExpressionNode<T>{
    @Serial
    private static final long serialVersionUID = 1L;
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
    public String PreFixSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append(OpName());
        sb.append('(');
        sb.append(leftnode.PreFixSyntax());
        sb.append(',');
        sb.append(rightnode.PreFixSyntax());
        sb.append(')');
        return sb.toString();
    }

    @Override
    public String ExcelSyntax(){
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(leftnode.ExcelSyntax());
        sb.append(' ');
        sb.append(InfixOpName());
        sb.append(' ');
        sb.append(rightnode.ExcelSyntax());
        sb.append(')');
        return sb.toString();
    }

    public void excelAppend(StringBuilder sb) {

    }

    @Override
    public void setProvider(DataProvider dp){
        leftnode.setProvider(dp);
        rightnode.setProvider(dp);
    }

    public abstract String OpName();
    public abstract String InfixOpName();
}
