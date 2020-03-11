import java.util.*;

/**
 * Standard implementation of a disjoint set, also known as the union-find data structure.
 *
 * Methods:
 * find(int i) - Returns the set id that i is in. Runs in amortized O(1).
 * union(int a, int b) - Unions index a and index b. Returns the id of the set they were
 * unioned into. Runs in amortized O(1).
 *
 * Memory: O(N)
 */

class DisjointSet {
    int size;
    int[] vals;

    DisjointSet(int size) {
        this.size = size;
        vals = new int[size];
        Arrays.fill(vals, -1);
    }

    int union(int a, int b) {
        int setA = find(a);
        int setB = find(b);
        if (setA == setB) return setA;

        if (vals[setA] < vals[setB]) {
            vals[setA] += vals[setB];
            vals[setB] = setA;
            return setA;
        } else {
            vals[setB] += vals[setA];
            vals[setA] = setB;
            return setB;
        }
    }

    int find(int i) {
        if (vals[i] < 0) return i;
        else {
            int res = find(vals[i]);
            vals[i] = res;
            return res;
        }
    }
}
