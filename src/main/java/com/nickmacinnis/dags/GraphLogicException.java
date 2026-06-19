package com.nickmacinnis.dags;

/**
 * Thrown when something tries to violate some principle of the graph, such as creating cycles or edges to nowhere.
 */
public class GraphLogicException extends Exception {
    @java.io.Serial
    private static final long serialVersionUID = 7969449737430643226L;

    public GraphLogicException(String string) {
        super(string);
    }
}
