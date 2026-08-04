package usace.hec.expressions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DataHub implements DataProvider{
    Map<String,Integer> currentIntValues = new HashMap<>();
    Map<String,Double> currentDoubleValues = new HashMap<>();
    Map<String,LocalDateTime> currentDateValues = new HashMap<>();
    Map<String,String> currentStringValues = new HashMap<>();
    Map<String,Boolean> currentBooleanValues = new HashMap<>();

    @Override
    public int provideInt(String name) {
       return currentIntValues.get(name);
    }

    @Override
    public double provideDouble(String name) {
        return currentDoubleValues.get(name);
    }

    @Override
    public LocalDateTime provideDate(String name) {
        return currentDateValues.get(name);
    }

    @Override
    public String provideString(String name) {
        return currentStringValues.get(name);
    }

    @Override
    public boolean provideBoolean(String name) {
        return currentBooleanValues.get(name);
    }

    @Override
    public void setInt(String name, int value) {
        currentIntValues.put(name,value);
    }

    @Override
    public void setDouble(String name, double value) {
        currentDoubleValues.put(name,value);
    }

    @Override
    public void setDate(String name, LocalDateTime value) {
        currentDateValues.put(name,value);
    }

    @Override
    public void setString(String name, String value) {
        currentStringValues.put(name,value);
    }

    @Override
    public void setBoolean(String name, boolean value) {
        currentBooleanValues.put(name,value);
    }
}
