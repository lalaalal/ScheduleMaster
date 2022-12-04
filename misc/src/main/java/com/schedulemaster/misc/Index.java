package com.schedulemaster.misc;

/**
 * Provide Index using hash table. Index saves lists of tuples that matches with a specific attribute value.
 *
 * @param <Attribute> Type of attribute (member variables).
 * @param <Tuple>     Type of tuple (class).
 * @author lalaalal
 */
public class Index<Attribute, Tuple> {
    /**
     * Interface able to get attribute from tuple.
     *
     * @param <Attribute> Type of attribute (member variables).
     * @param <Tuple>     Type of tuple (class).
     */
    public interface AttributeSelector<Attribute, Tuple> {
        /**
         * Get attribute from tuple.
         *
         * @param tuple A tuple to select attribute.
         * @return Selected attribute.
         */
        Attribute getAttribute(Tuple tuple);
    }

    private final Hash<Attribute, LinkedList<Tuple>> index = new Hash<>();
    private final AttributeSelector<Attribute, Tuple> selector;

    /**
     * Initialize with list of tuples (table) and attributeSelector.
     *
     * @param table             List of tuples.
     * @param attributeSelector Chooser for tuple's attribute.
     */
    public Index(LinkedList<Tuple> table, AttributeSelector<Attribute, Tuple> attributeSelector) {
        this.selector = attributeSelector;
        for (Tuple tuple : table) {
            add(tuple);
        }
    }

    public LinkedList<Attribute> getAttributes() {
        return index.getKeys();
    }

    /**
     * Get tuples that attribute matches.
     *
     * @param attribute Attribute value to search.
     * @return List of tuples.
     */
    public LinkedList<Tuple> get(Attribute attribute) {
        return index.get(attribute);
    }

    /**
     * Add a new tuple for indexing.
     *
     * @param tuple New tuple.
     */
    public void add(Tuple tuple) {
        Attribute attributeValue = selector.getAttribute(tuple);
        LinkedList<Tuple> tuples = index.get(attributeValue);
        if (tuples == null) {
            tuples = new LinkedList<>();
            index.put(attributeValue, tuples);
        }

        tuples.push(tuple);
    }

    /**
     * Change entire table.
     *
     * @param table New table.
     */
    public void changeTable(LinkedList<Tuple> table) {
        clear();
        for (Tuple tuple : table) {
            add(tuple);
        }
    }

    /**
     * Remove tuple form table.
     *
     * @param tuple Tuple to remove.
     */
    public void remove(Tuple tuple) {
        Attribute attributeValue = selector.getAttribute(tuple);
        LinkedList<Tuple> tuples = index.get(attributeValue);
        if (tuples == null)
            return;

        tuples.remove(tuple);
    }

    /**
     * Clear all indexed tuples.
     */
    public void clear() {
        index.clear();
    }
}
