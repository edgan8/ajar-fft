package org.hazy;

import java.util.*;

/**
 * Mutable Tuple class
 * Created by egan on 9/28/15.
 */
public class Tuple {
    public Map<String, String> attrs;
    public Annotation annot;

    public static Tuple empty() {
        return new Tuple(new HashMap<>(), null);
    }

    public Tuple(Map<String, String> attrs, Annotation a) {
        this.attrs = new HashMap<>(attrs);
        this.annot = a;
    }
    public Tuple(Map<String, String> attrs) {
        this.attrs = new HashMap<>(attrs);
        this.annot = null;
    }
    public Tuple(Tuple base) {
        this.attrs = new HashMap<>(base.attrs);
        this.annot = base.annot;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public Tuple setAnnot(Annotation a) {
        annot = a;
        return this;
    }

    public Annotation getAnnot() {
        return annot;
    }

    public Tuple append(String attrName, String attrValue) {
        attrs.put(attrName, attrValue);
        return this;
    }

    public Tuple remove(String attrName) {
        attrs.remove(attrName);
        return this;
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

    public boolean equals(Object otherObj) throws IndexOutOfBoundsException {
        if (!(otherObj instanceof Tuple)) {
            return false;
        }
        Tuple other = (Tuple)otherObj;
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
    public int hashCode() {
        StringBuilder s = new StringBuilder();
        for (String attrName : attrs.keySet()) {
            s.append(attrName+":"+attrs.get(attrName)+",");
        }
        return s.toString().hashCode();
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
