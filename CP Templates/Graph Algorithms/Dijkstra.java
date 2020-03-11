import java.util.*;
import java.io.*;

/**
 * Dijkstra with an adjacency list / priority queue! Handles everything; multiple
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
 * Runtime: O(N * log(M))
 */

@SuppressWarnings("unchecked")
public class Dijkstra {
    final long INF = 98765432123456789L;
    int N, M;
    ArrayList<Pair>[] adj;

    Dijkstra(BufferedReader in, PrintWriter out) throws IOException {
        StringTokenizer st = new StringTokenizer(in.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        adj = new ArrayList[N];
        for (int i = 0; i < N; i++) adj[i] = new ArrayList<>(2);
        int a, b, c;
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(in.readLine());
            a = Integer.parseInt(st.nextToken()) - 1;
            b = Integer.parseInt(st.nextToken()) - 1;
            c = Integer.parseInt(st.nextToken());
            adj[a].add(new Pair(b, c));
            adj[b].add(new Pair(a, c));
        }

        long dist = dijkstra(0, N-1);
        if (dist == INF) out.println(-1);
        else out.println(dist);
    }

    long dijkstra(int start, int end) {
        PriorityQueue<Pair> pq = new PriorityQueue<>(new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                long res = o1.c - o2.c;
                if (res > 0) return 1;
                else if (res < 0) return -1;
                else return 0;
            }
        });
        pq.add(new Pair(start, 0));
        long[] bestDist = new long[N];
        Arrays.fill(bestDist, INF);
        bestDist[start] = 0;
        Pair c;
        while (!pq.isEmpty()) {
            c = pq.poll();
            if (c.c != bestDist[c.n]) continue;
            if (c.n == end) return c.c;  // Path found
            for (Pair e : adj[c.n]) {
                if (c.c + e.c < bestDist[e.n]) {
                    bestDist[e.n] = c.c + e.c;
                    pq.add(new Pair(e.n, bestDist[e.n]));
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
        new Dijkstra(in, out);
        in.close();
        out.close();
    }
}
