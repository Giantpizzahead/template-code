import java.util.*;

/**
 * Standard implementation of a segment tree with deltaing. Supports range update and range
 * query operations.
 *
 * To modify the operation (default is set i to v, and sum [l, r]), change the mergeVals()
 * and update() functions. The propagate function should also be changed. If needed, change
 * the 'null value' in query() to something else.
 *
 * Methods:
 * update(l, r, v) - Updates the range [l, r] with v. Runs in O(log(N)).
 * query(l, r) - Returns the query result from [l, r] inclusive. Runs in O(log(N)).
 *
 * Memory: O(8N)
 */

class SegmentTreeDelta {
    int size;
    int[] vals, delta;

    SegmentTreeDelta(int size) {
        this.size = size;
        vals = new int[size * 4 + 1];
        delta = new int[size * 4 + 1];
    }

    int mergeVals(int a, int b) {
        return a + b;
    }

    void propagate(int n) {
        if (delta[n] != 0) {
            vals[n*2] += delta[n];
            vals[n*2+1] += delta[n];
            delta[n*2] += delta[n];
            delta[n*2+1] += delta[n];
            delta[n] = 0;
        }
    }

    void update(int l, int r, int v) {
        update(1, 0, size - 1, l, r, v);
    }

    void update(int n, int lb, int ub, int l, int r, int v) {
        if (lb > r || ub < l) return;
        else if (lb >= l && ub <= r) {
            vals[n] += v;
            delta[n] += v;
            return;
        }
        propagate(n);
        update(n*2, lb, (lb+ub)/2, l, r, v);
        update(n*2+1, (lb+ub)/2+1, ub, l, r, v);
        vals[n] = mergeVals(vals[n*2], vals[n*2+1]);
    }

    int query(int l, int r) {
        return query(1, 0, size - 1, l, r);
    }

    int query(int n, int lb, int ub, int l, int r) {
        if (lb > r || ub < l) return 0;
        else if (lb >= l && ub <= r) return vals[n];
        propagate(n);
        return mergeVals(query(n*2, lb, (lb+ub)/2, l, r), query(n*2+1, (lb+ub)/2+1, ub, l, r));
    }
}
