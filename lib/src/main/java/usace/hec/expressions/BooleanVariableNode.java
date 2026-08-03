package usace.hec.expressions;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class BooleanVariableNode implements BooleanExpressionNode, DataListener, DataRequester {
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
        if (dp != null) {
            return (boolean)dp.provideValue(name);
        }
        return this.value;
    }

    @Override
    public void onDataUpdate(DataUpdate newValue) {
        this.value = (boolean)newValue.newValue();
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
