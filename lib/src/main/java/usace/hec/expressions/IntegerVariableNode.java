package usace.hec.expressions;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class IntegerVariableNode implements IntegerExpressionNode, DataRequester {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final String name;
    protected volatile int value;
    protected transient DataProvider dp = null;

    public IntegerVariableNode(String name) {
        this.name = name;
        this.value = 0;
    }

    @Override
    public int evaluate() {
        return dp.provideInt(name);
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
