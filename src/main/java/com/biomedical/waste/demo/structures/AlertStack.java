package com.biomedical.waste.demo.structures;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/** Generic LIFO stack to maintain the biomedical waste alert history. */
public class AlertStack<T> {

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; this.next = null; }
    }

    private Node<T> top;
    private int size;

    /** Pushes a new alert onto the top of the history stack. */
    public void push(T item) {
        Node<T> node = new Node<>(item);
        node.next = top;
        top = node;
        size++;
    }

    /** Removes and returns the most recent alert. Throws EmptyStackException if empty. */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    /** Returns the most recent alert without removing it. */
    public T peek() {
        return isEmpty() ? null : top.data;
    }

    /** Returns true if no alerts are in the history. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of alerts in the history. */
    public int size() {
        return size;
    }

    /** Clears all alerts from the stack. */
    public void clear() {
        top = null;
        size = 0;
    }

    /** Returns all alerts as a List (top first). Uses java.util.ArrayList only here. */
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = top;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
}

