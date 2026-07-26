package usace.hec.expressions;

import java.util.ArrayList;
import java.util.List;

public abstract class DataUpdater {
    protected final List<DataListener> listeners = new ArrayList<>();
    public void register(DataListener listener) {
        for(DataListener d : listeners){
            if (d.owner()==listener.owner()){
                return;
            }
        }
        listeners.add(listener);
    }
    public  void publish(String variableName, Object value) {
        // Construct a strongly typed update payload
        DataUpdate event = new DataUpdate(variableName, value);
        for (DataListener listener : listeners) {
            if(((DataRequester)listener).getName().equals(variableName)){
                ((DataListener) listener).onDataUpdate(event);
            }
        }
    }
}