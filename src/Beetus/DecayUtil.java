package Beetus;

import java.util.ArrayList;
import java.util.Collections;

class DecayUtil {

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
                Weight weights = decayPerceptronEpochs(10, ex, rate, false);

                //Test the weights on the unused cross file
                double thisError = GeneralUtil.testError(weights, testFile);
                errorSum += thisError;
            }

            error = errorSum / crosses.length;

            System.out.println("Starting rate " + rate + " has accuracy: " + (1.0 - error));

            if (error < minError || minError == -1.0) {
                minError = error;
                bestRate = rate;
            }
        }

        return bestRate;
    }

    /**
     * Runs Decaying Perceptron a specified number of times with a given learning rate and examples.
     * Weights are given random, tiny values
     *
     * @return weights trained by running Decaying Perceptron a number of times
     */
    static Weight decayPerceptronEpochs(int epochs, ArrayList<Example> examples, double learnRate, boolean isTrain) throws Exception {
        //ArrayList<Double> weights = GeneralUtil.smallRandoms(20);
        Weight weights = new Weight();

        return decayPerceptronEpochs(epochs, examples, weights, learnRate, isTrain);
    }

    /**
     * Runs Decaying Perceptron a specified number of times with a given learning rate and examples.
     * The starting weights are as given.
     *
     * @return weights trained by running Decaying Perceptron a number of times
     */
    private static Weight decayPerceptronEpochs(int epochs, ArrayList<Example> examples, Weight weights, double learnRate, boolean isTrain) throws Exception {
        int totalUpdates = 0;

        if (isTrain)
            System.out.println("************");

        for (int i = 0; i < epochs; i++) {
            Collections.shuffle(examples);
            double decayedRate = learnRate / (1 + i);

            //No need to reassign to weights,
            // since it is changed within the method.
            totalUpdates += SimpleUtil.simplePerceptron(examples, weights, decayedRate);

            if (isTrain)
                GeneralUtil.testVsDev(i + 1, weights);
        }

        if (isTrain) {
            System.out.println("************");
            System.out.println("Updates for this set of Decay epochs: " + totalUpdates);
        }
        return weights;
    }
}
