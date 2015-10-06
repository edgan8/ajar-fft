package org.hazy;

import java.util.*;

/**
 * Immutable set of string attribute values
 * Created by egan on 9/29/15.
 */
public class AttrSet {
    private final String name;
    // assume values is in order
    private final Set<String> values;

    public AttrSet(String attrName, Set<String> attrValues) {
        name = attrName;
        values = attrValues;
    }

    public boolean containsValue(String val) {
        return values.contains(val);
    }

    public Set<String> getValues() {
        return values;
    }

    /**
     * Keep only the elements in other that match those here
     * @param other set to filter elements from
     */
    public void filter(Set<String> other) {
        other.retainAll(values);
    }

    public String toString(){
        return name+"|"+values.toString();
    }
}
