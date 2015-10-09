package org.hazy;

import java.util.*;

/**
 * Created by egan on 9/28/15.
 */
public class LFTJoin {
    private final ArrayList<String> attrOrdering;

    public LFTJoin(ArrayList<String> attrOrdering) {
        this.attrOrdering = new ArrayList<>(attrOrdering);
    }

    public Relation run(ArrayList<Relation> relations) throws Exception {
        Relation output = new RelationTrie(attrOrdering);
        join(relations, output, Tuple.empty(), 0);
        return output;
    }

    private void join(
            ArrayList<Relation> relations,
            Relation output,
            Tuple t,
            int attrIndex
    ) throws Exception {
        if (attrIndex == attrOrdering.size()) {
            Annotation a = null;
            for (Relation rTrie : relations) {
                Annotation rAnnot = rTrie.getAnnotation(t);
                if (a == null) {
                    a = rAnnot;
                } else {
                    a = a.mult(rAnnot);
                }
            }
            t = (new Tuple(t)).setAnnot(a);
            output.insert(t);
            return;
        }

        String curAttribute = attrOrdering.get(attrIndex);
        ArrayList<AttrSet> aSets = new ArrayList<>();

        for (Relation rTrie : relations) {
            if (rTrie.hasAttribute(curAttribute)) {
                aSets.add(rTrie.index(t, curAttribute));
            }
        }
        if (aSets.size() == 0) {
            throw new Exception("There should exist a relation for each attribute.");
        }
        Set<String> intersectionSet = new TreeSet<>(aSets.get(0).getValues());
        for (int i = 1; i < aSets.size(); i++) {
            aSets.get(i).filter(intersectionSet);
        }

        for (String attrValue : intersectionSet) {
            // run some selections on the relations to optimize
            ArrayList<Relation> newRelations = new ArrayList<>(relations.size());
            for (Relation rel : relations) {
                if (rel.supportsSelect() && rel.hasAttribute(curAttribute)) {
                    // value must exist since we calculated intersection
                    newRelations.add(rel.select(curAttribute, attrValue));
                } else {
                    newRelations.add(rel);
                }
            }

            t.append(curAttribute, attrValue);
            join(newRelations,
                    output,
                    t,
                    attrIndex + 1
            );
            t.remove(curAttribute);
        }
    }
}
