package com.biomedical.waste.demo.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Custom hash map using separate chaining for collision resolution. */
public class EntityHashMap<K, V> {

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;
        Entry(K key, V value) { this.key = key; this.value = value; this.next = null; }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    @SuppressWarnings("unchecked")
    private Entry<K, V>[] buckets = new Entry[DEFAULT_CAPACITY];
    private int size;

    /** Returns the bucket index for the given key using a custom hash function. */
    private int hash(K key) { return Math.abs(Objects.hashCode(key) % buckets.length); }

    /** Inserts or updates the value for the given key. Resizes if load factor exceeded. */
    public void put(K key, V value) {
        if ((size + 1.0) / buckets.length > LOAD_FACTOR_THRESHOLD) {
            resize();
        }
        int index = hash(key);
        Entry<K, V> head = buckets[index];
        Entry<K, V> current = head;
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Entry<K, V> entry = new Entry<>(key, value);
        entry.next = head;
        buckets[index] = entry;
        size++;
    }

    /** Returns the value for the given key, or null if not found. */
    public V get(K key) {
        int index = hash(key);
        Entry<K, V> current = buckets[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /** Removes the entry for the given key if it exists. */
    public void remove(K key) {
        int index = hash(key);
        Entry<K, V> current = buckets[index];
        Entry<K, V> prev = null;
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    /** Returns true if the given key exists in the map. */
    public boolean containsKey(K key) {
        return get(key) != null || containsNullValueKey(key);
    }

    private boolean containsNullValueKey(K key) {
        int index = hash(key);
        Entry<K, V> current = buckets[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Returns the number of key-value pairs in the map. */
    public int size() {
        return size;
    }

    /** Returns the current load factor (size / capacity). */
    public double loadFactor() { return (double) size / buckets.length; }

    /** Returns all keys as a List. */
    public List<K> keys() {
        List<K> list = new ArrayList<>();
        for (Entry<K, V> bucket : buckets) {
            Entry<K, V> current = bucket;
            while (current != null) {
                list.add(current.key);
                current = current.next;
            }
        }
        return list;
    }

    /** Returns all values as a List. */
    public List<V> values() {
        List<V> list = new ArrayList<>();
        for (Entry<K, V> bucket : buckets) {
            Entry<K, V> current = bucket;
            while (current != null) {
                list.add(current.value);
                current = current.next;
            }
        }
        return list;
    }

    /** Doubles the capacity and rehashes all entries. */
    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = new Entry[oldBuckets.length * 2];
        size = 0;
        for (Entry<K, V> bucket : oldBuckets) {
            Entry<K, V> current = bucket;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }
}

