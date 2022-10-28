package com.schedulemaster.misc;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

public class LinkedList<E> implements Iterable<E>, Serializable {
    public static final long serialVersionUID = 12L;

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

    private Node<E> head;

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

    public void addAll(Iterable<E> iterable) {
        for (E element : iterable) {
            push(element);
        }
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

    public boolean has(E data) {
        for (E element : this) {
            if (element.equals(data))
                return true;
        }
        return false;
    }

    public void clear() {
        head.next = null;
    }

    public int getLength() {
        return length;
    }

    public E[] toArray(E[] array) {
        E[] result = Arrays.copyOf(array, length);

        int index = 0;
        for (E e : this) {
            result[index] = e;

            index += 1;
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LinkedList[\n");
        for (E e : this)
            stringBuilder.append(e.toString()).append(",\n");
        stringBuilder.append("\n]");

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LinkedList<?> that = (LinkedList<?>) o;

        if (length != that.length) return false;
        for (int i = 0; i < length; i++) {
            if (!this.at(i).equals(that.at(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (E element : this)
            result = 31 * result + element.hashCode();

        return result;
    }

    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeInt(length);
        for (E e : this) {
            oos.writeObject(e);
        }
    }

    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        int readLength = ois.readInt();
        head = new Node<>(null, null);
        for (int i = 0; i < readLength; i++) {
            E e = (E) ois.readObject();
            push(e);
        }
    }
}
