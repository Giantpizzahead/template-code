import java.io.*;
import java.util.StringTokenizer;

/**
 * Variation of 0-1 Knapsack where the same item can be used multiple times.
 * This implementation finds the maximum possible value of the items in the bag while keeping
 * the sum of the costs <= the size of the bag.
 *
 * Input format:
 * N W
 * V1 C1
 * V2 C2
 * ...
 * Vn Cn
 *
 * N is the # of items, W is the size of the bag.
 * V is the value of the item, C is the cost of the item.
 *
 * Runtime: O(NW)
 * Memory: O(N + W)
 */

public class KnapsackMultiple {
    int N, W;
    int[] values, costs;

    KnapsackMultiple(BufferedReader in, PrintWriter out) throws IOException {
        StringTokenizer st = new StringTokenizer(in.readLine());
        N = Integer.parseInt(st.nextToken());
        W = Integer.parseInt(st.nextToken());
        values = new int[N];
        costs = new int[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(in.readLine());
            values[i] = Integer.parseInt(st.nextToken());
            costs[i] = Integer.parseInt(st.nextToken());
        }

        int answer = doKnapsack();
        out.println(answer);
    }

    int doKnapsack() {
        int[] dp = new int[W + 1];
        for (int i = 0; i < N; i++) {
            for (int j = costs[i]; j <= W; j++) {
                dp[j] = Math.max(dp[j-costs[i]] + values[i], dp[j]);
            }
        }

        int answer = 0;
        for (int j = 0; j <= W; j++) answer = Math.max(dp[j], answer);
        return answer;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        new KnapsackMultiple(in, out);
        in.close();
        out.close();
    }
}
