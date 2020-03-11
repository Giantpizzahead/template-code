/**
 * Standard implementation of a BIT. Supports point updates and range queries.
 * By default, this BIT finds the sum of a range. This can easily edited by modifying
 * the add() and query0() methods (change the += to something else). Remember, this BIT
 * is one-indexed!!!
 *
 * Methods:
 * add(int i, int v) - Adds v to index i. Runs in O(log(N)).
 * query(int l, int r) - Returns the query result for [l, r] inclusive. Runs in O(log(N)).
 *
 * Memory: O(N)
 */

public class BIT {
    int size;
    int[] vals;

    BIT(int size) {
        this.size = size;
        vals = new int[size + 1];
    }

    void add(int i, int v) {
        while (i <= size) {
            vals[i] += v;
            i += Integer.lowestOneBit(i);
        }
    }

    int query0(int i) {
        int res = 0;
        while (i > 0) {
            res += vals[i];
            i -= Integer.lowestOneBit(i);
        }
        return res;
    }

    int query(int l, int r) {
        return query0(r) - query0(l-1);
    }
}
