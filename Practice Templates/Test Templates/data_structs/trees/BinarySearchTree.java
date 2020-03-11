package data_structs.trees;

import java.util.*;

public class BinarySearchTree <T extends Comparable<T>> {
    private Node root;
    int size;

    class Node {
        T value;
        Node left, right;
        Node(T value) {
            this.value = value;
        }
    }

    BinarySearchTree() {
        size = 0;
    }

    /**
     * Adds a value to the binary search tree. Returns false if the addition failed (value already exists).
     * @param value - The value to add to the BST.
     * @return Whether or not the addition was successful.
     */
    public boolean add(T value) {
        if (root == null) {
            // Initialize the root node
            root = new Node(value);
            size++;
            return true;
        }

        int oldSize = size;
        add(root, value);
        return oldSize != size;
    }

    private Node add(Node node, T value) {
        // If node is null, add the new node here
        if (node == null) {
            size++;
            return new Node(value);
        }

        // Decide which path to go down using comparator value
        int comp = value.compareTo(node.value);

        if (comp > 0) {
            node.right = add(node.right, value);
        } else if (comp < 0) {
            node.left = add(node.left, value);
        } else {
            // Node already exists
            return node;
        }

        // If it got this far, it went down a path; don't edit the previous nodes' structures
        return node;
    }

    /**
     * Checks if the given value exists in the binary search tree.
     * @param value - The value to check for in the BST.
     * @return Whether or not the value was found.
     */
    public boolean contains(T value) {
        return contains(root, value);
    }

    private boolean contains(Node node, T value) {
        // If node is null, reached end of BST without finding value
        if (node == null) return false;

        // Decide which path to go down using comparator value (or return true if the element is found)
        int comp = value.compareTo(node.value);

        if (comp > 0) return contains(node.right, value);
        else if (comp < 0) return contains(node.left, value);
        else return true;
    }

    /**
     * Removes the given value from the binary search tree (if it exists). If it doesn't exist, return false.
     * @param value - The value to remove from the BST.
     * @return Whether or not the value was successfully removed.
     */
    public boolean remove(T value) {
        int oldSize = size;
        root = remove(root, value);
        return oldSize != size;
    }

    private Node remove(Node node, T value) {
        // If node is null, value doesn't exist
        if (node == null) return null;

        // Decide which path to go down using comparator value
        int comp = value.compareTo(node.value);

        if (comp > 0) {
            node.right = remove(node.right, value);
        } else if (comp < 0) {
            node.left = remove(node.left, value);
        } else {
            // Found node to remove
            size--;
            if (node.left == null) {
                // Case where either node has no subtrees, or node only has right subtree; take right node to satisfy
                // both cases
                return node.right;
            } else if (node.right == null) {
                // Case where node has only left subtree
                return node.left;
            } else {
                // Case where node has both subtrees; remove node from the left subtree arbitrarily
                Node maxNode = getMaxNode(node.left);
                node.value = maxNode.value;
                node.left = remove(node.left, maxNode.value);
                size++;
                return node;
            }
        }

        // If it got this far, it went down a path; don't edit the previous nodes' structures
        return node;
    }

    /**
     * Finds the max node in a given subtree.
     */
    private Node getMaxNode(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    public int size() {
        return size;
    }

    /**
     * Converts the BST to a printable format (prints values in sorted order).
     */
    @Override
    public String toString() {
        if (size == 0) return "BST[]";
        StringBuilder sb = new StringBuilder("BST[");
        printSortedTree(root, sb);
        return sb.delete(sb.length() - 2, sb.length()).append("]").toString();
    }

    private void printSortedTree(Node node, StringBuilder sb) {
        // First add all nodes on the left, then add the current node, then add all the nodes on the right
        // This will get all the elements in sorted order
        if (node == null) return;
        printSortedTree(node.left, sb);
        sb.append(node.value).append(", ");
        printSortedTree(node.right, sb);
    }

    public static void main(String[] args) {
        testBST(100000, 0.3d, 0.3d);
    }

    /**
     * Note: All times written here are impacted by the HashSet. Seems to roughly take about 1/6 of the time recorded
     * here, so actual runtime of the BST is a bit better than shown here.
     * Basic implementation of BST completed (Insert / query only)
     * Test 1 (Insert / Query, 2000000, 0.3d) -> 1.9 seconds
     * Addition sped up by not calling contains
     * Test 2 (Insert / Query, 2000000, 0.3d) -> 1.7 seconds
     * Removal added
     * Test 3 (Insert / Query / Remove, 2000000, 0.3d, 0.3d) -> 1.62 seconds
     * To make sure logarithmic behavior is proportional to BST size
     * Test 4 (Insert / Query / Remove, Max value 100000, 5000000, 0.6d, 0.95d) -> 1.30 seconds
     * Check usefulness in comp programming
     * Test 5 (Insert / Query / Remove, Max value 10^9, 500000, 0.7d, 0.1d) -> 0.295 seconds (Good!)
     *
     * Proof of AVL tree's time improvement in edge case
     * Test 6 (Insert / Query / Remove, Value being used is always i, 100000, 0.3d, 0.3d) -> StackOverflowError without
     * raising stack size; if stack size raised, then time = 17.5 seconds
     */
    private static void testBST(int numOperations, double chanceQuery, double chanceRemove) {
        System.out.println("Running " + numOperations + " operations on BST with query chance " + chanceQuery + " and remove chance " + chanceRemove);
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        // Confirm BST's effectiveness using a HashSet
        HashSet<Integer> hashset = new HashSet<>();
        Random rand = new Random();
        long startTime = System.currentTimeMillis();
        int MAX_INT = Integer.MAX_VALUE;

        for (int i = 0; i < numOperations; i++) {
            if (i != 0 && i % 100000 == 0) System.out.println("On operation " + i);
            if (Math.random() < chanceQuery) {
                // Query
                int toQuery = rand.nextInt(MAX_INT);
                if (bst.contains(toQuery) ^ hashset.contains(toQuery)) {
                    // Test failed
                    System.out.println("Operation #" + (i+1) + " failed!!!");
                    System.out.println("Attempted to query element " + toQuery);
                    System.out.println("BST structure: " + bst);
                    System.out.println("HashSet structure: " + hashset);
                    return;
                }
            } else if (Math.random() < chanceRemove) {
                // Remove
                int toRemove = rand.nextInt(MAX_INT);
                if (bst.remove(toRemove) ^ hashset.remove(toRemove)) {
                    // Test failed
                    System.out.println("Operation #" + (i+1) + " failed!!!");
                    System.out.println("Attempted to remove element " + toRemove);
                    System.out.println("BST structure: " + bst);
                    System.out.println("HashSet structure: " + hashset);
                    return;
                }
            } else {
                // Add
                int toAdd = rand.nextInt(MAX_INT);
                if (bst.add(toAdd) ^ hashset.add(toAdd)) {
                    // Test failed
                    System.out.println("Operation #" + (i+1) + " failed!!!");
                    System.out.println("Attempted to add element " + toAdd);
                    System.out.println("BST structure: " + bst);
                    System.out.println("HashSet structure: " + hashset);
                    return;
                }
            }
        }

        long timeTaken = System.currentTimeMillis() - startTime;
        System.out.println("BST size: " + bst.size());
        System.out.println("HashSet size: " + hashset.size());
        System.out.printf("Time taken: %.3f seconds", timeTaken / 1000f);
    }
}