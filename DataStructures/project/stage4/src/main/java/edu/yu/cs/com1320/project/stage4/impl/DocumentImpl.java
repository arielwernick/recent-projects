package edu.yu.cs.com1320.project.stage4.impl;


import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage4.Document;


import java.net.URI;
import java.util.*;

import static java.lang.System.nanoTime;

public class DocumentImpl implements Document{
    URI uri;
    String txt;
    byte[] binaryData;
    Set<String> getWordsList = null;
    HashMap< String, Integer> wordMap= new HashMap<>();
    boolean isByte = false;
    Long lastTime;

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
        this.binaryData = null;
        this.getWordsList = wordMachine();


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
        isByte = true;
        this.getWordsList = wordMachine();
        isByte= true;
        this.txt = null;
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
        return this.getWordsList;
    }

    /**
     * return the last time this document was used, via put/get or via a search result
     * (for stage 4 of project)
     */

    public long getLastUseTime() {

        return lastTime;
    }


    public void setLastUseTime(long timeInNanoseconds) {
      long time = timeInNanoseconds;
      lastTime = time;

    }


    //method to create wordList and wordMachine
    private Set<String> wordMachine(){
        Set<String> wordsSet = new HashSet<>();
        if(isByte){return wordsSet;}

        //reformat the text for storage
        String paragraphs = formatForUse(this.txt);
        //divide paragraphs into words
        String[] wordsInDoc = paragraphs.split(" ");

        //generate wordsMap
        for(String word : wordsInDoc){
            if(!word.equals("")) {
                int count = 1;
                wordsSet.add(word);
                if (wordMap.containsKey(word)) {
                    count += wordMap.get(word);
                }
                wordMap.put(word, count);
            }
        }
        //return set of words used
        return wordsSet;
    }





    //reformatting method
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



    @Override
    public int compareTo(Document o) {
        if(this.getLastUseTime() > o.getLastUseTime()){
            return 1;
        }else if(this.getLastUseTime() < o.getLastUseTime()){
            return -1;
        }else {
            return 0;
        }
    }
}