package edu.yu.cs.com1320.project.stage3.impl;


import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage3.Document;
import org.codehaus.classworlds.ClassWorldReverseAdapter;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DocumentImpl implements Document {
    URI uri;
    String txt;
    byte[] binaryData;
    HashMap< String, Integer> wordMap= new HashMap<>();
    boolean isByte = false;

    //constructor for a Document of type txt
    public DocumentImpl(URI uri, String txt){
        if(txt == null || txt == ""){
            throw new IllegalArgumentException();
        }

        if(uri == null || uri.equals((URI.create("")))){
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.txt = txt;
        for(String word: getWords()){
            wordMap.put(word,wordCount(word));
        }

    }

    //constructor for type binaryData
    public DocumentImpl(URI uri , byte[] binaryData){
        byte[] emptyArray = new byte[0];

        //check for null or empty values & throw errors if found
        if(binaryData == null || binaryData.equals(emptyArray)){
            throw new IllegalArgumentException();
        }
        if(uri == null|| uri == (URI.create(""))){
            throw new IllegalArgumentException();
        }


        this.uri = uri;
        this.binaryData = binaryData;
        isByte= true;
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

    /**
     * how many times does the given word appear in the document?
     *
     * @param word
     * @return the number of times the given words appears in the document. If it's a binary document, return 0.
     */
    public int wordCount(String word) {
        word = formatForUse(word);
        if(isByte|| wordMap.get(word) ==null){
            return 0;
        }
        int counter = wordMap.get(word);
        return counter;
    }

    /**
     * @return all the words that appear in the document
     */
    public Set<String> getWords() {

        Set<String> wordsSet = new HashSet<>();
        if(isByte){return wordsSet;}
        String paragraphs = formatForUse(this.txt);
        String[] wordsInDoc = paragraphs.split(" ");
        for(String word : wordsInDoc){
            if(!word.equals("")) {
                System.out.println(word);

                int count = 1;
                wordsSet.add(word);
                if (wordMap.containsKey(word)) {
                    count += wordMap.get(word);
                }
                wordMap.put(word, count);
            }
        }
        return wordsSet;
    }


    public int compareTo(String word, Document m2) {
        int result;
        if(this.wordCount(word) > m2.wordCount(word)){
            result = 1;
        }else if(this.wordCount(word) < m2.wordCount(word)){
            result = -1;
        }else{
            result = 0;
        }
        return result;
    }

    private String formatForUse(String word){
        String paragraphs = word;
        paragraphs = paragraphs.toLowerCase();
        paragraphs = paragraphs.replaceAll("[^1-9a-z ]", "");
        return paragraphs;
    }

    //overide hashCode method
    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + (txt != null ? txt.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(binaryData);
        return result;
    }

    //create a override method for .equals where two documents are cosnidered equal if they share the same hashcode
    @Override
    public boolean equals(Object o){
        if( o == null){
            return true;
        }
        if(this.hashCode() == o.hashCode()){
            return true;
        }
        if(this.toString() == o.toString() && this.toString() == null){
            return true;
        }
        return false;
    }



}