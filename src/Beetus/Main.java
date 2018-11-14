package Beetus;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    private static final double[] rates = new double[]{1.0, 0.1, 0.01};
    private static final String[] crosses = new String[]{
            "src/CVSplits/training00.data", "src/CVSplits/training01.data", "src/CVSplits/training02.data",
            "src/CVSplits/training03.data", "src/CVSplits/training04.data"};


    private static final String[] finalCrosses = new String[]{
            "src/finalFiles/test01", "src/finalFiles/test02", "src/finalFiles/test03",
            "src/finalFiles/test04", "src/finalFiles/test05"};

    private static final String finalTrain = "src/finalFiles/data.train";
    private static final String finalTest = "src/finalFiles/data.test";
    private static final String finalEval = "src/finalFiles/data.eval.anon";
    private static final String finalEvalIDs = "src/finalFiles/data.eval.anon.id";
    private static final String finalOutput = "src/finalFiles/output";



    enum Types {
        SIMPLE, DECAY, MARGIN, AVERAGE;
    }


    public static void main(String args[]) throws Exception {
//        double simple = simple();
//        double decay = decay();
//        double margin = margin();
//        double average = average();
//
//        System.out.println("Simple: " + simple);
//        System.out.println("Decay: " + decay);
//        System.out.println("Margin: " + margin);
//        System.out.println("Average: " + average);


        kaggle(); //We'll use average.
    }

    /**
     * Creates a good set of weights, then outputs a file for the final project.
     */
    private static double kaggle() throws Exception {
        ArrayList<Example> examples = GeneralUtil.readExamples(finalTrain);

        //Cross-Validate for best hyper-parameter, on the eval file
        //10 epochs per cross-validation check per rate.
        double bestRate = AverageUtil.crossValidateRates(rates, finalCrosses);

        System.out.println("The best Average learnRate was found to be " + bestRate);

        //With best rate, train a new weightset on the .train file 20x
        Weight kaggleWeights = AverageUtil.averagePerceptronEpochs(20, examples, bestRate, true);

        //then guess on the test file
        double finalAccuracy = 1.0 - GeneralUtil.testError(kaggleWeights, finalTest);

        System.out.println("Accuracy on the final is " + finalAccuracy);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");


        //Finally, guess on the eval file, and print to another file the guesses.
        ArrayList<Example> finalEvalExamples = GeneralUtil.readExamples(finalEval);
        GeneralUtil.printTestGuesses(kaggleWeights, finalEvalExamples, finalEvalIDs, finalOutput);


        return 0.0;
    }


    /**
     * Runs all of the tests that are required to test the most basic version of Perceptron.
     */
    private static double simple() throws Exception {
        ArrayList<Example> examples = GeneralUtil.readExamples("src/diabetes.train");

        //Cross-Validate for best hyper-parameter
        //10 epochs per cross-validation check per rate.
        double bestRate = SimpleUtil.crossValidateRates(rates, crosses);

        System.out.println("The best Simple learnRate was found to be " + bestRate);

        //With best rate, train a new weightset on the .train file 20x
        Weight simpleWeights = SimpleUtil.simplePerceptronEpochs(20, examples, bestRate, true);

        //then test error on the .test file.
        double simpleAccuracy = 1.0 - GeneralUtil.testError(simpleWeights, "src/diabetes.test");

        System.out.println("Accuracy for Simple Perceptron is " + simpleAccuracy);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");

        return simpleAccuracy;
    }

    /**
     * Runs all of the tests that are needed for testing a version of Perceptron that implements a decaying learning rate.
     * - Rate decays with each epoch.
     */
    private static double decay() throws Exception {
        ArrayList<Example> examples = GeneralUtil.readExamples("src/diabetes.train");

        //Cross-Validate for best hyper-parameter
        //10 epochs per cross-validation check per rate.
        double bestRate = DecayUtil.crossValidateRates(rates, crosses);

        System.out.println("The best Decaying learnRate was found to be " + bestRate);

        //With best rate, train a new weightset on the .train file 20x
        Weight decayWeights = DecayUtil.decayPerceptronEpochs(20, examples, bestRate, true);

        //then test error on the .test file.
        double decayAccuracy = 1.0 - GeneralUtil.testError(decayWeights, "src/diabetes.test");

        System.out.println("Accuracy for Decaying Perceptron is " + decayAccuracy);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");

        return decayAccuracy;
    }

    /**
     * Runs all of the tests that are needed for testing a version of Perceptron that implements a learning margin rate.
     * - Rate decays with each epoch.
     * - Weights update even when guess is correct, if within margin
     */
    private static double margin() throws Exception {
        ArrayList<Example> examples = GeneralUtil.readExamples("src/diabetes.train");

        //Cross-Validate for best hyper-parameter
        //10 epochs per cross-validation check per rate.
        double[] bestRateAndMargin = MarginUtil.crossValidateRates(rates, crosses);
        double bestRate = bestRateAndMargin[0];
        double bestMargin = bestRateAndMargin[1];

        System.out.println("Best Rate is: " + bestRate);
        System.out.println("Best Margin is: " + bestMargin);

        //With best rate, train a new weightset on the .train file 20x
        Weight marginWeights = MarginUtil.marginPerceptronEpochs(20, examples, bestRate, bestMargin, true);

        //then test error on the .test file.
        double marginAccuracy = 1.0 - GeneralUtil.testError(marginWeights, "src/diabetes.test");

        System.out.println("Accuracy for Margin Perceptron is " + marginAccuracy);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");

        return marginAccuracy;
    }

    /**
     * Runs all of the tests that are required to test the Averaged version of Perceptron.
     */
    private static double average() throws Exception {
        ArrayList<Example> examples = GeneralUtil.readExamples("src/diabetes.train");

        //Cross-Validate for best hyper-parameter
        //10 epochs per cross-validation check per rate.
        double bestRate = AverageUtil.crossValidateRates(rates, crosses);

        System.out.println("The best Average learnRate was found to be " + bestRate);

        //With best rate, train a new weightset on the .train file 20x
        Weight averageWeights = AverageUtil.averagePerceptronEpochs(20, examples, bestRate, true);

        //then test error on the .test file.
        double averageAccuracy = 1.0 - GeneralUtil.testError(averageWeights, "src/diabetes.test");

        System.out.println("Accuracy for Average Perceptron is " + averageAccuracy);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");

        return averageAccuracy;
    }
}
