package org.hazy;

import java.util.*;

/**
 * Implements a relation using a list of raw tuples.
 * Very inefficient but useful as a correctness benchmark.
 * Created by egan on 9/29/15.
 */
public class RelationList implements Relation {
    // specifies the attributes it contains
    public final Set<String> attrs;
    public ArrayList<Tuple> tuples;

    public RelationList(Collection<String> attrs, Collection<Tuple> tuples) {
        this.attrs = new HashSet<String>(attrs);
        this.tuples = new ArrayList<>(tuples);
    }
    public RelationList(Collection<String> attrs) {
        this.attrs = new HashSet<String>(attrs);
        this.tuples = new ArrayList<>();
    }
    public ArrayList<Tuple> getTuples() {
        return new ArrayList<>(tuples);
    }
    public boolean hasAttribute(String attr) {
        return attrs.contains(attr);
    }
    public ArrayList<String> getAttributes() {
        return new ArrayList<>(attrs);
    }

    public Annotation getAnnotation(Tuple tKey) throws IndexOutOfBoundsException {
        for (Tuple existingTuple : tuples) {
            if (existingTuple.match(tKey)) {
                return existingTuple.getAnnot();
            }
        }
        throw new IndexOutOfBoundsException("Tuple not found");
    }

    @Override
    public Relation select(Tuple t) {
        return this;
    }

    @Override
    public boolean supportsSelect() {
        return false;
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
            existingTuple.remove(attr);
            if (newTuples.containsKey(existingTuple)) {
                newTuples.put(
                        existingTuple,
                        newTuples.get(existingTuple).add(existingTuple.getAnnot()));
            } else {
                newTuples.put(existingTuple, existingTuple.getAnnot());
            }
        }
        HashSet<String> newAttrs = new HashSet<>(attrs);
        newAttrs.remove(attr);
        Relation newRelationList = new RelationList(newAttrs);
        for (Tuple newTuple : newTuples.keySet()) {
            newRelationList.insert(
                    (new Tuple(newTuple)).setAnnot(newTuples.get(newTuple)));
        }
        return newRelationList;
    }
}
