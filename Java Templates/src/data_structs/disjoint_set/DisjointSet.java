import java.util.*;

/**
 * My first (attempted) implementation of a disjoint set.
 * Negative numbers in the elements array represent the # of nodes under
 * that element's set tree, and they also mark that element as the root of a tree.
 * Positive numbers represent the parent of the element in the set tree.
 */
public class DisjointSet {
	int[] elements;
	int size;
	
	public DisjointSet(int size) {
		elements = new int[size];
		this.size = size;
		for (int i = 0; i < size; i++) {
			elements[i] = -1;
		}
	}
	
	/**
	 * Unions the two elements by combining their sets (if not already combined).
	 * @param a - The first element to union.
	 * @param b - The second element to union.
	 * @return Whether or not the two elements were merged successfully. If the two
	 * elements were already in the same set before the merge, false will be returned.
	 */
	public boolean union(int a, int b) {
		// Find the parent of both elements
		int parentA = find(a);
		int parentB = find(b);
		if (parentA == parentB) return false;
		else {
			// Merge both sets by setting the parent with less nodes to be a child of
			// the parent with more nodes
			if (elements[parentA] < elements[parentB]) {
				// parentA has more nodes
				elements[parentA] += elements[parentB];
				elements[parentB] = parentA;
			} else {
				// parentB has more nodes
				elements[parentB] += elements[parentA];
				elements[parentA] = parentB;
			}
			return true;
		}
	}
	
	/**
	 * Finds the set that the given element is in. Note that the returned set # is
	 * arbritrary unless compared with another element's set #.
	 * @param n - The element to find.
	 * @return The set # that n is in.
	 */
	public int find(int n) {
		// Keep track of all found elements to perform tree compression
		Stack<Integer> foundElements = new Stack<Integer>();
		while (elements[n] >= 0) {
			foundElements.add(n);
			n = elements[n];
		}
		
		// Perform tree compression
		while (!foundElements.isEmpty()) {
			elements[foundElements.pop()] = n;
		}
		
		return n;
	}
	
	/**
	 * Tester programs
	 */
	public static void interactiveTest() {
		DisjointSet set = new DisjointSet(20);
		Scanner s = new Scanner(System.in);
		String next;
		
		mainLoop:
		while (true) {
			System.out.println("Please input commands ([u]nion a b, [f]ind n, [p]rint, [q]uit)");
			next = s.next();
			switch (next.substring(0, 1)) {
			case "u":
				System.out.println(set.union(s.nextInt(), s.nextInt()));
				break;
			case "f":
				System.out.println(set.find(s.nextInt()));
				break;
			case "p":
				System.out.println("Current set structure:");
				System.out.println(Arrays.toString(set.elements));
				break;
			case "q":
				System.out.println("Goodbye!");
				break mainLoop;
			default:
				System.out.println("Unrecognized command " + next);
			}
		}
		
		s.close();
	}
	
	public static void automaticTest(int size, int ops, float rand, boolean randOn) {
		long startTime = System.currentTimeMillis();
		System.out.printf("Doing %d operations on a disjoint set of size %d\n", ops, size);
		System.out.printf("Approximately %d%% of the operations will be union\n", (int) (rand * 100 + 0.5));
		System.out.println("Using random: " + randOn);
		DisjointSet set = new DisjointSet(size);
		Random random = new Random();
		if (randOn) {
			for (int i = 0; i < ops; i++) {
				if (random.nextFloat() < rand) {
					// Union operation
					set.union(random.nextInt(size), random.nextInt(size));
				} else {
					// Find operation
					set.find(random.nextInt(size));
				}
				
				if (i % 1000000 == 0 && i != 0) {
					System.out.println("On operation " + i);
				}
			}
		} else {
			int nextRandNum = random.nextInt();
			int randLimit = (int) (Integer.MIN_VALUE + ((long) Integer.MAX_VALUE - Integer.MIN_VALUE) * rand);
			int temp;
			System.out.println("randLimit: " + randLimit);
			for (int i = 0; i < ops; i++) {
				nextRandNum = nextRand(nextRandNum);
				if (nextRandNum < randLimit) {
					// Union operation
					nextRandNum = nextRand(nextRandNum);
					temp = nextRandNum;
					nextRandNum = nextRand(nextRandNum);
					set.union(Math.abs(temp) % size, Math.abs(nextRandNum) % size);
				} else {
					// Find operation
					nextRandNum = nextRand(nextRandNum);
					set.find(Math.abs(nextRandNum) % size);
				}
				
				if (i % 1000000 == 0 && i != 0) {
					System.out.println("On operation " + i);
					// System.out.println("Rand num: " + nextRandNum);
				}
			}
		}
		
		// System.out.println("Final set structure:");
		// System.out.println(Arrays.toString(set.elements));
		System.out.printf("Done in %.3f seconds\n\n\n", (System.currentTimeMillis() - startTime) / 1000f);
	}
	
	public static int nextRand(int nextRandNum) {
		if (nextRandNum % 5 == 0) return nextRandNum * 3 - 12349817;
		else if (nextRandNum % 6 == 0) return nextRandNum * 8 - 382938219;
		else return nextRandNum + 338232381;
	}
	
	public static void main(String[] args) {
		automaticTest(5000000, 25000000, 0.9f, true);
		automaticTest(5000000, 25000000, 0.9f, false);
	}
}