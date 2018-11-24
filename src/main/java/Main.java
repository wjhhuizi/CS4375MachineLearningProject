import java.io.File;
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

        // Prepare ARFF File header
        sc = new Scanner(attributeFile);
        StringBuilder sb = new StringBuilder();
        sb.append("@RELATION CS4375Project\n\n");

        while (sc.hasNextLine()) {
            String   attr_line = sc.nextLine();

            // Check if is the last line, true -> class line.
            if (!sc.hasNextLine()){
                break;
            }

            String[] temp = attr_line.split(": ");
            //System.out.println(temp[0] + "--" + temp[1]);

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
                System.out.println(temp[1]);

                String num[] = temp[1].split(",");
                attrType = "{".concat(temp[1].substring(0,temp[1].length()-1)).concat("}");
                System.out.println(attrType);
            }

        }

    }



}
