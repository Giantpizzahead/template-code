import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * Modification of Dijkstra that returns the actual path of nodes taken from start to end.
 * Returns this as an int[] array (or null if no path was found).
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
public class DijkstraPath {
    final long INF = 98765432123456789L;
    int N, M;
    ArrayList<Pair>[] adj;

    DijkstraPath(BufferedReader in, PrintWriter out) throws IOException {
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

        int[] path = dijkstra(0, N-1);
        if (path == null) out.println(-1);
        else {
            for (int i = 0; i < path.length; i++) {
                if (i != 0) out.print(' ');
                out.print(path[i] + 1);
            }
        }
    }

    int[] dijkstra(int start, int end) {
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
        int[] from = new int[N];
        Arrays.fill(bestDist, INF);
        bestDist[start] = 0;
        from[start] = -1;
        Pair c;
        while (!pq.isEmpty()) {
            c = pq.poll();
            if (c.c != bestDist[c.n]) continue;
            if (c.n == end) return genPath(from, end);  // Path found
            for (Pair e : adj[c.n]) {
                if (c.c + e.c < bestDist[e.n]) {
                    bestDist[e.n] = c.c + e.c;
                    from[e.n] = c.n;
                    pq.add(new Pair(e.n, bestDist[e.n]));
                }
            }
        }
        // No path found
        return null;
    }

    int[] genPath(int[] from, int n) {
        Stack<Integer> stack = new Stack<>();
        while (n != -1) {
            stack.push(n);
            n = from[n];
        }
        int[] path = new int[stack.size()];
        for (int i = 0; i < path.length; i++) path[i] = stack.pop();
        return path;
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
        new DijkstraPath(in, out);
        in.close();
        out.close();
    }
}
