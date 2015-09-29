package org.hazy;

import java.util.*;

/**
 * Created by egan on 9/28/15.
 */
public interface RelationTrie {
    ArrayList<Tuple> getTuples();
    boolean hasAttribute(String attr);
    AttrSet index(Tuple t, String attr);
    void insert(Tuple t);
}
