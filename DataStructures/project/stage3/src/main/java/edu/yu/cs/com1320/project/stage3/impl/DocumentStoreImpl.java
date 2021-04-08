package edu.yu.cs.com1320.project.stage3.impl;

import edu.yu.cs.com1320.project.*;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DocumentStoreImpl implements DocumentStore {

    //create a hashTable object to store all the documents
    HashTable docTable = new HashTableImpl();
    private Stack history = new StackImpl<Undoable>();
    Trie<Document> trie = new TrieImpl<Document>();

    /**
     * @param input  the document being put
     * @param uri    unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the previous doc. If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     */


    public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException {
        //create int variable to save the return variable.
        int returnValue = 0;
        Document doc;
        if(uri == null){
            throw new IllegalArgumentException();
        }

        if (docTable.get(uri) != null){
            //if the uri is already in use save returnValue as the hashcode of the previous item
            returnValue = docTable.get(uri).hashCode();
            byte[] inputRead = input.readAllBytes();
            Document holder = (Document)docTable.get(uri);
            deleteForUndo(uri);
            //System.out.println("IF the URI is deleted this should say null: " + getDocument(uri));
            doc = docInitializer(inputRead, format, uri);
            putForUndo(uri,doc);
            GenericCommand command = new GenericCommand(uri,uri1 ->overwrite(uri,holder));
            history.push(command);
            return returnValue;

        }

        //read the input from the input stream into a byte array
        if (input == null){
            if(docTable.get(uri) == null){
                returnValue = 0;
                return returnValue;
            }
            returnValue = docTable.get(uri).hashCode();

            docTable.put(uri,null);

            return returnValue;
        }
        byte[] inputRead = input.readAllBytes();

        //if the value of the input stream is null, delete the document
        if (inputRead == null) {
            deleteDocument(uri);
        }

        doc  = docInitializer(inputRead,format,uri);

        docTable.put(uri, doc);
        for(String word : doc.getWords()){
            trie.put(word,doc);
        }

        GenericCommand command = new GenericCommand(uri,uri1 ->deleteForUndo(uri));
        history.push(command);
        return returnValue;


    }

    private Document docInitializer(byte[] inputRead, DocumentFormat format, URI uri){
       Document doc;
        if (format.equals(DocumentFormat.TXT)) {
            doc =  createTextDocument(uri,inputRead);
        }else if(format.equals(DocumentFormat.BINARY)) {
            doc =  createByteDocument(uri,inputRead);
        }else{
            throw new IllegalArgumentException();
        }
        return doc;
    }

    private Document createTextDocument(URI uri, byte[] inputRead) {

        String txt = new String(inputRead);
        if (txt == null || uri == null) {
            throw new IllegalArgumentException("the txt is blank");
        }
        Document doc = new DocumentImpl(uri, txt);
        return doc;
    }


    private Document createByteDocument(URI uri, byte[] inputRead){
        Document doc = new DocumentImpl(uri, inputRead);
        return doc;
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
        Document previousDoc = (Document) docTable.get(uri);

        docTable.put(uri,null);
        deleteDocTrieReferences(previousDoc);
        GenericCommand command = new GenericCommand(uri,uri1 ->putForUndo(uri,previousDoc));
        history.push(command);
        return true;
    }

    private void deleteDocTrieReferences(Document previousDoc){
        Set<String> docsWords = previousDoc.getWords();
        for(String toDelete : docsWords){
            trie.delete(toDelete, previousDoc);
        }
    }

    private boolean deleteForUndo(URI uri) {
        Document previousDoc = (Document) docTable.get(uri);
        deleteDocTrieReferences(previousDoc);
        docTable.put(uri, null);
        return true;
    }

    private boolean putForUndo(URI uri, Document doc)  {

        docTable.put(uri,doc);
        for(String word : doc.getWords()){
            trie.put(word,doc);
        }

        return true;
    }

    private boolean overwrite(URI uri, Document doc) {
        deleteForUndo(uri);
        putForUndo(uri, doc);
        return true;
    }



    public void undo() throws IllegalStateException {

        GenericCommand five = (GenericCommand) history.pop();
        if(five == null){
            throw new IllegalStateException();
        }
        five.undo();


    }


    public void undo(URI uri) throws IllegalStateException {
        Stack temp = new StackImpl();
        boolean found = false;
        int tracker =0;
        while(found == false || history.pop() == null){
            GenericCommand check = (GenericCommand) history.pop();
            if(check == null){
                throw new IllegalStateException();
            }
            if(check.getTarget()== uri){
                check.undo();
                found = true;
            }
            temp.push(check);
            tracker += 1;

        }
        while(tracker >= 0){
            GenericCommand putBack = (GenericCommand) temp.pop();
            history.push(putBack);
            tracker -= 1;
        }
    }

    /**
     * Retrieve all documents whose text contains the given keyword.
     * Documents are returned in sorted, descending order, sorted by the number of times the keyword appears in the document.
     * Search is CASE INSENSITIVE.
     *
     * @param keyword
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<Document> search(String keyword) {
       return null;
    }

    /**
     * Retrieve all documents whose text starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE INSENSITIVE.
     *
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<Document> searchByPrefix(String keywordPrefix) {

        Comparator<Document> comparator = (o1, o2) -> {
            int result = o1.compareTo(keywordPrefix, o2);
            return result;
        };

        List<Document> containsPrefix = trie.getAllWithPrefixSorted(keywordPrefix, comparator);
        return containsPrefix;

    }

    /**
     * Completely remove any trace of any document which contains the given keyword
     *
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */

    public Set<URI> deleteAll(String keyword) {
       Set<Document> toDelete =  trie.deleteAll(keyword);
       Set<URI> deleted = new HashSet<>();
       for(Document doc : toDelete){
           docTable.put(doc.getKey(),null);
           deleted.add(doc.getKey());
       }


        return deleted;
    }

    /**
     * Completely remove any trace of any document which contains a word that has the given prefix
     * Search is CASE INSENSITIVE.
     *
     * @param keywordPrefix
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAllWithPrefix(String keywordPrefix) {

        Set<Document> toDelete = trie.deleteAllWithPrefix(keywordPrefix);
        Set<URI> deleted = new HashSet<>();
        for(Document doc: toDelete){
            docTable.put(doc.getKey(),null);
            deleted.add(doc.getKey());
        }
        return deleted;
    }


}