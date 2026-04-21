package com.biomedical.waste.demo.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/** Generic doubly linked list to maintain the complete chain of custody for each waste item. */
public class TraceabilityLinkedList<T> {

    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;
        Node(T data) { this.data = data; this.prev = null; this.next = null; }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /** Appends a new traceability entry to the end of the custody chain. */
    public void addLast(T item) {
        Node<T> node = new Node<>(item);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    /** Prepends a new entry to the start of the custody chain. */
    public void addFirst(T item) {
        Node<T> node = new Node<>(item);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    /** Removes the first occurrence of the given item. Returns true if found. */
    public boolean remove(T item) {
        Node<T> current = head;
        while (current != null) {
            if ((current.data == null && item == null) || (current.data != null && current.data.equals(item))) {
                Node<T> prev = current.prev;
                Node<T> next = current.next;
                if (prev != null) {
                    prev.next = next;
                } else {
                    head = next;
                }
                if (next != null) {
                    next.prev = prev;
                } else {
                    tail = prev;
                }
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Returns the first entry matching the given condition, or null if not found. */
    public T find(Predicate<T> condition) {
        Node<T> current = head;
        while (current != null) {
            if (condition.test(current.data)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    /** Returns all entries from oldest to newest (head to tail). */
    public List<T> traverseForward() {
        List<T> result = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    /** Returns all entries from newest to oldest (tail to head). */
    public List<T> traverseBackward() {
        List<T> result = new ArrayList<>();
        Node<T> current = tail;
        while (current != null) {
            result.add(current.data);
            current = current.prev;
        }
        return result;
    }

    /** Returns the number of entries in the custody chain. */
    public int size() {
        return size;
    }

    /** Returns true if the custody chain has no entries. */
    public boolean isEmpty() {
        return size == 0;
    }
}

