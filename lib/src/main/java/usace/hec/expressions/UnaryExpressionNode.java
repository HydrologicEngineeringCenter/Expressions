package usace.hec.expressions;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public abstract class UnaryExpressionNode<T extends Serializable, C extends Serializable>  implements ExpressionNode<T> {
    protected ExpressionNode<C> child;
    @Serial
    private static final long serialVersionUID = 1L;
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
        sb.append(OpName());
        sb.append('(');
        sb.append(child.PreFixSyntax());
        sb.append(')');
        return sb.toString();
    }

    @Override
    public String ExcelSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(InfixOpName());
        sb.append(child.ExcelSyntax());
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void setProvider(DataProvider dp){
        child.setProvider(dp);
    }
    public abstract String OpName();
    public abstract String InfixOpName();

}
