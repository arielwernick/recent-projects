package edu.yu.cs.com1320.project.impl;

import com.google.inject.internal.MoreTypes;
import edu.yu.cs.com1320.project.MinHeap;
import edu.yu.cs.com1320.project.stage4.Document;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.util.NoSuchElementException;

public class MinHeapImpl<E extends Comparable<E>> extends MinHeap<E> {


    public MinHeapImpl(){


        this.elements = (E[]) Array.newInstance(Comparable.class,5);
    }

    @Override
    public void reHeapify(E element) {
        int index = getArrayIndex(element);
        upHeap(index);
        downHeap(index);

    }

    @Override
    protected int getArrayIndex(E element) {

        for(int i = 0; i <= count; i++){

            if(this.elements[i] != null && this.elements[i].equals(element)){
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    protected void doubleArraySize() {
        int previousSize = this.elements.length;
        //not sure if this is the correct method for doing this
        E[]  doubled = (E[]) Array.newInstance(Comparable.class,previousSize *2);;

        for(int i = 0; i < previousSize; i++) {
            if (this.elements[i] != null) {
                doubled[i] = this.elements[i];
            }
        }
        this.elements = doubled;


    }
}
