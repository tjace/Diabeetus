package Beetus;

import java.util.ArrayList;
import java.util.Collections;

class MarginUtil {

    /**
     * Returns the best of the passed-in rates
     *
     * @param rates   the rates to test
     * @param crosses the cross-validation files
     * @return a double array [bestRate, bestMargin]
     * @throws Exception when label is bad in data
     */
    @SuppressWarnings("Duplicates")
    static double[] crossValidateRates(double[] rates, String[] crosses) throws Exception {
        double bestRate = -1.0;
        double bestMargin = -1.0;
        double minError = -1.0;
        double errorSum;
        double error;

        for (double margin : rates) {
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
                    ArrayList<Double> weights = marginPerceptronEpochs(10, ex, rate, margin, false);

                    //Test the weights on the unused cross file
                    double thisError = GeneralUtil.testError(weights, testFile);
                    errorSum += thisError;
                }

                error = errorSum / crosses.length;

                System.out.println("Starting rate " + rate + " + margin " + margin + " has accuracy: " + (1.0 - error));

                if (error < minError || minError == -1.0) {
                    minError = error;
                    bestRate = rate;
                    bestMargin = margin;
                }
            }
        }
        return new double[]{bestRate, bestMargin};
    }

    /**
     * Runs Margin Perceptron a specified number of times with a given learning rate and examples.
     * Weights are given random, tiny values
     *
     * @return weights trained by running Decaying Perceptron a number of times
     */
    static ArrayList<Double> marginPerceptronEpochs(int epochs, ArrayList<Example> examples,
                                                    double learnRate, double margin, boolean isTrain) throws Exception {
        ArrayList<Double> weights = GeneralUtil.smallRandoms(20);

        return marginPerceptronEpochs(epochs, examples, weights, learnRate, margin, isTrain);
    }

    /**
     * Runs Margin Perceptron a specified number of times with a given learning rate and examples.
     * The starting weights are as given.
     *
     * @return weights trained by running Decaying Perceptron a number of times
     */
    private static ArrayList<Double> marginPerceptronEpochs(int epochs, ArrayList<Example> examples,
                                                            ArrayList<Double> weights, double learnRate,
                                                            double margin, boolean isTrain) throws Exception {
        int totalUpdates = 0;
        if (isTrain)
            System.out.println("************");

        for (int i = 0; i < epochs; i++) {
            Collections.shuffle(examples);
            double decayedRate = learnRate / (1 + i);

            //No need to reassign to weights,
            // since it is changed within the method.
            totalUpdates += marginPerceptron(examples, weights, decayedRate, margin);

            if (isTrain)
                GeneralUtil.testVsDev(i + 1, weights);
        }

        if (isTrain) {
            System.out.println("************");
            System.out.println("Updates for this set of Margin epochs: " + totalUpdates);
        }

        return weights;
    }

    private static int marginPerceptron(ArrayList<Example> examples, ArrayList<Double> weights,
                                        double decayedRate, double margin) {
        int updates = 0;

        for (Example ex : examples) {
            boolean update = underMargin(weights, ex, margin);

            if (update) {
                updates++;
                double y = ex.get(0);

                //Update each weight
                //w <- w + (atualSign * learnRate * featureValue)
                for (int i = 1; i < 20; i++) {
                    weights.set(i, weights.get(i) + (y * decayedRate * ex.get(i)));
                }
                //b <- b + (actualSign * learnRate)
                weights.set(0, weights.get(0) + (y * decayedRate));
            }
        }

        return updates;
    }

    private static boolean underMargin(ArrayList<Double> weights, Example ex, double margin) {
        double sum = 0;

        for (int i = 1; i < 20; i++)
            sum += (weights.get(i) * ex.get(i));

        sum += weights.get(0);
        sum *= ex.get(0);

        return sum < margin;
    }
}
