package usace.hec.expressions;

public interface DataListener<T> {
    ExpressionNode<T> owner();
    void onDataUpdate(DataUpdate<T> update);
}
