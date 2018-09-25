package Beetus;

import java.util.ArrayList;

public class Main {

    private static final double[] rates = new double[]{1.0, 0.1, 0.01};
    private static final String[] crosses = new String[]{
            "src/CVSplits/training00.data", "src/CVSplits/training01.data", "src/CVSplits/training02.data",
            "src/CVSplits/training03.data", "src/CVSplits/training04.data"};


    public static void main(String args[]) throws Exception {
        simple();
        decay();
        margin();
    }

    /**
     * Runs all of the tests that are required to test the most basic version of Perceptron.
     */
    private static void simple() throws Exception {
        ArrayList<Example> examples = GeneralUtil.readExamples("src/diabetes.train");

        //Cross-Validate for best hyper-parameter
        //10 epochs per cross-validation check per rate.
        double bestRate = SimpleUtil.crossValidateRates(rates, crosses);

        System.out.println("The best Simple learnRate was found to be " + bestRate);

        //With best rate, train a new weightset on the .train file 20x
        ArrayList<Double> simpleWeights = SimpleUtil.simplePerceptronEpochs(20, examples, bestRate);

        //then test error on the .test file.
        double simpleError = GeneralUtil.testError(simpleWeights, "src/diabetes.test");

        System.out.println("Error for Simple Perceptron is " + simpleError);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");


    }

    /**
     * Runs all of the tests that are needed for testing a version of Perceptron that implements a decaying learning rate.
     *  - Rate decays with each epoch.
     */
    private static void decay() throws Exception {
        ArrayList<Example> examples = GeneralUtil.readExamples("src/diabetes.train");

        //Cross-Validate for best hyper-parameter
        //10 epochs per cross-validation check per rate.
        double bestRate = DecayUtil.crossValidateRates(rates, crosses);

        System.out.println("The best Decaying learnRate was found to be " + bestRate);

        //With best rate, train a new weightset on the .train file 20x
        ArrayList<Double> decayWeights = DecayUtil.decayPerceptronEpochs(20, examples, bestRate);

        //then test error on the .test file.
        double marginError = GeneralUtil.testError(decayWeights, "src/diabetes.test");

        System.out.println("Error for Decaying Perceptron is " + marginError);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
    }

    /**
     * Runs all of the tests that are needed for testing a version of Perceptron that implements a learning margin rate.
     *  - Rate decays with each epoch.
     *  - Weights update even when guess is correct, if within margin
     *
     */
    private static void margin() throws Exception {
        ArrayList<Example> examples = GeneralUtil.readExamples("src/diabetes.train");

        //Cross-Validate for best hyper-parameter
        //10 epochs per cross-validation check per rate.
        double[] bestRateAndMargin = MarginUtil.crossValidateRates(rates, crosses);
        double bestRate = bestRateAndMargin[0];
        double bestMargin = bestRateAndMargin[1];

        System.out.println("Best Rate is: " + bestRate);
        System.out.println("Best Margin is: " + bestMargin);

        //With best rate, train a new weightset on the .train file 20x
        ArrayList<Double> marginWeights = MarginUtil.marginPerceptronEpochs(20, examples, bestRate, bestMargin);

        //then test error on the .test file.
        double marginError = GeneralUtil.testError(marginWeights, "src/diabetes.test");

        System.out.println("Error for Margin Perceptron is " + marginError);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
    }
}
