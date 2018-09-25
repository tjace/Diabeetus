package Beetus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private static double[] rates = new double[]{1.0, 0.1, 0.01};
    private static String[] crosses = new String[]{
            "src/CVSplits/training00.data", "src/CVSplits/training01.data", "src/CVSplits/training02.data",
            "src/CVSplits/training03.data", "src/CVSplits/training04.data"};


    public static void main(String args[]) throws Exception {
        simple();
        decay();
    }

    private static void decay() throws Exception {
        ArrayList<Example> examples = GeneralUtil.readExamples("src/diabetes.train");



    }

    public static void simple() throws Exception {
        ArrayList<Example> examples = GeneralUtil.readExamples("src/diabetes.train");

        //Cross-Validate for best hyper-parameter
        //10 epochs per cross-validation check per rate.
        double bestRate = SimpleUtil.crossValidateRates(rates, crosses);

        System.out.println("The best rate was found to be " + bestRate);

        //With best rate, train a new weightset on the .train file 20x
        ArrayList<Double> simpleWeights = SimpleUtil.simplePerceptronEpochs(20, examples, bestRate);

        //then test error on the .test file.
        double simpleError = GeneralUtil.testError(simpleWeights, "src/diabetes.test");

        System.out.println("Error for Simple Perceptron is " + simpleError);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");


    }
}
