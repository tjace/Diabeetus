package Beetus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("Duplicates")
class GeneralUtil {

    /**
     * Returns an estimated sign based on:
     * b (weight[0])
     * the weights
     * an example
     *
     * @param weights the weights to guess with
     * @param ex      the example to be guessed
     * @return true if (exampleFeatures * weights) + b >= 0, else false
     */
    static boolean sgn(Weight weights, Example ex) {
        double sum = 0;

        for (String key : ex.getAllKeys()) {
            if (!(weights.hasKey(key)))
                weights.put(key, smallRandom());

            sum += (weights.get(key) * ex.get(key));
            //else skip this key
        }

        sum += weights.getB(); //Don't forget to add b (for bonus lol)

        return sum >= 0;
    }

    static double testError(Weight weights, String testFile) {
        ArrayList<Example> ex = readExamples(testFile);

        double failed = 0;
        double total = ex.size();
        for (Example each : ex) {

            boolean guess = sgn(weights, each);
            boolean actual = each.getLabel();

            if (guess != actual)
                failed++;
        }
        return failed / total;
    }

    /**
     * Returns an ArrayList full of the examples from all given files
     *
     * @param files the files to read for the Examples
     * @return an ArrayList full of the examples from all given files
     */
    static ArrayList<Example> readExamples(String[] files) {
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
     */
    static ArrayList<Example> readExamples(String fileName) {
        ArrayList<Example> ret = new ArrayList<>();

        BufferedReader reader = null;
        String line;


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

    /*
    static HashMap<String, Double> smallRandoms(ArrayList<Example> examples) {
        ArrayList<Double> nums = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            nums.add(i, ThreadLocalRandom.current().nextDouble(0.0, 0.01));
        }

        return nums;
    }
     */

    private static Double smallRandom() {
        return ThreadLocalRandom.current().nextDouble(0.0, 0.01);
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

    static void testVsDev(int epochNum, Weight weights) {
        StringBuilder out = new StringBuilder("Epoch ");
        out.append(epochNum);

        if (epochNum < 10)
            out.append(" ");

        out.append(": ");
        out.append(1.0 - GeneralUtil.testError(weights, "src/diabetes.dev"));

        System.out.println(out);
    }

    static void printTestGuesses(Weight weights, ArrayList<Example> evalExamples, String evalIDFile, String outputFile) throws Exception {

        //This is what will write the output.
        PrintWriter writer = new PrintWriter(outputFile, StandardCharsets.UTF_8);
        writer.println("example_id,label");

        //This is for reading the IDs file
        BufferedReader evalReader = null;
        String IDLine;
        int lineNumber = 1;

        try {
            evalReader = new BufferedReader(new FileReader(evalIDFile));

            for (Example ex : evalExamples) {
                //Grab the example's ID (they are in order)
                IDLine = evalReader.readLine();
                String postLine;
                lineNumber++;

                boolean guess = sgn(weights, ex);
                if (guess)
                    postLine = IDLine + "," + "1";
                else
                    postLine = IDLine + "," + "0";
                System.out.println("Line " + lineNumber + ": " + postLine);
                writer.println(postLine);
            }

        } catch (
                FileNotFoundException e) {
            System.out.println("File " + evalIDFile + " not found.");
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();

            if (evalReader != null) {
                try {
                    evalReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
