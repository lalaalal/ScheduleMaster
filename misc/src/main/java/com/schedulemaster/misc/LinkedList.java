package com.schedulemaster.misc;

import java.io.Serializable;
import java.util.Iterator;

public class LinkedList<E> implements Iterable<E>, Serializable {

    private class LinkedListIterator implements Iterator<E> {
        private Node<E> curr = head;

        @Override
        public boolean hasNext() {
            return curr.next != null;
        }

        @Override
        public E next() {
            curr = curr.next;
            return curr.data;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    private static class Node<E> implements Serializable {
        public E data;
        public Node<E> next;

        public Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private final Node<E> head;

    private int length = 0;

    public LinkedList() {
        head = new Node<>(null, null);
    }

    public boolean isEmpty() {
        return head.next == null;
    }

    private Node<E> getLastNode() {
        Node<E> curr = head;
        while (curr.next != null) {
            curr = curr.next;
        }

        return curr;
    }

    // push data to tail
    public void push(E data) {
        Node<E> prev = getLastNode();

        prev.next = new Node<>(data, null);
        length += 1;
    }

    public void remove(E data) {
        Node<E> prev = head;
        Node<E> curr = prev.next;

        while (curr != null) {
            if (curr.data.equals(data)) {
                prev.next = curr.next;
                length -= 1;
            }
            prev = curr;
            curr = curr.next;
        }
    }

    // start with 0
    public E at(int index) {
        Node<E> curr = head.next;

        for (int i = 0; i < index; i++) {
            if (curr == null)
                throw new IndexOutOfBoundsException();

            curr = curr.next;
        }

        return curr.data;
    }

    public int getLength() {
        return length;
    }
}
