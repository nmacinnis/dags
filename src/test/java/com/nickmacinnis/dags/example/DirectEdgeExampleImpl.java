package com.nickmacinnis.dags.example;

import com.nickmacinnis.dags.DirectEdge;

public class DirectEdgeExampleImpl extends DirectEdge<NodeExample, EdgeExample> implements EdgeExample {

    public DirectEdgeExampleImpl(NodeExample startNode, NodeExample endNode) {
        super(startNode, endNode);
    }

    @Override
    protected EdgeExample getThis() {
        return this;
    }

}
