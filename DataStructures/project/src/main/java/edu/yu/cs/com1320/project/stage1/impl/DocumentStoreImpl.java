package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.stage1.DocumentStore;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;



public class DocumentStoreImpl implements DocumentStore {



    //create a hashTable object to store all the documents
    HashTable docTable = new HashTableImpl();

    /**
     * @param input  the document being put
     * @param uri    unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the previous doc. If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     */
    public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException {
        //create int variable to save the return variable.
        int returnValue;
       //if the uri is not already in the system return 0
        if (docTable.get(uri) == null) {
            returnValue = 0;
        } else {
            //if the uri is already in use save returnValue as the hashcode of the previous item
            returnValue = docTable.get(uri).hashCode();
        }

        //read the input from the input stream into a byte array
        byte[] inputRead = input.readAllBytes();

        //if the value of the input stream is null, delete the document
        if (inputRead == null) {
            deleteDocument(uri);
        }

        //document creation for a txt type document
        if (format.equals(DocumentFormat.TXT)) {
            //System.out.println("txt");
            String txt = new String(inputRead);
            if(txt ==null || uri == null){
                throw new IllegalArgumentException("the txt is blank");
            }
            //create a document of type txt
            Document doc = new DocumentImpl(uri, txt);
            //place in the hashTable
            docTable.put(uri, doc);
            return returnValue;

        }

        //document creation for a byte type document
        if (format.equals(DocumentFormat.BINARY)) {
            //create a document of type byte
           // System.out.println("byte");
           // if(inputRead ==null || uri == null){
            //    throw new IllegalArgumentException();
           // }
            Document doc = new DocumentImpl(uri, inputRead);
            docTable.put(uri, doc);
            return returnValue;
        }

        return 0;
    }

    //retrieving a document from the
    public Document getDocument(URI uri) {
        Document get = (Document) docTable.get(uri);

        return get;
    }

    //
    public boolean deleteDocument(URI uri) {
        if (docTable.get(uri) == null) {
            return false;
        }
        docTable.put(uri,null);
        return true;
    }




}
