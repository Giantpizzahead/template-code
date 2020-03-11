import java.io.*;
import java.util.*;

/**
 * Variation of 0-1 Knapsack where you can get a fraction of an item. This can actually be
 * solved using a greedy approach (sort in decreasing order of 'value per unit cost').
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
 * Runtime: O(N * log(N))
 * Memory: O(N)
 */

public class KnapsackFraction {
    int N, W;
    Item[] items;

    KnapsackFraction(BufferedReader in, PrintWriter out) throws IOException {
        StringTokenizer st = new StringTokenizer(in.readLine());
        N = Integer.parseInt(st.nextToken());
        W = Integer.parseInt(st.nextToken());
        items = new Item[N];
        int c, v;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(in.readLine());
            v = Integer.parseInt(st.nextToken());
            c = Integer.parseInt(st.nextToken());
            items[i] = new Item(c, v);
        }

        // Sort in decreasing order of value per unit cost
        Arrays.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                double res = o2.unitVal - o1.unitVal;
                if (res < 0) return -1;
                else if (res > 0) return 1;
                else return 0;
            }
        });

        int currCost = 0;
        double currValue = 0;
        for (Item item : items) {
            if (currCost + item.c < W) {
                // Use whole item
                currValue += item.v;
                currCost += item.c;
            } else {
                // Use partial item & break
                int remaining = W - currCost;
                currValue += item.unitVal * remaining;
                break;
            }
        }

        out.printf("%.6f\n", currValue);
    }

    static class Item {
        int c, v;
        double unitVal;
        Item(int c, int v) {
            this.c = c;
            this.v = v;
            unitVal = (double) v / c;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        new KnapsackFraction(in, out);
        in.close();
        out.close();
    }
}
