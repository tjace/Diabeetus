package Beetus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

class GeneralUtil {

    /**
     * Returns an estimated sign based on:
     * b (weight[0])
     * the weights
     * an example
     *
     * @param weights
     * @param ex
     * @return true if (exampleFeatures * weights) + b >= 0, else false
     */
    static boolean sgn(ArrayList<Double> weights, Example ex) {
        double sum = 0;

        for (int i = 1; i < 20; i++) {
            sum += (weights.get(i) * ex.get(i));
        }

        sum += weights.get(0);

        return sum >= 0;
    }

    static double testError(ArrayList<Double> weights, String testFile) throws Exception {
        ArrayList<Example> ex = readExamples(testFile);

        double failed = 0;
        double total = ex.size();
        for (Example each : ex) {

            boolean guess = sgn(weights, each);
            boolean actual = toBool(each.get(0));

            if (guess != actual)
                failed++;
        }
        double percentFailed = failed / total;
        return failed / total;
    }

    /**
     * Returns an ArrayList full of the examples from all given files
     *
     * @param files
     * @return
     * @throws Exception
     */
    static ArrayList<Example> readExamples(String[] files) throws Exception {
        ArrayList<Example> ret = new ArrayList<>();

        for (String file : files) {
            ArrayList<Example> part = readExamples(file);
            ret.addAll(part);
        }

        return ret;
    }

    /**
     * Creates an ArrayList full of examples, as read in from a given file.
     *
     * @param fileName where the Example lines are read from
     * @return an ArrayList of read examples
     * @throws Exception
     */
    static ArrayList<Example> readExamples(String fileName) throws Exception {
        ArrayList<Example> ret = new ArrayList<>();

        BufferedReader reader = null;
        String line = "";


        try {
            reader = new BufferedReader(new FileReader(fileName));

            while ((line = reader.readLine()) != null) {
                Example next = new Example(line);
                ret.add(next);
            }


        } catch (
                FileNotFoundException e) {
            System.out.println("File " + fileName + " not found.");
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    static ArrayList<Double> smallRandoms(int n) {
        ArrayList<Double> nums = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            nums.add(i, ThreadLocalRandom.current().nextDouble(0.0, 0.01));
        }

        return nums;
    }

    static boolean toBool(Double dub) {
        if (dub == -1.0)
            return false;
        else if (dub == 1.0)
            return true;
        else {
            System.out.println("Unexpected number in toBool: " + dub);
            return false;
        }
    }
}
