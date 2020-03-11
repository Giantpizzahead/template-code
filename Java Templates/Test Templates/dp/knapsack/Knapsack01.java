package dp.knapsack;

import java.util.*;

/**
 * Template: Knapsack 0-1
 * A dynamic programming implementation of the 0-1 Knapsack problem. This version assumes
 * that there are N items, each with a cost C, and a value V. The goal is to take items to
 * maximize the value without going over a total cost M. The 0-1 Knapsack problem states that
 * each item can only be taken once, and that the whole item must be taken.
 * There are multiple methods to solve this problem. Their stats are shown below.
 *
 * Method A
 * Runtime: O(NM) where N = # of items and M = Max cost allowed
 * Memory: O(NM) [Can be improved to O(M) if not keeping track of what items need to be picked]
 *
 * Method B
 * Runtime:
 * Memory:
 *
 * Example:
 * 			Items
 * Item	#	Cost	Value
 * 	1		10		10
 * 	2		5		15
 * 	3		15		20
 * 	4		13		25
 * Max cost allowed = 25
 * Solution = Take items 2 and 4 for a final cost of 18, and the maximum possible value of 40.
 */
public class Knapsack01 {
	
	public static final int INF = Integer.MAX_VALUE;

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int numItems, maxCost, totalCost, totalValue;
		int[] costs, values, itemsToPick;
		
		while (true) {
			// Ask for # of items
			System.out.print("# of items (-1 to quit): ");
			numItems = s.nextInt();
			if (numItems == -1) break;
			costs = new int[numItems];
			values = new int[numItems];
			
			// Parse all item costs and values
			for (int i = 0; i < numItems; i++) {
				System.out.print("Item " + (i+1) + " cost / value: ");
				costs[i] = s.nextInt();
				values[i] = s.nextInt();
			}
			
			// Ask for max possible cost
			System.out.print("Max cost: ");
			maxCost = s.nextInt();
			
			// Do the 0-1 Knapsack
			itemsToPick = knapsack01(costs, values, maxCost);
			// Print all found info
			totalCost = 0;
			totalValue = 0;
			System.out.println();
			System.out.println("Solution:");
			for (int i : itemsToPick) {
				System.out.printf("Pick item %d (Cost %d, Value %d)\n", i+1, costs[i], values[i]);
				totalCost += costs[i];
				totalValue += values[i];
			}
			System.out.println("Total cost: " + totalCost);
			System.out.println("Total value: " + totalValue);
			System.out.println();
		}
		
		s.close();
	}
	
	/**
	 * Runs the standard 0-1 Knapsack algorithm (dynamic programming).
	 * Uses tabulation with two optimized 1D dp arrays. One keeps track of
	 * the best value found, while the other keeps track of which items were
	 * picked to get that best value. If there are multiple optimal sets of items
	 * to pick, returns the one with the least lexicographical ordering.
	 */
	public static int[] knapsack01(int[] costs, int[] values, int maxCost) {
		
		// #TODO: Keep one integer for from dp array instead of arraylist, use that to
		// backtrack, reducing memory to O(M)
		
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] dpItems = new ArrayList[maxCost + 1];
		for (int i = 0; i < maxCost + 1; i++) {
			dpItems[i] = new ArrayList<Integer>();
		}
		int[] dp = new int[maxCost + 1];
		int bestValue = -INF, bestLoc = -1;
		
		// Main dp loop
		for (int i = 0; i < costs.length; i++) {
			for (int j = maxCost; j >= 0; j--) {
				// Check if item can be used, and if the resulting value is better than the current one
				if (j - costs[i] >= 0 && dp[j-costs[i]] + values[i] > dp[j]) {
					// Use the item
					dp[j] = dp[j-costs[i]] + values[i];
					dpItems[j] = new ArrayList<Integer>(dpItems[j-costs[i]]);
					dpItems[j].add(i);
					
					if (dp[j] > bestValue) {
						// New best value found; keep track of it
						bestValue = dp[j];
						bestLoc = j;
					}
				}
			}
		}
		
		// Generate items to pick array
		int[] bestItems = new int[dpItems[bestLoc].size()];
		for (int i = 0; i < bestItems.length; i++) {
			bestItems[i] = dpItems[bestLoc].get(i);
		}
		
		return bestItems;
	}

}
