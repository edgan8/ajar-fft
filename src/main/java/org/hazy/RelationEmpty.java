package org.hazy;

import java.util.*;

/**
 * Implements a relation with an empty tuple and an annotation
 */
public class RelationEmpty implements Relation {
    public Annotation annot;

    public RelationEmpty(Annotation a) {
        annot = a;
    }

    public ArrayList<Tuple> getTuples() {
        return new ArrayList<>();
    }
    public boolean hasAttribute(String attr){
        return false;
    }
    public ArrayList<String> getAttributes() {
        return new ArrayList<>();
    }

    public Annotation getAnnotation(Tuple tKey) {
        return annot;
    }

    public Relation select(Tuple t) {
        return this;
    }

    public boolean supportsSelect() {
        return false;
    }

    public AttrSet index(Tuple tKey, String attr) throws IndexOutOfBoundsException {
        throw new IndexOutOfBoundsException("No attrs in empty relation");
    }

    public void insert(Tuple t) {
        throw new UnsupportedOperationException("No insertion into empty relation");
    }

    public Relation aggregate(String attr) {
        return this;
    }
}
