package usace.hec.expressions;

import java.io.Serial;

public class DoubleVariableNode implements DoubleExpressionNode, DataRequester {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final String name;
    protected transient DataProvider dp = null;

    public DoubleVariableNode(String name) {
        this.name = name;
      }

    @Override
    public double evaluate() {
        ee.clear();
        return dp.provideDouble(name);
    }


    @Override
    public String PreFixSyntax() {
        return "[" + this.name + "]";
    }

    @Override
    public String ExcelSyntax() {
        return "[" + this.name + "]";
    }

    @Override
    public void setProvider(DataProvider dp) {
        this.dp = dp;
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.VARIABLE;
    }

    @Override
    public String getName() {
        return this.name;
    }
}