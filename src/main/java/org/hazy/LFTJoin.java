package org.hazy;

import java.util.*;

/**
 * Created by egan on 9/28/15.
 */
public class LFTJoin {
    private final ArrayList<String> attrOrdering;
    // which relations are done by each point in the attr ordering
    public boolean[][] relationsDone;

    public LFTJoin(ArrayList<String> attrOrdering) {
        this.attrOrdering = new ArrayList<>(attrOrdering);
    }

    public Relation run(ArrayList<Relation> relations) throws Exception {
        System.out.println(attrOrdering);
        Relation output = new RelationTrie(attrOrdering);
        calcDonePoints(relations);
        join(relations, output, Tuple.empty(), 0, null);
        return output;
    }

    private void calcDonePoints(ArrayList<Relation> relations) {
        relationsDone = new boolean[attrOrdering.size()][relations.size()];
        for (int attrIndex = 0; attrIndex < attrOrdering.size(); attrIndex++) {
            List<String> curAttrs = attrOrdering.subList(0, attrIndex + 1);
            for (int i = 0; i < relations.size(); i++) {
                Relation r = relations.get(i);
                if (curAttrs.containsAll(r.getAttributes())) {
                    relationsDone[attrIndex][i] = true;
                }
            }
        }
    }

    private void join(
            ArrayList<Relation> relations,
            Relation output,
            Tuple t,
            int attrIndex,
            Annotation partialResult
    ) throws Exception {
        if (attrIndex == attrOrdering.size()) {
            output.insert(t.setAnnot(partialResult));
            return;
        }

        String curAttribute = attrOrdering.get(attrIndex);
        Set<String> intersectionSet = getIntersectionSet(relations, t, curAttribute);
        ArrayList<Relation> newRelations = new ArrayList<>(relations);

        for (String attrValue : intersectionSet) {
            t.append(curAttribute, attrValue);

            Annotation newPartialResult = getAnnotations(
                    relations,
                    newRelations,
                    t,
                    attrIndex,
                    partialResult
            );

            join(newRelations,
                    output,
                    t,
                    attrIndex + 1,
                    newPartialResult
            );
            t.remove(curAttribute);
        }
    }

    private Annotation getAnnotations(
            ArrayList<Relation> relations,
            ArrayList<Relation> newRelations,
            Tuple t,
            int attrIndex,
            Annotation partialResult) {
        int numRelations = relations.size();
        for (int rIndex = 0; rIndex < numRelations; rIndex++) {
            // if the relation should be done by now, the upper level should have already processed it
            if (attrIndex > 0 && relationsDone[attrIndex-1][rIndex]) {
                continue;
            } else {
                Relation r = relations.get(rIndex);
                if (relationsDone[attrIndex][rIndex]) {
                    Annotation a = r.getAnnotation(t);
                    if (partialResult == null) {
                        partialResult = a;
                    } else {
                        partialResult = partialResult.mult(a);
                    }
                    continue;
                }
                else if (r.supportsSelect()) {
                    newRelations.set(rIndex, r.select(t));
                }
            }
        }
        return partialResult;
    }

    private Set<String> getIntersectionSet(
            ArrayList<Relation> relations,
            Tuple t,
            String curAttribute) {
        Set<String> intersectionSet = null;

        for (Relation rTrie : relations) {
            if (rTrie.hasAttribute(curAttribute)) {
                AttrSet curAttrSet = rTrie.index(t, curAttribute);
                if (intersectionSet == null) {
                    intersectionSet = new TreeSet<>(curAttrSet.getValues());
                } else {
                    curAttrSet.filter(intersectionSet);
                }
            }
        }
        if (intersectionSet == null) {
            throw new UnsupportedOperationException(
                    "There should exist a relation for each attribute."
            );
        }
        return intersectionSet;
    }
}
