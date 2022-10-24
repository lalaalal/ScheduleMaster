package com.schedulemaster.misc;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Hash<K, V> implements Iterable<V>, Serializable {
    public static final long serialVersionUID = 13L;

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

    private static class Bucket<K, V> implements Serializable {
        public K key;
        public V value;

        public Bucket(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Bucket<?, ?> bucket = (Bucket<?, ?>) o;

            if (!Objects.equals(key, bucket.key)) return false;
            return Objects.equals(value, bucket.value);
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }
    }

    // LinkedList<Bucket<K, V>>
    private final Object[] elements;

    private final LinkedList<K> keys = new LinkedList<>();

    private static final int DEFAULT_SIZE = 10240;
    private int length = 0;

    public Hash() {
        elements = new Object[DEFAULT_SIZE];
    }

    public Hash(int indexLength) {
        elements = new Object[indexLength];
    }

    private int getIndex(Object key) {
        return Math.abs(key.hashCode()) % elements.length;
    }

    public void put(K key, V value) {
        if (hasKey(key))
            throw new RuntimeException("Key " + key + " already exists");

        int index = getIndex(key);
        Bucket<K, V> bucket = new Bucket<>(key, value);

        if (elements[index] == null)
            elements[index] = new LinkedList<Bucket<K, V>>();

        @SuppressWarnings("unchecked")
        LinkedList<Bucket<K, V>> bucketList = (LinkedList<Bucket<K, V>>) elements[index];
        bucketList.push(bucket);
        keys.push(key);

        length += 1;
    }

    public boolean hasKey(K keyToCheck) {
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

    public void set(K key, V value) {
        int index = getIndex(key);

        @SuppressWarnings("unchecked")
        LinkedList<Bucket<K, V>> bucketList = (LinkedList<Bucket<K, V>>) elements[index];

        if (bucketList == null)
            throw new RuntimeException("No such key " + key);
        for (Bucket<K, V> bucket : bucketList) {
            if (bucket.key.equals(key))
                bucket.value = value;
        }
        throw new RuntimeException("No such key " + key);
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
        keys.remove(key);
        bucketList.remove(select);

        length -= 1;
    }

    public void clear() {
        for (K key : keys) {
            int index = getIndex(key);
            elements[index] = null;
        }
        keys.clear();
        length = 0;
    }

    public int getLength() {
        return length;
    }

    public LinkedList<K> getKeys() {
        return keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hash<?, ?> hash = (Hash<?, ?>) o;

        if (length != hash.length) return false;
        
        if (!keys.equals(hash.keys)) return false;

        for (K key : keys) {
            int index = this.getIndex(key);
            Object my = this.elements[index];
            Object other = hash.elements[index];
            if ((my != null && other != null && !my.equals(other))
                    || !(my == null && other == null))
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(elements);
        result = 31 * result + keys.hashCode();
        result = 31 * result + length;
        return result;
    }
}
