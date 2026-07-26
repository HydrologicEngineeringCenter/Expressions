package usace.hec.expressions;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class IntegerVariableNode implements IntegerExpressionNode, DataListener, DataRequester {
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
        if (dp != null) {
            return (int)dp.provideValue(name);
        }
        return this.value;
    }

    @Override
    public void onDataUpdate(DataUpdate newValue) {
            this.value = (int)newValue.newValue();
    }

    @Override
    public List<DataListener> fetchListeners() {
        List<DataListener> list = new ArrayList<>();
        list.add(this);
        return list;
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
    public ExpressionNode owner() {
        return this;
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
