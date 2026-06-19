package com.nickmacinnis.dags;

import java.util.List;
import java.util.Set;

public interface Node<N extends Node<N, E>, E extends Edge<N, E>> extends Iterable<N> {

    /**
     * Add a direct edge from this node to endNode.
     * Implicit (transitive) edges are generated automatically.
     * @return false if the edge already existed; true otherwise
     * @throws GraphLogicException if the edge would create a cycle or endNode is null
     */
    boolean addChild(N endNode);

    /**
     * Add a pre-built direct edge to the graph.
     * @return false if the edge already existed; true otherwise
     * @throws GraphLogicException if the edge would create a cycle or the end node is null
     */
    boolean addDirectEdge(E edge);

    /**
     * Remove the direct edge from this node to endNode.
     * Dependent implicit edges are cascade-detached.
     * @return false if no such edge existed; true otherwise
     */
    boolean removeChild(N endNode);

    /** @return true if this node has no incoming edges */
    boolean isOrphaned();

    /** @return the set of all nodes reachable from this node (direct and transitive) */
    Set<N> collectChildren();

    /** @return the set of all edges reachable from this node (direct and implicit) */
    Set<E> collectEdges();

    /** @return the list of direct children in insertion order, with duplicates for shared nodes */
    List<N> listChildren();

    List<E> getIncomingEdges();

    List<E> getOutgoingEdges();

    boolean addIncomingEdge(E edge);

    boolean removeIncomingEdge(E edge);

    boolean addOutgoingEdge(E edge);

    boolean removeOutgoingEdge(E edge);

    /** @return the maximum hop count from any incoming edge (depth from root) */
    int calculateDepth();

    /**
     * @return a 2D list where each row is one path from this node to a leaf,
     *         suitable for coordinate layout
     */
    List<List<N>> generateGrid();

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    /** @return a new empty node of the same concrete type */
    N copy();

    /** @return the set of all direct edges reachable from this node */
    Set<E> collectDirectEdges();

    /** @return nodes in depth-first order (direct edges only, duplicates possible) */
    List<N> dft();

    /** @return nodes in breadth-first order (direct edges only, duplicates possible) */
    List<N> bft();
}
