package usace.hec.expressions;

public interface DataListener<U, V> {
    ExpressionNode<U> owner();
    void onDataUpdate(DataUpdate<V> update);
}
