package com.nickmacinnis.dags;

import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.nickmacinnis.dags.example.DirectEdgeExampleImpl;
import com.nickmacinnis.dags.example.EdgeExample;
import com.nickmacinnis.dags.example.ImplicitEdgeExampleImpl;
import com.nickmacinnis.dags.example.NodeExample;

public class ImplicitEdgeTest {

    /**
     * Test method for {@link ImplicitEdge#attach()}.
     * @throws GraphLogicException
     */
    @Test
    public void testAttach()
            throws GraphLogicException {
        NodeExample m = new NodeExample();
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(m, n);
        DirectEdgeExampleImpl f = new DirectEdgeExampleImpl(n, o);
        DirectEdgeExampleImpl g = new DirectEdgeExampleImpl(o, p);

        ImplicitEdgeExampleImpl h = new ImplicitEdgeExampleImpl(m, p, e, f, g, 2);

        assertTrue(h.attach());
        assertTrue(m.getOutgoingEdges().contains(h));
        assertTrue(p.getIncomingEdges().contains(h));

        assertTrue(e.getIncomingImplicitEdges().isEmpty());
        assertTrue(e.getDependentImplicitEdges().isEmpty());
        assertEquals(1, e.getOutgoingImplicitEdges().size());
        assertTrue(e.getOutgoingImplicitEdges().contains(h));

        assertTrue(f.getIncomingImplicitEdges().isEmpty());
        assertEquals(1, f.getDependentImplicitEdges().size());
        assertTrue(f.getDependentImplicitEdges().contains(h));
        assertTrue(f.getOutgoingImplicitEdges().isEmpty());

        assertEquals(1, g.getIncomingImplicitEdges().size());
        assertTrue(g.getIncomingImplicitEdges().contains(h));
        assertTrue(g.getDependentImplicitEdges().isEmpty());
        assertTrue(g.getOutgoingImplicitEdges().isEmpty());
    }

    /**
     * Test method for {@link ImplicitEdge#detach()}.
     * @throws GraphLogicException
     */
    @Test
    public void testDetach()
            throws GraphLogicException {

        NodeExample m = new NodeExample();
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(m, n);
        DirectEdgeExampleImpl f = new DirectEdgeExampleImpl(n, o);
        DirectEdgeExampleImpl g = new DirectEdgeExampleImpl(o, p);

        ImplicitEdgeExampleImpl h = new ImplicitEdgeExampleImpl(m, p, e, f, g, 2);

        h.attach();
        assertTrue(h.detach());

        assertFalse(m.getOutgoingEdges().contains(h));
        assertFalse(p.getIncomingEdges().contains(h));

        assertTrue(e.getOutgoingImplicitEdges().isEmpty());
        assertTrue(f.getDependentImplicitEdges().isEmpty());
        assertTrue(g.getIncomingImplicitEdges().isEmpty());
    }

    /**
     * Test method for {@link ImplicitEdge#detach()}.
     * @throws GraphLogicException
     */
    @Test
    public void testDetachImplicitEdges()
            throws GraphLogicException {
        NodeExample m = new NodeExample();
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();

        m.addChild(n);
        o.addChild(p);
        n.addChild(o);

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(m, n);
        DirectEdgeExampleImpl g = new DirectEdgeExampleImpl(o, p);

        assertTrue(n.removeChild(o));

        assertEquals(1, m.getOutgoingEdges().size());
        assertTrue(m.getOutgoingEdges().contains(e));
        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));

        assertTrue(n.getOutgoingEdges().isEmpty());
        assertTrue(o.getIncomingEdges().isEmpty());

        assertEquals(1, o.getOutgoingEdges().size());
        assertTrue(o.getOutgoingEdges().contains(g));
        assertEquals(1, p.getIncomingEdges().size());
        assertTrue(p.getIncomingEdges().contains(g));
    }

    /**
     * Test method for {@link Edge#collectAttachedEdges()}.
     */
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
        f.outgoingImplicitEdges.add(h);
        g.incomingImplicitEdges.add(h);

        Set<EdgeExample> collectedEdges = e.collectAttachedEdges();

        assertEquals(3, collectedEdges.size());
        assertTrue(collectedEdges.contains(f));
        assertTrue(collectedEdges.contains(g));
        assertTrue(collectedEdges.contains(h));
    }
}
