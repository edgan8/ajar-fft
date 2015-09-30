package org.hazy;

/**
 * Created by egan on 9/29/15.
 */
public interface Annotation {
    Annotation zero();
    Annotation one();
    Annotation add(Annotation a);
    Annotation mult(Annotation a);
    Annotation copy();
    String toString();
}
