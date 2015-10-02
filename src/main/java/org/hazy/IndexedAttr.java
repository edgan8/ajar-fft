package org.hazy;

/**
 * Created by egan on 10/1/15.
 */
public class IndexedAttr {
    public String attrBase;
    public int idx;
    public IndexedAttr(String attrBase, int idx) {
        this.attrBase = attrBase;
        this.idx = idx;
    }
    public IndexedAttr(String encoded) {
        int separatorIdx = encoded.indexOf('$');
        String baseAttr = encoded.substring(0, separatorIdx);
        int idx = Integer.parseInt(encoded.substring(separatorIdx + 1));
        this.attrBase = baseAttr;
        this.idx = idx;
    }
    public static boolean isIndexed(String encoded) {
        return encoded.contains("$");
    }
    public boolean equals(IndexedAttr other) {
        return this.attrBase.equals(other.attrBase) &&
                this.idx == other.idx;
    }
    public boolean match(String attrBase, int idx) {
        return this.attrBase.equals(attrBase) && this.idx == idx;
    }
    public String toString() {
        return attrBase+"$"+idx;
    }
}
