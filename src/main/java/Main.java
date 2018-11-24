import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static Scanner sc;
    private static int num_of_attr = 0;
    private static RandomForest randomForest;

    public static void main(String[] args) throws Exception {

        String attributeFileName    = "attr.txt";
        String trainDataFileName    = "train.mv.txt";
        String train_ARFF_FileName  = "train.arff";
        String testDataFileName     = "train.mv0.txt";
        String test_ARFF_Filename   = "test.arff";

        File   attributeFile        = new File(attributeFileName);
        File   trainDataFile        = new File(trainDataFileName);
        File   train_ARFF_File      = new File(train_ARFF_FileName);
        File   testDataFile         = new File(testDataFileName);
        File   test_ARFF_File       = new File(test_ARFF_Filename);

        generate_ARFF(attributeFile, trainDataFile, train_ARFF_File);
        generate_ARFF(attributeFile, testDataFile,test_ARFF_File);
        train_RandomForest(train_ARFF_FileName);
        eval_RandomForest(train_ARFF_FileName, test_ARFF_Filename);
    }

    private static void generate_ARFF(File attributeFile, File dataFile, File ARFF_File) throws IOException{

        String header  ;
        String data    ;
        String attrName;
        String attrType;
        boolean containsMissingValue = false;

        //////// Prepare ARFF File header ////////
        sc = new Scanner(attributeFile);
        StringBuilder sb = new StringBuilder();
        sb.append("@RELATION CS4375Project\n\n");

        while (sc.hasNextLine()) {
            String attr_line = sc.nextLine();

            // Check if is the last line, true -> class line.
            if (!sc.hasNextLine()){
                break;
            }

            // Start parsing attribute file...
            String[] temp = attr_line.split(": ");
            attrName = temp[0];

            if (temp[1].contains("?")){
                containsMissingValue = true;
            }


            if (temp[1].contains("cont")){
                attrType = "numeric";
            }
            else {
                temp[1] = temp[1].replace("(", "");
                temp[1] = temp[1].replace(")", "");
                temp[1] = temp[1].replace("?", "");
                temp[1] = temp[1].replace(".", "");

                attrType = "{".concat(temp[1].substring(0,temp[1].length()-1)).concat("}");
            }

            sb.append("@ATTRIBUTE ");
            sb.append(attrName);
            sb.append(" ");
            sb.append(attrType);
            sb.append("\n");
        }
        /* Hard Coded class NOT GOOD!!!*/
        sb.append("@ATTRIBUTE class {0,1}\n");
        sb.append("\n");
        sc.close();
        sc = null;
        header = sb.toString();
        sb = null;

        //////// Prepare ARFF File DATA ////////
        sc = new Scanner(dataFile);
        sb = new StringBuilder();
        sb.append("@DATA\n");

        while (sc.hasNextLine()) {

            String temp = sc.nextLine();
            temp = temp.replaceAll(" ", ",");

            if (temp.charAt(temp.length()-1) == ',') {
                temp = temp.substring(0, temp.length()-1);
            }

            sb.append(temp);
            sb.append("\n");
        }
        sc.close();
        sc = null;
        data = sb.toString();
        sb = null;

        BufferedWriter bw = new BufferedWriter(new FileWriter(ARFF_File));
        bw.write(header);
        bw.write(data);
        bw.close();
    }

    private static void dataClean(){
        //TODO: Complete the data cleaning session later...
    }

    private static void train_RandomForest(String training_ARFF_file) throws Exception{

        DataSource training_source = new DataSource(training_ARFF_file);
        Instances data = training_source.getDataSet();
        // Setting Class index to the last attribute
        if (data.classIndex() == -1){
            data.setClassIndex(data.numAttributes() - 1);
        }

        System.out.println("Building Classifier...");
        String[] options = weka.core.Utils.splitOptions("-P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1");
        randomForest = new RandomForest();
        randomForest.setOptions(options);
        randomForest.buildClassifier(data);
        System.out.println("Complete Building Classifier...");

    }

    private static void eval_RandomForest(String training_ARFF_file, String test_ARFF_file) throws Exception{

        DataSource  training_source = new DataSource(training_ARFF_file);
        DataSource  test_source     = new DataSource(test_ARFF_file);
        Instances   trainData       = training_source.getDataSet();
        Instances   testData        = test_source.getDataSet();

        if (trainData.classIndex() == -1){
            trainData.setClassIndex(trainData.numAttributes() - 1);
        }
        if (testData.classIndex() == -1){
            testData.setClassIndex(testData.numAttributes() - 1);
        }

        Evaluation  eval = new Evaluation(trainData);
        eval.evaluateModel(randomForest, testData);
        System.out.println(eval.toSummaryString("\nResult\n===============\n",false));

    }

    private static int classify(Instance inst) {




        return 0;
    }



}
