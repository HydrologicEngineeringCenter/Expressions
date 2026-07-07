package usace.hec.expressions;

import java.util.HashMap;
import java.util.Map;

public class DataHub implements DataProvider{
    private final Map<String, Object> currentValues = new HashMap<>();

    @Override
    public <T> T provideValueForCurrentTimestep(String name) {
        return null;
    }
}
