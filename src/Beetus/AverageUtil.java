package Beetus;

import java.util.ArrayList;
import java.util.Collections;

class AverageUtil {


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

                //Read the examples from the chosen files
                ArrayList<Example> ex = GeneralUtil.readExamples(usedFiles);

                //Using the rate for this run, epoch 10x over the targets
                ArrayList<Double> weights = averagePerceptronEpochs(10, ex, rate);

                //Test the weights on the unused cross file
                double thisError = GeneralUtil.testError(weights, testFile);
                errorSum += thisError;
            }

            error = errorSum / crosses.length;

            System.out.println("Rate " + rate + " has error: " + error);

            if (error < minError || minError == -1.0) {
                minError = error;
                bestRate = rate;
            }
        }

        return bestRate;
    }


    /**
     * Runs Simple Perceptron a specified number of times with a given learning rate and examples.
     * Weights are given random, small values
     *
     * @return weights trained by running Simple Perceptron a number of times
     */
    static ArrayList<Double> averagePerceptronEpochs(int epochs, ArrayList<Example> examples, double learnRate) {
        ArrayList<Double> weights = GeneralUtil.smallRandoms(20);

        return averagePerceptronEpochs(epochs, examples, weights, learnRate);
    }

    private static ArrayList<Double> averagePerceptronEpochs(int epochs, ArrayList<Example> examples, ArrayList<Double> weights, double learnRate) {

        ArrayList<Double> avgWeights = new ArrayList<>(weights);

        for (int i = 0; i < epochs; i++) {
            Collections.shuffle(examples);

            //No need to reassign to weights,
            // since it is changed within the method.
            SimpleUtil.simplePerceptron(examples, weights, learnRate);

            //After each epoch, add the current values for weights into the avg.
            for(int j = 0; j < weights.size(); j++)
                avgWeights.set(j, avgWeights.get(j) + weights.get(j));

        }
        return avgWeights;
    }


}
