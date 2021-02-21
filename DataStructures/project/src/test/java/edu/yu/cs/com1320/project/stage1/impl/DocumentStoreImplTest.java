package edu.yu.cs.com1320.project.stage1.impl;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.stage1.impl.*;
import edu.yu.cs.com1320.project.stage1.*;
//import org.junit.jupiter.api.Test;

public class DocumentStoreImplTest {
    @Test
    public void hashTableImplSimplePutAndGet() {
        HashTable<Integer, Integer> hashTable = new HashTableImpl<Integer, Integer>();
        hashTable.put(1, 2);
        hashTable.put(3, 6);
        hashTable.put(7, 14);
        int x = hashTable.get(1);
        int y = hashTable.get(3);
        int z = hashTable.get(7);
        assertEquals(2, x);
        assertEquals(6, y);
        assertEquals(14, z);

    }

    @Test
    public void hashTableImplALotOfInfoTest() {
        HashTable<Integer, Integer> hashTable = new HashTableImpl<Integer, Integer>();
        for (int i = 0; i < 1000; i++) {
            hashTable.put(i, 2 * i);
        }

        int aa = hashTable.get(450);
        assertEquals(900, aa);
        p("passed Test: hashTableImplALotOfInfoTest");
    }

    @Test
    public void hashTableImplCollisionTest() {
        HashTable<Integer, Integer> hashTable = new HashTableImpl<Integer, Integer>();
        hashTable.put(1, 9);
        hashTable.put(6, 12);
        hashTable.put(11, 22);
        int a = hashTable.get(1);
        int b = hashTable.get(6);
        int c = hashTable.get(11);
        assertEquals(9, a);
        assertEquals(12, b);
        assertEquals(22, c);
        p("passed Test: hashTableImplCollisionTest");
    }

    @Test
    public void hashTableImplReplacementTest() {
        HashTable<Integer, Integer> hashTable = new HashTableImpl<Integer, Integer>();
        hashTable.put(1, 2);
        int a = hashTable.put(1, 3);
        assertEquals(2, a);
        int b = hashTable.put(1, 4);
        assertEquals(3, b);
        int c = hashTable.put(1, 9);
        assertEquals(4, c);
        p("passed Test: hashTableImplReplacementTest");
    }

    @Test
    public void hashTableImplReplacementTest2() {
        HashTable<Integer, Integer> hashTable = new HashTableImpl<Integer, Integer>();
        hashTable.put(1, 2);
        p("passed Test: hashTableImplReplacementTest2");
    }

    @Test
    public void docTest() throws URISyntaxException {
        URI me1 = new URI("hello1");
        URI me2 = new URI("hello2");
        String s1 = "Four xcor and ajdfjajfjf this i the fghhghg jdfjdjkjfdkfkdsfk sdjfdf this is my password backwards ffsfsdf%^&*^%&^%&^";
        byte[] b1 = { 1, 2, 4, 56, 7, 7, 6, 5, 43, 4, 6, 7, 8, 8, 8, 55, 52, 5, 2, 52, 75, 95, 25, 85, 74, 52, 52, 5, 67,
                61 };
        Document doc1 = new DocumentImpl(me1, s1);
        Document doc2 = new DocumentImpl(me2, b1);
        assertEquals("Four xcor and ajdfjajfjf this i the fghhghg jdfjdjkjfdkfkdsfk sdjfdf this is my password backwards ffsfsdf%^&*^%&^%&^", doc1.getDocumentTxt());
        assertEquals(b1, doc2.getDocumentBinaryData());
        p("passed Test: docTest");
    }

    @Test
    public void basicCollision() {
        HashTable<Integer, String> hashTable = new HashTableImpl<Integer, String>();
        hashTable.put(1, "Avi");
        hashTable.put(5, "dinsky");
        hashTable.put(6, "Radinsky");
        hashTable.put(11, "gami");
        assertEquals("gami", hashTable.put(11, "gthir"));
        assertEquals("gthir", hashTable.get(11));
        assertEquals("Avi", hashTable.get(1));
        assertEquals("Radinsky", hashTable.get(6));
        p("passed Test: basicCollision");
    }
    private void p(String s) {
        System.out.println(s);
    }

    @Test
    public void testCollisionPutValues() throws IOException {
        DocumentStore documentStore = new DocumentStoreImpl();
        String string1 = "It was a dark and stormy night";
        String string2 = "It was the best of times, it was the worst of times";
        String string3 = "It was a bright cold day in April, and the clocks were striking thirteen";
        String string4 = "I am free, no matter what rules surround me.";
        byte[] bytes3 = string3.getBytes();
        byte[] bytes4 = string4.getBytes();

        InputStream inputStream1 = new ByteArrayInputStream(string1.getBytes());
        InputStream inputStream2 = new ByteArrayInputStream(string2.getBytes());
        InputStream inputStream3 = new ByteArrayInputStream(bytes3);
        InputStream inputStream4 = new ByteArrayInputStream(bytes4);
        URI uri1 =  URI.create("www.wrinkleintime.com");
        URI uri2 =  URI.create("www.taleoftwocities.com");
        URI uri3 =  URI.create("www.1984.com");
        URI uri4 =  URI.create("www.themoonisaharshmistress.com");
        documentStore.putDocument(inputStream1,uri1,DocumentStore.DocumentFormat.TXT);
        int putTXT2 = documentStore.putDocument(inputStream2,uri2,DocumentStore.DocumentFormat.TXT);
        int putBINARY1 = documentStore.putDocument(inputStream3,uri3,DocumentStore.DocumentFormat.BINARY);
        int putBINARY2 = documentStore.putDocument(inputStream4,uri4,DocumentStore.DocumentFormat.BINARY);
        int test1 = documentStore.getDocument(uri1).hashCode();
        int test2 = documentStore.getDocument(uri2).hashCode();
        int test3 = documentStore.getDocument(uri3).hashCode();
        int test4 = documentStore.getDocument(uri4).hashCode();
        InputStream inputStream1b = new ByteArrayInputStream(string1.getBytes());
        InputStream inputStream2b = new ByteArrayInputStream(string2.getBytes());
        InputStream inputStream3b = new ByteArrayInputStream(bytes3);
        InputStream inputStream4b = new ByteArrayInputStream(bytes4);
        int putBINARY1Collision = documentStore.putDocument(inputStream1b,uri1,DocumentStore.DocumentFormat.BINARY);
        int putBINARY2Collision = documentStore.putDocument(inputStream2b,uri2,DocumentStore.DocumentFormat.BINARY);
        int putTXT1Collision = documentStore.putDocument(inputStream3b,uri3,DocumentStore.DocumentFormat.TXT);
        int putTXT2Collision = documentStore.putDocument(inputStream4b,uri4,DocumentStore.DocumentFormat.TXT);
        Document document1 = new DocumentImpl(uri1, string1.getBytes());
        Document document2 = new DocumentImpl(uri2, string2.getBytes());
        Document document3 = new DocumentImpl(uri3, string3);
        Document document4 = new DocumentImpl(uri4, string4);
        assertEquals(putBINARY1Collision,test1);
        assertEquals(putBINARY2Collision,test2);
        assertEquals(putTXT1Collision,test3);
        assertEquals(putTXT2Collision,test4);
    }
}