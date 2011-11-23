package gj.trigram;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class KeyStoreTest {
    
    @Test
    public void testAddKeys() throws IOException {
        KeyStore store = new KeyStore();
        store.addKeys(new File(KeyStoreTest.class.getResource("simple").getFile()));
        assertThat(store.getSize(), equalTo(8));
        assertThat(store.getList(Arrays.asList("a","b")), equalTo(Arrays.asList("c")));
        assertThat(store.getList(Arrays.asList("b","c")), equalTo(Arrays.asList("d")));
        assertThat(store.getList(Arrays.asList("c","d")), equalTo(Arrays.asList("e")));
        assertThat(store.getList(Arrays.asList("d","e")), equalTo(Arrays.asList("f")));
        assertThat(store.getList(Arrays.asList("e","f")), equalTo(Arrays.asList("g")));
        assertThat(store.getList(Arrays.asList("f","g")), equalTo(Arrays.asList("h")));
        assertThat(store.getList(Arrays.asList("g","h")), equalTo(Arrays.asList("i")));
        assertThat(store.getList(Arrays.asList("h","i")), equalTo(Arrays.asList("j")));
    }
    
    @Test
    public void testAddMultiKeys() throws IOException {
        KeyStore store = new KeyStore();
        store.addKeys(new File(KeyStoreTest.class.getResource("complex").getFile()));
        assertThat(store.getSize(), equalTo(3));
        assertThat(store.getList(Arrays.asList("a","b")), equalTo(Arrays.asList("c", "d")));
        assertThat(store.getList(Arrays.asList("b","c")), equalTo(Arrays.asList("a")));
        assertThat(store.getList(Arrays.asList("c","a")), equalTo(Arrays.asList("b")));
    }
    
    @Test
    public void testGetRoot() throws IOException {
        KeyStore store = new KeyStore();
        store.addKeys(new File(KeyStoreTest.class.getResource("simple").getFile()));
        assertThat(store.getRoot(), equalTo(Arrays.asList("a","b")));
    }

    @Test
    public void testClear() throws IOException {
        KeyStore store = new KeyStore();
        store.addKeys(new File(KeyStoreTest.class.getResource("simple").getFile()));
        assertThat(store.getSize(), equalTo(8));
        store.clear();
        assertThat(store.getSize(), equalTo(0));
    }

    @Test
    public void testDoesContain() throws IOException {
        KeyStore store = new KeyStore();
        store.addKeys(new File(KeyStoreTest.class.getResource("simple").getFile()));
        assertTrue(store.doesContain(Arrays.asList("a","b")));
    }
    
}
