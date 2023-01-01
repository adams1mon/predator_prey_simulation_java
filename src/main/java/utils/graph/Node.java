package utils.graph;

public class Node<T> {

    private NodeStatus status = NodeStatus.NOT_VISITED;
    private final T content;

    public Node(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public boolean isVisited() {
        return status == NodeStatus.VISITED;
    }

    public void visit() {
        status = NodeStatus.VISITED;
    }

    public void doneVisiting() {
        status = NodeStatus.DONE_VISITING;
    }

    public boolean notDoneVisiting() {
        return status != NodeStatus.DONE_VISITING;
    }

    public String toString() {
        return "node {" +
                "content: " + content +
                "}";
    }
}
