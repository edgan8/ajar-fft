package org.hazy;

import java.util.*;

/**
 * Created by egan on 9/28/15.
 */
public class Tuple {
    public Map<String, String> attrs;
    public Annotation annot;

    public static Tuple empty() {
        return new Tuple(new HashMap<>(), null);
    }

    public Tuple(Map<String, String> attrs, Annotation a) {
        this.attrs = attrs;
        this.annot = a;
    }

    public Annotation getAnnot() {
        return annot;
    }

    public Tuple append(String attrName, String attrValue, Annotation attrAnnot) {
        Map<String, String> newAttrMap = new HashMap<String, String>(attrs);
        newAttrMap.put(attrName, attrValue);
        Annotation newAnnot;
        if (annot == null) {
            newAnnot = attrAnnot;
        } else {
            newAnnot = annot.mult(attrAnnot);
        }

        return new Tuple(newAttrMap, newAnnot);
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

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String attrName : attrs.keySet()) {
            s.append(attrName+":"+attrs.get(attrName)+",");
        }
        return s.toString();
    }
}
