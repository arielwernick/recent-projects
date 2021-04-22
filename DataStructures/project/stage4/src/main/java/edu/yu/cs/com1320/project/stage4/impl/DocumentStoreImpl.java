package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.*;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.DocumentStore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;



public class DocumentStoreImpl implements DocumentStore {

    //create a hashTable object to store all the documents
    HashTable docTable = new HashTableImpl();
    private Stack history = new StackImpl<Undoable>();
    Trie<Document> trie = new TrieImpl<Document>();
    MinHeap<Document> docHeap = new MinHeapImpl<Document>();
    int maxDocumentCount;
    int maxDocBytesCount;
    Set<URI> keyRing = new HashSet<>();

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
            returnValue = putOverwrite(input,uri,format);
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
        if(!checkForByteSpace()) {
            //insert for document deletion
            Document toDelete = docHeap.remove();
            deleteDocument(toDelete.getKey());
        }
        if(!checkForSpace()){
            Document toDelete = docHeap.remove();
            docHeap.insert(toDelete);
            deleteDocument(toDelete.getKey());
        }
        doc.setLastUseTime(System.nanoTime());

        docTable.put(uri, doc);
        docHeap.insert(doc);
        docHeap.reHeapify(doc);
        keyRing.add(uri);


        for(String word : doc.getWords()){
            trie.put(word,doc);
        }
        GenericCommand command = new GenericCommand(uri,uri1 ->deleteForUndo(uri));
        history.push(command);

        return returnValue;

    }

    private int putOverwrite(InputStream input , URI uri, DocumentFormat format) throws IOException {
        //if the uri is already in use save returnValue as the hashcode of the previous item
       int  returnValue = docTable.get(uri).hashCode();
        byte[] inputRead = input.readAllBytes();
        Document holder = (Document)docTable.get(uri);
        deleteForUndo(uri);
        //System.out.println("IF the URI is deleted this should say null: " + getDocument(uri));
        Document doc = docInitializer(inputRead, format, uri);
        putForUndo(uri,doc);
        GenericCommand command = new GenericCommand(uri,uri1 ->overwrite(uri,holder));
        history.push(command);
        doc.setLastUseTime(System.nanoTime());
        docHeap.reHeapify(doc);
        return returnValue;
    }

    private Document docInitializer(byte[] inputRead, DocumentFormat format, URI uri){
       Document doc;
        if(format == null) {
            throw new IllegalArgumentException();
        } else if (format.equals(DocumentFormat.TXT)) {
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
        //set the time on the previous doc to be a really low time
        previousDoc.setLastUseTime((1));
        docHeap.reHeapify(previousDoc);
        docHeap.remove();
        docTable.put(uri,null);
        deleteDocTrieReferences(previousDoc);
        GenericCommand command = new GenericCommand(uri,uri1 ->putForUndo(uri,previousDoc));
        history.push(command);
        return true;
    }

    private void deleteDocTrieReferences(Document previousDoc){
        Set<String> docsWords = new HashSet<>();
        //System.out.println(previousDoc.getDocumentTxt());
        if(previousDoc != null) {


            if (previousDoc.getWords().isEmpty() == false) {
                docsWords = previousDoc.getWords();
            }
            for (String toDelete : docsWords) {
                trie.delete(toDelete, previousDoc);
            }
        }
    }

    private boolean deleteForUndo(URI uri) {
        Document previousDoc = (Document) docTable.get(uri);
        deleteDocTrieReferences(previousDoc);
        docTable.put(uri, null);
        return true;
    }

    private boolean putForUndo(URI uri, Document doc)  {
        if(!checkForByteSpace()) {
            //insert for document deletion
            Document toDelete = docHeap.remove();
            deleteDocument(toDelete.getKey());
        }
        if(!checkForSpace()){
            Document toDelete = docHeap.remove();
            deleteDocument(toDelete.getKey());
        }
        docTable.put(uri,doc);
        doc.setLastUseTime(System.nanoTime());
        docHeap.insert(doc);
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
        Set<GenericCommand> setCommands = new CommandSet();
        GenericCommand fiveCommand = new GenericCommand(null,null);
        Object five = history.peek();
        if(five instanceof CommandSet){
            setCommands = (CommandSet)history.pop();
            for(GenericCommand commander: setCommands){
                URI updateTime = (URI) commander.getTarget();
                getDocument(updateTime);
                commander.undo();
            }

        }else if(five instanceof GenericCommand){
            fiveCommand = (GenericCommand)history.pop();
            URI updateTime = (URI) fiveCommand.getTarget();
            getDocument(updateTime);
            fiveCommand.undo();
        }

        if(five == null){
            throw new IllegalStateException();
        }



    }


    public void undo(URI uri) throws IllegalStateException {
            Set<GenericCommand> setCommands = new CommandSet();
            GenericCommand fiveCommand = new GenericCommand(null, null);
            Stack temp = new StackImpl();
            boolean found = false;
            int tracker = 0;
            while (found == false) {

                Object five = history.peek();
                if (five instanceof CommandSet) {
                    setCommands = (CommandSet) history.pop();
                    for (GenericCommand commander : setCommands) {
                        if (commander == null) {
                            throw new IllegalStateException();
                        }
                        if (commander.getTarget() == uri) {

                            getDocument(uri);
                            commander.undo();
                            found = true;
                        } else {
                            temp.push(commander);
                            tracker += 1;
                        }
                    }

                }else if (five instanceof GenericCommand) {
                    fiveCommand = (GenericCommand) history.pop();

                    if (fiveCommand == null) {
                        throw new IllegalStateException();
                    }
                    if (fiveCommand.getTarget() == uri) {
                        getDocument(uri);
                        fiveCommand.undo();
                        found = true;
                    }else {
                        temp.push(fiveCommand);
                        tracker += 1;
                    }
                }else{
                    throw new IllegalStateException();
                }
            }
            while (tracker >= 0) {
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
        Comparator<Document> comparator = (o1, o2) -> {
            int result = compareAmountOfWords(keyword,o1,o2);
            return result;
        };

        List<Document> returnDocuments = trie.getAllSorted(keyword,comparator);
        Set<Document> toReturn = new HashSet<>();
        toReturn.addAll(returnDocuments);
        returnDocuments.clear();
        returnDocuments.addAll(toReturn);
        returnDocuments.sort(comparator);

       return returnDocuments;
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
            int result = compareAmountOfWords(keywordPrefix,o1, o2);
            return result;
        };

        List<Document> containsPrefix = trie.getAllWithPrefixSorted(keywordPrefix, comparator);
        Set<Document> setOfDocuments = new HashSet();
        setOfDocuments.addAll(containsPrefix);
        List<Document> listToReturn = new ArrayList<>();
        listToReturn.addAll(setOfDocuments);
        Comparator<Document> reSort = (o1, o2) -> {
            int result = compareToPrefix(o1,o2,containsPrefix,keywordPrefix);
            return result;
        };

        listToReturn.sort(reSort);
        return listToReturn;

    }
    private int compareAmountOfWords(String word,Document m1, Document m2) {
        int result;
        if(m1.wordCount(word) < m2.wordCount(word)){
            result = 1;
        }else if(m1.wordCount(word) > m2.wordCount(word)){
            result = -1;
        }else{
            result = 0;
        }
        return result;
    }


    private int compareToPrefix(Document first, Document second, List<Document> coPrefix, String keywordPrefix){
        if(prefixDocCount(first,coPrefix, keywordPrefix) < prefixDocCount(second,coPrefix, keywordPrefix)){
           return 1;
        }else if(prefixDocCount(first,coPrefix, keywordPrefix) > prefixDocCount(second,coPrefix,keywordPrefix)){
            return -1;
        }else{
            return 0;
        }
    }
    private int prefixDocCount(Document checker, List<Document> coPrefix, String keyWordPrefix){
       int count = 0;
        for(Document doc : coPrefix) {
            if (doc.getKey() == checker.getKey()) {

                for (String word : doc.getWords()) {
                    if(word.length() > keyWordPrefix.length()) {

                        if (word.subSequence(0, keyWordPrefix.length()).equals(keyWordPrefix)) {
                            count += doc.wordCount(word);
                        }
                    }
                }
            }
        }
        return count;
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
       CommandSet<GenericCommand> deletedAllCommand = new CommandSet();
       for(Document doc : toDelete){
           Document save = doc;
           docTable.put(doc.getKey(),null);
           deleted.add(doc.getKey());
           GenericCommand command = new GenericCommand(doc.getKey(),uri1 ->putForUndo(doc.getKey(),save));
           deletedAllCommand.addCommand(command);
       }
       history.push(deletedAllCommand);
      // for()


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
        CommandSet<GenericCommand> deletedAllCommand = new CommandSet();
        Set<Document> toDelete = trie.deleteAllWithPrefix(keywordPrefix);
        Set<URI> deleted = new HashSet<>();
        for(Document doc: toDelete){
            Document save = doc;
            docTable.put(doc.getKey(),null);
            deleted.add(doc.getKey());
            GenericCommand command = new GenericCommand(doc.getKey(),uri1 ->putForUndo(doc.getKey(),save));
            deletedAllCommand.addCommand(command);
        }
        return deleted;
    }

    /**
     * set maximum number of documents that may be stored
     *
     * @param limit
     */


    public void setMaxDocumentCount(int limit) {
        this.maxDocumentCount = limit;

    }

    private boolean checkForSpace(){
        int counter = 0;
        for(URI uri : keyRing){
            if(docTable.get(uri) != null) {
                counter++;
            }
        }
        if(counter >= maxDocumentCount){
            return false;
        }
        return true;
    }


    /**
     * set maximum number of bytes of memory that may be used by all the documents in memory combined
     *
     * @param limit
     */

    public void setMaxDocumentBytes(int limit) {

        this.maxDocBytesCount = limit;
    }

    private boolean checkForByteSpace(){
        int counter = 0;
        for(URI uri: keyRing){
            Document doc = (Document) docTable.get(uri);
            if(doc != null) {
                if (doc.getDocumentTxt() != null) {
                    String text = doc.getDocumentTxt();
                    counter += text.getBytes().length;
                } else if (doc.getDocumentBinaryData() != null) {
                    counter += doc.getDocumentBinaryData().length;
                }
            }
        }
        if(counter>=maxDocBytesCount){
            return false;
        }

        return true;

    }



}