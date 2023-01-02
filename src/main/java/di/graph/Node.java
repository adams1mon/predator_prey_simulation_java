package di.graph;

public class Node<T> {

    private NodeStatus status = NodeStatus.NOT_VISITED;
    private final T content;

    public Node(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public String toString() {
        return "node {" +
            "content: " + content + ", " +
            "status: " + status +
            "}";
    }
}
