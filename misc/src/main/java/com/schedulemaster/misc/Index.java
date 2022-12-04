package com.schedulemaster.misc;

public class Index<Attribute, Tuple> {
    public interface AttributeSelector<Attribute, Tuple> {
        Attribute getAttribute(Tuple tuple);
    }

    private final Hash<Attribute, LinkedList<Tuple>> index = new Hash<>();
    private final AttributeSelector<Attribute, Tuple> selector;

    public Index(LinkedList<Tuple> table, AttributeSelector<Attribute, Tuple> attributeSelector) {
        this.selector = attributeSelector;
        for (Tuple tuple : table) {
            add(tuple);
        }
    }

    public LinkedList<Attribute> getAttributes() {
        return index.getKeys();
    }

    public LinkedList<Tuple> get(Attribute attribute) {
        return index.get(attribute);
    }

    public void add(Tuple tuple) {
        Attribute attributeValue = selector.getAttribute(tuple);
        LinkedList<Tuple> tuples = index.get(attributeValue);
        if (tuples == null) {
            tuples = new LinkedList<>();
            index.put(attributeValue, tuples);
        }

        tuples.push(tuple);
    }

    public void changeTable(LinkedList<Tuple> table) {
        clear();
        for (Tuple tuple : table) {
            add(tuple);
        }
    }

    public void remove(Tuple tuple) {
        Attribute attributeValue = selector.getAttribute(tuple);
        LinkedList<Tuple> tuples = index.get(attributeValue);
        if (tuples == null)
            return;

        tuples.remove(tuple);
    }

    public void clear() {
        index.clear();
    }
}
