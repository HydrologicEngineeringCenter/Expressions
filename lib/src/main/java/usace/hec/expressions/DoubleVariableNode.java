package usace.hec.expressions;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DoubleVariableNode implements DoubleExpressionNode, DataListener, DataRequester {
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
        if (dp != null) {
            
            return (double) dp.provideValue(name);
        }
        return this.value;
    }

    @Override
    public void onDataUpdate(DataUpdate newValue) {
        this.value = (double)newValue.newValue();
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