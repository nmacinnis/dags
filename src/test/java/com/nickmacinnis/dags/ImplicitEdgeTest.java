package com.nickmacinnis.dags;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class ImplicitEdgeTest {

    /**
     * Test method for {@link ImplicitEdge#attach()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAttach()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        DirectEdgeImpl g = new DirectEdgeImpl(o, p);

        ImplicitEdgeImpl h = new ImplicitEdgeImpl(m, p, e, f, g, 2);

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

        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        DirectEdgeImpl g = new DirectEdgeImpl(o, p);

        ImplicitEdgeImpl h = new ImplicitEdgeImpl(m, p, e, f, g, 2);

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
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        m.addChild(n);
        o.addChild(p);
        n.addChild(o);

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);
        DirectEdgeImpl g = new DirectEdgeImpl(o, p);

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
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        DirectEdgeImpl e = new DirectEdgeImpl(n, o);
        ImplicitEdgeImpl f = new ImplicitEdgeImpl(m, n, null, null, null, 0);
        ImplicitEdgeImpl g = new ImplicitEdgeImpl(n, p, null, null, null, 0);
        ImplicitEdgeImpl h = new ImplicitEdgeImpl(m, p, null, null, null, 0);
        e.incomingImplicitEdges.add(f);
        e.outgoingImplicitEdges.add(g);
        f.outgoingImplicitEdges.add(h);
        g.incomingImplicitEdges.add(h);

        Set<EdgeImpl> collectedEdges = e.collectAttachedEdges();

        assertEquals(3, collectedEdges.size());
        assertTrue(collectedEdges.contains(f));
        assertTrue(collectedEdges.contains(g));
        assertTrue(collectedEdges.contains(h));
    }
}
