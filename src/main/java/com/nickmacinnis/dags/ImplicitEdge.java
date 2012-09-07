package com.nickmacinnis.dags;

/**
 * A calculated edge joining a start and end node by way of one direct edge and one direct or implicit edge.
 * @author nmacinnis
 */
public abstract class ImplicitEdge<N extends Node<N, E>, E extends Edge<N, E>> extends AbstractEdge<N, E> {
    /**
     * @param startNode The start node
     * @param endNode The end node
     * @param entryEdge The outgoing edge from the start node which implied this edge
     * @param directEdge The direct edge whose addition to the graph implied this edge
     * @param exitEdge The incoming edge to the end node which implied this edge
     * @param hops The number of implied edges in this edge's chain (always at least one, the edge itself)
     */
    protected ImplicitEdge(N startNode, N endNode, E entryEdge, E directEdge, E exitEdge, int hops) {
        super(startNode, endNode, hops);
        this.entryEdge = entryEdge;
        this.directEdge = directEdge;
        this.exitEdge = exitEdge;
    }

    @Override
    public boolean attach()
            throws GraphLogicException {
        entryEdge.attachOutgoingEdge(getThis());
        exitEdge.attachIncomingEdge(getThis());
        directEdge.attachDependentEdge(getThis());
        return super.attach();
    }

    @Override
    public boolean detach() {
        entryEdge.detachOutgoingEdge(getThis());
        exitEdge.detachIncomingEdge(getThis());
        directEdge.detachDependentEdge(getThis());
        return super.detach();
    }
}
