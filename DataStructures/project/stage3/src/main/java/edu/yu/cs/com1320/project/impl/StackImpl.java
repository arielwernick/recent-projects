package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Stack;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.EmptyStackException;
import java.util.function.Function;


public class StackImpl<T> implements Stack<T> {

    private class Node <Key,Value>{
        Key key;
        Value data;
        Node<Key, Value> next;

        private Node(Value action) {
            this.data = action;
            this.next = null;
        }
    }






    Node head;
    Node temp;
    int stackSize;

    public void push(T element) {
        if(head == null) {
            head = new Node(element);
        }else{
            temp = head;
            head = new Node(element);
            head.next = temp;
        }
        stackSize +=1;

    }


    public T pop() {
        if(head == null){
            return null;
        }
        temp = head;
        if(head.next == null){
            head = null;
            stackSize -=1;
            return (T) temp.data;
        }

        head = head.next;
        stackSize -=1;
        return (T)temp.data;
    }


    public T peek() {
        if(head ==null){
            return null;
        }
        return (T)head.data;
    }


    public int size() {
        return stackSize;
    }
}
