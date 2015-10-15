package org.hazy;

import java.util.*;

/**
 * Implements a relation using a trie
 * Created by egan on 10/6/15.
 */
public class RelationTrie implements Relation {
    // specifies the attributes it contains in order
    private ArrayList<String> attrs;

    // Can't both have subtrees and an annotation

    // if the key exists, then relationtrie != null
    private SortedMap<String, RelationTrie> subTrees;
    private Annotation annot;

    public RelationTrie(List<String> attrs) {
        this.attrs = new ArrayList<>(attrs);
        this.subTrees = new TreeMap<>();
        this.annot = null;
    }

    public ArrayList<Tuple> getTuples() {
        return getTuplesRec(Tuple.empty());
    }

    private ArrayList<Tuple> getTuplesRec(Tuple t) {
        ArrayList<Tuple> output = new ArrayList<>();
        if (subTrees.isEmpty()) {
            output.add((new Tuple(t)).setAnnot(annot));
            return output;
        }
        String rootAttrName = attrs.get(0);
        for (String rootAttrValue : subTrees.keySet()) {
            t.append(rootAttrName, rootAttrValue);
            RelationTrie subTrie = subTrees.get(rootAttrValue);
            output.addAll(subTrie.getTuplesRec(t));
            t.remove(rootAttrName);
        }
        return output;
    }

    public boolean hasAttribute(String attr) {
        return attrs.contains(attr);
    }

    public ArrayList<String> getAttributes() {
        return attrs;
    }

    public AttrSet index(Tuple t, String attr) {
        // Always assume that t contains the first n attributes
        // and attr is right after
        if (attr.equals(attrs.get(0))) {
            return (new AttrSet(attr, subTrees.keySet()));
        }
        String rootAttrName = attrs.get(0);
        if (!t.containsAttr(rootAttrName)) {
            System.out.println(t);
            System.out.println(attr);
            System.out.println(attrs);
            System.out.println(this);
            throw new IndexOutOfBoundsException("Out of order indexing");
        }
        String rootAttrValue = t.getAttrValue(attrs.get(0));
        if (subTrees.containsKey(rootAttrValue)) {
            return subTrees.get(rootAttrValue).index(t, attr);
        } else {
            return new AttrSet(attr, new TreeSet<>());
        }
    }

    public Annotation getAnnotation(Tuple t) {
        if (subTrees.size() == 0) {
            return annot;
        }
        else {
            String rootValue = t.getAttrValue(attrs.get(0));
            if (subTrees.containsKey(rootValue)) {
                return subTrees.get(rootValue).getAnnotation(t);
            } else {
                return null;
            }
        }
    }

    @Override
    public Relation select(Tuple t) {
        Map<String, String> tAttrs = t.getAttrs();
        for (String attrName : tAttrs.keySet()) {
            if (attrName.equals(attrs.get(0))) {
                String attrVal = tAttrs.get(attrName);
                if (subTrees.containsKey(attrVal)) {
                    return subTrees.get(attrVal);
                } else {
                    throw new UnsupportedOperationException("trie attrval not found");
                }
            }
        }
        return this;
    }

    @Override
    public boolean supportsSelect() {
        return true;
    }

    public void insert(Tuple t) {
        if (attrs.isEmpty()) {
            this.annot = t.getAnnot();
            return;
        }
        String rootAttr = attrs.get(0);
        String rootValue = t.getAttrValue(rootAttr);
        if (subTrees.containsKey(rootValue)) {
            subTrees.get(rootValue).insert(t);
        } else {
            RelationTrie newTrie = new RelationTrie(attrs.subList(1, attrs.size()));
            subTrees.put(rootValue, newTrie);
            newTrie.insert(t);
        }
    }

    // Can only aggregate the last attribute in the ordering
    public RelationTrie aggregate(String attr) {
        if (attrs.size() == 1) {
            if (attr.equals(attrs.get(0))) {
                if (attrs.size() != 1) {
                    throw new UnsupportedOperationException("invalid aggregation on trie");
                }
                Annotation a = calcSum();
                annot = a;
                subTrees = new TreeMap<>();
                attrs = new ArrayList<>();
                return this;
            } else {
                return this;
            }
        } else {
            for (RelationTrie subTrie : subTrees.values()) {
                subTrie.aggregate(attr);
                attrs.remove(attr);
            }
            return this;
        }
    }

    private Annotation calcSum() {
        Annotation a = null;
        if (subTrees.isEmpty()) {
            return annot;
        }
        for (RelationTrie subTrie : subTrees.values()) {
            Annotation subAnnot = subTrie.calcSum();
            if (a == null) {
                a = subAnnot;
            } else {
                a = a.add(subAnnot);
            }
        }
        return a;
    }

    public String toString() {
        if (attrs.isEmpty()) {
            return "LEAF:"+annot;
        } else {
            return attrs.get(0)+"|"+subTrees.toString();
        }
    }
}
