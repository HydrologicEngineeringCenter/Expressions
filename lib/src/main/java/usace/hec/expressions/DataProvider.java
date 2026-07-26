package usace.hec.expressions;

public interface DataProvider {
    public <T> T provideValue(String name);
    public void setValue(String name, Object value);
}
