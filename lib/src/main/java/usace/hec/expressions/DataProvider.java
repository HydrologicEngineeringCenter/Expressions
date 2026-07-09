package usace.hec.expressions;

public interface DataProvider {
    public <T> T provideValueForCurrentTimestep(String name);
    public void setValueForCurrentTimestep(String name, Object value);
}
