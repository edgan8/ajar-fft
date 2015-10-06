package org.hazy;

import java.util.*;

/**
 * Stores sets of tuples, should only be modified during creation
 * Created by egan on 9/28/15.
 */
interface Relation {
    ArrayList<Tuple> getTuples();
    boolean hasAttribute(String attr);
    ArrayList<String> getAttributes();
    /**
     * Returns all the values of attr which match tKey if applicable.
     * @param t fields on which the output must agree
     * @param attr attribute values we care about.
     * @return Set if the attribute is in the relation, emptyset if no matches.
     */
    AttrSet index(Tuple t, String attr);
    Annotation getAnnotation(Tuple t);

    void insert(Tuple t);
    Relation aggregate(String attr);
}
