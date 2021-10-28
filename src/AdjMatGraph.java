import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class AdjMatGraph {
    private int[][] matrix;

    public AdjMatGraph(int size) {
        matrix = new int[size][size];
    }

    public void addEdge(int i, int j) {
        matrix[i][j] = 1;
    }

    public void addEdge(int i, int j, int w) {
        matrix[i][j] = w;
    }

    public void removeEdge(int i, int j) {
        matrix[i][j] = 0;
    }

    public boolean hasEdge(int i, int j) {
        return matrix[i][j] != 0;
    }

    public int getEdgeWeight(int i, int j) {
        return matrix[i][j];
    }

    public List<Integer> outEdges(int i) {
        ArrayList<Integer> edges = new ArrayList<>();
        for (int j=0; j<matrix[i].length; ++j)
            if (matrix[i][j] != 0)
                edges.add(j);
        return edges;
    }

    // in-degree: the # of edges going into a vertex
    public List<Integer> inEdges(int j) {
        ArrayList<Integer> edges = new ArrayList<>();
        for (int i=0; i<matrix.length; ++i)
            if (matrix[i][j] != 0)
                edges.add(i);
        return edges;
    }

    int[] dfs(int start) {
        int[] pred = new int[matrix.length];
        Arrays.fill(pred, -1);
        boolean[] visited = new boolean[matrix.length];

        // for for vertex i in the graph, pred[i] will be the index of the
        // (discovered) path from start to i. This is a clever data structure
        // that we can use to reconstruct all the paths we discover. For any
        // vertex in the graph, use pred[] to work backwards to the start.

        Stack<Integer> s = new Stack<>();
        s.add(start);
        visited[start] = true;

        // O(N^2) vs O(N+M)
        // dense graph - M = O(N^2) (really big-theta)
        //       --> dfs for AM is O(M) = O(N^2).
        //           dfs for AL is O(N + M) = O(M) = O(N^2)
        // sparse graph - M = O(N)
        //       --> dfs for AM is O(N^2) = O(M^2)
        //       --> dfs for AL is O(N+M) = O(M) = O(N)
        //
        while (!s.isEmpty()) {
            int i = s.pop();

            for (int j=0; j < matrix[i].length; ++j) {
                if (matrix[i][j] != 0 && !visited[j]) {
                    s.add(j);
                    visited[j] = true;
                    pred[j] = i;
                }
            }
        }
        return pred;
    }

}
