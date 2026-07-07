package usace.hec.expressions;

public interface DataProvider {
    public <T> T provideValueForCurrentTimestep(String name);
}
