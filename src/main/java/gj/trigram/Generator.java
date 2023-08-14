package gj.trigram;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

public class Generator {

    private KeyStore store;
    
    public Generator(KeyStore store) {
        this.store = store;
    }
    
    public String generateText(int moreOrLessMaxWords, List<String> root) {
        StringBuilder text = new StringBuilder();

        if (root == null || root.isEmpty()) {
            root = store.getRoot();
        }

        List<String> last = new ArrayList<String>(root);
        text.append(Joiner.on(" ").join(last)).append(" ");
        int cnt = 0;
        while (store.doesContain(last)) {
            List<String> options = store.getList(last);
            int which = (int)(Math.random() * options.size());
            String word = options.get(which);
            text.append(word).append(" ");
            cnt++;
            last.remove(0);
            last.add(word);
            if (cnt > moreOrLessMaxWords && word.trim().endsWith(".")) {
                break;
            }
        }
        return text.toString();
    }

}
