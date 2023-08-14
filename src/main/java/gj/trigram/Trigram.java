package gj.trigram;

import com.google.common.base.Joiner;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Trigram {

    private static final int LINE_LENGTH = 80;
    private static final int MAX_WORDS = 100;

    public static String read(InputStream is) throws IOException {
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String record = inputStream.readLine();
        while (record != null) {
            builder.append(record).append("\n");
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

    public void generate(List<String> seed, File... file) {
        List<FileInputStream> fis = new ArrayList<>();
        try {
            for (File f:  file) {
                fis.add(new FileInputStream(f));
            }
            generate(seed, fis.toArray(new FileInputStream[0]));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("WARNING! Could not find the file for the keys...", e);
        } finally {
            for (FileInputStream f:  fis) {
                closeStream(f);
            }
        }
    }

    public void generate(List<String> seed, InputStream... is) {
        try {
            KeyStore store = new KeyStore();
            StringBuilder b = new StringBuilder();
            for (InputStream i: is) {
                b.append(read(i));
            }
            String text = b.toString();
            store.addKeys(text);
            System.out.println(store);

            System.out.println();
            //System.out.println("Original\n========");
            //System.out.println(text);

            System.out.println("Trigram\n=======");
            System.out.println(Joiner.on("\n").join(wrapText(
                    new Generator(store).generateText(MAX_WORDS, seed), LINE_LENGTH)));

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
            return Collections.singletonList(text);
        }

        char[] chars = text.toCharArray();
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();

        for (char aChar : chars) {
            word.append(aChar);
            if (aChar == ' ') {
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

            new Trigram().generate(Arrays.asList("In", "a"), Trigram.class.getResourceAsStream("one"));

//            new Trigram().generate(Arrays.asList("In", "a"),
//                    Trigram.class.getResourceAsStream("fellowship"),
//                    Trigram.class.getResourceAsStream("thehobbit"));

//            new Trigram().generate(Arrays.asList("Once", "upon"),
//                    Trigram.class.getResourceAsStream("slyfox"),
//                    Trigram.class.getResourceAsStream("3littlepigs"));

//            new Trigram().generate(Arrays.asList("This", "is"),
//                    Trigram.class.getResourceAsStream("one"),
//                    Trigram.class.getResourceAsStream("two"));
        } else {
            new Trigram().generate(Arrays.asList("Once", "upon"), new File(args[0]));
        }
    }

}
