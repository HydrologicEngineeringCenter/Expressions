package usace.hec.expressions;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class DoubleVariableNode implements DoubleExpressionNode, DataRequester {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final String name;
    // Volatile ensures visibility across threads without synchronization overhead
    protected volatile double value;
    protected transient DataProvider dp = null;

    public DoubleVariableNode(String name) {
        this.name = name;
        this.value = 0.0;
    }

    @Override
    public double evaluate() {
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