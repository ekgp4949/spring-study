package com.eomcs.algorithm.data_structure.linkedlist;

import java.lang.reflect.Array;

public class LinkedList<E> {

  Node<E> first;

  Node<E> last;

  int size;

  public void add(E value) {
    Node<E> newNode = new Node<>();
    newNode.value = value;

    if (first == null) {
      last = first = newNode;
    } else {
      last.next = newNode;
      last = newNode;
    }

    this.size++;
  }

  public void add(int index, E value) {
    if (index < 0 || index >= size) {
      return;
    }

    Node<E> newNode = new Node<>();
    newNode.value = value;

    Node<E> cursor = first;
    for (int i = 0; i < index - 1; i++) {
      cursor = cursor.next;
    }

    if (index == 0) {
      newNode.next = cursor;
      first = newNode;
    } else {
      newNode.next = cursor.next;
      cursor.next = newNode;
    }

    this.size++;
  }
  
  public E remove(int index) {
    if (index < 0 || index >= size) {
      return null;
    }
    
    Node<E> cursor = first;
    for (int i = 0; i < index - 1; i++) {
      cursor = cursor.next;
    }

    Node<E> deletedNode = null;
    if (index == 0) {
      deletedNode = first;
      first = deletedNode.next;
    } else {
      deletedNode = cursor.next;
      cursor.next = deletedNode.next;
    }
    deletedNode.next = null;
    
    this.size--;
    return deletedNode.value;
  }

  // get() 추가;
  public E get(int index) {
    if (index < 0 || index >= size) {
      return null;
    }

    Node<E> cursor = first;
    for (int i = 0; i < index; i++) {
      cursor = cursor.next;
    }

    return cursor.value;
  }
  
  public E set(int index, E value) {
    if (index < 0 || index >= size) {
      return null;
    }
    
    Node<E> cursor = first;
    for (int i = 0; i < index; i++) {
      cursor = cursor.next;
    }
    E oldValue = cursor.value;
    cursor.value = value;
    
    return oldValue;
  }
  
  public Object[] toArray() {
    Object[] arr = new Object[this.size];
    Node<E> cursor = first;
    
    for (int i = 0; i < this.size; i++) {
      arr[i] = cursor.value;
      cursor = cursor.next;
    }
    return arr;
  }
  
  @SuppressWarnings("unchecked")
  public E[] toArray(E[] arr) {
    
    if(arr.length < this.size) {
      arr = (E[]) Array.newInstance(arr.getClass().getComponentType(), this.size);
    }
    
    Node<E> cursor = first;
    
    for (int i = 0; i < this.size; i++) {
      arr[i] = cursor.value;
      cursor = cursor.next;
    }
    return (E[]) arr;
  }
  
  public int size() {
    return this.size;
  }

  static class Node<T> {
    T value;
    Node<T> next;
  }
}
