package com.nickmacinnis.dags;

public class NodeImpl extends AbstractNode<NodeImpl, EdgeImpl> {

    @Override
    protected EdgeImpl buildDirectEdge(NodeImpl startNode, NodeImpl endNode) {
        return new DirectEdgeImpl(startNode, endNode);
    }

    @Override
    protected NodeImpl getThis() {
        return this;
    }

    @Override
    protected EdgeImpl buildImplicitEdge(NodeImpl startNode, NodeImpl endNode, EdgeImpl entryEdge,
            EdgeImpl directEdge, EdgeImpl exitEdge, int hops) {
        return new ImplicitEdgeImpl(startNode, endNode, entryEdge, directEdge, exitEdge, hops);
    }

    @Override
    public NodeImpl clone()
            throws CloneNotSupportedException {
        return new NodeImpl();
    }
}
