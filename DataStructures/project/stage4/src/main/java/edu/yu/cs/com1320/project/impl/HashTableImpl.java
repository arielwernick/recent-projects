package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;



public class HashTableImpl<Key, Value> implements HashTable<Key, Value> {


    private int size = 5;
    private int loadFactor;
    //create node subclass for creation of hashTable
    //
    class Node <Key,Value>{
        Key k;
        Value data;
        Node<Key, Value> next;



        private Node(Key k, Value data) {
            if(k == null){
                throw new IllegalArgumentException();
            }
            this.k = k;
            this.data = data;
            this.next = null;
        }
    }


    private Node<?, ?>[] table;



    public HashTableImpl () {
        this.table = new Node[size];
         

        }


    ///**
    //     * @param k the key whose value should be returned
    //     * @return the value that is stored in the HashTable for k, or null if there is no such key in the table
    //     */
    public Value get(Key k) {
       //replace with override of Hashcode
    int slot = (k.hashCode() & 0x7fffffff) % table.length;
    Node current = this.table[slot];

       if(current != null) {
           if (current.k.equals(k)) {
              // System.out.println("this is k:" + k);
               return (Value) current.data;
           }
           while (current.next != null) {
               current = (Node) current.next;
               if (current.k.equals(k)) {
                   return (Value) current.data;
               }
           }
       }

        return null;
    }


    /**
     * @param k the key at which to store the value
     * @param v the value to store.
     * To delete an entry, put a null value.
     * @return if the key was already present in the HashTable, return the previous value stored for the key. If the key was not already present, return null.
     */
    public Value put(Key k, Value v) {
        //hash the key to find the slot in the hashTable and store in variable slot.
        //System.out.println("THIS IS THE LOAD FACTRO: "+loadFactor);
       // System.out.println("this is the table size:  "+ table.length);
        if(loadFactor >= table.length *.75){
            reSizeHashTable();
        }
        int slot = (k.hashCode() & 0x7fffffff) % table.length;

        Node previous;
        Node temp;
        //if the Value for K was null, delete the item from the hashTable.
        //return to fix - not sure it is working correctly
        if (v == null) {

            loadFactor -= 1;
           // System.out.println("Slot to be palced in "+ slot);
           // System.out.println("Slot hashCode" + this.table[slot].k);
            if (this.table[slot].k.equals(k)) {
                this.table[slot] = this.table[slot].next;
                return null;
            }



            previous = this.table[slot];
            if (previous != null) {
                while (previous.next != null) {
                    if (previous.next.equals(k)) {
                        previous.next = previous.next.next;
                        return null;
                    }
                    previous = previous.next;
                }
            }
            }

        //create new Node with the k,v pair provided
        Node<Key, Value> newNode = new Node<>(k, v);
            //if slot was empty create first node in the slot
            if (this.table[slot] == null) {
                this.table[slot] = newNode;
                loadFactor +=1;
                //since the item has no previous item at slot, return null
                return null;
            }

            //overwriting a uri in the same slot
            if (this.table[slot].k == k) {
                temp = this.table[slot];
                this.table[slot] = newNode;
                newNode.next = temp.next;
                return (Value) temp.data;
            }

            //if slot already had a value, iterate to first null slot within the linked list and insert Node
            // System.out.println("K was full, we are placing in the next slot in the link list " + slot);
            previous = this.table[slot];
            while (previous.next != null) {
                if (previous.next.k == k) {
                    temp = previous.next;
                    previous.next = newNode;
                    newNode.next = temp.next;
                    return (Value) temp.data;
                }
                previous = previous.next;
            }

            previous.next = newNode;
            return null;
        }


    private void reSizeHashTable(){
        int previousSize = this.table.length;
        Node[] doubled =  new Node[previousSize*2];

        for(int i = 0; i < previousSize; i++) {
            if (this.table[i] != null) {
                int slot = (this.table[i].k.hashCode() & 0x7fffffff) % doubled.length;
                doubled[slot] = this.table[i];
            }
        }
            this.table = doubled;
            size = doubled.length;
    }

    private Value deleteFromHashTable(int slot, Key k) {
        // System.out.println("Slot hashCode" + this.table[slot].k);
        if (this.table[slot].k.equals(k)) {
            this.table[slot] = this.table[slot].next;
            return null;
        }


        Node previous = this.table[slot];
        if (previous != null) {
            while (previous.next != null) {
                if (previous.next.equals(k)) {
                    previous.next = previous.next.next;
                    return null;
                }
            }
        }
        return null;
    }
}
