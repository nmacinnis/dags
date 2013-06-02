package com.nickmacinnis.dags;

import java.util.Set;

/**
 * An edge joining two nodes. The edge has a direction, e.g. a start and end node.
 * @author nmacinnis
 */
public interface Edge<N extends Node<N, E>, E extends Edge<N, E>> {
    public N getStartNode();

    public N getEndNode();

    public int getHops();

    public E getEntryEdge();

    public E getDirectEdge();

    public E getExitEdge();

    /**
     * @param Edge The edge which will be added to this edge's internal collection of incoming edges
     */
    public boolean attachIncomingEdge(E edge);

    /**
     * @param Edge The edge which will be removed from this edge's internal collection of incoming edges
     */
    public boolean detachIncomingEdge(E edge);

    /**
     * @param Edge The edge which will be added to this edge's internal collection of incoming edges
     */
    public boolean attachOutgoingEdge(E edge);

    /**
     * @param Edge The edge which will be removed from this edge's internal collection of outgoing edges
     */
    public boolean detachOutgoingEdge(E edge);

    /**
     * @param Edge The edge which will be added to this edge's internal collection of dependent edges
     */
    public boolean attachDependentEdge(E edge);

    /**
     * @param Edge The edge which will be removed from this edge's internal collection of dependent edges
     */
    public boolean detachDependentEdge(E edge);

    /**
     * @return The set of all edges which are attached to this edge directly or transitively through an implicit edge
     */
    public Set<E> collectAttachedEdges();

    /**
     * Notify the edge's start and end nodes to attach this edge to their respective outgoing and incoming edges
     */
    public boolean attach()
            throws GraphLogicException;

    /**
     * Notify the edge's start and end nodes to remove this edge from their respective outgoing and incoming edges.
     * Notify any implicit edges which are attached to this edge to detach themselves recursively.
     */
    public boolean detach();

}
