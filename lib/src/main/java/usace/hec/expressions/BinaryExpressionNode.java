package usace.hec.expressions;


import java.util.List;

/**
 * A generic {@link ExpressionNode} that evaluates two {@link ExpressionNode}s to evaluate simple mathematical operations between them
 */
public interface BinaryExpressionNode extends ExpressionNode{
    ExpressionNode left();
    ExpressionNode right();
    @Override
    public default List<DataListener> fetchListeners() {
       List<DataListener> list = left().fetchListeners();
       list.addAll(right().fetchListeners());
       return list;
    }
    @Override
    public default String PreFixSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append(Operator().getPrefixName());
        sb.append('(');
        sb.append(left().PreFixSyntax());
        sb.append(',');
        sb.append(right().PreFixSyntax());
        sb.append(')');
        return sb.toString();
    }
    @Override
    public default String ExcelSyntax(){
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(left().ExcelSyntax());
        sb.append(' ');
        sb.append(Operator().getInfixName());
        sb.append(' ');
        sb.append(right().ExcelSyntax());
        sb.append(')');
        return sb.toString();
    }

    @Override
    public default void setProvider(DataProvider dp){
        left().setProvider(dp);
        right().setProvider(dp);
    }
}
