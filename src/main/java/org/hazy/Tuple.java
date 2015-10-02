package org.hazy;

import java.util.*;

/**
 * Immutable Tuple class
 * Created by egan on 9/28/15.
 */
public class Tuple {
    public final Map<String, String> attrs;
    public final Annotation annot;

    public static Tuple empty() {
        return new Tuple(new HashMap<>(), null);
    }

    public Tuple(Map<String, String> attrs, Annotation a) {
        this.attrs = attrs;
        this.annot = a;
    }
    public Tuple(Map<String, String> attrs) {
        this.attrs = attrs;
        this.annot = null;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public Tuple setAnnot(Annotation a) {
        return new Tuple(attrs, a);
    }

    public Annotation getAnnot() {
        return annot;
    }

    public Tuple append(String attrName, String attrValue) {
        Map<String, String> newAttrMap = new HashMap<>(attrs);
        newAttrMap.put(attrName, attrValue);
        return new Tuple(newAttrMap, annot);
    }

    public boolean containsAttr(String attrName) {
        return attrs.containsKey(attrName);
    }

    public String getAttrValue(String attrName) {
        return attrs.get(attrName);
    }

    public boolean match(Tuple other) {
        Set<String> attrNames = attrs.keySet();
        boolean matchesAllShared = true;
        for(String attrName : attrNames) {
            if (other.attrs.containsKey(attrName)) {
                matchesAllShared &= getAttrValue(attrName).equals(other.getAttrValue(attrName));
            }
        }
        return matchesAllShared;
    }

    public boolean equalAttrs(Tuple other) throws IndexOutOfBoundsException {
        Set<String> attrNames = attrs.keySet();
        for (String attrName : attrNames) {
            if (!other.containsAttr(attrName)) {
                return false;
            }
            if (!other.getAttrValue(attrName).equals(getAttrValue(attrName))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String attrName : attrs.keySet()) {
            s.append(attrName+":"+attrs.get(attrName)+",");
        }
        if (annot != null) {
            s.append("ANNOT:"+annot.toString());
        }
        return s.toString();
    }
}
