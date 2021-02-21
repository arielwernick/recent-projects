package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.stage1.Document;

import java.net.URI;
import java.util.Arrays;

public class DocumentImpl implements Document {
    URI uri;
    String txt;
    byte[] binaryData = new byte[0];

   //constructor for a Document of type txt
    public DocumentImpl(URI uri, String txt){
        if(txt == null || txt == ""){
            throw new IllegalArgumentException();
        }

        if(uri == null){
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.txt = txt;

    }

    //constructor for type binaryData
    public DocumentImpl(URI uri , byte[] binaryData){
        byte[] emptyArray = new byte[0];

        //check for null or empty values & throw errors if found
        if(binaryData == null || binaryData.equals(emptyArray)){
            throw new IllegalArgumentException();
        }
        if(uri == null){
            throw new IllegalArgumentException();
        }

        this.uri = uri;
        this.binaryData = binaryData;
    }
    public String getDocumentTxt() {

        return this.txt;
    }

    public byte[] getDocumentBinaryData() {

        return this.binaryData;
    }

    public URI getKey() {

        return this.uri;
    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + (txt != null ? txt.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(binaryData);
        return result;
    }

       //create a override method for .equals where two documents are cosnidered equal if they share the same hashcode


}
