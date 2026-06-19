package com.nickmacinnis.dags;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.nickmacinnis.dags.example.DirectEdgeExampleImpl;
import com.nickmacinnis.dags.example.EdgeExample;
import com.nickmacinnis.dags.example.ImplicitEdgeExampleImpl;
import com.nickmacinnis.dags.example.NodeExample;

public class DirectEdgeTest {

    @Test
    public void testCollectAttachedEdges() {
        NodeExample m = new NodeExample();
        NodeExample n = new NodeExample();

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(m, n);
        Set<EdgeExample> collectedEdges = e.collectAttachedEdges();
        assertTrue(collectedEdges.isEmpty());
    }

    @Test
    public void testAttach() {
        NodeExample m = new NodeExample();
        NodeExample n = new NodeExample();

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(m, n);
        assertTrue(e.attach());
        assertTrue(m.getOutgoingEdges().contains(e));
        assertTrue(n.getIncomingEdges().contains(e));
    }

    @Test
    public void testAttachWithNulls() {
        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(null, null);
        assertFalse(e.attach());
    }

    @Test
    public void testDetach() {
        NodeExample m = new NodeExample();
        NodeExample n = new NodeExample();

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(m, n);
        e.attach();

        assertTrue(e.detach());
        assertTrue(m.getOutgoingEdges().isEmpty());
        assertTrue(n.getIncomingEdges().isEmpty());
    }

    @Test
    public void testDetachWithNulls() {
        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(null, null);
        assertFalse(e.detach());
    }

    @Test
    public void testCollectAttachedEdgesWithImplicits() {
        NodeExample m = new NodeExample();
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(n, o);
        ImplicitEdgeExampleImpl f = new ImplicitEdgeExampleImpl(m, n, null, null, null, 0);
        ImplicitEdgeExampleImpl g = new ImplicitEdgeExampleImpl(n, p, null, null, null, 0);
        ImplicitEdgeExampleImpl h = new ImplicitEdgeExampleImpl(m, p, null, null, null, 0);
        e.incomingImplicitEdges.add(f);
        e.outgoingImplicitEdges.add(g);
        e.dependentImplicitEdges.add(h);

        Set<EdgeExample> collectedEdges = e.collectAttachedEdges();

        assertEquals(3, collectedEdges.size());
        assertTrue(collectedEdges.contains(f));
        assertTrue(collectedEdges.contains(g));
        assertTrue(collectedEdges.contains(h));
    }
}
