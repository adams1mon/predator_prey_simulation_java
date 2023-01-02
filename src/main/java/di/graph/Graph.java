package di.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph<T> {

    private final HashMap<Node<T>, List<Node<T>>> nodes = new HashMap<>();

    public void addNode(Node<T> node) {
        if (!nodes.containsKey(node)) {
            nodes.put(node, new ArrayList<>());
        }
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
        var topologicalOrder = new ArrayList<Node<T>>();
        dfs(topologicalOrder);
        return topologicalOrder;
    }

    // entry dfs call
    private void dfs(List<Node<T>> topologicalOrder) {
        for (var startNode : nodes.keySet()) {
            if (startNode.getStatus() != NodeStatus.DONE_VISITING) {
                dfs(startNode, topologicalOrder);
            }
        }
    }

    // recursive dfs call
    private void dfs(Node<T> node, List<Node<T>> topologicalOrder) {
        node.setStatus(NodeStatus.VISITED);
        for (var neighbour : nodes.get(node)) {
            if (neighbour.getStatus() == NodeStatus.NOT_VISITED) {
                dfs(neighbour, topologicalOrder);
            } else if (neighbour.getStatus() == NodeStatus.VISITED) {
                // cycle found
                throw new GraphCycleException();
            }
        }

        // mark the node as done visited, to not include it when cycles are checked
        node.setStatus(NodeStatus.DONE_VISITING);
        topologicalOrder.add(node);
    }
}
