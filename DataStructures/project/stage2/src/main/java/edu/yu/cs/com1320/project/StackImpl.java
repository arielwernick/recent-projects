package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.Stack;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.EmptyStackException;
import java.util.function.Function;


public class StackImpl<T> implements Stack<T> {

    private class Node <Key,Value>{
        Key k;
        Value data;
        Node<Key, Value> next;

        private Node(Value action) {
            this.data = action;
            this.next = null;
        }
    }

    public static void main(String[] args) {
        Stack<Integer> jim = new StackImpl<>();
        jim.push(5);
        jim.push(10);
        for(int i = 1; i<=10; i++){
            jim.push(i);
        }
        System.out.println(jim.peek());
        System.out.println("Stack Size: " +jim.size());
        jim.pop();
        System.out.println(jim.size());
        String fire = "fire";


    }

    public static boolean compute(int x, int y){
        return true;
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
            throw new IllegalStateException();
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
        return (T)head.data;
    }


    public int size() {
        return stackSize;
    }
}
