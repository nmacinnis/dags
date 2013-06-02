package com.nickmacinnis.dags.example;

import com.nickmacinnis.dags.ImplicitEdge;

public class ImplicitEdgeExampleImpl extends ImplicitEdge<NodeExample, EdgeExample> implements EdgeExample {

    public ImplicitEdgeExampleImpl(NodeExample startNode, NodeExample endNode, EdgeExample entryEdge, EdgeExample directEdge, EdgeExample exitEdge, int hops) {
        super(startNode, endNode, entryEdge, directEdge, exitEdge, hops);
    }

    @Override
    protected EdgeExample getThis() {
        return this;
    }

}
