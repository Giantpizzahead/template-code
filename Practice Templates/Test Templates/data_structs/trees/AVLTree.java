package data_structs.trees;

import java.util.*;

public class AVLTree <T extends Comparable<T>> {
    Node root;
    int size;
    boolean hasChanged;

    class Node {
        Node left, right;
        T value;
        int height, bf;

        Node(T value) {
            this.value = value;
            height = 0;
            bf = 0;
        }
    }

    AVLTree() {
        size = 0;
    }

    /**
     * Adds a given value to the BBST. Returns whether or not the addition was successful (value didn't already exist).
     * @param value - The value to add to the BBST
     * @return Whether or not the addition was successful (value didn't already exist).
     */
    public boolean add(T value) {
        // Special case for uninitialized BBST
        if (size == 0) {
            root = new Node(value);
            size++;
            return true;
        }

        hasChanged = false;
        root = add(root, value);
        return hasChanged;
    }

    private Node add(Node node, T value) {
        if (node == null) {
            hasChanged = true;
            size++;
            return new Node(value);  // Insert at the right location in the BBST
        }

        int comp = value.compareTo(node.value);

        if (comp > 0) {
            // Value is greater, so on right side
            node.right = add(node.right, value);
        } else if (comp < 0) {
            // Value is less, so on left side
            node.left = add(node.left, value);
        } else {
            // Found the value; don't do anything and quit
            return node;
        }

        if (hasChanged) {
            // Update the height and balance factor of this node
            update(node);
            // System.out.println("Inserting " + value + ": update " + node.value + " height " + node.height + " bf " + node.bf);

            // Balance this node if necessary
            return balance(node);
        } else return node;
    }

    /**
     * Checks whether or not the given value exists in the BBST.
     * @param value - The value to check for in the BBST.
     * @return Whether or not the BBST contains the given value.
     */
    public boolean contains(T value) {
        return contains(root, value);
    }

    private boolean contains(Node node, T value) {
        if (node == null) return false;  // Reached end of BBST; value not found

        int comp = value.compareTo(node.value);

        if (comp > 0) {
            // Value is greater, so on right side
            return contains(node.right, value);
        } else if (comp < 0) {
            // Value is less, so on left side
            return contains(node.left, value);
        } else {
            // Found the value
            return true;
        }
    }

    /**
     * Removes the given value from the BBST (if it exists). Returns whether or not the value was deleted successfully
     * (false if it wasn't found).
     * @param value - The value to remove from the BBST.
     * @return Whether or not the value was found and removed successfully.
     */
    public boolean remove(T value) {
        hasChanged = false;
        root = remove(root, value);
        return hasChanged;
    }

    private Node remove(Node node, T value) {
        if (node == null) return null;  // Value not found in BBST

        int comp = value.compareTo(node.value);

        if (comp > 0) {
            // Value is greater, so on right side
            node.right = remove(node.right, value);
        } else if (comp < 0) {
            // Value is less, so on left side
            node.left = remove(node.left, value);
        } else {
            // Found the value; remove it
            hasChanged = true;
            size--;
            if (node.left == null) {
                // Node either has only right subtree or no subtrees
                return node.right;
            } else if (node.right == null) {
                // Node has only left subtree
                return node.left;
            } else {
                // Node has both a left and right subtree; Choose which subtree to remove from via a height heuristic
                size++;
                Node toReplace;
                if (node.left.height >= node.right.height) {
                    // Remove from left subtree
                    toReplace = findMaxNode(node.left);
                    node.value = toReplace.value;
                    node.left = remove(node.left, toReplace.value);
                } else {
                    // Remove from right subtree
                    toReplace = findMinNode(node.right);
                    node.value = toReplace.value;
                    node.right = remove(node.right, toReplace.value);
                }
            }
        }

        if (hasChanged) {
            // Update the height and balance factor of this node
            update(node);
            // System.out.println("Inserting " + value + ": update " + node.value + " height " + node.height + " bf " + node.bf);

            // Balance this node if necessary
            return balance(node);
        } else return node;
    }

    Node findMinNode(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    Node findMaxNode(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    /*--------------BALANCING METHODS---------------*/

    /**
     * Updates the height and balance factor of this node using the left and right nodes.
     */
    void update(Node node) {
        int leftHeight = (node.left == null) ? -1 : node.left.height;
        int rightHeight = (node.right == null) ? -1 : node.right.height;
        node.height = Math.max(leftHeight, rightHeight) + 1;
        node.bf = rightHeight - leftHeight;
    }

    /**
     * Balances the given node according to the AVL tree invariant using tree rotations.
     */
    private Node balance(Node node) {
        if (Math.abs(node.bf) <= 1) return node;  // No balancing needs to be done

        // System.out.println("balance " + node.value);
        if (node.bf == 2) {
            // Right subtree needs to be edited
            if (node.right.bf >= 0) {
                // Right subtree of right subtree is longer (or same length); Right-right case
                return rightRightCase(node);
            } else {
                // Left subtree of right subtree is longer; Right-left case
                return rightLeftCase(node);
            }
        } else {
            // Left subtree needs to be edited
            if (node.left.bf > 0) {
                // Right subtree of left subtree is longer; Left-right case
                return leftRightCase(node);
            } else {
                // Left subtree of left subtree is longer (or same length); Left-left case
                return leftLeftCase(node);
            }
        }
    }

    private Node rightRightCase(Node node) {
        return leftRotation(node);
    }

    private Node leftLeftCase(Node node) {
        return rightRotation(node);
    }

    private Node rightLeftCase(Node node) {
        node.right = rightRotation(node.right);
        return rightRightCase(node);
    }

    private Node leftRightCase(Node node) {
        node.left = leftRotation(node.left);
        return leftLeftCase(node);
    }

    private Node rightRotation(Node node) {
        Node newCenter = node.left;
        node.left = newCenter.right;
        newCenter.right = node;
        update(node);
        update(newCenter);
        return newCenter;
    }

    private Node leftRotation(Node node) {
        Node newCenter = node.right;
        node.right = newCenter.left;
        newCenter.left = node;
        update(node);
        update(newCenter);
        return newCenter;
    }

    /*--------------DEBUG METHODS---------------*/

    /**
     * Checks if the balance factor of every node is either -1, 0, or 1 (AVL tree invariant).
     */
    public boolean checkBFs(Node node) {
        if (node == null) return true;
        return checkBFs(node.left) && checkBFs(node.right) && Math.abs(node.bf) <= 1;
    }

    /**
     * Converts the BST to a printable format (prints values in sorted order).
     */
    @Override
    public String toString() {
        if (size == 0) return "BBST[]";
        StringBuilder sb = new StringBuilder("BBST[");
        printSortedTree(root, sb);
        return sb.delete(sb.length() - 2, sb.length()).append("]").toString();
    }

    private void printSortedTree(Node node, StringBuilder sb) {
        // First add all nodes on the left, then add the current node, then add all the nodes on the right
        // This will get all the elements in sorted order
        if (node == null) return;
        printSortedTree(node.left, sb);
        sb.append("(").append(node.value).append(", ").append(node.height).append(", ").append(node.bf).append("), ");
        printSortedTree(node.right, sb);
    }

    public static void main(String[] args) {
        testBBST(5000000, 0.3d, 0.3d);
    }

    /**
     * Note: All times written here are impacted by the HashSet. Seems to roughly take about 1/6 of the time recorded
     * here, so actual runtime of the BBST is a bit better than shown here.
     * Basic implementation of BST completed (Insert / query only, no balancing)
     * Test 1 (Insert / Query, 2000000, 0.3d, 0.0d) -> 2.12 seconds
     * Basic balancing of BBST completed (Insert / query only)
     * Test 2 (Insert / Query, 2000000, 0.3d, 0.0d) -> 2.16 seconds
     * add() function optimized by not calling contains() unnecessarily
     * Test 3 (Insert / Query, 2000000, 0.3d, 0.0d) -> 2.07 seconds
     * BBST fully implemented (Insert / query / remove)
     * Test 4 (Insert / Query / Remove, 2000000, 0.3d, 0.3d) -> 1.93 seconds
     * Check for logarithmic behavior with # of nodes in tree
     * Test 5 (Insert / Query / Remove, Max value 100000, 5000000, 0.6d, 0.95d) -> 1.45 seconds
     * Check usefulness in comp programming
     * Test 6 (Insert / Query / Remove, Max value 10^9, 500000, 0.7d, 0.1d) -> 0.33 seconds (Good!)
     *
     * Proof of AVL tree's time improvement in edge case
     * Test 7 (Insert / Query / Remove, Value being used is always i, 100000, 0.3d, 0.3d) -> 0.032 seconds!
     * Test 8 (Insert / Query / Remove, Value being used is always i, 5000000, 0.3d, 0.3d) -> 0.97 seconds
     */
    private static void testBBST(int numOperations, double chanceQuery, double chanceRemove) {
        System.out.println("Running " + numOperations + " operations on BBST with query chance " + chanceQuery + " and remove chance " + chanceRemove);
        AVLTree<Integer> bbst = new AVLTree<>();
        // Confirm BBST's effectiveness using a HashSet
        HashSet<Integer> hashset = new HashSet<>();
        Random rand = new Random();
        long startTime = System.currentTimeMillis();
        int MAX_INT = Integer.MAX_VALUE;

        for (int i = 0; i < numOperations; i++) {
            if (i != 0 && i % 100000 == 0) System.out.println("On operation " + i);
            if (Math.random() < chanceQuery) {
                // Query
                int toQuery = rand.nextInt(MAX_INT);
                if (bbst.contains(toQuery) ^ hashset.contains(toQuery)) {
                    // Test failed
                    System.out.println("Operation #" + (i+1) + " failed!!!");
                    System.out.println("Attempted to query element " + toQuery);
                    System.out.println("BBST structure: " + bbst);
                    System.out.println("HashSet structure: " + hashset);
                    return;
                }
            } else if (Math.random() < chanceRemove) {
                // Remove
                int toRemove = rand.nextInt(MAX_INT);
                if (bbst.remove(toRemove) ^ hashset.remove(toRemove)) {
                    // Test failed
                    System.out.println("Operation #" + (i+1) + " failed!!!");
                    System.out.println("Attempted to remove element " + toRemove);
                    System.out.println("BBST structure: " + bbst);
                    System.out.println("HashSet structure: " + hashset);
                    return;
                }
            } else {
                // Add
                int toAdd = rand.nextInt(MAX_INT);
                if (bbst.add(toAdd) ^ hashset.add(toAdd)) {
                    // Test failed
                    System.out.println("Operation #" + (i+1) + " failed!!!");
                    System.out.println("Attempted to add element " + toAdd);
                    System.out.println("BBST structure: " + bbst);
                    System.out.println("HashSet structure: " + hashset);
                    return;
                }
            }
        }

        long timeTaken = System.currentTimeMillis() - startTime;
        System.out.println("BBST size: " + bbst.size);
        System.out.println("BBST height: " + bbst.root.height);
        System.out.println("BBST invariant satisfied: " + bbst.checkBFs(bbst.root));
        System.out.println("HashSet size: " + hashset.size());
        System.out.printf("Time taken: %.3f seconds", timeTaken / 1000f);
    }
}
