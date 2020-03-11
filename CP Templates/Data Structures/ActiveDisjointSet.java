import java.util.*;

/**
 * Special implementation of the disjoint set, allowing you to turn indexes on manually. The
 * find method will return -1 for cells that aren't active, and it will refuse to merge two
 * cells if either one of them is not active. This also includes some helper methods for
 * tracking some count associated with the sets (normally used in conjunction with the
 * active feature).
 *
 * Edit the activate() and mergeCounts() functions to get the right counts when merging sets.
 *
 * Methods:
 * find(int i) - Returns the set id i is in, or -1 if it isn't active. Runs in amortized O(1).
 * union(int a, int b) - Unions index a and index b. Returns the id of the set they were
 * unioned into, or -1 if either a or b is inactive. Runs in amortized O(1).
 *
 * Memory: O(N)
 */

class ActiveDisjointSet {
    int size;
    int[] vals, count;
    boolean[] isActive;

    ActiveDisjointSet(int size) {
        this.size = size;
        vals = new int[size];
        Arrays.fill(vals, -1);
        count = new int[size];
        isActive = new boolean[size];
    }

    void activate(int i) {
        if (isActive[i]) return;
        isActive[i] = true;

        count[i] = 1;
    }

    int mergeCounts(int a, int b) {
        return a + b;
    }

    int union(int a, int b) {
        int setA = find(a);
        int setB = find(b);
        if (setA == -1 || setB == -1) return -1;
        if (setA == setB) return setA;

        if (vals[setA] < vals[setB]) {
            count[setA] = mergeCounts(setA, setB);
            count[setB] = 0;
            vals[setA] += vals[setB];
            vals[setB] = setA;
            return setA;
        } else {
            count[setB] = mergeCounts(setB, setA);
            count[setA] = 0;
            vals[setB] += vals[setA];
            vals[setA] = setB;
            return setB;
        }
    }

    int find(int i) {
        if (vals[i] < 0) return (isActive[i] ? i : -1);
        else {
            int res = find(vals[i]);
            vals[i] = res;
            return res;
        }
    }
}
