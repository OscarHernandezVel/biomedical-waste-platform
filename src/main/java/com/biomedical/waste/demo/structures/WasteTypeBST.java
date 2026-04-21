package com.biomedical.waste.demo.structures;

import java.util.ArrayList;
import java.util.List;

/** Generic Binary Search Tree for efficient lookup of comparable items. */
public class WasteTypeBST<T extends Comparable<T>> {

    private static class Node<T> {
        T data;
        Node<T> left;
        Node<T> right;
        Node(T data) { this.data = data; this.left = null; this.right = null; }
    }

    private Node<T> root;

    /** Inserts a value maintaining the BST ordering property. */
    public void insert(T value) {
        root = insertRec(root, value);
    }

    private Node<T> insertRec(Node<T> node, T value) {
        if (node == null) return new Node<>(value);
        int cmp = value.compareTo(node.data);
        if (cmp < 0) node.left = insertRec(node.left, value);
        else if (cmp > 0) node.right = insertRec(node.right, value);
        else node.data = value;
        return node;
    }

    /** Returns true if the given value exists in the tree. */
    public boolean search(T value) {
        Node<T> current = root;
        while (current != null) {
            int cmp = value.compareTo(current.data);
            if (cmp == 0) return true;
            current = cmp < 0 ? current.left : current.right;
        }
        return false;
    }

    /** Removes the node with the given value. Handles all 3 deletion cases. */
    public void delete(T value) {
        root = deleteRec(root, value);
    }

    private Node<T> deleteRec(Node<T> node, T value) {
        if (node == null) return null;
        int cmp = value.compareTo(node.data);
        if (cmp < 0) {
            node.left = deleteRec(node.left, value);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, value);
        } else {
            if (node.left == null && node.right == null) return null;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node<T> successor = minNode(node.right);
            node.data = successor.data;
            node.right = deleteRec(node.right, successor.data);
        }
        return node;
    }

    /** Returns all values in ascending order (left → root → right). */
    public List<T> inorder() {
        List<T> list = new ArrayList<>();
        inorderRec(root, list);
        return list;
    }

    private void inorderRec(Node<T> node, List<T> list) {
        if (node == null) return;
        inorderRec(node.left, list);
        list.add(node.data);
        inorderRec(node.right, list);
        }

    /** Returns values in pre-order (root → left → right). */
    public List<T> preorder() {
        List<T> list = new ArrayList<>();
        preorderRec(root, list);
        return list;
    }

    private void preorderRec(Node<T> node, List<T> list) {
        if (node == null) return;
        list.add(node.data);
        preorderRec(node.left, list);
        preorderRec(node.right, list);
    }

    /** Returns values in post-order (left → right → root). */
    public List<T> postorder() {
        List<T> list = new ArrayList<>();
        postorderRec(root, list);
        return list;
    }

    private void postorderRec(Node<T> node, List<T> list) {
        if (node == null) return;
        postorderRec(node.left, list);
        postorderRec(node.right, list);
        list.add(node.data);
    }

    /** Returns the smallest value in the tree. Throws if empty. */
    public T findMin() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        return minNode(root).data;
    }

    private Node<T> minNode(Node<T> node) {
        Node<T> current = node;
        while (current.left != null) current = current.left;
        return current;
    }

    /** Returns the largest value in the tree. Throws if empty. */
    public T findMax() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        Node<T> current = root;
        while (current.right != null) current = current.right;
        return current.data;
    }

    /** Returns the height of the tree. Empty tree has height 0. */
    public int height() {
        return heightRec(root);
    }

    private int heightRec(Node<T> node) {
        if (node == null) return 0;
        int left = heightRec(node.left);
        int right = heightRec(node.right);
        return Math.max(left, right) + 1;
    }
}

