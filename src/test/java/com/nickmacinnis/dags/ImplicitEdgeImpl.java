package com.nickmacinnis.dags;

public class ImplicitEdgeImpl extends ImplicitEdge<NodeImpl, EdgeImpl> implements  EdgeImpl {

    protected ImplicitEdgeImpl(NodeImpl startNode, NodeImpl endNode, EdgeImpl entryEdge,
            EdgeImpl directEdge, EdgeImpl exitEdge, int hops) {
        super(startNode, endNode, entryEdge, directEdge, exitEdge, hops);
    }

    @Override
    protected EdgeImpl getThis() {
        return this;
    }

}
