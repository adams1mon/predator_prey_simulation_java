package utils.graph;

import java.util.*;

public class Graph<T> {

    private final HashMap<Node<T>, List<Node<T>>> nodes = new HashMap<>();

    public void addNode(Node<T> node) {
        if (!nodes.containsKey(node)) {
            nodes.put(node, new ArrayList<>());
        }
    }

    public List<Node<T>> getNeighbours(Node<T> node) {
        return nodes.get(node);
    }

    public void addEdge(Node<T> from, Node<T> to) {
        if (!nodes.containsKey(from)) {
            var neighbours = new ArrayList<Node<T>>();
            neighbours.add(to);
            nodes.put(from, neighbours);
        } else {
            nodes.get(from).add(to);
        }

        if (!nodes.containsKey(to)) {
            nodes.put(to, new ArrayList<>());
        }
    }

    public List<Node<T>> getReverseTopologicalOrder() {
        System.out.println(nodes);
        var topologicalOrder = new ArrayList<Node<T>>();
        dfs(topologicalOrder);
        return topologicalOrder;
    }

    // entry dfs call
    private void dfs(List<Node<T>> topologicalOrder) {
        for (var startNode : nodes.keySet()) {
            if (startNode.notDoneVisiting()) {
                dfs(startNode, topologicalOrder);
            }
        }
    }

    // recursive dfs call
    private void dfs(Node<T> node, List<Node<T>> topologicalOrder) {
        node.visit();
        System.out.println("visiting " + node);
        for (var neighbour : nodes.get(node)) {
            if (neighbour.notDoneVisiting()) {
                dfs(neighbour, topologicalOrder);
            } else if (neighbour.isVisited()) {
                // cycle found
                throw new GraphCycleException();
            }
        }

        // mark the node as done visited, to not include it when cycles are checked
        node.doneVisiting();
        System.out.println("done visiting " + node);
        topologicalOrder.add(node);
    }
}
