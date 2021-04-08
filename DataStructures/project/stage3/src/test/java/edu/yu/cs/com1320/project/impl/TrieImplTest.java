package edu.yu.cs.com1320.project.impl;

import static java.lang.System.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.*;

import edu.yu.cs.com1320.project.Trie;


public class TrieImplTest {
    Trie<String> trie = new TrieImpl<>();
    String string1 = "It was a dark and stormy night";
    String string2 = "It was the best of times it was the worst of times";
    String string3 = "It was a bright cold day in April and the clocks were striking thirteen";
    String string4 = "I am free no matter what rules surround me";

    @BeforeEach
    public void init() {
        int i = 1;
        int k = 1;
        int j = 1;

        for (String word : string1.split(" ")) {
            String Prop = "hello" + i;
            trie.put(word,Prop);
            i++;
        }
       for (String word : string2.split(" ")) {
           trie.put(word, "string two object " + k);
           k++;
       }

       for (String word : string3.split(" ")) {
           trie.put(word, "string three object" + j);
           j++;
       }
       
       //or (String word : string4.split(" ")) {
       //   trie.put(word, string4.indexOf(word));
       //
    }
    @Test
    public void testPut() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.put(null, "pickles");
        });

        trie.put("doc1", "peanut butter");

        trie.put("the", "does this work");
        //System.out.println(trie.getAllSorted("the",Comparator.naturalOrder()).size());
        assertEquals(trie.getAllSorted("the", Comparator.naturalOrder()).size(),4);
        assertEquals(trie.getAllSorted("the", Comparator.naturalOrder()).size(),4);
    }
    @Test
    public void testGetAllSorted() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.getAllSorted("the", null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            trie.getAllSorted(null, Comparator.naturalOrder());
        });

        out.println(trie.getAllSorted("the",Comparator.naturalOrder()));
        out.println(trie.getAllSorted("the",Comparator.reverseOrder()));
        assertEquals(trie.getAllSorted("", Comparator.naturalOrder()).size(), 0);

    }
    @Test
    public void testGetAllPrefixSorted() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.getAllWithPrefixSorted("the", null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            trie.getAllWithPrefixSorted(null, Comparator.naturalOrder());
        });

        assertEquals(trie.getAllWithPrefixSorted("st", Comparator.naturalOrder()).size(), 2);
        assertEquals(trie.getAllWithPrefixSorted("st", Comparator.naturalOrder()).size(), 2);
    }
    @Test
    public void testDeleteWithPrefix() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.deleteAllWithPrefix(null);
        });

       //assertTrue(trie.deleteAllWithPrefix("").size()==0);
        assertEquals(trie.getAllWithPrefixSorted("te", Comparator.naturalOrder()).size(), 0);

        out.println(trie.getAllWithPrefixSorted("the",Comparator.naturalOrder()).size());
        trie.deleteAllWithPrefix("th");
        out.println(trie.getAllWithPrefixSorted("the",Comparator.naturalOrder()).size());
        trie.put("these","I should take a shower soon");
        out.println(trie.getAllWithPrefixSorted("the",Comparator.naturalOrder()).size());
        assertEquals(trie.getAllWithPrefixSorted("the", Comparator.naturalOrder()).size(), 1);

    }
    @Test
    public void testDeleteAll() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.deleteAll(null);
        });

        assertTrue(trie.deleteAll("").size()==0);
        trie.deleteAllWithPrefix("the");
        trie.deleteAll("the");
        assertEquals(trie.getAllSorted("the", Comparator.naturalOrder()).size(), 0);
    }
    @Test
    public void testDelete() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.delete("the", null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            trie.delete(null, "pipe");
        });

        trie.delete("it","hello1");
        out.println(trie.getAllSorted("it" ,Comparator.naturalOrder()));

        assertTrue(trie.delete("", "")==null);
        //Deliberately don't use Integer factory
        @SuppressWarnings("deprecation")
        Integer largeInteger = new Integer(1000);
        @SuppressWarnings("deprecation")
        Integer largeInteger2 = new Integer(1000);
        trie.put("largeInteger", "pppp");
        assertTrue(largeInteger != largeInteger2);
        //assertTrue(trie.delete("largeInteger", "largeInteger2")=="largeInteger2");
    }
}
