package com.nickmacinnis.dags;

/**
 * A concrete edge joining two nodes. Number of hops is always zero.
 * @author nmacinnis
 */
public abstract class DirectEdge<N extends Node<N, E>, E extends Edge<N, E>> extends AbstractEdge<N, E> implements Edge<N, E> {
    /**
     * Initializes the internal state of the edge.
     */
    protected DirectEdge(N startNode, N endNode) {
        super(startNode, endNode, 0);
        this.entryEdge = getThis();
        this.directEdge = getThis();
        this.exitEdge = getThis();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endNode == null) ? 0 : endNode.hashCode());
        result = prime * result + hops;
        result = prime * result + ((startNode == null) ? 0 : startNode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DirectEdge<?, ?> other = (DirectEdge<?, ?>) obj;
        if (endNode == null) {
            if (other.endNode != null)
                return false;
        } else if (!endNode.equals(other.endNode))
            return false;
        if (startNode == null) {
            if (other.startNode != null)
                return false;
        } else if (!startNode.equals(other.startNode))
            return false;
        return true;
    }

}
