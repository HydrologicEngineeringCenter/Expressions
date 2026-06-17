package usace.hec.expressions;

public interface DataListener<T> {
    void onDataUpdate(DataUpdate<T> update);
}
