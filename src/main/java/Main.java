import java.io.File;
import java.io.IOException;
import java.util.Scanner;
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

        String header = "";
        String data   = "";

        // Prepare ARFF File header
        sc = new Scanner(attributeFile);
        StringBuilder sb = new StringBuilder();
        sb.append("@RELATION CS4375Project\n\n");

        while (sc.hasNextLine()) {
            String   attr_line = sc.nextLine();
            String[] temp = attr_line.split(": ");
            //System.out.println(temp[0] + "--" + temp[1]);
            Pattern  pattern1 = Pattern.compile("^cont ");

        }

    }



}
