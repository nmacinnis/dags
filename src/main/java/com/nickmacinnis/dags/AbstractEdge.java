package com.nickmacinnis.dags;

import java.util.Set;
import java.util.LinkedHashSet;

/**
 * An edge joining two nodes. The edge has a direction, e.g. a start and end node.
 * @author nmacinnis
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

    /**
     * Initializes the internal state of the edge.
     */
    protected AbstractEdge(N startNode, N endNode, int hops) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.hops = hops;

        incomingImplicitEdges = new LinkedHashSet();
        outgoingImplicitEdges = new LinkedHashSet();
        dependentImplicitEdges = new LinkedHashSet();
    }

    /**
     * @return this
     */
    protected abstract E getThis();

    /**
     * @param Edge The edge which will be added to this edge's internal collection of incoming edges
     */
    @Override
    public boolean attachIncomingEdge(E edge) {
        return incomingImplicitEdges.add(edge);
    }

    @Override
    public boolean detachIncomingEdge(E edge) {
        return incomingImplicitEdges.remove(edge);
    }

    /**
     * @param Edge The edge which will be added to this edge's internal collection of incoming edges
     */
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
        return incomingImplicitEdges;
    }

    public Set<E> getOutgoingImplicitEdges() {
        return outgoingImplicitEdges;
    }

    public Set<E> getDependentImplicitEdges() {
        return dependentImplicitEdges;
    }

    /**
     * @return The set of all edges which are attached to this edge directly or transitively through an implicit edge
     */
    @Override
    public Set<E> collectAttachedEdges() {
        Set<E> collectedEdges = new LinkedHashSet();
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
    public boolean attach()
            throws GraphLogicException {
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
        final int prime = 31;
        int result = 1;
        result = prime * result + ((directEdge == null) ? 0 : directEdge.hashCode());
        result = prime * result + ((endNode == null) ? 0 : endNode.hashCode());
        result = prime * result + ((entryEdge == null) ? 0 : entryEdge.hashCode());
        result = prime * result + ((exitEdge == null) ? 0 : exitEdge.hashCode());
        result = prime * result + hops;
        result = prime * result + ((startNode == null) ? 0 : startNode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractEdge<?, ?> other = (AbstractEdge<?, ?>) obj;
        if (directEdge == null) {
            if (other.directEdge != null) {
                return false;
            }
        } else if (!directEdge.equals(other.directEdge)) {
            return false;
        }
        if (endNode == null) {
            if (other.endNode != null) {
                return false;
            }
        } else if (!endNode.equals(other.endNode)) {
            return false;
        }
        if (entryEdge == null) {
            if (other.entryEdge != null) {
                return false;
            }
        } else if (!entryEdge.equals(other.entryEdge)) {
            return false;
        }
        if (exitEdge == null) {
            if (other.exitEdge != null) {
                return false;
            }
        } else if (!exitEdge.equals(other.exitEdge)) {
            return false;
        }
        if (hops != other.hops) {
            return false;
        }
        if (startNode == null) {
            if (other.startNode != null) {
                return false;
            }
        } else if (!startNode.equals(other.startNode)) {
            return false;
        }
        return true;
    }

}
