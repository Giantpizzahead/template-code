package dp.lcs;

import java.util.*;

/**
 * Template: LCS (Longest Common Substring)
 * A program that uses dynamic programming to find the longest common substring(s) of two strings.
 * The LCS must be contiguous in both strings.
 * Currently, the program returns an array with all the unique LCSs of the two strings.
 * Runtime: O(AB) where A = s1.length() and B = s2.length()
 * Memory: O(B)
 *
 * Examples:
 * LCS of "hello" and "jello" is "ello"
 * LCS of "jfjjfff" and "fjfjfj" is "jfj"
 * LCSs of "abcd" and "dcba" are "a", "b", "c", and "d"
 */
public class LCS {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		while (true) {
			// Ask for the two strings
			System.out.print("Enter string 1 (q to quit): ");
			String s1 = s.nextLine();
			if (s1.equalsIgnoreCase("q")) break;
			System.out.print("Enter string 2: ");
			String s2 = s.nextLine();
			
			// Find the longest common substring
			String[] lcs = findLCS(s1, s2);
			// Print out all found info
			if (lcs.length == 1) {
				System.out.printf("The LCS of the strings \"%s\" and \"%s\" is \"%s\".\n", s1, s2, lcs[0]);
			} else if (lcs.length == 0) {
				System.out.printf("The strings \"%s\" and \"%s\" do not have any letters in common.\n", s1, s2);
			} else {
				System.out.printf("The strings \"%s\" and \"%s\" have multiple LCSs. Here are all of them:\n", s1, s2);
				for (String l : lcs) {
					System.out.println("\"" + l + "\"");
				}
			}
			
			System.out.println();
		}
		
		s.close();
	}
	
	/**
	 * Finds the LCS of two strings using dynamic programming.
	 * Uses tabluation with an optimized, 1D dp array.
	 */
	public static String[] findLCS(String s1, String s2) {
		int[] dp = new int[s2.length() + 1];
		char[] s1Chars = s1.toCharArray(), s2Chars = s2.toCharArray();
		
		ArrayList<Integer> tempLCS = new ArrayList<Integer>();
		int maxLength = 0;
		// Main DP loop
		for (int i = 1; i < s1.length() + 1; i++) {
			for (int j = s2.length(); j > 0; j--) {
				if (s1Chars[i-1] == s2Chars[j-1]) {
					// Characters match; extend LCS at this location
					dp[j] = dp[j-1] + 1;
					
					if (dp[j] > maxLength) {
						// New best length found
						maxLength = dp[j];
						tempLCS.clear();
						// Record where found LCS is
						tempLCS.add(i);
					} else if (dp[j] == maxLength) {
						// Record where found LCS is
						tempLCS.add(i);
					}
				} else {
					// Characters don't match; reset LCS at this location
					dp[j] = 0;
				}
			}
		}
		
		// Generate all the LCSs
		HashSet<String> lcsSet = new HashSet<String>();
		for (int i = 0; i < tempLCS.size(); i++) {
			int loc = tempLCS.get(i);
			String temp = "";
			// Get chars from first string to make the LCS
			for (int c = loc - maxLength; c < loc; c++) {
				temp += s1Chars[c];
			}
			lcsSet.add(temp);
		}
		
		// Only return unique LCSs
		String[] lcs = new String[lcsSet.size()];
		lcsSet.toArray(lcs);
		
		return lcs;
	}

}
