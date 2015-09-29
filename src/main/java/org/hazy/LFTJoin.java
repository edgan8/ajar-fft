package org.hazy;

import java.util.*;

/**
 * Created by egan on 9/28/15.
 */
public class LFTJoin {
    private ArrayList<String> attrOrdering;

    public LFTJoin(ArrayList<String> attrOrdering) {
        this.attrOrdering = attrOrdering;
    }

    public RelationTrie run(
            ArrayList<RelationTrie> relations
    ) throws Exception {
        RelationTrie output = new RelationTreeImpl(attrOrdering);
        join(relations, output, Tuple.empty(), 0);
        return output;
    }

    private void join(
            ArrayList<RelationTrie> relations,
            RelationTrie output,
            Tuple t,
            int attrIndex
    ) throws Exception {
        if (attrIndex == attrOrdering.size()) {
            output.insert(t);
            return;
        }

        String curAttribute = this.attrOrdering.get(attrIndex);

        ArrayList<AttrSet> aSets = new ArrayList<>();
        for (RelationTrie rTrie : relations) {
            AttrSet aSet = rTrie.index(t, curAttribute);
            // null means doesn't refer to the attribute
            if (aSet != null) {
                aSets.add(aSet);
            }
        }
        if (aSets.size() == 0) {
            throw new Exception("should have a set");
        }
        AttrSet intersectionSet = new AttrSet(aSets.get(0));
        for (int i = 0; i < aSets.size(); i++) {
            intersectionSet.intersect(aSets.get(i));
        }
        Set<String> intersectedValues = intersectionSet.getValues();
        for (String attrValue : intersectedValues) {
            Tuple newTuple = t.append(curAttribute, attrValue, intersectionSet.getAnnotation(attrValue));
            join(relations,
                    output,
                    newTuple,
                    attrIndex + 1
            );
        }
    }
}
