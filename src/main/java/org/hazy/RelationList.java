package org.hazy;

import java.util.*;

/**
 * Implements a relation using a list of raw tuples.
 * Very inefficient but useful as a correctness benchmark.
 * Created by egan on 9/29/15.
 */
public class RelationList implements Relation {
    // specifies both the order and the attributes it contains
    public final String[] attrOrder;
    public ArrayList<Tuple> tuples;

    public RelationList(String[] attrOrder, Collection<Tuple> tuples) {
        this.attrOrder = attrOrder;
        this.tuples = new ArrayList<>(tuples);
    }
    public RelationList(String[] attrOrder) {
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

    public Relation aggregate(String attr) {
        HashMap<Tuple, Annotation> newTuples = new HashMap<>();
        for (Tuple existingTuple : tuples) {
            Tuple tempNewTuple = existingTuple.remove(attr);
            if (newTuples.containsKey(tempNewTuple)) {
                newTuples.put(
                        tempNewTuple,
                        newTuples.get(tempNewTuple).add(tempNewTuple.getAnnot()));
            } else {
                newTuples.put(tempNewTuple, tempNewTuple.getAnnot());
            }
        }
        Relation newRelationList = new RelationList(attrOrder);
        for (Tuple newTuple : newTuples.keySet()) {
            newRelationList.insert(newTuple.setAnnot(newTuples.get(newTuple)));
        }
        return newRelationList;
    }
}
