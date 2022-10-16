package com.schedulemaster.misc;

import java.util.Iterator;

public class Hash<K, V> implements Iterable<V> {
    private class HashIterator implements Iterator<V> {
        private int index = 0;
        private int bucketIndex = 0;

        @Override
        public boolean hasNext() {
            if (index == getLength() - 1) {
                @SuppressWarnings("unchecked")
                LinkedList<Bucket<K, V>> bucketList = (LinkedList<Bucket<K, V>>) elements[index];
                if (bucketIndex == bucketList.getLength() - 1)
                    return false;
            }
            return index + 1 < getLength();
        }

        @Override
        @SuppressWarnings("unchecked")
        public V next() {
            LinkedList<Bucket<K, V>> bucketList = (LinkedList<Bucket<K,V>>) elements[index];
            if (bucketIndex >= bucketList.getLength()) {
                bucketList = (LinkedList<Bucket<K,V>>) elements[++index];
                bucketIndex = 0;
            }


            return bucketList.at(bucketIndex++).value;
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

    private static final int DEFAULT_SIZE = 1024;
    private int length = 0;

    public Hash() {
        elements = new Object[DEFAULT_SIZE];
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % elements.length;
    }

    public void put(K key, V value) {
        int index = getIndex(key);
        Bucket<K, V> bucket = new Bucket<>(key, value);

        if (elements[index] == null)
            elements[index] = new LinkedList<>();

        @SuppressWarnings("unchecked")
        LinkedList<Bucket<K, V>> bucketList = (LinkedList<Bucket<K, V>>) elements[index];
        bucketList.push(bucket);

        length += 1;
    }

    public V get(K key) {
        int index = getIndex(key);

        @SuppressWarnings("unchecked")
        LinkedList<Bucket<K, V>> bucketList = (LinkedList<Bucket<K,V>>) elements[index];

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
        LinkedList<Bucket<K, V>> bucketList = (LinkedList<Bucket<K,V>>) elements[index];

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
