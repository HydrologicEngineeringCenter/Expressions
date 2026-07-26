package usace.hec.expressions;

import java.util.List;

public interface UnaryExpressionNode  extends ExpressionNode {
    ExpressionNode child();
    @Override
    public default List<DataListener> fetchListeners() {
        return child().fetchListeners();
    }

    @Override
    public default String PreFixSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append(Operator().getPrefixName());
        sb.append('(');
        sb.append(child().PreFixSyntax());
        sb.append(')');
        return sb.toString();
    }

    @Override
    public default String ExcelSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(Operator().getInfixName());
        sb.append(child().ExcelSyntax());
        sb.append(')');
        return sb.toString();
    }

    @Override
    public default void setProvider(DataProvider dp){
        child().setProvider(dp);
    }
}
