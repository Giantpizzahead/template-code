package data_structs.trees;

import java.util.*;

/**
 * Template: Tree
 * A base tree where each node contains a value. Contains some generic algorithms for trees.
 *
 * Observations:
 * DFS seems to be faster than BFS in almost every case when traversing trees. So, use DFS over BFS!
 *
 */
public class BaseTree {
	
	int[] values, parents;
	ArrayList<Integer>[] children;
	int maxSize;
	
	BaseTree() {
		int treeSize = 200000;
		genRandomTree(treeSize);
		
		
		System.out.println("BFS:");
		doBFS();
		System.out.println("DFS:");
		doDFS();
		System.out.println("Done");
	}
	
	/**
	 * Finds the path from node 1 to node 2, and returns it in an ArrayList.
	 * Time complexity goal: O(log(N)) with precomputation of O(N*log(N)) 
	 */
	ArrayList<Integer> findPath(int n1, int n2) {
		return null;
	}
	
	/**
	 * Runs a BFS search through the tree, printing each value it comes across.
	 */
	void doBFS() {
		Queue<Integer> toEval = new LinkedList<Integer>();
		toEval.add(0);
		boolean[] visited = new boolean[maxSize];
		visited[0] = true;
		
		int curr;
		while (!toEval.isEmpty()) {
			curr = toEval.poll();
			// System.out.println("Value of node " + curr + ": " + values[curr]);
			
			// Add all unvisited neighbors to queue
			for (int neighbor : children[curr]) {
				if (!visited[neighbor]) {
					visited[neighbor] = true;
					toEval.add(neighbor);
				}
			}
		}
	}
	
	/**
	 * Runs a DFS search through the tree, printing each value it comes across.
	 */
	void doDFS() {
		Stack<Integer> toEval = new Stack<Integer>();
		toEval.add(0);
		boolean[] visited = new boolean[maxSize];
		visited[0] = true;
		
		int curr;
		while (!toEval.isEmpty()) {
			curr = toEval.pop();
			// System.out.println("Value of node " + curr + ": " + values[curr]);
			
			// Add all unvisited neighbors to queue
			for (int neighbor : children[curr]) {
				if (!visited[neighbor]) {
					visited[neighbor] = true;
					toEval.add(neighbor);
				}
			}
		}
	}
	
	/**
	 * Initializes the required arrays with the given maxSize.
	 */
	@SuppressWarnings("unchecked")
	void initTree(int maxSize) {
		this.maxSize = maxSize;
		values = new int[maxSize];
		parents = new int[maxSize];
		children = new ArrayList[maxSize];
		
		for (int i = 0; i < maxSize; i++) {
			children[i] = new ArrayList<Integer>();
			// All nodes start with no parent
			parents[i] = -1;
		}
	}
	
	/**
	 * Generates a random tree with the given treeSize (for testing).
	 */
	void genRandomTree(int treeSize) {
		initTree(treeSize);
		// Set all node values to their IDs
		for (int i = 0; i < treeSize; i++) {
			setValue(i, i);
		}
		
		// Make a random tree
		Random random = new Random();
		for (int i = 1; i < treeSize; i++) {
			setParent(i, random.nextInt(i));
		}
	}
	
	/**
	 * Sets the parent of a given node. Updates the children ArrayList of the parent if needed.
	 * 
	 * Note that if removing a node, this method will take O(N) time due to removing! Change the
	 * children data structure to a HashSet to fix this issue.
	 */
	void setParent(int node, int parent) {
		if (parents[node] != -1) {
			children[parents[node]].remove(node);
		}
		parents[node] = parent;
		children[parent].add(node);
	}
	
	/**
	 * Sets the value of a given node.
	 */
	void setValue(int node, int value) {
		values[node] = value;
	}
	
	public static void main(String[] args) {
		new BaseTree();
	}

}
