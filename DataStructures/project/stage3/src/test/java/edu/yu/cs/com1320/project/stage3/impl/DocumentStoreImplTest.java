package edu.yu.cs.com1320.project.stage3.impl;

import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class DocumentStoreImplTest {
    private URI uri1;
    private String txt1;

    //variables to hold possible values for doc2
    private URI uri2;
    private String txt2;

    //variables to hold possible values for doc2
    private URI uri3;
    private String txt3;

    //variables to hold possible values for doc2
    private URI uri4;
    private String txt4;

    @BeforeEach
    public void init() throws Exception {
        //init possible values for doc1
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";

        //init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Text for doc2. A plain old String.";

        //init possible values for doc1
        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "This is the text of doc3 - doc doc goose";

        //init possible values for doc2
        this.uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        this.txt4 = "doc4: how much wood would a woodchuck chuck...";
    }
    private DocumentStoreImpl createStoreAndPutOne() throws IOException {
        DocumentStoreImpl dsi = new DocumentStoreImpl();
        ByteArrayInputStream bas1 = new ByteArrayInputStream(this.txt1.getBytes());
        dsi.putDocument(bas1,this.uri1, DocumentStore.DocumentFormat.TXT);
        return dsi;
    }
    @Test
    void putDocument() throws IOException {
        DocumentStoreImpl dsi = createStoreAndPutOne();
        //undo after putting only one doc
        Document doc1 = new DocumentImpl(this.uri1, this.txt1);
        Document returned1 = dsi.getDocument(this.uri1);
        assertNotNull(returned1,"Did not get a document back after putting it in");
        assertEquals(doc1.getKey(),returned1.getKey(),"Did not get doc1 back");

    }

    @Test
    void getDocument() {
    }

    @Test
    void deleteDocument() {
    }

    @Test
    void undo() {
    }

    @Test
    void testUndo() {
    }

    @Test
    void search() {
    }

    @Test
    void searchByPrefix() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void deleteAllWithPrefix() {
    }
}