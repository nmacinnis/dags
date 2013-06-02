package com.nickmacinnis.dags.example;

import com.nickmacinnis.dags.DirectedAcyclicGraph;

public class DirectedAcyclicGraphExampleImpl extends DirectedAcyclicGraph<NodeExample, EdgeExample> {

    public DirectedAcyclicGraphExampleImpl() {
        super(new NodeExample());
    }

    public NodeExample getRootNode() {
        return rootNode;
    }

    @Override
    protected DirectedAcyclicGraph<NodeExample, EdgeExample> constructThis() {
        return new DirectedAcyclicGraphExampleImpl();
    }

}
