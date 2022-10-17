package com.schedulemaster.misc;

import java.io.Serializable;
import java.util.Iterator;

public class Hash<K, V> implements Iterable<V>, Serializable {
    public static final long serialVersionUID = 1L;

    private class HashIterator implements Iterator<V> {
        private final Iterator<K> iterator = keys.iterator();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public V next() {
            K key = iterator.next();

            return get(key);
        }
    }

    @Override
    public Iterator<V> iterator() {
        return new HashIterator();
    }

    private static class Bucket<K, V> {
        public K key;
        public V value;

        public Bucket(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // LinkedList<Bucket<K, V>>
    private final Object[] elements;
    private final LinkedList<K> keys = new LinkedList<>();

    private static final int DEFAULT_SIZE = 1024;
    private int length = 0;

    public Hash() {
        elements = new Object[DEFAULT_SIZE];
    }

    public Hash(int indexLength) {
        elements = new Object[indexLength];
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % elements.length;
    }

    public void put(K key, V value) {
        if (hasKey(key))
            throw new RuntimeException("Key " + key + " already exists");

        int index = getIndex(key);
        Bucket<K, V> bucket = new Bucket<>(key, value);

        if (elements[index] == null)
            elements[index] = new LinkedList<>();

        @SuppressWarnings("unchecked")
        LinkedList<Bucket<K, V>> bucketList = (LinkedList<Bucket<K, V>>) elements[index];
        bucketList.push(bucket);
        keys.push(key);

        length += 1;
    }

    private boolean hasKey(K keyToCheck) {
        for (K key : keys) {
            if (key.equals(keyToCheck))
                return true;
        }
        return false;
    }

    public V get(K key) {
        int index = getIndex(key);

        @SuppressWarnings("unchecked")
        LinkedList<Bucket<K, V>> bucketList = (LinkedList<Bucket<K, V>>) elements[index];

        if (bucketList == null)
            return null;
        for (Bucket<K, V> bucket : bucketList) {
            if (bucket.key.equals(key))
                return bucket.value;
        }

        return null;
    }

    public void remove(K key) {
        int index = getIndex(key);

        @SuppressWarnings("unchecked")
        LinkedList<Bucket<K, V>> bucketList = (LinkedList<Bucket<K, V>>) elements[index];

        Bucket<K, V> select = null;
        for (Bucket<K, V> bucket : bucketList) {
            if (bucket.key.equals(key))
                select = bucket;
        }
        bucketList.remove(select);

        length -= 1;
    }

    public int getLength() {
        return length;
    }
}
