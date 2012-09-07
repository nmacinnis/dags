package com.nickmacinnis.dags;

import java.util.List;
import java.util.Set;

public interface Node<N extends Node<N, E>, E extends Edge<N, E>> extends Iterable<N>, Cloneable {

    /**
     * Add the edge to the graph.
     * If the edge's start node is null, the edge will be added to the root node.
     * Implicit edges are added to the graph as necessary.
     * @param edge The edge to be added.
     * @return false if the graph already contained the edge; otherwise true.
     * @throws GraphLogicException if the edge would create a cycle or if the end node is null.
     */
    public boolean addChild(N endNode)
            throws GraphLogicException;

    /**
     * Add a direct edge to the graph.
     * This works similarly to addChild.
     * @param edge The edge
     * @return false if the graph already contained the edge; otherwise true.
     * @throws GraphLogicException if the edge would create a cycle or if the end node is null.
     */
    boolean addDirectEdge(E edge)
            throws GraphLogicException;

    /**
     * Remove a child node. It is possible to orphan nodes by calling this method.
     * @param endNode The child node to remove
     * @return false if the node was not a child of this node; true otherwise.
     */
    public boolean removeChild(N endNode);

    /**
     * @return true if this node has no incoming edges; otherwise false
     */
    public boolean isOrphaned();

    /**
     * @return The set of all nodes which can be reached directly or transitively via this node's outgoing edges
     */
    public Set<N> collectChildren();

    /**
     * @return The set of all edges in the graph
     */
    public Set<E> collectEdges();

    /**
     * @return The list of all nodes which can be reached directly or transitively via this node's outgoing edges
     */
    public List<N> listChildren();

    public List<E> getIncomingEdges();

    public List<E> getOutgoingEdges();

    public boolean addIncomingEdge(E edge)
            throws GraphLogicException;

    public boolean removeIncomingEdge(E edge);

    public boolean addOutgoingEdge(E edge);

    public boolean removeOutgoingEdge(E edge);

    public int calculateDepth();

    public List<List<N>> generateGrid();

    public double getX();

    public void setX(double x);

    public double getY();

    public void setY(double y);

    public N clone()
            throws CloneNotSupportedException;

    public Set<E> collectDirectEdges();

    public List<N> dft();

}
