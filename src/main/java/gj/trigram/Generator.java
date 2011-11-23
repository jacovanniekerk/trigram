package gj.trigram;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

public class Generator {

    private KeyStore store;
    
    public Generator(KeyStore store) {
        this.store = store;
    }
    
    public String generateText() {
        StringBuilder text = new StringBuilder();
        List<String> last = new ArrayList<String>(); 
        last.addAll(store.getRoot());
        text.append(Joiner.on(" ").join(last) + " ");
        while (store.doesContain(last)) {
            List<String> options = store.getList(last);
            int which = (int)(Math.random() * options.size());
            String word = options.get(which);
            text.append(word + " ");
            last.remove(0);
            last.add(word);
        }
        return text.toString();
    }

}
