package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;



public class DocumentStoreImpl implements DocumentStore {

    //create a hashTable object to store all the documents
    HashTable docTable = new HashTableImpl();
    private Stack history = new StackImpl();

    /**
     * @param input  the document being put
     * @param uri    unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the previous doc. If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     */
    public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException {
        //create int variable to save the return variable.
        int returnValue = 0;
        Document doc = null;
        if(uri == null){
            throw new IllegalArgumentException();
        }

        if (docTable.get(uri) != null){
            //if the uri is already in use save returnValue as the hashcode of the previous item
            returnValue = docTable.get(uri).hashCode();
            byte[] inputRead = input.readAllBytes();
            Document holder = (Document)docTable.get(uri);
            deleteForUndo(uri);
            if(format.equals(DocumentFormat.TXT)){
                createTextDocument(uri,inputRead);
            }
            if(format.equals(DocumentFormat.BINARY)){
                createByteDocument(uri,inputRead);
            }
            docTable.put(uri,doc);
            Command command = new Command(uri,uri1 ->overwrite(uri,holder));
            command.undo();
            return returnValue;

        }

        //read the input from the input stream into a byte array
            if (input == null){
                throw new IllegalArgumentException();
            }
            byte[] inputRead = input.readAllBytes();

        //if the value of the input stream is null, delete the document
        if (inputRead == null) {
            deleteDocument(uri);
        }

        //document creation for a txt type document
        if(format == null){
            throw new IllegalArgumentException();
        }
        if (format.equals(DocumentFormat.TXT)) {
           doc =  createTextDocument(uri,inputRead);
        }



        //document creation for a byte type document
        if (format.equals(DocumentFormat.BINARY)) {
           doc =  createByteDocument(uri,inputRead);
        }

        docTable.put(uri, doc);

        Command command = new Command(uri,uri1 ->deleteForUndo(uri));
        history.push(command);
        System.out.println(history.peek());
        return returnValue;


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
        docTable.put(uri, doc);
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
        Command command = new Command(uri,uri1 ->putForUndo(uri,previousDoc));
        history.push(command);
        return true;
    }

    public boolean deleteForUndo(URI uri) {
        docTable.put(uri, null);
        return true;
    }

    public boolean putForUndo(URI uri, Document doc)  {

        docTable.put(uri,doc);

        return false;
    }

    private boolean overwrite(URI uri, Document doc) {
        deleteForUndo(uri);
        putForUndo(uri, doc);
        return true;
    }


    @Override
    public void undo() throws IllegalStateException {

            Command five = (Command) history.pop();
            five.undo();


    }

    @Override
    public void undo(URI uri) throws IllegalStateException {
        Stack temp = new StackImpl();
        boolean found = false;
        int tracker =0;
        while(found == false){
            Command check = (Command) history.pop();
            if(check.getUri()== uri){
                check.undo();
                found = true;
            }
            temp.push(check);
            tracker += 1;

        }
        while(tracker > 0){
            Command putBack = (Command) temp.pop();
            history.push(putBack);
            tracker -= 1;
        }
    }


}
