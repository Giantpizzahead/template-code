import java.util.*;
import java.io.*;

/**
 * Classic DP implementation of the edit distance problem. The operations are:
 * Do nothing (chars are the same) -> i, j
 * Insert a character -> i, j-1
 * Remove a character -> i-1, j
 * Replace a character -> i-1, j-1
 * To add custom edit costs, just replace the 1s with the desired values.
 *
 * Runtime: O(AB) where A & B are the lengths of the 2 strings
 * Memory: O(AB)
 */

public class EditDistance {

    EditDistance(BufferedReader in, PrintWriter out) throws IOException {
        String strA = in.readLine();
        String strB = in.readLine();
        int editDist = editDistance(strA, strB);
        out.println(editDist);
    }

    int editDistance(String strA, String strB) {
        int[][] dp = new int[strA.length() + 1][strB.length() + 1];
        // Base cases
        for (int i = 1; i <= strA.length(); i++) dp[i][0] = i;
        for (int j = 1; j <= strB.length(); j++) dp[0][j] = j;
        // Main DP
        for (int i = 1; i <= strA.length(); i++) {
            for (int j = 1; j <= strB.length(); j++) {
                int bestTrans;
                if (strA.charAt(i-1) == strB.charAt(j-1)) {
                    // No edits needed
                    bestTrans = dp[i-1][j-1];
                } else {
                    // Replace character
                    bestTrans = dp[i-1][j-1] + 1;
                }
                // Insert from A
                bestTrans = Math.min(dp[i][j-1] + 1, bestTrans);
                // Remove from A
                bestTrans = Math.min(dp[i-1][j] + 1, bestTrans);
                dp[i][j] = bestTrans;
            }
        }
        // for (int i = 0; i <= strA.length(); i++) System.out.println(Arrays.toString(dp[i]));
        return dp[strA.length()][strB.length()];
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        new EditDistance(in, out);
        in.close();
        out.close();
    }
}
