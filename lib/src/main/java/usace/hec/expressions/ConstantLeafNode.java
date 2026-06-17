package usace.hec.expressions;

import java.util.ArrayList;
import java.util.List;

public class ConstantLeafNode<T> implements ExpressionNode<T>, LeafNode<T>{
    private final T value;

    public ConstantLeafNode( T value) {
        this.value = value;
    }

    @Override
    public T evaluate() {
        return this.value;
    }

    @Override
    public List<DataListener<?>> fetchListeners() {
        return new ArrayList<>();
    }
    @Override
    public String stringify(){
        return value.toString();
    }
        @Override
    public String stringifyInFix(){
        return value.toString();
    }
}