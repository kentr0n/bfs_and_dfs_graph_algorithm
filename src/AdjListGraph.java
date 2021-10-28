import java.util.*;

public class AdjListGraph {
    private Vertex[] vertices;
    private boolean isDirected;

    public AdjListGraph(int size, boolean isDirected) {
        vertices = new Vertex[size];
        for (int i=0; i<vertices.length; ++i) {
            vertices[i] = new Vertex(i);
        }
        this.isDirected = isDirected;
    }

    public void addEdge(int i, int j) {
        if (i < 0 || j < 0 || i >= vertices.length ||  j >= vertices.length)
            throw new IndexOutOfBoundsException();

        vertices[i].edges.add(new Edge(j));
        if (!isDirected)
            vertices[j].edges.add(new Edge(i));
    }

    public void addEdge(int i, int j, int w) {
        if (i < 0 || j < 0 || i >= vertices.length ||  j >= vertices.length)
            throw new IndexOutOfBoundsException();

        vertices[i].edges.add(new Edge(j, w));
        if (!isDirected)
            vertices[j].edges.add(new Edge(i, w));
    }

    public void removeEdge(int i, int j) {
        if (i < 0 || j < 0 || i >= vertices.length ||  j >= vertices.length)
            throw new IndexOutOfBoundsException();

        for (int k=0; k<vertices[i].edges.size(); ++k) {
            Edge e = vertices[i].edges.get(k);
            if (e.target == j) {
                // TODO swap edges[k] with last element, then remove
                vertices[i].edges.remove(k);
                break;
            }
        }
    }

    public boolean hasEdge(int i, int j) {
        if (i < 0 || j < 0 || i >= vertices.length ||  j >= vertices.length)
            throw new IndexOutOfBoundsException();

        for (Edge e : vertices[i].edges) {
            if (e.target == j) {
                return true;
            }
        }
        return false;
    }

    public int getEdgeWeight(int i, int j) {
        if (i < 0 || j < 0 || i >= vertices.length ||  j >= vertices.length)
            throw new IndexOutOfBoundsException();

        for (Edge e : vertices[i].edges) {
            if (e.target == j) {
                return e.weight;
            }
        }
        return -1;
    }

    // doesn't violate encapsulation, but does take O(deg(i)) time. Advice:
    // don't call this method, ever.
    public List<Integer> outEdges(int i) {
        ArrayList<Integer> oe = new ArrayList<>();
        for (Edge e : vertices[i].edges)
            oe.add(e.target);
        return oe;
    }

    // Doesn't violate encapsulation. Not bad performance-wise (given the
    // design of the adjacency list)
    // this runs in O(n + m) time -- do you agree?
    public List<Integer> inEdges(int j) {
        ArrayList<Integer> ie = new ArrayList<>();

        for (Vertex v : vertices) {
            if (hasEdge(v.u, j))
                ie.add(v.u);
        }
        return ie;
    }

    public boolean isConnected() {
        boolean[] reachable = reachable(0);

        for (boolean b : reachable)
            if (!b)
                return false;
        return true;
    }

    // return an array of booleans indicating which vertices are
    // reachable from start
    public boolean[] reachable(int start) {
        Queue<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[vertices.length];

        // initialize
        queue.add(start);
        visited[start] = true;

        // loop invariant: at the start of each iteration of this loop, the
        // queue contains (and the array marks) all of the vertices that have
        // been visited, with potentially unvisited neighbors, thus far. Visit
        // the neighbors of the visited vertices in FIFO order.
        // Works in O(n+m) time
        while (!queue.isEmpty()) {
            int u = queue.remove();
            // visit each neighbor of u -- find u's outEdges
            // for loop does O(deg(v_u)) work.
            for (Edge e : vertices[u].edges) {
                // have we seen e.target?
                if (!visited[e.target]) {
                    // mess with edge e if we want to.
                    queue.add(e.target);
                    visited[e.target] = true;
                    // if you need to respond to the fact that we're at
                    // e.target, do it here
                }
            }
        }
        return visited;
    }

    // returns a predecessor array
    int[] dfs(int start) {
        int[] pred = new int[vertices.length];
        Arrays.fill(pred, -1);
        boolean[] visited = new boolean[vertices.length];

        // for for vertex i in the graph, pred[i] will be the index of the
        // (discovered) path from start to i. This is a clever data structure
        // that we can use to reconstruct all the paths we discover. For any
        // vertex in the graph, use pred[] to work backwards to the start.

        Stack<Integer> s = new Stack<>();
        s.add(start);
        visited[start] = true;

        while (!s.isEmpty()) {
            int i = s.pop();

            for (Edge e : vertices[i].edges) {
                if (!visited[e.target]) {
                    s.add(e.target);
                    visited[e.target] = true;
                    pred[e.target] = i;
                }
            }
        }
        return pred;
    }

    // objects of this class holds the info related to a vertex. Gives us a
    // place to store vertex-related properties, if they become relevant to an
    // algorithm.
    private static class Vertex {
        int u;
        // would using a linked list be better than a resizeable array?
        ArrayList<Edge> edges;

        Vertex(int u) {
            this.u = u;
            edges = new ArrayList<>();
        }
    }

    // objects of this class store info related to an edge. To save space, we
    // do NOT store the source of the edge; that is implicitly encoded in the
    // Vertex object that contains this edge.
    private static class Edge {
        int target;
        int weight;

        Edge(int t, int w) {
            this.target = t;
            this.weight = w;
        }

        Edge(int t) {
            this(t, 0);
        }
    }
}
