package com.nickmacinnis.dags;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class DirectedAcyclicGraphTest {
    DirectedAcyclicGraphImpl r;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp()
            throws Exception {
        r = new DirectedAcyclicGraphImpl();
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddChild()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();

        assertTrue(r.addChild(n));

        DirectEdgeImpl e = new DirectEdgeImpl(r.getRootNode(), n);

        assertEquals(1, r.getRootNode().getOutgoingEdges().size());
        assertTrue(r.getRootNode().getOutgoingEdges().contains(e));
        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));

        assertEquals(2, r.getNodeCount());
        assertEquals(1, r.getEdgeCount());
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddDirectEdgeImplTwice()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();

        assertTrue(r.addChild(n));
        assertFalse(r.addChild(n));

        DirectEdgeImpl e = new DirectEdgeImpl(r.getRootNode(), n);

        assertEquals(1, r.getRootNode().getOutgoingEdges().size());
        assertTrue(r.getRootNode().getOutgoingEdges().contains(e));
        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));

        assertEquals(2, r.getNodeCount());
        assertEquals(1, r.getEdgeCount());
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddDirectEdgeImplWithTwoDirectEdges()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();

        r.addChild(n);
        n.addChild(o);

        DirectEdgeImpl e = new DirectEdgeImpl(r.getRootNode(), n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        ImplicitEdgeImpl g = new ImplicitEdgeImpl(r.getRootNode(), o, e, f, f, 1);

        assertEquals(2, r.getRootNode().getOutgoingEdges().size());
        assertTrue(r.getRootNode().getOutgoingEdges().contains(e));
        assertTrue(r.getRootNode().getOutgoingEdges().contains(g));

        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));
        assertEquals(1, n.getOutgoingEdges().size());
        assertTrue(n.getOutgoingEdges().contains(f));

        assertEquals(2, o.getIncomingEdges().size());
        assertTrue(o.getIncomingEdges().contains(f));
        assertTrue(o.getIncomingEdges().contains(g));

        assertEquals(3, r.getNodeCount());
        assertEquals(3, r.getEdgeCount());
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddDirectEdgeImplWithThreeDirectEdgesFrontToBack()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        r.addChild(n);
        n.addChild(o);
        o.addChild(p);

        DirectEdgeImpl e = new DirectEdgeImpl(r.getRootNode(), n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        DirectEdgeImpl g = new DirectEdgeImpl(o, p);

        ImplicitEdgeImpl h = new ImplicitEdgeImpl(r.getRootNode(), o, e, f, f, 1);
        ImplicitEdgeImpl i = new ImplicitEdgeImpl(n, p, f, g, g, 1);
        ImplicitEdgeImpl j = new ImplicitEdgeImpl(r.getRootNode(), p, h, g, g, 2);

        assertEquals(3, r.getRootNode().getOutgoingEdges().size());
        assertTrue(r.getRootNode().getOutgoingEdges().contains(e));
        assertTrue(r.getRootNode().getOutgoingEdges().contains(h));
        assertTrue(r.getRootNode().getOutgoingEdges().contains(j));

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

        assertEquals(4, r.getNodeCount());
        assertEquals(6, r.getEdgeCount());
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddDirectEdgeImplWithThreeDirectEdgesBackToFront()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        o.addChild(p);
        n.addChild(o);
        r.addChild(n);

        DirectEdgeImpl e = new DirectEdgeImpl(r.getRootNode(), n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        DirectEdgeImpl g = new DirectEdgeImpl(o, p);

        ImplicitEdgeImpl h = new ImplicitEdgeImpl(r.getRootNode(), o, e, e, f, 1);
        ImplicitEdgeImpl i = new ImplicitEdgeImpl(n, p, f, f, g, 1);
        ImplicitEdgeImpl j = new ImplicitEdgeImpl(r.getRootNode(), p, e, e, i, 2);

        assertEquals(3, r.getRootNode().getOutgoingEdges().size());
        assertTrue(r.getRootNode().getOutgoingEdges().contains(e));
        assertTrue(r.getRootNode().getOutgoingEdges().contains(h));
        assertTrue(r.getRootNode().getOutgoingEdges().contains(j));

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

        assertEquals(4, r.getNodeCount());
        assertEquals(6, r.getEdgeCount());
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddDirectEdgeImplWithThreeDirectEdgesMiddleFrontBack()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        n.addChild(o);
        r.addChild(n);
        o.addChild(p);

        DirectEdgeImpl e = new DirectEdgeImpl(r.getRootNode(), n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        DirectEdgeImpl g = new DirectEdgeImpl(o, p);

        ImplicitEdgeImpl h = new ImplicitEdgeImpl(r.getRootNode(), o, e, e, f, 1);
        ImplicitEdgeImpl i = new ImplicitEdgeImpl(n, p, f, g, g, 1);
        ImplicitEdgeImpl j = new ImplicitEdgeImpl(r.getRootNode(), p, h, g, g, 2);

        assertEquals(3, r.getRootNode().getOutgoingEdges().size());
        assertTrue(r.getRootNode().getOutgoingEdges().contains(e));
        assertTrue(r.getRootNode().getOutgoingEdges().contains(h));
        assertTrue(r.getRootNode().getOutgoingEdges().contains(j));

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

        assertEquals(4, r.getNodeCount());
        assertEquals(6, r.getEdgeCount());
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testAddDirectEdgeImplWithThreeDirectEdgesFrontBackMiddle()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        r.addChild(n);
        o.addChild(p);
        n.addChild(o);

        DirectEdgeImpl e = new DirectEdgeImpl(r.getRootNode(), n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        DirectEdgeImpl g = new DirectEdgeImpl(o, p);

        ImplicitEdgeImpl h = new ImplicitEdgeImpl(r.getRootNode(), o, e, f, f, 1);
        ImplicitEdgeImpl i = new ImplicitEdgeImpl(n, p, f, f, g, 1);
        ImplicitEdgeImpl j = new ImplicitEdgeImpl(r.getRootNode(), p, e, f, g, 2);

        assertEquals(3, r.getRootNode().getOutgoingEdges().size());
        assertTrue(r.getRootNode().getOutgoingEdges().contains(e));
        assertTrue(r.getRootNode().getOutgoingEdges().contains(h));
        assertTrue(r.getRootNode().getOutgoingEdges().contains(j));

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

        assertEquals(4, r.getNodeCount());
        assertEquals(6, r.getEdgeCount());
    }

    /**
     * Test method for {@link AbstractNode#removeChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testRemoveChild()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();

        r.addChild(n);

        assertTrue(r.removeChild(n));

        assertEquals(0, r.getRootNode().getOutgoingEdges().size());

        assertEquals(1, r.getNodeCount());
        assertEquals(0, r.getEdgeCount());
    }

    /**
     * Test method for {@link AbstractNode#removeChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testRemoveChildWithImplicitEdgeImpl()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();

        r.addChild(n);
        n.addChild(o);

        assertTrue(r.removeChild(n));

        assertEquals(0, r.getRootNode().getOutgoingEdges().size());
        assertEquals(0, n.getIncomingEdges().size());

        DirectEdgeImpl e = new DirectEdgeImpl(n, o);
        assertEquals(1, n.getOutgoingEdges().size());
        assertTrue(n.getOutgoingEdges().contains(e));
        assertEquals(1, o.getIncomingEdges().size());
        assertTrue(o.getIncomingEdges().contains(e));

        assertEquals(1, r.getNodeCount());
        assertEquals(0, r.getEdgeCount());
    }

    /**
     * Test method for {@link AbstractNode#removeChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testRemoveChildStillReachableWithImplicitEdgeImpl()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();

        r.addChild(n);
        n.addChild(o);
        r.addChild(o);

        assertTrue(r.removeChild(o));

        DirectEdgeImpl e = new DirectEdgeImpl(r.getRootNode(), n);
        DirectEdgeImpl f = new DirectEdgeImpl(n, o);
        ImplicitEdgeImpl h = new ImplicitEdgeImpl(r.getRootNode(), o, e, f, f, 1);

        assertEquals(2, r.getRootNode().getOutgoingEdges().size());
        assertTrue(r.getRootNode().getOutgoingEdges().contains(e));
        assertTrue(r.getRootNode().getOutgoingEdges().contains(h));

        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));
        assertEquals(1, n.getOutgoingEdges().size());
        assertTrue(n.getOutgoingEdges().contains(f));

        assertEquals(2, o.getIncomingEdges().size());
        assertTrue(o.getIncomingEdges().contains(f));
        assertTrue(o.getIncomingEdges().contains(h));

        assertEquals(3, r.getNodeCount());
        assertEquals(3, r.getEdgeCount());
    }

    /**
     * Test method for {@link AbstractNode#removeChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test
    public void testRemoveChildWithNonAddedEdgeImpl()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        assertFalse(r.removeChild(n));
    }

    /**
     * Test method for {@link AbstractNode#addChild(AbstractNode)}.
     * @throws GraphLogicException 
     */
    @Test(expected = GraphLogicException.class)
    public void testAddDirectEdgeImplToNullEndNodeImpl()
            throws GraphLogicException {
        r.addChild(null);
    }

    /**
     * Test method for {@link DirectedAcyclicGraph#iterator()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testGraphTraversalOrder()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        n.addChild(p);
        r.addChild(n);
        o.addChild(p);
        r.addChild(o);

        Iterator<NodeImpl> iterator = r.iterator();
        assertEquals(r.getRootNode(), iterator.next());
        assertEquals(n, iterator.next());
        assertEquals(p, iterator.next());
        assertEquals(o, iterator.next());
        assertEquals(p, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link DirectedAcyclicGraph#calculateNodeCoordinates()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testCalculateNodeCoordinates()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        r.addChild(n);
        r.addChild(o);
        n.addChild(p);
        o.addChild(p);
        
        r.calculateNodeCoordinates();
        
        assertEquals(0.0, r.getRootNode().getY(), .01);
        assertEquals(0.5, r.getRootNode().getX(), .01);
        
        assertEquals(0.0, n.getY(), .01);
        assertEquals(0.0, n.getX(), .01);
        
        assertEquals(0.0, o.getY(), .01);
        assertEquals(1.0, o.getX(), .01);
        
        assertEquals(1.0, p.getY(), .01);
        assertEquals(0.5, p.getX(), .01);
        
        
    }

    /**
     * Test method for {@link DirectedAcyclicGraph#calculateNodeCoordinates()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testCalculateNodeCoordinatesWithRootPassthrough()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();

        r.addChild(n);
        r.addChild(o);
        n.addChild(p);
        o.addChild(p);
        r.addChild(p);
        
        r.calculateNodeCoordinates();
        
        assertEquals(0.0, r.getRootNode().getY(), .01);
        assertEquals(1.0, r.getRootNode().getX(), .01);
        
        assertEquals(0.0, n.getY(), .01);
        assertEquals(0.0, n.getX(), .01);
        
        assertEquals(0.0, o.getY(), .01);
        assertEquals(2.0, o.getX(), .01);
        
        assertEquals(1.0, p.getY(), .01);
        assertEquals(1.0, p.getX(), .01);
        
        
    }

    /**
     * Test method for {@link DirectedAcyclicGraph#calculateNodeCoordinates()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testCalculateNodeCoordinatesWith_A_AB_B_Setup()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();
        NodeImpl q = new NodeImpl();
        NodeImpl s = new NodeImpl();

        r.addChild(n);
        r.addChild(o);

        n.addChild(p);
        n.addChild(q);
        
        o.addChild(q);
        o.addChild(s);
        
        r.calculateNodeCoordinates();
        
        assertEquals(0.0, r.getRootNode().getY(), .01);
        assertEquals(1.5, r.getRootNode().getX(), .01);
        
        assertEquals(0.0, n.getY(), .01);
        assertEquals(0.5, n.getX(), .01);
        
        assertEquals(0.0, o.getY(), .01);
        assertEquals(2.5, o.getX(), .01);
        
        assertEquals(1.0, p.getY(), .01);
        assertEquals(0.0, p.getX(), .01);
        
        assertEquals(1.0, q.getY(), .01);
        assertEquals(1.5, q.getX(), .01);
        
        assertEquals(1.0, s.getY(), .01);
        assertEquals(3.0, s.getX(), .01);
    }

    /**
     * Test method for {@link DirectedAcyclicGraph#calculateNodeCoordinates()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testCalculateNodeCoordinatesWith_A_BA_B_Setup()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();
        NodeImpl q = new NodeImpl();
        NodeImpl s = new NodeImpl();

        r.addChild(n);
        r.addChild(o);

        n.addChild(q);
        n.addChild(p);
        
        o.addChild(s);
        o.addChild(q);
        
        r.calculateNodeCoordinates();
        
        assertEquals(0.0, r.getRootNode().getY(), .01);
        assertEquals(1.5, r.getRootNode().getX(), .01);
        
        assertEquals(0.0, n.getY(), .01);
        assertEquals(0.5, n.getX(), .01);
        
        assertEquals(0.0, o.getY(), .01);
        assertEquals(2.5, o.getX(), .01);
        
        assertEquals(1.0, p.getY(), .01);
        assertEquals(0.0, p.getX(), .01);
        
        assertEquals(1.0, q.getY(), .01);
        assertEquals(1.5, q.getX(), .01);
        
        assertEquals(1.0, s.getY(), .01);
        assertEquals(3.0, s.getX(), .01);
    }

    /**
     * Test method for {@link DirectedAcyclicGraph#calculateNodeCoordinates()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testCalculateNodeCoordinatesWith_G_A_BA_B_H_Setup()
            throws GraphLogicException {
        NodeImpl g = new NodeImpl();
        NodeImpl i = new NodeImpl();
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();
        NodeImpl q = new NodeImpl();
        NodeImpl s = new NodeImpl();
        NodeImpl h = new NodeImpl();
        NodeImpl j = new NodeImpl();

        r.addChild(g);
        r.addChild(n);
        r.addChild(o);
        r.addChild(h);
        
        g.addChild(i);
        h.addChild(j);

        n.addChild(q);
        n.addChild(p);
        
        o.addChild(s);
        o.addChild(q);
        
        r.calculateNodeCoordinates();
        
        assertEquals(0.0, r.getRootNode().getY(), .01);
        assertEquals(2.5, r.getRootNode().getX(), .01);
        
        assertEquals(0.0, g.getY(), .01);
        assertEquals(0.0, g.getX(), .01);

        assertEquals(1.0, i.getY(), .01);
        assertEquals(0.0, i.getX(), .01);
        
        assertEquals(0.0, n.getY(), .01);
        assertEquals(1.5, n.getX(), .01);
        
        assertEquals(0.0, o.getY(), .01);
        assertEquals(3.5, o.getX(), .01);
        
        assertEquals(1.0, p.getY(), .01);
        assertEquals(1.0, p.getX(), .01);
        
        assertEquals(1.0, q.getY(), .01);
        assertEquals(2.5, q.getX(), .01);
        
        assertEquals(1.0, s.getY(), .01);
        assertEquals(4.0, s.getX(), .01);
        
        assertEquals(0.0, h.getY(), .01);
        assertEquals(5.0, h.getX(), .01);
        
        assertEquals(1.0, j.getY(), .01);
        assertEquals(5.0, j.getX(), .01);
    }

    /**
     * Test method for {@link DirectedAcyclicGraph#calculateNodeCoordinates()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testCalculateNodeCoordinatesWith_1_3_2_Setup()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();
        NodeImpl q = new NodeImpl();
        NodeImpl s = new NodeImpl();

        r.addChild(n);
        r.addChild(o);
        r.addChild(p);

        n.addChild(q);
        
        o.addChild(s);
        o.addChild(q);
        
        p.addChild(s);
        
        r.calculateNodeCoordinates();
        
        assertEquals(0.0, r.getRootNode().getY(), .01);
        assertEquals(1.5, r.getRootNode().getX(), .01);
        
        assertEquals(0.0, n.getY(), .01);
        assertEquals(0.0, n.getX(), .01);
        
        assertEquals(0.0, o.getY(), .01);
        assertEquals(1.5, o.getX(), .01);
        
        assertEquals(0.0, p.getY(), .01);
        assertEquals(3.0, p.getX(), .01);
        
        assertEquals(1.0, q.getY(), .01);
        assertEquals(0.5, q.getX(), .01);
        
        assertEquals(1.0, s.getY(), .01);
        assertEquals(2.5, s.getX(), .01);
    }

    /**
     * Test method for {@link DirectedAcyclicGraph#calculateNodeCoordinates()}.
     * @throws GraphLogicException 
     */
    @Test
    public void testCalculateNodeCoordinatesWith_1_2_2_2_Setup()
            throws GraphLogicException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        NodeImpl p = new NodeImpl();
        NodeImpl q = new NodeImpl();
        NodeImpl s = new NodeImpl();
        NodeImpl t = new NodeImpl();

        r.addChild(n);
        r.addChild(o);

        o.addChild(q);
        n.addChild(p);
        
        p.addChild(s);
        q.addChild(t);
        
        
        r.calculateNodeCoordinates();
        
        assertEquals(0.0, r.getRootNode().getY(), .01);
        assertEquals(0.5, r.getRootNode().getX(), .01);
        
        assertEquals(0.0, n.getY(), .01);
        assertEquals(0.0, n.getX(), .01);
        
        assertEquals(0.0, o.getY(), .01);
        assertEquals(1.0, o.getX(), .01);
        
        assertEquals(1.0, p.getY(), .01);
        assertEquals(0.0, p.getX(), .01);
        
        assertEquals(1.0, q.getY(), .01);
        assertEquals(1.0, q.getX(), .01);
        
        assertEquals(2.0, s.getY(), .01);
        assertEquals(0.0, s.getX(), .01);

        assertEquals(2.0, t.getY(), .01);
        assertEquals(1.0, t.getX(), .01);
    }

    /**
     * Test method for {@link DirectedAcyclicGraph#clone()}.
     * @throws GraphLogicException 
     * @throws CloneNotSupportedException 
     */
    @Test
    public void testClone() throws GraphLogicException, CloneNotSupportedException {
        NodeImpl n = new NodeImpl();
        NodeImpl o = new NodeImpl();
        
        r.addChild(n);
        n.addChild(o);
        
        DirectedAcyclicGraph<NodeImpl, EdgeImpl> clone = r.clone();
        NodeImpl rClone = clone.rootNode;
        assertEquals(2, rClone.getOutgoingEdges().size());
        assertTrue(! rClone.collectChildren().contains(n));
        assertTrue(! rClone.collectChildren().contains(o));
    }
}
