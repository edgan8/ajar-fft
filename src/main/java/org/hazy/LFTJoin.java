package org.hazy;

import java.util.*;

/**
 * Created by egan on 9/28/15.
 */
public class LFTJoin {
    private final String[] attrOrdering;

    public LFTJoin(String[] attrOrdering) {
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
        if (attrIndex == attrOrdering.length) {
            Annotation a = new IntAnnot(1);
            for (RelationTrie rTrie : relations) {
                a = a.mult(rTrie.getAnnotation(t));
            }
            t = t.setAnnot(a);
            output.insert(t);
            return;
        }

        String curAttribute = this.attrOrdering[attrIndex];

        ArrayList<AttrSet> aSets = new ArrayList<>();
        for (RelationTrie rTrie : relations) {
            if (rTrie.hasAttribute(curAttribute)) {
                aSets.add(rTrie.index(t, curAttribute));
            }
        }
        if (aSets.size() == 0) {
            throw new Exception("There should exist a relation for each attribute.");
        }
        AttrSet intersectionSet = new AttrSet(aSets.get(0));
        for (int i = 1; i < aSets.size(); i++) {
            intersectionSet.intersect(aSets.get(i));
        }
        Set<String> intersectedValues = intersectionSet.getValues();
        for (String attrValue : intersectedValues) {
            Tuple newTuple = t.append(curAttribute, attrValue);
            join(relations,
                    output,
                    newTuple,
                    attrIndex + 1
            );
        }
    }
}
