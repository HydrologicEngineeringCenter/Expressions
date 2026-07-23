package usace.hec.expressions;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateableLeafNode<T extends Serializable> implements ExpressionNode<T>, DataListener<T>{
    protected final String name;
    protected T value;
    @Serial
    private static final long serialVersionUID = 1L;

    public UpdateableLeafNode(String name) {
        this.name = name;
        this.value = null;
    }

    @Override
    public T evaluate() {
        return this.value;
    }

    // Listens directly to data changes without tree traversal
    @Override
    public void onDataUpdate(DataUpdate<T> update) {
        if (this.name.equals(update.variableName())) {
            this.value = update.newValue();
        }
    }

    @Override
    public List<DataListener<?>> fetchListeners() {
        List<DataListener<?>> list = new ArrayList<>();
        list.add(this);
        return list;
        
    }

    @Override
    public String PreFixSyntax(){
        return "[" + this.name + "]";
    }
        @Override
    public String ExcelSyntax(){
        return "[" + this.name + "]";
    }

    @Override
    public ExpressionNode<T> owner() {
        return this;
    }

    @Override
    public ExpressionOperator Operator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Operator'");
    }
}