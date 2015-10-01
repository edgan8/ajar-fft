package org.hazy;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Implements a relation using a list of raw tuples
 * Created by egan on 9/29/15.
 */
public class RelationTreeImpl implements RelationTrie{
    // specifies both the order and the attributes it contains
    public final String[] attrOrder;
    public ArrayList<Tuple> tuples;

    public RelationTreeImpl (String[] attrOrder) {
        this.attrOrder = attrOrder;
        this.tuples = new ArrayList<>();
    }
    public ArrayList<Tuple> getTuples() {
        return new ArrayList<>(tuples);
    }
    public boolean hasAttribute(String attr) {
        for (int i = 0; i < attrOrder.length; i++) {
            if (attrOrder[i].equals(attr)) {
                return true;
            }
        }
        return false;
    }

    public Annotation getAnnotation(Tuple tKey) throws IndexOutOfBoundsException {
        for (Tuple existingTuple : tuples) {
            if (existingTuple.match(tKey)) {
                return existingTuple.getAnnot();
            }
        }
        throw new IndexOutOfBoundsException("Tuple not found");
    }

    public AttrSet index(Tuple tKey, String attr) throws IndexOutOfBoundsException {
        if (!hasAttribute(attr)) {
            throw new IndexOutOfBoundsException("Attribute not part of relation");
        }

        TreeSet<String> attrValues = new TreeSet<>();
        for (Tuple existingTuple : tuples) {
            if (existingTuple.containsAttr(attr)) {
                if (existingTuple.match(tKey)) {
                    attrValues.add(existingTuple.getAttrValue(attr));
                }
            }
        }
        return new AttrSet(attr, attrValues);
    }
    public void insert(Tuple t) {
        tuples.add(t);
    }
}
