package usace.hec.expressions;

public interface TernaryExpressionNode extends ExpressionNode {
    ExpressionNode left();
    ExpressionNode middle();
    ExpressionNode right();

    @Override
    public default String PreFixSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append(Operator().getPrefixName());
        sb.append('(');
        sb.append(left().PreFixSyntax());
        sb.append(',');
        sb.append(middle().PreFixSyntax());
        sb.append(',');
        sb.append(right().PreFixSyntax());
        sb.append(')');
        return sb.toString();
    }
    @Override
    public default String ExcelSyntax(){
        StringBuilder sb = new StringBuilder();
        sb.append(Operator().getInfixSyntax());
        sb.append(left().ExcelSyntax());
        sb.append(',');
        sb.append(middle().ExcelSyntax());
        sb.append(',');
        sb.append(right().ExcelSyntax());
        sb.append(')');
        return sb.toString();
    }
    @Override
    public default void setProvider(DataProvider dp){
        left().setProvider(dp);
        middle().setProvider(dp);
        right().setProvider(dp);
    }
}
