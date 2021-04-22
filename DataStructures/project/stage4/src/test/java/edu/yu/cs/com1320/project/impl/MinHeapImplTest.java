package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.impl.DocumentImpl;
import org.junit.jupiter.api.Test;

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


}