package com.biomedical.waste.demo.structures;

import java.util.NoSuchElementException;

/** Generic circular queue to manage the order of biomedical waste collection. */
public class CollectionQueue<T> {

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; this.next = null; }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    /** Adds a waste item to the rear of the collection queue. */
    public void enqueue(T item) {
        Node<T> node = new Node<>(item);
        if (front == null) {
            front = node;
            rear = node;
            rear.next = front;
        } else {
            rear.next = node;
            rear = node;
            rear.next = front;
        }
        size++;
    }

    /** Removes and returns the front waste item. Throws NoSuchElementException if empty. */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("CollectionQueue is empty");
        }
        T data = front.data;
        if (front == rear) {
            front = null;
            rear = null;
        } else {
            front = front.next;
            rear.next = front;
        }
        size--;
        return data;
    }

    /** Returns the front item without removing it. Throws NoSuchElementException if empty. */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("CollectionQueue is empty");
        }
        return front.data;
    }

    /** Returns true if the queue has no pending waste items. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of waste items waiting for collection. */
    public int size() {
        return size;
    }

    /** Returns a string showing all items from front to rear. */
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<T> current = front;
        for (int i = 0; i < size; i++) {
            sb.append(String.valueOf(current.data));
            if (i < size - 1) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append(']');
        return sb.toString();
    }
}

