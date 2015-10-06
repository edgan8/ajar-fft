package org.hazy;

import java.util.*;

/**
 * Implements a relation using a trie
 * Created by egan on 10/6/15.
 */
public class RelationTrie implements Relation {
    // specifies the attributes it contains in order
    private final ArrayList<String> attrs;

    // Can't both have subtrees and an annotation

    // if the key exists, then relationtrie != null
    private SortedMap<String, RelationTrie> subTrees;
    private Annotation annot;

    public RelationTrie(List<String> attrs) {
        this.attrs = new ArrayList<>(attrs);
        this.subTrees = new TreeMap<>();
        this.annot = null;
    }

    private ArrayList<Tuple> getTuples(Tuple t) {
        ArrayList<Tuple> output = new ArrayList<>();
        if (subTrees.isEmpty()) {
            output.add((new Tuple(t)).setAnnot(annot));
            return output;
        }
        return null;
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

    public void insert(Tuple t) {
        if (t.getAttrs().isEmpty()) {
            this.annot = t.getAnnot();
            return;
        }
        String rootAttr = attrs.get(0);
        String rootValue = t.getAttrValue(attrs.get(0));
        if (subTrees.containsKey(rootValue)) {
            subTrees.get(rootValue).insert(t.remove(rootAttr));
        } else {
            RelationTrie newTrie = new RelationTrie(attrs.subList(1, attrs.size()));
            subTrees.put(rootValue, newTrie);
            newTrie.insert(t.remove(rootAttr));
        }
        t.append(rootAttr, rootValue);
    }

    public Relation aggregate(String attr) {
        return null;
    }

    public String toString() {
        if (attrs.isEmpty()) {
            return "LEAF:"+annot;
        } else {
            return attrs.get(0)+"|"+subTrees.toString();
        }
    }
}
