package com.schedulemaster.misc;

import java.util.Iterator;
import java.util.function.Function;

public class Heap<E> implements Iterable<E> {
    private static final int DEFAULT_SIZE = 10240;
    private static final int ROOT_INDEX = 1;

    private final Object[] elements;
    private int lastIndex = 0;
    private final Comparator<E> comparator;
    private TreeIterator iterator;
    private final InorderIterator inorder = new InorderIterator();
    private final PreorderIterator preorder = new PreorderIterator();
    private final PostorderIterator postorder = new PostorderIterator();


    public Heap(Comparator<E> comparator) {
        elements = new Object[DEFAULT_SIZE];
        this.comparator = comparator;
        iterator = inorder;
    }

    public Heap(Comparator<E> comparator, int size) {
        elements = new Object[size];
        this.comparator = comparator;
        iterator = inorder;
    }

    public boolean isEmpty() {
        return lastIndex < ROOT_INDEX;
    }

    public void insert(E data) {
        if (lastIndex >= elements.length)
            throw new RuntimeException("Tree is full");
        lastIndex += 1;
        elements[lastIndex] = data;

        ensureInsertion(lastIndex);
    }

    @SuppressWarnings("unchecked")
    private void ensureInsertion(int index) {
        int parentIndex = getParentNode(index);

        if (parentIndex < ROOT_INDEX)
            return;

        if (comparator.compare((E) elements[index], (E) elements[parentIndex])) {
            swap(index, parentIndex);
            ensureInsertion(parentIndex);
        }
    }

    public E pop() {
        E value = top();
        elements[ROOT_INDEX] = elements[lastIndex];
        lastIndex -= 1;

        ensureDeletion(ROOT_INDEX);

        return value;
    }

    @SuppressWarnings("unchecked")
    private void ensureDeletion(int index) {
        if (getLeftNode(index) > lastIndex)
            return;
        int childIndex = getComparedChildIndex(index);

        if (!comparator.compare((E) elements[index], (E) elements[childIndex])) {
            swap(index, childIndex);
            ensureDeletion(childIndex);
        }
    }

    @SuppressWarnings("unchecked")
    private int getComparedChildIndex(int index) {
        int leftIndex = getLeftNode(index);
        int rightIndex = getRightNode(index);

        if (comparator.compare((E) elements[leftIndex], (E) elements[rightIndex]))
            return leftIndex;
        return rightIndex;
    }

    @SuppressWarnings("unchecked")
    public E top() {
        return (E) elements[ROOT_INDEX];
    }

    protected void swap(int aIndex, int bIndex) {
        Object tmp = elements[aIndex];
        elements[aIndex] = elements[bIndex];
        elements[bIndex] = tmp;
    }

    public int getLength() {
        return lastIndex - 1;
    }

    protected int getParentNode(int index) {
        return index / 2;
    }

    protected int getLeftNode(int index) {
        return index * 2;
    }

    protected int getRightNode(int index) {
        return index * 2 + 1;
    }

    public int getHeight() {
        return (int) (Math.log(lastIndex) / Math.log(2));
    }

    public void setInorder() {
        iterator = inorder;
    }

    public void setPostorder() {
        iterator = postorder;
    }

    public void setPreorder() {
        iterator = preorder;
    }

    @Override
    public Iterator<E> iterator() {
        iterator.updateOrder();
        return iterator;
    }

    private abstract class TreeIterator implements Iterator<E> {
        protected int current = 0;
        protected int[] order;

        public void updateOrder() {
            current = 0;
            order = new int[lastIndex];
            makeOrder(ROOT_INDEX);
            current = 0;
        }

        protected abstract void makeOrder(int index);

        @Override
        public boolean hasNext() {
            return current < order.length;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            int index = order[current++];
            return (E) elements[index];
        }
    }

    private class InorderIterator extends TreeIterator {
        protected void makeOrder(int index) {
            if (index <= lastIndex) {
                makeOrder(getLeftNode(index));
                order[current++] = index;
                makeOrder(getRightNode(index));
            }
        }
    }

    private class PreorderIterator extends TreeIterator {
        protected void makeOrder(int index) {
            if (index <= lastIndex) {
                order[current++] = index;
                makeOrder(getLeftNode(index));
                makeOrder(getRightNode(index));
            }
        }
    }

    private class PostorderIterator extends TreeIterator {
        protected void makeOrder(int index) {
            if (index <= lastIndex) {
                makeOrder(getLeftNode(index));
                makeOrder(getRightNode(index));
                order[current++] = index;
            }
        }
    }

    /** *
     * If compare(a, b) returns true, param a will be root, or b will be root
     */

    @FunctionalInterface
    public interface Comparator<E> {
        /** *
         *
         * @return If returns true, param a will be root, or b will be root
         */
        boolean compare(E a, E b);

        static <E> Comparator<E> maxHeapInt(Function<E, Integer> function) {
            return (a, b) -> function.apply(a) > function.apply(b);
        }

        static <E> Comparator<E> minHeapInt(Function<E, Integer> function) {
            return (a, b) -> function.apply(a) < function.apply(b);
        }
    }
}
