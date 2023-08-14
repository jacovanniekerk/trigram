package gj.trigram;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class KeyStoreTest {
    
    @Test
    public void testAddKeys() throws IOException {
        KeyStore store = new KeyStore();
        store.addKeys(Trigram.read(KeyStoreTest.class.getResourceAsStream("simple")));
        assertThat(store.getSize(), equalTo(8));
        assertThat(store.getList(Arrays.asList("a","b")), equalTo(Collections.singletonList("c")));
        assertThat(store.getList(Arrays.asList("b","c")), equalTo(Collections.singletonList("d")));
        assertThat(store.getList(Arrays.asList("c","d")), equalTo(Collections.singletonList("e")));
        assertThat(store.getList(Arrays.asList("d","e")), equalTo(Collections.singletonList("f")));
        assertThat(store.getList(Arrays.asList("e","f")), equalTo(Collections.singletonList("g")));
        assertThat(store.getList(Arrays.asList("f","g")), equalTo(Collections.singletonList("h")));
        assertThat(store.getList(Arrays.asList("g","h")), equalTo(Collections.singletonList("i")));
        assertThat(store.getList(Arrays.asList("h","i")), equalTo(Collections.singletonList("j")));
    }
    
    @Test
    public void testAddMultiKeys() throws IOException {
        KeyStore store = new KeyStore();
        store.addKeys(Trigram.read(KeyStoreTest.class.getResourceAsStream("complex")));
        assertThat(store.getSize(), equalTo(3));
        assertThat(store.getList(Arrays.asList("a","b")), equalTo(Arrays.asList("c", "d")));
        assertThat(store.getList(Arrays.asList("b","c")), equalTo(Collections.singletonList("a")));
        assertThat(store.getList(Arrays.asList("c","a")), equalTo(Collections.singletonList("b")));
    }
    
    @Test
    public void testGetRoot() throws IOException {
        KeyStore store = new KeyStore();
        store.addKeys(Trigram.read(KeyStoreTest.class.getResourceAsStream("simple")));
        assertThat(store.getRoot(), equalTo(Arrays.asList("a","b")));
    }

    @Test
    public void testClear() throws IOException {
        KeyStore store = new KeyStore();
        store.addKeys(Trigram.read(KeyStoreTest.class.getResourceAsStream("simple")));
        assertThat(store.getSize(), equalTo(8));
        store.clear();
        assertThat(store.getSize(), equalTo(0));
    }

    @Test
    public void testDoesContain() throws IOException {
        KeyStore store = new KeyStore();
        store.addKeys(Trigram.read(KeyStoreTest.class.getResourceAsStream("simple")));
        assertTrue(store.doesContain(Arrays.asList("a","b")));
    }
    
}
