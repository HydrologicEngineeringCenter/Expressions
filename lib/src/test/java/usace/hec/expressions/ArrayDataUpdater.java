package usace.hec.expressions;

import java.util.ArrayList;

public class ArrayDataUpdater extends DataUpdater {
    private final ArrayList<Number> values;
    private int index = 0;
    public ArrayDataUpdater(ArrayList<Number> values){
        this.values = values;
    }
    public <V> void publishNext(String variable) {
        super.publish(variable, values.get(index));
    }
    public void advance(){
        index ++;
    }
}
