package gj.trigram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;

/**
 * Class to generate the keys for the trigram.
 * 
 * @author Jaco van Niekerk
 * 
 */
public class KeyStore {

    private List<String> first = Collections.emptyList();
    private Map<String, List<String>> trigram = Collections.emptyMap();

    public KeyStore() {
    }

    /**
     * Given an InputStream, this reads the file into memory, ignoring new
     * lines.
     * 
     * @param in
     * @return
     * @throws IOException
     */
    public static String read(File file) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(fis));
            StringBuilder builder = new StringBuilder();
            String record = inputStream.readLine();
            while (record != null) {
                builder.append(record + "\n");
                record = inputStream.readLine();
            }
            return builder.toString();
        } finally {
            closeStream(fis);
        }
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

    /**
     * Creates a map of keys and accepted values for each key.
     * 
     * @TODO This is actually terrible on memory and needs to be refactored so
     *       that it uses less memory; but for now this will work fine on most
     *       files.
     * 
     * @param in
     * @throws IOException
     */
    public void addKeys(File file) throws IOException {
        String[] words = read(file).replaceAll("\n", " ").split(" ");
        List<String> wordList = new ArrayList<String>();
        for (String string : words) {
            if (string.trim().length() > 0)
                wordList.add(string);
        }
        if (wordList.size() < 3) {
            return;
        }
        first = Arrays.asList(wordList.get(0), wordList.get(1));
        trigram = new HashMap<String, List<String>>();
        for (int i = 0; i < wordList.size() - 2; i++) {
            String key = wordList.get(i) + '\0' + wordList.get(i + 1);
            List<String> list = trigram.get(key);
            if (list == null) {
                trigram.put(key, (list = new ArrayList<String>()));
            }
            list.add(wordList.get(i + 2));
        }
    }

    /**
     * Clears the map.
     */
    public void clear() {
        first = Collections.emptyList();
        trigram = Collections.emptyMap();
    }

    /**
     * Returns the size of the map.
     * 
     * @return
     */
    public int getSize() {
        return trigram.size();
    }

    /**
     * Returns the first two words in the file which acts as the root.
     * 
     * @return
     */
    public List<String> getRoot() {
        return first;
    }

    /**
     * Given a key, this returns a list of acceptable words that can follow the
     * key.
     * 
     * @param key
     * @return
     */
    public List<String> getList(List<String> key) {
        String keyValue = Joiner.on("\0").useForNull("").join(key);
        return trigram.get(keyValue);
    }

    public boolean doesContain(List<String> key) {
        String keyValue = Joiner.on("\0").useForNull("").join(key);
        return trigram.containsKey(keyValue);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, List<String>> set : trigram.entrySet()) {
            String[] key = set.getKey().split("\0");
            String keyPart = Joiner.on(",").useForNull("-").join(key);
            String valuePart = Joiner.on(",").useForNull("").join(set.getValue());
            builder.append(keyPart + "=" + valuePart + "\n");
        }
        return builder.toString();
    }

}
