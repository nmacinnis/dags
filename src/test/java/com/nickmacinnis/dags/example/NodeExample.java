package com.nickmacinnis.dags.example;

import com.nickmacinnis.dags.AbstractNode;

public class NodeExample extends AbstractNode<NodeExample, EdgeExample> {

    @Override
    protected EdgeExample buildDirectEdge(NodeExample startNode, NodeExample endNode) {
        return new DirectEdgeExampleImpl(startNode, endNode);
    }

    @Override
    protected NodeExample getThis() {
        return this;
    }

    @Override
    protected EdgeExample buildImplicitEdge(NodeExample startNode, NodeExample endNode, EdgeExample entryEdge, EdgeExample directEdge, EdgeExample exitEdge,
            int hops) {
        return new ImplicitEdgeExampleImpl(startNode, endNode, entryEdge, directEdge, exitEdge, hops);
    }

    @Override
    public NodeExample clone()
            throws CloneNotSupportedException {
        return new NodeExample();
    }
}
