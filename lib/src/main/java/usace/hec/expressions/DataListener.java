package usace.hec.expressions;

public interface DataListener {
    ExpressionNode owner();
    void onDataUpdate(DataUpdate update);
}
