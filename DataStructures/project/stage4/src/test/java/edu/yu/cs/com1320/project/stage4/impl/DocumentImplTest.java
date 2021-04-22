package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

import edu.yu.cs.com1320.project.Utils;

public class DocumentImplTest {
    private URI textUri;
    private String textString;
    private URI textUri2;
    private String textString2;

    private URI binaryUri;
    private byte[] binaryData;

    @BeforeEach
    public void setUp() throws Exception {
        this.textUri = new URI("http://edu.yu.cs/com1320/txt");
        this.textString = "This is text content. Lots of it.";

        this.binaryUri = new URI("http://edu.yu.cs/com1320/binary");
        this.binaryData = "This is a PDF, brought to you by Adobe.".getBytes();

        this.textUri2 = new URI("http://edu.yu.cs/com1320/txttwo");
        this.textString2 = "this is the text of text two that I am seeing.";

    }

    @Test
    public void stage3WordCount() {
        DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString);
        assertEquals(1, textDocument.wordCount("This"));
        assertEquals(0, textDocument.wordCount("blah"));
    }

    @Test
    public void stage3CaseInsensitiveWordCount() {
        DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString);
        assertEquals(1, textDocument.wordCount("this"));
        assertEquals(1, textDocument.wordCount("tHis"));
    }

    //stage 1 tests
    @Test
    public void testGetTextDocumentAsTxt() {
        DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString);
        assertEquals(this.textString, textDocument.getDocumentTxt());
    }

    @Test
    public void testGetDocumentBinaryData() {
        DocumentImpl binaryDocument = new DocumentImpl(this.binaryUri, this.binaryData);
        assertArrayEquals(this.binaryData,binaryDocument.getDocumentBinaryData());
    }

    @Test
    public void testGetTextDocumentTextHashCode() {
        DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString);
        int code = Utils.calculateHashCode(this.textUri, this.textString,null);
        assertEquals(code, textDocument.hashCode());
    }

    @Test
    public void testGetBinaryDocumentTextHashCode() {
        DocumentImpl binaryDocument = new DocumentImpl(this.binaryUri, this.binaryData);
        int code = Utils.calculateHashCode(this.binaryUri, null, this.binaryData);
        assertEquals(code, binaryDocument.hashCode());
    }

    @Test
    public void testGetTextDocumentKey() {
        DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString);
        assertEquals(this.textUri, textDocument.getKey());
    }

    @Test
    public void testGetBinaryDocumentKey() {
        DocumentImpl binaryDocument = new DocumentImpl(this.binaryUri, this.binaryData);
        assertEquals(this.binaryUri, binaryDocument.getKey());
    }

    @Test
    public void testCompareTo(){
        DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString);
        DocumentImpl textDocument2 = new DocumentImpl(this.textUri2,this.textString2);
        textDocument.setLastUseTime(50);
        textDocument2.setLastUseTime(100);
        System.out.println(textDocument.compareTo(textDocument2));
        MinHeapImpl docHeap = new MinHeapImpl();
        docHeap.insert(textDocument);
        docHeap.insert(textDocument2);
        textDocument.setLastUseTime(1000);
        docHeap.reHeapify(textDocument);
        assertEquals(docHeap.remove(),textDocument2);

    }
}