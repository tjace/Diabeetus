package Beetus;

import java.util.ArrayList;
import java.util.Collections;

public class Example {

    boolean label;
    ArrayList<Double> features;

    public Example(String fullLine) throws Exception {
        String[] pieces = fullLine.split(" ");
        features = new ArrayList<Double>(Collections.nCopies(20, 0.0));

        if (pieces[0].equals("-1")) {
            label = false;
        } else if (pieces[0].equals("+1")) {
            label = true;
        } else {
            throw new Exception("bad first line :(");
        }

        for (String each : pieces) {
            if (!Character.isDigit(each.charAt(0))) {
                if (pieces[0].equals("-1")) {
                    label = false;
                    features.add(0, -1.0);
                } else if (pieces[0].equals("+1")) {
                    label = true;
                    features.add(0, 1.0);
                } else {
                    throw new Exception("bad first line :(");
                }
            } else {
                String[] splits = each.split(":");
                System.out.println("Parsing " + splits[0] + ", " + splits[1]);
                features.add(Integer.parseInt(splits[0]), Double.parseDouble(splits[1]));
            }

        }


    }

    public Example(boolean _label, ArrayList<Double> _features) {
        label = _label;
        features = _features;
    }

    public Double get(int n)
    {
        return features.get(n);
    }
}
