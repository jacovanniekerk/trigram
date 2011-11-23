package gj.trigram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Joiner;

public class Trigram {

    public static String read(InputStream is) throws IOException {
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String record = inputStream.readLine();
        while (record != null) {
            builder.append(record + "\n");
            record = inputStream.readLine();
        }
        return builder.toString();
    }

    private static void closeStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("WARNING! Could not close file...", e);
            }
        }
    }

    public void generate(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            generate(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("WARNING! Could not find the file for the keys...", e);
        } finally {
            closeStream(fis);
        }
    }

    public void generate(InputStream is) {
        try {
            KeyStore store = new KeyStore();
            String text = read(is);
            store.addKeys(text);

            System.out.println("Original\n========");
            System.out.println(text);

            System.out.println("Trigram\n=======");
            System.out.println(Joiner.on("\n").join(wrapText(new Generator(store).generateText(), 80)));

        } catch (FileNotFoundException e) {
            throw new RuntimeException("WARNING! Could not find the file for the keys...", e);
        } catch (IOException e) {
            throw new RuntimeException("WARNING! Could not extract the keys...", e);
        }
    }

    private List<String> wrapText(String text, int len) {
        if (text == null) {
            return Collections.emptyList();
        }

        if (len <= 0 || text.length() <= len) {
            return Arrays.asList(text);
        }

        char[] chars = text.toCharArray();
        List<String> lines = new ArrayList<String>();
        StringBuffer line = new StringBuffer();
        StringBuffer word = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);
            if (chars[i] == ' ') {
                if ((line.length() + word.length()) > len) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }
                line.append(word);
                word.delete(0, word.length());
            }
        }

        if (word.length() > 0) {
            if ((line.length() + word.length()) > len) {
                lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }

        if (line.length() > 0) {
            lines.add(line.toString());
        }

        return lines;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar trigram-<version>.jar <filename>\n");
            System.out.println("Just to be nice, I'll give you an example from a built-in file:\n");
            new Trigram().generate(Trigram.class.getResourceAsStream("twostories"));
        } else {
            new Trigram().generate(new File(args[0]));
        }
    }

}
