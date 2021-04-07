package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;

import java.util.*;

public class TrieImpl<Value> implements Trie<Value> {

    private static final int alphabetSize = 36;
    private Node root;
    int depth;
    Node builder = new Node();
    Node nonNullList = new Node();
    final List<Value> emptyList = Collections.emptyList();

    private static class Node<Value>
    {
        private List<Value> val = new ArrayList<>();
        private Node[] links = new Node[alphabetSize];

    }



    /**
     * add the given value at the given key
     *
     */

    public TrieImpl(){
        root = new Node();
        builder = root;
    }
    public void put(String key, Value val) {
        if(key == null){
            throw new IllegalArgumentException();
        }
        depth += 0;

        key = formatForUse(key);



        //we've reached the last node in the key,
        //set the value for the key and return the node
        if (depth == key.length())
        {
            builder.val.add(val);
            depth = 0;
            builder = root;

        }else {
            //proceed to the next node in the chain of nodes that
            //forms the desired key
            char c = key.charAt(depth);
            int slot;
            depth += 1;
            if(c>=96){slot = c-96;}else{
                slot = c-21;
            }

            if(builder.links[c-96] ==null || builder.links.length == 1) {
                builder.links[slot] = new Node();
            }
            builder = builder.links[slot];
            this.put(key, val);
        }
    }

    private String formatForUse(String word){
        String paragraphs = word;
        paragraphs = paragraphs.toLowerCase();
        paragraphs = paragraphs.replaceAll("[^1-9a-z ]", "");
        return paragraphs;
    }
    /**
     * get all exact matches for the given key, sorted in descending order.
     * Search is CASE INSENSITIVE.
     *
     * @param key
     * @param comparator used to sort  values
     * @return a List of matching Values, in descending order
     */
    public List<Value> getAllSorted(String key, Comparator<Value> comparator) {
        if( root == null){
            return emptyList;
        }
        if(comparator == null || key ==null){
            throw new IllegalArgumentException();
        }

       builder = getTheBuilder(key);
        Node found = builder;
        if(found.val == null){
            return emptyList;
        }
        builder = null;

        List<Value> returnValues = found.val;

        returnValues.sort(comparator);
        return returnValues;
    }

    private Node<Value> getTheBuilder(String key){

        if(depth == 0){
            builder = root;
            key = formatForUse(key);
        }
        if(depth == key.length()){
            depth = 0;
            if (builder == null) {
                nonNullList.val = emptyList;
                nonNullList.links= new Node[1];
                builder = nonNullList;
            }
            return builder;
        }else {
            char c = key.charAt(depth);
            int slot;
            depth += 1;
            if (c >= 96) {
                slot = c - 96;
            } else {
                slot = c - 21;
            }
            if (builder == null || builder.links.length == 1) {
                builder.val = emptyList;
                builder.links = new Node[1];
                return builder;
            }
            builder = builder.links[slot];
            getTheBuilder(key);
        }
            depth = 0;
            return builder;


    }

    private Node<Value> deleteTheParent(String key){
        Node parent = null;
        key = formatForUse(key);
        if(depth == 0){
            parent = root;
        }
        if(depth == key.length()-1){
            depth = 0;
            if (parent == null) {
                nonNullList.val = emptyList;
                nonNullList.links= new Node[1];
                parent = nonNullList;
            }
            return parent;
        }else {
            char c = key.charAt(depth);
            int slot;
            depth += 1;
            if (c >= 96) {
                slot = c - 96;
            } else {
                slot = c - 21;
            }
            if (parent == null) {
                parent.val = emptyList;
                parent.links = new Node[1];
                return parent;
            }
            parent = parent.links[slot];
            deleteTheParent(key);
        }
        depth = 0;
        return parent;

    }


    /**
     * get all matches which contain a String with the given prefix, sorted in descending order.
     * For example, if the key is "Too", you would return any value that contains "Tool", "Too", "Tooth", "Toodle", etc.
     * Search is CASE INSENSITIVE.
     *
     * @param prefix
     * @param comparator used to sort values
     * @return a List of all matching Values containing the given prefix, in descending order
     */
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
        if( root == null){
            return emptyList;
        }
        if(comparator == null || prefix ==null){
            throw new IllegalArgumentException();
        }
        List<Value> allThePrefix = new ArrayList<>();
        builder = getTheBuilder(prefix);
        if(builder.links.length == 1 || builder.links == null){
            return emptyList;
        }
        allThePrefix = recursiveListAdder(builder,allThePrefix);
        allThePrefix.sort(comparator);
        builder = root;
        return allThePrefix;

    }

    private List<Value> recursiveListAdder(Node builder, List<Value> allThePrefix){
       if(builder == null){
           return emptyList;
       }
        if(allThePrefix.isEmpty()){
            allThePrefix.addAll(builder.val);

        }
        for(Node filled: builder.links){
            if(filled != null){
                allThePrefix.addAll(filled.val);
                recursiveListAdder(filled,allThePrefix);
            }
        }
        return allThePrefix;
    }

    /**
     * Delete the subtree rooted at the last character of the prefix.
     * Search is CASE INSENSITIVE.
     *
     * @param prefix
     * @return a Set of all Values that were deleted.
     */
    public Set<Value> deleteAllWithPrefix(String prefix) {
        if(prefix == null){
            throw new IllegalArgumentException();
        }
        Node toDelete = getTheBuilder(prefix);
        List<Value> deleted = new ArrayList<>();
        deleted = recursiveListAdder(toDelete,deleted);
        if(toDelete != null) {
            toDelete.links = new Node[1];
        }
        Set<Value> deletedSet = new HashSet<>();
        deletedSet.addAll(deleted);
        return deletedSet;
    }

    /**
     * Delete all values from the node of the given key (do not remove the values from other nodes in the Trie)
     *
     * @param key
     * @return a Set of all Values that were deleted.
     */
    public Set<Value> deleteAll(String key) {
        if(key == null){
            throw new IllegalArgumentException();
        }
        Node toDelete = getTheBuilder(key);
        Set<Value> deletedSet = new HashSet<>();
        if(toDelete.val == null){
            return deletedSet;
        }
        deletedSet.addAll(toDelete.val);
        toDelete.val = null;
        Node romba = new Node();
        if(checkIfNodeEmpty(toDelete)){
            //money for it
            int level = 1;
            boolean cleanedUp = false;
            int i = 0;

            while(key.length()>i){
                if(key.length()>=i) {
                    level = key.length() - i;
                }
                romba = getTheBuilder(key.substring(0,level));
                if(romba.val == emptyList){
                    cleanedUp = true;

                }
                i++;
            }
            if(cleanedUp) {
                romba = null;
            }
        }
        return deletedSet;
    }

    private boolean checkIfNodeEmpty(Node checker){
        int i = 0;
        for(Node node: checker.links){
            if(node != null ){
                i++;
            }
        }
        if( i != 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Remove the given value from the node of the given key (do not remove the value from other nodes in the Trie)
     *
     * @param key
     * @param val
     * @return the value which was deleted. If the key did not contain the given value, return null.
     */
    public Value delete(String key, Value val) {
        if(key == null || val == null){
            throw new IllegalArgumentException();
        }
        Node toDelete = getTheBuilder(key);
        for(Object deleter : toDelete.val){
            if(val.equals(deleter)){
                toDelete.val.remove(deleter);
                return val;
            }

        }
        return null;
    }
}
