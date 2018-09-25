package Beetus;

import java.util.ArrayList;
import java.util.Collections;

/**
 * an Example's features array contains:
 * features[0]:         -1.0 or 1.0, corresponding with the label
 * features [1 - 19]:   a double, corresponding to the fields 1 - 19.  1 is the lowest field, and 19 is the highest.
 */
class Example {

    private boolean label;
    private ArrayList<Double> features;

    Example(String fullLine) throws Exception {
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
                features.add(Integer.parseInt(splits[0]), Double.parseDouble(splits[1]));
            }

        }


    }

    Double get(int n)
    {
        return features.get(n);
    }
}


