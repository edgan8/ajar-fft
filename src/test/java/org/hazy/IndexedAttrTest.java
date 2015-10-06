package org.hazy;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by egan on 9/29/15.
 */
public class IndexedAttrTest {
    @Test
    public void simpleParsing() {
        IndexedAttr iAttr = new IndexedAttr("Y$1");
        assertEquals(1, iAttr.idx);
        assertTrue(IndexedAttr.isIndexed(iAttr.toString()));
    }
}
