package data_structs.trees;

import java.util.*;

class SegmentTree {
    private int size, MAX_SIZE;
    private int[] lb, ub, delta;
    public int[] vals;
    // Set this based on the operation that you're doing
    private final int NULL_VAL = 0;

    SegmentTree(int size) {
        this.size = size;
        this.MAX_SIZE = size * 4 + 1;
        lb = new int[MAX_SIZE];
        ub = new int[MAX_SIZE];
        delta = new int[MAX_SIZE];
        vals = new int[MAX_SIZE];
        Arrays.fill(delta, NULL_VAL);
        Arrays.fill(vals, NULL_VAL);
        initTree(1, 0, size-1);
    }

    // Update this based on what operation you're doing
    private int calcValue(int a, int b) {
        return Math.min(a, b);
    }

    public void update(int lower, int upper, int value) {
        update(1, lower, upper, value);
    }

    private int update(int node, int lower, int upper, int value) {
        if (lb[node] > upper || ub[node] < lower) return NULL_VAL;  // Out of range
        else if (lb[node] >= lower && ub[node] <= upper) {
            // Completely in range
            delta[node] = calcValue(delta[node], value);
            return calcValue(vals[node], delta[node]);
        }

        // Propagate changes
        propagate(node);

        // Recurse
        int leftVal = update(node * 2, lower, upper, value);
        int rightVal = update(node * 2 + 1, lower, upper, value);

        // Update this node's value
        vals[node] = calcValue(leftVal, rightVal);
        return vals[node];
    }

    public int query(int lower, int upper) {
        return query(1, lower, upper);
    }

    private int query(int node, int lower, int upper) {
        if (lb[node] > upper || ub[node] < lower) return NULL_VAL;  // Out of range
        else if (lb[node] >= lower && ub[node] <= upper) {
            // Completely in range
            return calcValue(vals[node], delta[node]);
        }

        // Propagate changes
        propagate(node);

        // Recurse
        int childrenVal = calcValue(query(node * 2, lower, upper), query(node * 2 + 1, lower, upper));
        return calcValue(vals[node], childrenVal);
    }

    private void propagate(int node) {
        vals[node] = calcValue(vals[node], delta[node]);
        if (lb[node] != ub[node]) {
            delta[node * 2] = calcValue(delta[node * 2], delta[node]);
            delta[node *2 + 1] = calcValue(delta[node * 2 + 1], delta[node]);
        }
        delta[node] = NULL_VAL;
    }

    private void initTree(int node, int lower, int upper) {
        lb[node] = lower;
        ub[node] = upper;
        if (lower != upper) {
            // Recursively init lower nodes
            initTree(node * 2, lower, (lower + upper) / 2);
            initTree(node * 2 + 1, (lower + upper) / 2 + 1, upper);
        }
    }
}
