package usace.hec.expressions;

public interface DataRequester<T> {
    String getName();
    void setProvider(DataProvider dp);
}
