package com.nickmacinnis.dags;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class AbstractNodeTest {
    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddChild()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();

        assertTrue(m.addChild(n));

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);

        assertEquals(1, m.getOutgoingEdges().size());
        assertTrue(m.getOutgoingEdges().contains(e));
        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddChildTwice()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();

        assertTrue(m.addChild(n));
        assertFalse(m.addChild(n));

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);

        assertEquals(1, m.getOutgoingEdges().size());
        assertTrue(m.getOutgoingEdges().contains(e));
        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test(expected = GraphLogicException.class)
    public void testAddChildDirectEdgeImplCycle()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        m.addChild(n);

        n.addChild(m);
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test(expected = GraphLogicException.class)
    public void testAddChildSelfReferentCycle()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        m.addChild(m);
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddChildWithTwoDirectEdges()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();

        m.addChild(n);
        n.addChild(o);

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        ImplicitEdgeImpl g = new ImplicitEdgeImpl(m, o, e, f, f, 1);

        assertEquals(2, m.getOutgoingEdges().size());
        assertTrue(m.getOutgoingEdges().contains(e));
        assertTrue(m.getOutgoingEdges().contains(g));

        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));
        assertEquals(1, n.getOutgoingEdges().size());
        assertTrue(n.getOutgoingEdges().contains(f));

        assertEquals(2, o.getIncomingEdges().size());
        assertTrue(o.getIncomingEdges().contains(f));
        assertTrue(o.getIncomingEdges().contains(g));
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test(expected = GraphLogicException.class)
    public void testAddChildWithImplicitEdgeImplCycle()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();

        m.addChild(n);
        n.addChild(o);
        o.addChild(m);
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddChildWithThreeDirectEdgesFrontToBack()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        m.addChild(n);
        n.addChild(o);
        o.addChild(p);

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        DirectEdgeImpl g = new DirectEdgeImpl(o, p);

        ImplicitEdgeImpl h = new ImplicitEdgeImpl(m, o, e, f, f, 1);
        ImplicitEdgeImpl i = new ImplicitEdgeImpl(n, p, f, g, g, 1);
        ImplicitEdgeImpl j = new ImplicitEdgeImpl(m, p, h, g, g, 2);

        assertEquals(3, m.getOutgoingEdges().size());
        assertTrue(m.getOutgoingEdges().contains(e));
        assertTrue(m.getOutgoingEdges().contains(h));
        assertTrue(m.getOutgoingEdges().contains(j));

        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));
        assertEquals(2, n.getOutgoingEdges().size());
        assertTrue(n.getOutgoingEdges().contains(f));
        assertTrue(n.getOutgoingEdges().contains(i));

        assertEquals(2, o.getIncomingEdges().size());
        assertTrue(o.getIncomingEdges().contains(f));
        assertTrue(o.getIncomingEdges().contains(h));
        assertEquals(1, o.getOutgoingEdges().size());
        assertTrue(o.getOutgoingEdges().contains(g));

        assertEquals(3, p.getIncomingEdges().size());
        assertTrue(p.getIncomingEdges().contains(g));
        assertTrue(p.getIncomingEdges().contains(i));
        assertTrue(p.getIncomingEdges().contains(j));
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddChildWithThreeDirectEdgesBackToFront()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        o.addChild(p);
        n.addChild(o);
        m.addChild(n);

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        DirectEdgeImpl g = new DirectEdgeImpl(o, p);

        ImplicitEdgeImpl h = new ImplicitEdgeImpl(m, o, e, e, f, 1);
        ImplicitEdgeImpl i = new ImplicitEdgeImpl(n, p, f, f, g, 1);
        ImplicitEdgeImpl j = new ImplicitEdgeImpl(m, p, e, e, i, 2);

        assertEquals(3, m.getOutgoingEdges().size());
        assertTrue(m.getOutgoingEdges().contains(e));
        assertTrue(m.getOutgoingEdges().contains(h));
        assertTrue(m.getOutgoingEdges().contains(j));

        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));
        assertEquals(2, n.getOutgoingEdges().size());
        assertTrue(n.getOutgoingEdges().contains(f));
        assertTrue(n.getOutgoingEdges().contains(i));

        assertEquals(2, o.getIncomingEdges().size());
        assertTrue(o.getIncomingEdges().contains(f));
        assertTrue(o.getIncomingEdges().contains(h));
        assertEquals(1, o.getOutgoingEdges().size());
        assertTrue(o.getOutgoingEdges().contains(g));

        assertEquals(3, p.getIncomingEdges().size());
        assertTrue(p.getIncomingEdges().contains(g));
        assertTrue(p.getIncomingEdges().contains(i));
        assertTrue(p.getIncomingEdges().contains(j));
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddChildWithThreeDirectEdgesMiddleFrontBack()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        n.addChild(o);
        m.addChild(n);
        o.addChild(p);

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        DirectEdgeImpl g = new DirectEdgeImpl(o, p);

        ImplicitEdgeImpl h = new ImplicitEdgeImpl(m, o, e, e, f, 1);
        ImplicitEdgeImpl i = new ImplicitEdgeImpl(n, p, f, g, g, 1);
        ImplicitEdgeImpl j = new ImplicitEdgeImpl(m, p, h, g, g, 2);

        assertEquals(3, m.getOutgoingEdges().size());
        assertTrue(m.getOutgoingEdges().contains(e));
        assertTrue(m.getOutgoingEdges().contains(h));
        assertTrue(m.getOutgoingEdges().contains(j));

        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));
        assertEquals(2, n.getOutgoingEdges().size());
        assertTrue(n.getOutgoingEdges().contains(f));
        assertTrue(n.getOutgoingEdges().contains(i));

        assertEquals(2, o.getIncomingEdges().size());
        assertTrue(o.getIncomingEdges().contains(f));
        assertTrue(o.getIncomingEdges().contains(h));
        assertEquals(1, o.getOutgoingEdges().size());
        assertTrue(o.getOutgoingEdges().contains(g));

        assertEquals(3, p.getIncomingEdges().size());
        assertTrue(p.getIncomingEdges().contains(g));
        assertTrue(p.getIncomingEdges().contains(i));
        assertTrue(p.getIncomingEdges().contains(j));
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddChildWithThreeDirectEdgesFrontBackMiddle()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        m.addChild(n);
        o.addChild(p);
        n.addChild(o);

        DirectEdgeImpl e = new DirectEdgeImpl(m, n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        DirectEdgeImpl g = new DirectEdgeImpl(o, p);

        ImplicitEdgeImpl h = new ImplicitEdgeImpl(m, o, e, f, f, 1);
        ImplicitEdgeImpl i = new ImplicitEdgeImpl(n, p, f, f, g, 1);
        ImplicitEdgeImpl j = new ImplicitEdgeImpl(m, p, e, f, g, 2);

        assertEquals(3, m.getOutgoingEdges().size());
        assertTrue(m.getOutgoingEdges().contains(e));
        assertTrue(m.getOutgoingEdges().contains(h));
        assertTrue(m.getOutgoingEdges().contains(j));

        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));
        assertEquals(2, n.getOutgoingEdges().size());
        assertTrue(n.getOutgoingEdges().contains(f));
        assertTrue(n.getOutgoingEdges().contains(i));

        assertEquals(2, o.getIncomingEdges().size());
        assertTrue(o.getIncomingEdges().contains(f));
        assertTrue(o.getIncomingEdges().contains(h));
        assertEquals(1, o.getOutgoingEdges().size());
        assertTrue(o.getOutgoingEdges().contains(g));

        assertEquals(3, p.getIncomingEdges().size());
        assertTrue(p.getIncomingEdges().contains(g));
        assertTrue(p.getIncomingEdges().contains(i));
        assertTrue(p.getIncomingEdges().contains(j));
    }

    /**
     * Test method for {@link AbstractNode#removeChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testRemoveChild()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();

        m.addChild(n);

        assertTrue(m.removeChild(n));

        assertEquals(0, m.getOutgoingEdges().size());
    }

    /**
     * Test method for {@link AbstractNode#removeChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testRemoveChildWithImplicitEdgeImpl()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();

        m.addChild(n);
        n.addChild(o);

        assertTrue(m.removeChild(n));

        assertEquals(0, m.getOutgoingEdges().size());
        assertEquals(0, n.getIncomingEdges().size());

        DirectEdgeImpl e = new DirectEdgeImpl(n, o);
        assertEquals(1, n.getOutgoingEdges().size());
        assertTrue(n.getOutgoingEdges().contains(e));
        assertEquals(1, o.getIncomingEdges().size());
        assertTrue(o.getIncomingEdges().contains(e));
    }

    /**
     * Test method for {@link AbstractNode#removeChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testRemoveChildWithNonAddedEdgeImpl()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        assertFalse(m.removeChild(n));
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test(expected = GraphLogicException.class)
    public void testAddDirectEdgeImplToNullEndNodeImpl()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        m.addChild(null);
    }

    /**
     * Test method for {@link AbstractNode#isOrphaned()}.
     */
    @Test
    public void testIsOrphaned() {
        NodeImpl m = new NodeImpl();
        assertTrue(m.isOrphaned());
    }

    /**
     * Test method for {@link AbstractNode#isOrphaned()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testIsOrphanedOnChild()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        m.addChild(n);
        assertFalse(n.isOrphaned());
    }

    /**
     * Test method for {@link AbstractNode#collectChildren()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testCollectChildren()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        n.addChild(p);
        m.addChild(n);
        o.addChild(p);
        m.addChild(o);

        Set<NodeImpl> collectedChildren = m.collectChildren();
        assertEquals(3, collectedChildren.size());
        assertTrue(collectedChildren.contains(n));
        assertTrue(collectedChildren.contains(o));
        assertTrue(collectedChildren.contains(p));
    }

    /**
     * Test method for {@link AbstractNode#listChildren()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testListChildren()
            throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        n.addChild(p);
        m.addChild(n);
        o.addChild(p);
        m.addChild(o);

        List<NodeImpl> listedChildren = m.listChildren();
        Iterator<NodeImpl> iterator = listedChildren.iterator();
        assertEquals(n, iterator.next());
        assertEquals(p, iterator.next());
        assertEquals(o, iterator.next());
        assertEquals(p, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link AbstractNode#collectDirectEdges()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testCollectDirectEdges() throws GraphLogicException {
        NodeImpl m = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        m.addChild(n);
        m.addChild(o);
        n.addChild(p);
        o.addChild(p);
        
        assertEquals(4, m.collectDirectEdges().size());
    }
}
