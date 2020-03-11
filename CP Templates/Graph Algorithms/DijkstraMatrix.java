import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * Dijkstra with an adjacency matrix! Handles everything; multiple
 * edges, self-loops, 0-cost edges? No problem!
 *
 * Note: The graph is undirected by default. To change it to directed, remove the reverse
 * edge when parsing input.
 *
 * Input format:
 * N M
 * A1 B1 C1
 * A2 B2 C2
 * ...
 * An Bn Cn
 *
 * N is the number of nodes, M is the number of edges.
 * A -> B is an edge in the graph with cost C. (Assumes A and B are offset by +1.)
 *
 * Runtime: O(N^2)
 */

@SuppressWarnings("unchecked")
public class DijkstraMatrix {
    final int INF = 987654321;
    int N, M;
    int[][] adj;

    DijkstraMatrix(BufferedReader in, PrintWriter out) throws IOException {
        StringTokenizer st = new StringTokenizer(in.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        adj = new int[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(adj[i], INF);
            adj[i][i] = 0;
        }
        int a, b, c;
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(in.readLine());
            a = Integer.parseInt(st.nextToken()) - 1;
            b = Integer.parseInt(st.nextToken()) - 1;
            c = Integer.parseInt(st.nextToken());
            adj[a][b] = Math.min(c, adj[a][b]);
            adj[b][a] = Math.min(c, adj[b][a]);
        }

        int dist = dijkstra(0, N-1);
        if (dist == INF) out.println(-1);
        else out.println(dist);
    }

    int dijkstra(int start, int end) {
        int[] bestDist = new int[N];
        Arrays.fill(bestDist, INF);
        bestDist[start] = 0;
        boolean[] visited = new boolean[N];
        int c = start, currMinDist;
        while (c != -1) {
            visited[c] = true;
            if (c == end) return bestDist[c];  // Path found
            for (int e = 0; e < N; e++) {
                if (e == c) continue;
                if (bestDist[c] + adj[c][e] < bestDist[e]) {
                    bestDist[e] = bestDist[c] + adj[c][e];
                }
            }
            // Find new node to evaluate
            c = -1;
            currMinDist = INF;
            for (int n = 0; n < N; n++) {
                if (!visited[n] && bestDist[n] < currMinDist) {
                    currMinDist = bestDist[n];
                    c = n;
                }
            }
        }
        // No path found
        return INF;
    }

    static class Pair {
        int n;
        long c;
        Pair(int n, long c) {
            this.n = n;
            this.c = c;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        // BufferedReader in = new BufferedReader(new FileReader("Dijkstra.in"));
        // PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Dijkstra.out")));
        new DijkstraMatrix(in, out);
        in.close();
        out.close();
    }
}
