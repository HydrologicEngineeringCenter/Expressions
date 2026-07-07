package usace.hec.expressions;

import java.util.ArrayList;
import java.util.List;

public abstract class DataUpdater {
    protected final List<DataListener<?>> listeners = new ArrayList<>();
    public void register(DataListener<?> listener) {
        for(DataListener<?> d : listeners){
            if (d.owner()==listener.owner()){
                return;
            }
        }
        listeners.add(listener);
    }
    public <V> void publish(String variableName, V value) {
        // Construct a strongly typed update payload
        DataUpdate<V> event = new DataUpdate<>(variableName, value);
        for (DataListener<?> listener : listeners) {
            ((DataListener) listener).onDataUpdate(event);
        }
    }
}

//TODO: Change architechture of DataUpdate instead of pushing to LeafNodes to instead have
// LeafNodes pull from DataUpdater, something like a Map<>. Not sure how to do so yet.
