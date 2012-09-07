package com.nickmacinnis.dags;

public class DirectEdgeImpl extends DirectEdge<NodeImpl, EdgeImpl> implements EdgeImpl {

    protected DirectEdgeImpl(NodeImpl startNode, NodeImpl endNode) {
        super(startNode, endNode);
    }

    @Override
    protected EdgeImpl getThis() {
        return this;
    }

}
