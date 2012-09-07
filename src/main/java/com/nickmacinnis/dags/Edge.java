package com.nickmacinnis.dags;

import java.util.Set;

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

    public boolean detachIncomingEdge(E edge);

    /**
     * @param Edge The edge which will be added to this edge's internal collection of incoming edges
     */
    public boolean attachOutgoingEdge(E edge);

    public boolean detachOutgoingEdge(E edge);

    public boolean attachDependentEdge(E edge);

    public boolean detachDependentEdge(E edge);

    public Set<E> collectAttachedEdges();

    public boolean attach()
            throws GraphLogicException;

    public boolean detach();

}
