package com.nickmacinnis.dags;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

public class DirectEdgeTest {

    /**
     * Test method for {@link Edge#collectAttachedEdges()}.
     */
    @Test
    public void testCollectAttachedEdges() {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);
        Set<EdgeImpl> collectedEdges = e.collectAttachedEdges();
        assertTrue(collectedEdges.isEmpty());
    }

    /**
     * Test method for {@link Edge#attach()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAttach()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);
        assertTrue(e.attach());
        assertTrue(m.getOutgoingEdges().contains(e));
        assertTrue(n.getIncomingEdges().contains(e));
    }

    /**
     * Test method for {@link Edge#attach()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAttachWithNulls()
            throws GraphLogicException {
        DirectEdgeImpl e = new DirectEdgeImpl(null, null);
        assertFalse(e.attach());
    }

    /**
     * Test method for {@link Edge#detach()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testDetach()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);
        e.attach();

        assertTrue(e.detach());
        assertTrue(m.getOutgoingEdges().isEmpty());
        assertTrue(n.getIncomingEdges().isEmpty());
    }

    /**
     * Test method for {@link Edge#detach()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testDetachWithNulls()
            throws GraphLogicException {
        DirectEdgeImpl e = new DirectEdgeImpl(null, null);
        assertFalse(e.detach());
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
        e.dependentImplicitEdges.add(h);

        Set<EdgeImpl> collectedEdges = e.collectAttachedEdges();

        assertEquals(3, collectedEdges.size());
        assertTrue(collectedEdges.contains(f));
        assertTrue(collectedEdges.contains(g));
        assertTrue(collectedEdges.contains(h));
    }
}
