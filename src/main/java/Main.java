import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static Scanner sc;
    private static int num_of_attr = 0;

    public static void main(String[] args) throws Exception {

        String attributeFileName = "attr.txt";
        String dataFileName      = "train.mv.txt";
        File   attributeFile     = new File(attributeFileName);
        File   dataFile          = new File(dataFileName);

        generate_ARFF(attributeFile, dataFile);
    }

    public static void generate_ARFF(File attributeFile, File dataFile) throws IOException{

        String header   = "";
        String data     = "";
        String attrName = "";
        String attrType = "";
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

                String num[] = temp[1].split(",");
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

        //System.out.println(header + data);
        BufferedWriter bw = new BufferedWriter(new FileWriter("train.arff"));
        bw.write(header);
        bw.write(data);
        bw.close();
    }



}
