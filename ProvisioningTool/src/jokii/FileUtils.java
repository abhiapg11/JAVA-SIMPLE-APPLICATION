package jokii;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class FileUtils {

    public static String readFileContent(String filePath) throws IOException {
        String fileContentString = "";
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            fileContentString = sb.toString();
        } finally {
            br.close();
        }

        return fileContentString;
    }

    public static String readFileContentFromResource(String pathToResouceTextFile) {
        String fileContentString = "";

        InputStream stream = Main.class.getResourceAsStream("/"+pathToResouceTextFile);

        if (stream != null) {
            Scanner input = null;
            try {
                input = new Scanner (stream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();

            while (input.hasNextLine()) {
                sb.append(input.nextLine());
                sb.append(System.lineSeparator());
            }

            fileContentString = sb.toString();
        }

        return fileContentString;
    }
}
