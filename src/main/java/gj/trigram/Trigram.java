package gj.trigram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Joiner;

public class Trigram {

    public void generate(File file) {
        try {
            KeyStore store = new KeyStore();
            store.addKeys(file);

            System.out.println("Original\n========");
            System.out.println((KeyStore.read(file)));

            System.out.println("Trigram\n=======");
            System.out.println(Joiner.on("\n").join(wrapText(new Generator(store).generateText(), 80)));

        } catch (FileNotFoundException e) {
            throw new RuntimeException("WARNING! Could not find the file for the keys...", e);
        } catch (IOException e) {
            throw new RuntimeException("WARNING! Could not extract the keys...", e);
        } 
    }
    
    private List<String> wrapText (String text, int len) {
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
        String fileName = Trigram.class.getResource("twostories").getFile();
        
        if (args.length < 1) {
            System.out.println("Usage: java -jar trigram-<version>.jar <filename>\n");
            System.out.println("Just to be nice, I'll give you an example from a built-in file:\n");
        } else {
            fileName = args[0];
        }

        new Trigram().generate(new File(fileName));
    }

}
