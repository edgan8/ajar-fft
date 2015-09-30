package org.hazy;

import java.util.*;

/**
 * Created by egan on 9/29/15.
 */
public class AttrSet {
    public String name;
    public SortedMap<String, Annotation> values;

    public AttrSet(String attrName, SortedMap<String, Annotation> attrValues) {
        name = attrName;
        values = attrValues;
    }

    public AttrSet(AttrSet base) {
        name = base.name;
        values = new TreeMap<>(base.values);
    }

    public boolean containsValue(String val) {
        return values.containsKey(val);
    }

    public Set<String> getValues() {
        return values.keySet();
    }

    public Annotation getAnnotation(String attrValue) {
        return values.get(attrValue);
    }

    public void intersect(AttrSet other) {
        values.keySet().retainAll(other.getValues());
        for (String val : values.keySet()) {
            Annotation a1 = getAnnotation(val);
            Annotation a2 = other.getAnnotation(val);
            Annotation a3;
            if (a1 == null) {
                a3 = a2;
            } else if (a2 == null) {
                a3 = a1;
            } else {
                a3 = a1.mult(a2);
            }
            values.put(val, a3);
        }
        return;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(name+"||Values:");
        for (String val : values.keySet()) {
            s.append(val+"=>"+values.get(val).toString());
        }
        return s.toString();
    }
}
