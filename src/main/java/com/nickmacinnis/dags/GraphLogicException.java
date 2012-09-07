package com.nickmacinnis.dags;

/**
 * Thrown when something tries to violate some principle of the graph, such as creating cycles or edges to nowhere.
 * @author nmacinnis
 *
 */
public class GraphLogicException extends Exception {
    private static final long serialVersionUID = 7969449737430643226L;

    public GraphLogicException(String string) {
        super(string);
    }
}
