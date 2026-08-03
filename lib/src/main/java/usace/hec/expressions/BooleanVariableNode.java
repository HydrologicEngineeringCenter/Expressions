package usace.hec.expressions;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BooleanVariableNode implements BooleanExpressionNode, DataRequester {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final String name;
    protected volatile boolean value;
    protected transient DataProvider dp = null;

    public BooleanVariableNode(String name) {
        this.name = name;
        this.value = false;
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
