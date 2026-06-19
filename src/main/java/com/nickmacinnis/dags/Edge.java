package com.nickmacinnis.dags;

import java.util.Set;

/**
 * A directed edge joining two nodes.
 */
public interface Edge<N extends Node<N, E>, E extends Edge<N, E>> {

    N getStartNode();

    N getEndNode();

    /** @return 0 for direct edges; number of implied hops for implicit edges */
    int getHops();

    E getEntryEdge();

    E getDirectEdge();

    E getExitEdge();

    boolean attachIncomingEdge(E edge);

    boolean detachIncomingEdge(E edge);

    boolean attachOutgoingEdge(E edge);

    boolean detachOutgoingEdge(E edge);

    boolean attachDependentEdge(E edge);

    boolean detachDependentEdge(E edge);

    /** @return all implicit edges attached to this edge, directly or transitively */
    Set<E> collectAttachedEdges();

    /** Register this edge with its start and end nodes. */
    boolean attach();

    /** Unregister this edge and cascade-detach all dependent implicit edges. */
    boolean detach();
}
