package com.nickmacinnis.dags;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An edge joining two nodes. The edge has a direction, e.g. a start and end node.
 */
public abstract class AbstractEdge<N extends Node<N, E>, E extends Edge<N, E>> implements Edge<N, E> {
    protected N startNode;
    protected N endNode;
    protected E entryEdge;
    protected E directEdge;
    protected E exitEdge;
    protected int hops;
    protected Set<E> incomingImplicitEdges;
    protected Set<E> outgoingImplicitEdges;
    protected Set<E> dependentImplicitEdges;

    protected AbstractEdge(N startNode, N endNode, int hops) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.hops = hops;

        incomingImplicitEdges = new LinkedHashSet<>();
        outgoingImplicitEdges = new LinkedHashSet<>();
        dependentImplicitEdges = new LinkedHashSet<>();
    }

    /** @return this */
    protected abstract E getThis();

    @Override
    public boolean attachIncomingEdge(E edge) {
        return incomingImplicitEdges.add(edge);
    }

    @Override
    public boolean detachIncomingEdge(E edge) {
        return incomingImplicitEdges.remove(edge);
    }

    @Override
    public boolean attachOutgoingEdge(E edge) {
        return outgoingImplicitEdges.add(edge);
    }

    @Override
    public boolean detachOutgoingEdge(E edge) {
        return outgoingImplicitEdges.remove(edge);
    }

    @Override
    public boolean attachDependentEdge(E edge) {
        return dependentImplicitEdges.add(edge);
    }

    @Override
    public boolean detachDependentEdge(E edge) {
        return dependentImplicitEdges.remove(edge);
    }

    @Override
    public N getStartNode() {
        return startNode;
    }

    @Override
    public N getEndNode() {
        return endNode;
    }

    @Override
    public E getEntryEdge() {
        return entryEdge;
    }

    @Override
    public E getDirectEdge() {
        return directEdge;
    }

    @Override
    public E getExitEdge() {
        return exitEdge;
    }

    @Override
    public int getHops() {
        return hops;
    }

    public Set<E> getIncomingImplicitEdges() {
        return Collections.unmodifiableSet(incomingImplicitEdges);
    }

    public Set<E> getOutgoingImplicitEdges() {
        return Collections.unmodifiableSet(outgoingImplicitEdges);
    }

    public Set<E> getDependentImplicitEdges() {
        return Collections.unmodifiableSet(dependentImplicitEdges);
    }

    /**
     * @return The set of all edges which are attached to this edge directly or transitively through an implicit edge
     */
    @Override
    public Set<E> collectAttachedEdges() {
        Set<E> collectedEdges = new LinkedHashSet<>();
        for (E incomingEdge : incomingImplicitEdges) {
            collectedEdges.add(incomingEdge);
            collectedEdges.addAll(incomingEdge.collectAttachedEdges());
        }
        for (E outgoingEdge : outgoingImplicitEdges) {
            collectedEdges.add(outgoingEdge);
            collectedEdges.addAll(outgoingEdge.collectAttachedEdges());
        }
        for (E dependentEdge : dependentImplicitEdges) {
            collectedEdges.add(dependentEdge);
            collectedEdges.addAll(dependentEdge.collectAttachedEdges());
        }
        return collectedEdges;
    }

    @Override
    public boolean attach() throws GraphLogicException {
        if (startNode == null || endNode == null) {
            return false;
        }
        startNode.addOutgoingEdge(getThis());
        endNode.addIncomingEdge(getThis());
        return true;
    }

    @Override
    public boolean detach() {
        if (startNode == null || endNode == null) {
            return false;
        }
        startNode.removeOutgoingEdge(getThis());
        endNode.removeIncomingEdge(getThis());
        for (E edge : outgoingImplicitEdges) {
            edge.detach();
        }
        for (E edge : incomingImplicitEdges) {
            edge.detach();
        }
        for (E edge : dependentImplicitEdges) {
            edge.detach();
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(directEdge, endNode, entryEdge, exitEdge, hops, startNode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AbstractEdge<?, ?> other = (AbstractEdge<?, ?>) obj;
        return hops == other.hops
                && Objects.equals(directEdge, other.directEdge)
                && Objects.equals(endNode, other.endNode)
                && Objects.equals(entryEdge, other.entryEdge)
                && Objects.equals(exitEdge, other.exitEdge)
                && Objects.equals(startNode, other.startNode);
    }
}
