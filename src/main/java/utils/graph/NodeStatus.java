package utils.graph;

public enum NodeStatus {
    NOT_VISITED(0),
    VISITED(1),
    DONE_VISITING(2);

    final int status;

    NodeStatus(int status) {
        this.status = status;
    }
}
