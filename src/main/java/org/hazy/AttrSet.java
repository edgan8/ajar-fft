package org.hazy;

import java.util.*;

/**
 * Mostly immutable set of string attribute values
 * Created by egan on 9/29/15.
 */
public class AttrSet {
    public String name;
    public SortedSet<String> values;

    public AttrSet(String attrName, SortedSet<String> attrValues) {
        name = attrName;
        values = attrValues;
    }

    public AttrSet(AttrSet base) {
        name = base.name;
        values = new TreeSet<>(base.values);
    }

    public boolean containsValue(String val) {
        return values.contains(val);
    }

    public Set<String> getValues() {
        return values;
    }

    public AttrSet intersect(AttrSet other) {
        values.retainAll(other.getValues());
        return this;
    }

    public String toString(){
        return name+"|"+values.toString();
    }
}
