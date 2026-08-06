package usace.hec.expressions;

import java.io.Serial;

public class BooleanVariableNode implements BooleanExpressionNode, DataRequester {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final String name;
    protected transient DataProvider dp = null;

    public BooleanVariableNode(String name) {
        this.name = name;
    }

    @Override
    public boolean evaluate() {
        return dp.provideBoolean(name);
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
