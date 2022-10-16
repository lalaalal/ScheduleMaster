package com.schedulemaster.misc;

public class Index<Attribute, Tuple> {
    public interface AttributeSelector<Attribute, Tuple> {
        Attribute getAttribute(Tuple tuple);
    }

    private final Hash<Attribute, LinkedList<Tuple>> index = new Hash<>();

    public Index(LinkedList<Tuple> table, AttributeSelector<Attribute, Tuple> attributeSelector) {
        for (Tuple tuple : table) {
            add(attributeSelector.getAttribute(tuple), tuple);
        }
    }

    public LinkedList<Tuple> get(Attribute attribute) {
        return index.get(attribute);
    }

    public void add(Attribute attribute, Tuple tuple) {
        LinkedList<Tuple> tuples = index.get(attribute);
        if (tuples == null) {
            tuples = new LinkedList<>();
            index.put(attribute, tuples);
        }

        tuples.push(tuple);
    }
}
