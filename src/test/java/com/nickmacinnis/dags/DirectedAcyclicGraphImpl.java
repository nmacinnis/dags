package com.nickmacinnis.dags;

public class DirectedAcyclicGraphImpl extends DirectedAcyclicGraph<NodeImpl, EdgeImpl> {
    
    public DirectedAcyclicGraphImpl() {
        super(new NodeImpl());
    }

    public NodeImpl getRootNode() {
        return rootNode;
    }

    @Override
    protected DirectedAcyclicGraph<NodeImpl, EdgeImpl> constructThis() {
        return new DirectedAcyclicGraphImpl();
    }
    
    
}
