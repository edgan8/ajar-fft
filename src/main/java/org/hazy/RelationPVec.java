package org.hazy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by egan on 10/1/15.
 */
public class RelationPVec implements Relation {
    public String name;
    public Annotation[] values;
    public int base;
    public int numDigits;
    public int n;
    public TreeSet<String> completeValues;
    public ArrayList<String> attrNames;
    public Map<String, Integer> attrNumPower;

    public RelationPVec(String name, Annotation[] values, int base, int numDigits) {
        this.name = name;
        this.values = values;
        this.base = base;
        this.numDigits = numDigits;
        this.n = MathUtils.intPow(base, numDigits);
        this.completeValues = new TreeSet<>();
        for (int i = 0; i < base; i++) {
            this.completeValues.add(Integer.toString(i));
        }

        attrNames = new ArrayList<>();
        attrNumPower = new HashMap<>();
        for (int i = 0; i < numDigits; i++) {
            String attrName = new IndexedAttr(name, i).toString();
            attrNames.add(attrName);
            attrNumPower.put(attrName, MathUtils.intPow(base, i));
        }

    }

    public ArrayList<String> getAttributes() {
        return attrNames;
    }

    public ArrayList<Tuple> getTuples() {
        int[] digits = new int[30];
        ArrayList<Tuple> output = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            HashMap<String, String> attrValues = new HashMap<>();
            MathUtils.convertBaseN(i, base, digits);
            for (int j = 0; j < numDigits; j++){
                attrValues.put(
                        attrNames.get(j),
                        Integer.toString(digits[j])
                );
            }
            output.add(new Tuple(attrValues, values[i]));
        }
        return output;
    }

    public boolean hasAttribute(String attr) {
        if (IndexedAttr.isIndexed(attr)) {
            IndexedAttr iAttr = new IndexedAttr(attr);
            return (iAttr.attrBase.equals(name) && iAttr.idx >= 0 && iAttr.idx < numDigits);
        }
        return false;
    }

    public AttrSet index(Tuple t, String attr) {
        return new AttrSet(attr, completeValues);
    }

    public Annotation getAnnotation(Tuple t) {
        int tIntValue = 0;
        Map<String, String> attrs = t.getAttrs();
        for (String attrName : attrs.keySet()) {
            if (attrNumPower.containsKey(attrName)) {
                int attrValue = Integer.parseInt(attrs.get(attrName));
                tIntValue += attrValue * attrNumPower.get(attrName);
            }
        }
        return values[tIntValue];
    }

    @Override
    public Relation select(Tuple t) {
        for (String attrName : attrNames) {
            if (!t.containsAttr(attrName)) {
                return this;
            }
        }
        // if we are selecting all our attributes, return an empty relation
        return new RelationEmpty(getAnnotation(t));
    }

    @Override
    public boolean supportsSelect() {
        return false;
    }

    public void insert(Tuple t) {
        throw new UnsupportedOperationException("Can't insert into Pvec");
    }

    public Relation aggregate(String attr) {
        throw new UnsupportedOperationException("Can't aggregate into Pvec");
    }
}
