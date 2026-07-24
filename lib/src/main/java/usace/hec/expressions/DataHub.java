package usace.hec.expressions;

import java.util.HashMap;
import java.util.Map;

public class DataHub implements DataProvider{
    private final Map<String, Object> currentValues = new HashMap<>();


    //Unsafe?
    @Override
    public <T> T provideValueForCurrentTimestep(String name) {
        Object value = currentValues.get(name);
        if (value == null){
            throw new IllegalArgumentException("No value available for: " + name);
        }
        return (T) value;
    }

    public void setValueForCurrentTimestep(String name, Object value) {
        currentValues.put(name, value);
    }

    public void switchName(String oldName, String newName){
    }
}
