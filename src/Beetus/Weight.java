package Beetus;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Weight {

    private HashMap<String, Double> weights;
    private double b;

    public Weight() {
        weights = new HashMap<>();
        b = ThreadLocalRandom.current().nextDouble(0.0, 0.01);
    }

    Double get(String key) {
        return weights.get(key);
    }

    void put(String key, double value) {
        weights.put(key, value);
    }

    boolean hasKey(String key) {
        return weights.containsKey(key);
    }

    double getB() {
        return b;
    }

    void setB(Double num) {
        b = num;
    }

    Set<String> getAllKeys() {
        return weights.keySet();
    }

    Weight copy()
    {
        Weight ret = new Weight();

        ret.setB(this.getB());

        for (String key : this.getAllKeys())
        {
            ret.put(key, this.get(key));
        }

        return ret;
    }

}
