package Beetus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    static double[] rates = new double[]{1.0, 0.1, 0.01};
    static String[] crosses = new String[]{"training00.data", "training01.data", "training02.data", "training03.data", "training04.data"}
    static ArrayList<Example> examples = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        examples = readExamples("src/diabetes.train");

        for (double learnRate : rates) {
            ArrayList<Double> weights = simplePerceptronEpochs(10, examples, learnRate);
        }
    }

    private static ArrayList<Double> simplePerceptronEpochs(int epochs, ArrayList<Example> examples, double learnRate) {
        ArrayList<Double> weights = smallRandoms(20);


        for (int i = 0; i < epochs; i++) {
            Collections.shuffle(examples);
            simplePerceptron(examples, weights, learnRate);
        }

        return weights;
    }

    private static ArrayList<Double> smallRandoms(int n) {
        ArrayList<Double> nums = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            nums.add(i, ThreadLocalRandom.current().nextDouble(0.0, 0.01));
        }

        return nums;
    }


    private static ArrayList<Double> simplePerceptron(ArrayList<Example> examples, double learnRate) {
        ArrayList<Double> blankWeights = new ArrayList<Double>(Collections.nCopies(20, 0.0));

        return simplePerceptron(examples, blankWeights, learnRate);
    }

    /**
     * Runs Perceptron Simple, somehow. Returns ideal weights. First weight is b, in this case.
     *
     * @param examples A list of Examples to run on.
     */
    private static ArrayList<Double> simplePerceptron(ArrayList<Example> examples, ArrayList<Double> weights, double learnRate) {

        for (Example ex : examples) {
            boolean sign = sgn(weights, ex);
            boolean actual = toBool(ex.get(0));

            if (sign == actual)
                continue;

            //TODO: Change on error

            if (actual) {
                for (int i = 1; i < 20; i++) {
                    weights.add(i, weights.get(i) + (learnRate * ex.get(i)));
                }
                weights.add(0, weights.get(0) + learnRate);

            } else {
                for (int i = 1; i < 20; i++) {
                    weights.add(i, weights.get(i) - (learnRate * ex.get(i)));
                }
                weights.add(0, weights.get(0) - learnRate);

            }

        }


        return weights;
    }

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
    private static boolean sgn(ArrayList<Double> weights, Example ex) {
        double sum = 0;

        for (int i = 1; i < 20; i++) {
            sum += (weights.get(i) * ex.get(i));
        }

        sum += weights.get(0);

        return sum >= 0;
    }


    private static ArrayList<Example> readExamples(String[] files) throws Exception {



    }

    /**
     * Creates an ArrayList full of examples, as read in from a given file.
     *
     * @param fileName where the Example lines are read from
     * @return an ArrayList of read examples
     * @throws Exception
     */
    private static ArrayList<Example> readExamples(String fileName) throws Exception {
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

        return null;
    }

    private static boolean toBool(Double dub) {
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
