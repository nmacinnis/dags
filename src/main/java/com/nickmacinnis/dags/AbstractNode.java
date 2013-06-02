package com.nickmacinnis.dags;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a single node in the graph.
 * @author nmacinnis
 */
public abstract class AbstractNode<N extends Node<N, E>, E extends Edge<N, E>> implements Iterable<N>, Node<N, E> {
    protected List<E> incomingEdges;
    protected List<E> outgoingEdges;
    protected double x;
    protected double y;

    /**
     * Initializes the internal state of this node.
     */
    public AbstractNode() {
        this.incomingEdges = new ArrayList<E>();
        this.outgoingEdges = new ArrayList<E>();
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Add the edge to the graph.
     * If the edge's start node is null, the edge will be added to the root node.
     * Implicit edges are added to the graph as necessary.
     * @param edge The edge to be added.
     * @return false if the graph already contained the edge; otherwise true.
     * @throws GraphLogicException if the edge would create a cycle or if the end node is null.
     */
    @Override
    public boolean addChild(N endNode)
            throws GraphLogicException {
        E edge = buildDirectEdge(getThis(), endNode);
        return addDirectEdge(edge);
    }

    @Override
    public boolean addDirectEdge(E edge)
            throws GraphLogicException {
        N endNode = edge.getEndNode();

        if (null == endNode) {
            throw new GraphLogicException("Cannot create an edge to nowhere"); // unless you're in alaska
        }

        if (equals(endNode)) {
            throw new GraphLogicException("This direct edge would create a self-referent cycle in the graph");
        }

        for (E checkEdge : getIncomingEdges()) {
            if (endNode.equals(checkEdge.getStartNode())) {
                throw new GraphLogicException("This direct edge would create a cycle in the graph");
            }
        }

        if (outgoingEdges.contains(edge)) {
            return false;
        }

        addEdge(edge);

        for (E incomingEdge : getIncomingEdges()) {
            addEdge(buildImplicitEdge(incomingEdge.getStartNode(), endNode, incomingEdge, edge, edge, incomingEdge.getHops() + 1));
        }

        for (E outgoingEdge : endNode.getOutgoingEdges()) {
            addEdge(buildImplicitEdge(getThis(), outgoingEdge.getEndNode(), edge, edge, outgoingEdge, outgoingEdge.getHops() + 1));
        }

        for (E incomingEdge : getIncomingEdges()) {
            for (E outgoingEdge : endNode.getOutgoingEdges()) {
                addEdge(buildImplicitEdge(incomingEdge.getStartNode(), outgoingEdge.getEndNode(), incomingEdge, edge, outgoingEdge,
                        incomingEdge.getHops() + outgoingEdge.getHops() + 2));
            }
        }
        return true;
    }

    /**
     * @return this
     */
    protected abstract N getThis();

    /**
     * @return A DirectEdge from startNode to endNode
     */
    protected abstract E buildDirectEdge(N startNode, N endNode);

    /**
     * @return An ImplicitEdge from startNode to endNode
     */
    protected abstract E buildImplicitEdge(N startNode, N endNode, E entryEdge, E directEdge, E exitEdge, int hops);

    /**
     * Add the direct edge to the graph.
     * The edge is added to the start and end nodes' collection of incoming/outgoing edges.
     * @param edge The edge to add.
     * @return True if the graph did not contain the edge.
     * @throws GraphLogicException
     */
    protected boolean addEdge(Edge<N, E> edge)
            throws GraphLogicException {
        return edge.attach();
    }

    /**
     * Remove a child node. It is possible to orphan nodes by calling this method.
     * @param endNode The child node to remove
     * @return false if the node was not a child of this node; true otherwise.
     */
    @Override
    public boolean removeChild(N endNode) {
        Edge<N, E> edge = buildDirectEdge(getThis(), endNode);
        if (!outgoingEdges.contains(edge)) {
            return false;
        }
        Edge<N, E> actualEdge = findActual(edge, outgoingEdges);
        return actualEdge.detach();
    }

    /**
     * @return true if this node has no incoming edges; otherwise false
     */
    @Override
    public boolean isOrphaned() {
        return incomingEdges.isEmpty();
    }

    /**
     * @return The set of all nodes which can be reached directly or transitively via this node's outgoing edges
     */
    @Override
    public Set<N> collectChildren() {
        Set<N> children = new LinkedHashSet();
        for (Edge<N, E> edge : outgoingEdges) {
            children.add(edge.getEndNode());
        }
        return children;
    }

    @Override
    public Set<E> collectEdges() {
        Set<E> edges = new LinkedHashSet();
        for (E edge : outgoingEdges) {
            edges.add(edge);
            edges.addAll(edge.getEndNode().collectEdges());
        }
        return edges;
    }

    @Override
    public Set<E> collectDirectEdges() {
        Set<E> edges = new LinkedHashSet();
        for (E edge : outgoingEdges) {
            if (edge instanceof DirectEdge) {
                edges.add(edge);
            }
            edges.addAll(edge.getEndNode().collectDirectEdges());
        }
        return edges;
    }

    /**
     * @return The list of all nodes which can be reached directly or transitively via this node's outgoing edges
     */
    @Override
    public List<N> listChildren() {
        List<N> children = new ArrayList<N>();
        for (E edge : outgoingEdges) {
            children.add(edge.getEndNode());
        }
        return children;
    }

    @Override
    public List<N> dft() {
        List<N> nodes = new ArrayList<N>();
        nodes.add(getThis());
        for (E edge : outgoingEdges) {
            if (edge instanceof DirectEdge) {
                nodes.addAll(edge.getEndNode().dft());
            }
        }
        return nodes;
    }

    @Override
    public boolean addIncomingEdge(E edge)
            throws GraphLogicException {
        return incomingEdges.add(edge);
    }

    @Override
    public boolean removeIncomingEdge(E edge) {
        return incomingEdges.remove(edge);
    }

    @Override
    public List<E> getIncomingEdges() {
        return incomingEdges;
    }

    @Override
    public boolean addOutgoingEdge(E edge) {
        return outgoingEdges.add(edge);
    }

    @Override
    public boolean removeOutgoingEdge(E edge) {
        return outgoingEdges.remove(edge);
    }

    @Override
    public List<E> getOutgoingEdges() {
        return outgoingEdges;
    }

    /**
     * Find the element in the list which matches the object
     * @param matchingObject The object to be matched against
     * @param list The list to search
     * @return The object which equals matchingObject, or null
     */
    private <T> T findActual(T matchingObject, List<? extends T> list) {
        T actual = null;
        for (T check : list) {
            if (check.equals(matchingObject)) {
                actual = check;
            }
        }
        return actual;
    }

    @Override
    public Iterator<N> iterator() {
        List<N> nodes = new ArrayList<N>();
        nodes.add(getThis());
        nodes.addAll(listChildren());
        return nodes.iterator();
    }

    @Override
    public int calculateDepth() {
        int depth = 0;
        for (E edge : incomingEdges) {
            if (edge.getHops() > depth) {
                depth = edge.getHops();
            }
        }
        return depth;
    }

    @Override
    public List<List<N>> generateGrid() {

        List<List<N>> tree = new ArrayList<List<N>>();
        for (E edge : outgoingEdges) {
            if (edge instanceof DirectEdge) {
                N endpoint = edge.getEndNode();
                List<List<N>> childGrid = endpoint.generateGrid();
                for (List<N> row : childGrid) {
                    row.add(0, getThis());
                    tree.add(row);
                }
            }
        }
        if (tree.isEmpty()) {
            List<N> row = new ArrayList<N>();
            row.add(getThis());
            tree.add(row);
        }
        return tree;
    }

    @Override
    public abstract N clone()
            throws CloneNotSupportedException;
}
