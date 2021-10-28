import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void testAM() throws IOException {
        Scanner in = new Scanner(new FileReader("graph.txt"));

        int N = in.nextInt();
        int M = in.nextInt();

        AdjMatGraph g = new AdjMatGraph(N);

        for (int i=0; i<M; ++i) {
            int u = in.nextInt();
            int v = in.nextInt();
            g.addEdge(u, v);
        }

        for (int i=0; i < N; i++) {
            System.out.print(i + ": ");
            for (int j : g.outEdges(i)) // O(m+n)!!!!
                System.out.print(j + ", ");
            System.out.println();
        }
    }

    public static void testAL() throws IOException {
        Scanner in = new Scanner(new FileReader("graph.txt"));

        int N = in.nextInt();
        int M = in.nextInt();

        AdjListGraph g = new AdjListGraph(N, true);

        for (int i=0; i<M; ++i) {
            int u = in.nextInt();
            int v = in.nextInt();
            g.addEdge(u, v);
        }

        // TODO What do we think about this code? What's its complexity for
        // adj-list? adj-matrix?
        for (int i=0; i<N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (g.hasEdge(i, j)) {
                    // process edge (i, j)
                    System.out.printf("an edge from %d to %d\n", i, j);
                }
            }
        }

        var reachable = g.reachable(0);

        for (int i=0; i<reachable.length; ++i) {
            if (reachable[i])
                System.out.printf("vertex %d is reachable from vertex 0\n", i);
        }

        var pred = g.dfs(0);
        for (int i=0; i<N; ++i) {
            if (pred[i] == -1)
                continue;

            Stack<Integer> s = new Stack<>();
            int j = i;
            while (j != -1) {
                s.push(j);
                j = pred[j];
            }

            System.out.println("Path to vertex " + i);
            while (s.size() > 1) {
                System.out.print(s.pop() + " -> ");
            }
            System.out.println(i);
        }
    }

    public static void main(String[] args) throws IOException {
        testAL();

    }
}