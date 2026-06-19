package com.nickmacinnis.dags;

import java.util.Objects;

/**
 * A concrete edge joining two nodes. Number of hops is always zero.
 */
public abstract non-sealed class DirectEdge<N extends Node<N, E>, E extends Edge<N, E>> extends AbstractEdge<N, E> {

    protected DirectEdge(N startNode, N endNode) {
        super(startNode, endNode, 0);
        this.entryEdge = getThis();
        this.directEdge = getThis();
        this.exitEdge = getThis();
    }

    @Override
    public int hashCode() {
        return Objects.hash(endNode, hops, startNode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DirectEdge<?, ?> other = (DirectEdge<?, ?>) obj;
        return Objects.equals(endNode, other.endNode)
                && Objects.equals(startNode, other.startNode);
    }
}
