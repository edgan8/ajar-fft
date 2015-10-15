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
        join(relations, output, Tuple.empty(), 0, null);
        return output;
    }

    private void join(
            ArrayList<Relation> relations,
            Relation output,
            Tuple t,
            int attrIndex,
            Annotation partialResult
    ) throws Exception {
        if (attrIndex == attrOrdering.size()) {
            if (relations.size() != 0) {
                System.out.println("wut");
                Annotation a = getAnnotation(relations, t);
                // TODO: look at this
                if (partialResult != null) {
                    output.insert(t.setAnnot(partialResult.mult(a)));
                }
            } else {
                output.insert(t.setAnnot(partialResult));
            }
            return;
        }

        String curAttribute = attrOrdering.get(attrIndex);
        Set<String> intersectionSet = getIntersectionSet(relations, t, curAttribute);

        for (String attrValue : intersectionSet) {
            t.append(curAttribute, attrValue);
            // run some selections on the relations to optimize
            ArrayList<Relation> subRelations = getSubRelations(relations, t);
            ArrayList<Relation> filteredRelations = new ArrayList<>();
            Annotation newPartialResult = partialResult;
            for (Relation r : subRelations) {
                if(r.getAttributes().size()==0) {
                    Annotation a = r.getAnnotation(t);
                    if (newPartialResult == null) {
                        newPartialResult = a;
                    } else {
                        newPartialResult = newPartialResult.mult(a);
                    }
                } else {
                    filteredRelations.add(r);
                }
            }

            join(filteredRelations,
                    output,
                    t,
                    attrIndex + 1,
                    newPartialResult
            );
            t.remove(curAttribute);
        }
    }

    private Set<String> getIntersectionSet(
            ArrayList<Relation> relations,
            Tuple t,
            String curAttribute) {
        ArrayList<AttrSet> aSets = new ArrayList<>();

        for (Relation rTrie : relations) {
            if (rTrie.hasAttribute(curAttribute)) {
                aSets.add(rTrie.index(t, curAttribute));
            }
        }
        if (aSets.size() == 0) {
            throw new UnsupportedOperationException(
                    "There should exist a relation for each attribute."
            );
        }
        Set<String> intersectionSet = new TreeSet<>(aSets.get(0).getValues());
        for (int i = 1; i < aSets.size(); i++) {
            aSets.get(i).filter(intersectionSet);
        }
        return intersectionSet;
    }

    private Annotation getAnnotation(
            ArrayList<Relation> relations,
            Tuple tupleAttrs) {
        Annotation a = null;
        int numRelations = relations.size();

        for (int i = 0; i < numRelations; ++i) {
            Relation rTrie = relations.get(i);
            Annotation rAnnot = rTrie.getAnnotation(tupleAttrs);
            if (a == null) {
                a = rAnnot;
            } else {
                a = a.mult(rAnnot);
            }
        }
        return a;
    }

    private ArrayList<Relation> getSubRelations(
            ArrayList<Relation> relations,
            Tuple t) {
        ArrayList<Relation> newRelations = new ArrayList<>(relations.size());
        for (Relation rel : relations) {
            // value must exist since we calculated intersection
            newRelations.add(rel.select(t));
        }
        return newRelations;
    }
}
