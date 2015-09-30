package org.hazy;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by egan on 9/29/15.
 */
public class RelationTreeImpl implements RelationTrie{
    public ArrayList<String> attrOrder;
    public ArrayList<Tuple> tuples;

    public RelationTreeImpl (ArrayList<String> attrOrder) {
        this.attrOrder = attrOrder;
        this.tuples = new ArrayList<>();
    }
    public ArrayList<Tuple> getTuples() {
        return new ArrayList<>(tuples);
    }
    public boolean hasAttribute(String attr) {
        return attrOrder.contains(attr);
    }
    public AttrSet index(Tuple tKey, String attr) {
        TreeMap<String, Annotation> attrValues = new TreeMap<>();
        for (Tuple existingTuple : tuples) {
            if (existingTuple.containsAttr(attr)) {
                if (existingTuple.match(tKey)) {
                    attrValues.put(existingTuple.getAttrValue(attr), existingTuple.getAnnot());
                }
            }
        }
        return new AttrSet(attr, attrValues);
    }
    public void insert(Tuple t) {
        tuples.add(t);
    }
}
