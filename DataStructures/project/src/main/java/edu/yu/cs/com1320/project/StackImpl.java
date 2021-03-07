package edu.yu.cs.com1320.project;

import edu.yu.cs.com1320.project.impl.HashTableImpl;

import java.util.EmptyStackException;

public class StackImpl<T> implements Stack<T> {

    private class Node <Key,Value>{
        Key k;
        Value data;
        Node<Key, Value> next;

        private Node( Value data) {

            this.data = data;
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
        System.out.println(jim.size());
        jim.pop();
        System.out.println(jim.size());

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
            throw new EmptyStackException();
        }
        temp = head;
        if(head.next == null){
            head = null;
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
