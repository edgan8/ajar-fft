package org.hazy;

import java.util.*;

/**
 * Created by egan on 9/29/15.
 */
public class AttrSet {
    public SortedMap<String, Annotation> attrs;

    public AttrSet(SortedMap<String, Annotation> attrs) {
        this.attrs = attrs;
    }

    public AttrSet(AttrSet base) {
        this.attrs = new TreeMap<>(base.attrs);
    }

    public Set<String> getValues() {
        return attrs.keySet();
    }

    public Annotation getAnnotation(String attrValue) {
        return attrs.get(attrValue);
    }

    public static AttrSet getEmpty() {
        return new AttrSet(new TreeMap<String, Annotation>());
    }

    public void intersect(AttrSet other) {
        return;
    }
}
