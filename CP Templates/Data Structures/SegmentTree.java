/**
 * Standard implementation of a segment tree. This segment tree does NOT have deltaing (check
 * SegmentTreeDelta for that). So, it only supports point updates and range queries.
 *
 * To modify the operation (default is set i to v, and sum [l, r]), change the mergeVals()
 * function, and change what happens when lb == ub in update(). You may need to change the
 * 'null value' in the query method too.
 *
 * Methods:
 * update(i, v) - Updates the value of index i to v (absolute, not relative!). Runs in O(log(N)).
 * query(l, r) - Returns the query result from [l, r] inclusive. Runs in O(log(N)).
 *
 * Memory: O(4N)
 */

class SegmentTree {
    int size;
    int[] vals;

    SegmentTree(int size) {
        this.size = size;
        vals = new int[size * 4 + 1];
    }

    int mergeVals(int a, int b) {
        return a + b;
    }

    void update(int i, int v) {
        update(1, 0, size - 1, i, v);
    }

    void update(int n, int lb, int ub, int i, int v) {
        if (lb == ub) {
            vals[n] = v;
            return;
        }
        if ((lb+ub)/2 < i) update(n*2+1, (lb+ub)/2+1, ub, i, v);
        else update(n*2, lb, (lb+ub)/2, i, v);
        vals[n] = mergeVals(vals[n*2], vals[n*2+1]);
    }

    int query(int l, int r) {
        return query(1, 0, size - 1, l, r);
    }

    int query(int n, int lb, int ub, int l, int r) {
        if (lb > r || ub < l) return 0;
        else if (lb >= l && ub <= r) return vals[n];
        else return mergeVals(query(n*2, lb, (lb+ub)/2, l, r), query(n*2+1, (lb+ub)/2+1, ub, l, r));
    }
}
