package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.DocumentStore;
import edu.yu.cs.com1320.project.stage4.impl.DocumentImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


class MinHeapImplTest {
    MinHeapImpl<String> stringHeap = new MinHeapImpl<>();
    @Test
    public void testInsert() {//tests that the class only implements one interface and its the correct one
        stringHeap.insert("hello");
        stringHeap.insert("hello1");
        String he = "hello";
        String hi = "hello1";
        System.out.print(he.compareTo(hi));
        assertEquals(stringHeap.remove(),"hello");

    }

    @Test
    public void testDoubleArraySize() {
        for (int i = 0; i < 10; i++) {
            stringHeap.insert("string#: " + i);

        }
        for (int i = 0; i < 10; i++) {
            assertEquals(stringHeap.remove(), "string#: " + i);

        }
    }

    @Test
    public void getArrayIndexTest(){

        try { stringHeap.getArrayIndex("shut");

            fail("no such element exists");
        }catch(NoSuchElementException e){ }

        try { stringHeap.reHeapify("shut");

            fail("no such element exists");
        }catch(NoSuchElementException e){ }

    }


}