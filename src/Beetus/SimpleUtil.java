package Beetus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class SimpleUtil {

    /**
     * Runs Simple Perceptron a specified number of times with a given learning rate and examples.
     * Weights are given random, small values
     *
     * @return weights trained by running Simple Perceptron a number of times
     */
    static Weight simplePerceptronEpochs(int epochs, ArrayList<Example> examples, double learnRate, boolean isTrain) throws Exception {
        //HashMap<String, Double> weights = GeneralUtil.smallRandoms(examples);
        //HashMap<String, Double> weights = new HashMap<>();
        Weight weights = new Weight();

        return simplePerceptronEpochs(epochs, examples, weights, learnRate, isTrain);
    }


    private static Weight simplePerceptronEpochs(int epochs, ArrayList<Example> examples, Weight weights, double learnRate, boolean isTrain) throws Exception {
        int totalUpdates = 0;

        if (isTrain)
            System.out.println("************");

        for (int i = 0; i < epochs; i++) {
            Collections.shuffle(examples);

            //No need to reassign to weights,
            // since it is changed within the method.
            totalUpdates += simplePerceptron(examples, weights, learnRate);

            if (isTrain)
                GeneralUtil.testVsDev(i + 1, weights);
        }

        if (isTrain) {
            System.out.println("************");
            System.out.println("Updates for this set of Simple epochs: " + totalUpdates);
        }
        return weights;
    }

    /**
     * Runs Perceptron Simple. Updates weights.
     *
     * @param examples A list of Examples to run on.
     * @return the number of updates that happened in this epoch.
     */
    static int simplePerceptron(ArrayList<Example> examples, Weight weights, double learnRate) {
        int updates = 0;

        for (Example ex : examples) {
            boolean sign = GeneralUtil.sgn(weights, ex);
            boolean actual = ex.getLabel();

            if (sign != actual) {
                updates++;

                double y; //+1 if label is actually +, -1 if label is actually -
                if (ex.getLabel())
                    y = 1.0;
                else
                    y = -1.0;


                //Update each weight
                //w <- w + (actualSign * learnRate * featureValue)
                for (String key : ex.getAllKeys()) {
                    //weights.set(i, weights.get(i) + (y * learnRate * ex.get(i)));
                    double newWeight = weights.get(key) + (y * learnRate * ex.get(key));
                    weights.put(key, newWeight);
                }
                //b <- b + (actualSign * learnRate)
                //weights.set(0, weights.get(0) + (y * learnRate));
                double newB = weights.getB() + (y * learnRate);
                weights.setB(newB);
            }
        }

        return updates;
    }


    /**
     * Returns the best of the passed-in rates
     *
     * @param rates   the rates to test
     * @param crosses the cross-validation files
     * @return the best learning rate
     * @throws Exception when label is bad in data
     */
    @SuppressWarnings("Duplicates")
    static double crossValidateRates(double[] rates, String[] crosses) throws Exception {
        double bestRate = -1.0;
        double minError = -1.0;
        double errorSum;
        double error;

        for (double rate : rates) {

            errorSum = 0.0;

            for (int i = 0; i < crosses.length; i++) {

                //Use all of the files but one
                String[] usedFiles = new String[crosses.length - 1];
                String testFile = "";
                int found = 0;
                for (int j = 0; j < crosses.length; j++) {
                    if (j == i) {
                        found = 1;
                        testFile = crosses[j];
                        continue;
                    }
                    usedFiles[j - found] = crosses[j];
                }

                //Read the examples from the chosen files,,,
                ArrayList<Example> ex = GeneralUtil.readExamples(usedFiles);

                //Using the rate for this run, epoch 10x over the targets
                Weight weights = simplePerceptronEpochs(10, ex, rate, false);

                //Test the weights on the unused cross file
                double thisError = GeneralUtil.testError(weights, testFile);
                errorSum += thisError;
            }

            error = errorSum / crosses.length;

            System.out.println("Rate " + rate + " has accuracy: " + (1.0 - error));

            if (error < minError || minError == -1.0) {
                minError = error;
                bestRate = rate;
            }
        }

        return bestRate;
    }
}
