package com.nickmacinnis.dags;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

/**
 * Represents a single node in the graph.
 */
public abstract class AbstractNode<N extends Node<N, E>, E extends Edge<N, E>> implements Iterable<N>, Node<N, E> {
    protected List<E> incomingEdges;
    protected List<E> outgoingEdges;
    protected double x;
    protected double y;

    public AbstractNode() {
        this.incomingEdges = new ArrayList<>();
        this.outgoingEdges = new ArrayList<>();
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

    @Override
    public boolean addChild(N endNode) {
        E edge = buildDirectEdge(getThis(), endNode);
        return addDirectEdge(edge);
    }

    @Override
    public boolean addDirectEdge(E edge) {
        N endNode = edge.getEndNode();

        if (endNode == null) {
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
     * Returns this node typed as N.
     * Safe by construction: the self-referential constraint (N extends Node<N, E>) guarantees
     * that any class extending AbstractNode<N, E> is N.
     */
    @SuppressWarnings("unchecked")
    protected N getThis() {
        return (N) this;
    }

    /** @return a DirectEdge from startNode to endNode */
    protected abstract E buildDirectEdge(N startNode, N endNode);

    /** @return an ImplicitEdge from startNode to endNode */
    protected abstract E buildImplicitEdge(N startNode, N endNode, E entryEdge, E directEdge, E exitEdge, int hops);

    protected boolean addEdge(Edge<N, E> edge) {
        return edge.attach();
    }

    @Override
    public boolean removeChild(N endNode) {
        E edge = buildDirectEdge(getThis(), endNode);
        if (!outgoingEdges.contains(edge)) {
            return false;
        }
        E actualEdge = findActual(edge, outgoingEdges).orElseThrow();
        return actualEdge.detach();
    }

    @Override
    public boolean isOrphaned() {
        return incomingEdges.isEmpty();
    }

    @Override
    public Set<N> collectChildren() {
        Set<N> children = new LinkedHashSet<>();
        for (Edge<N, E> edge : outgoingEdges) {
            children.add(edge.getEndNode());
        }
        return children;
    }

    @Override
    public Set<E> collectEdges() {
        Set<E> edges = new LinkedHashSet<>();
        for (E edge : outgoingEdges) {
            edges.add(edge);
            edges.addAll(edge.getEndNode().collectEdges());
        }
        return edges;
    }

    @Override
    public Set<E> collectDirectEdges() {
        Set<E> edges = new LinkedHashSet<>();
        for (E edge : outgoingEdges) {
            if (edge instanceof DirectEdge<?, ?>) {
                edges.add(edge);
            }
            edges.addAll(edge.getEndNode().collectDirectEdges());
        }
        return edges;
    }

    @Override
    public List<N> listChildren() {
        List<N> children = new ArrayList<>();
        for (E edge : outgoingEdges) {
            children.add(edge.getEndNode());
        }
        return children;
    }

    @Override
    public List<N> dft() {
        List<N> nodes = new ArrayList<>();
        nodes.add(getThis());
        for (E edge : outgoingEdges) {
            if (edge instanceof DirectEdge<?, ?>) {
                nodes.addAll(edge.getEndNode().dft());
            }
        }
        return nodes;
    }

    @Override
    public List<N> bft() {
        List<N> result = new ArrayList<>();
        Queue<N> queue = new ArrayDeque<>();
        queue.add(getThis());
        while (!queue.isEmpty()) {
            N current = queue.poll();
            result.add(current);
            for (E edge : current.getOutgoingEdges()) {
                if (edge instanceof DirectEdge<?, ?>) {
                    queue.add(edge.getEndNode());
                }
            }
        }
        return result;
    }

    @Override
    public boolean addIncomingEdge(E edge) {
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

    private <T> Optional<T> findActual(T matchingObject, List<T> list) {
        return list.stream()
                .filter(t -> t.equals(matchingObject))
                .findFirst();
    }

    @Override
    public Iterator<N> iterator() {
        List<N> nodes = new ArrayList<>();
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
        List<List<N>> tree = new ArrayList<>();
        for (E edge : outgoingEdges) {
            if (edge instanceof DirectEdge<?, ?>) {
                N endpoint = edge.getEndNode();
                List<List<N>> childGrid = endpoint.generateGrid();
                for (List<N> row : childGrid) {
                    row.add(0, getThis());
                    tree.add(row);
                }
            }
        }
        if (tree.isEmpty()) {
            List<N> row = new ArrayList<>();
            row.add(getThis());
            tree.add(row);
        }
        return tree;
    }

    @Override
    public abstract N copy();
}
