package com.nickmacinnis.dags;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nickmacinnis.dags.example.DirectEdgeExampleImpl;
import com.nickmacinnis.dags.example.DirectedAcyclicGraphExampleImpl;
import com.nickmacinnis.dags.example.EdgeExample;
import com.nickmacinnis.dags.example.ImplicitEdgeExampleImpl;
import com.nickmacinnis.dags.example.NodeExample;

public class DirectedAcyclicGraphTest {
    DirectedAcyclicGraphExampleImpl r;

    @BeforeEach
    public void setUp() {
        r = new DirectedAcyclicGraphExampleImpl();
    }

    @Test
    public void testAddChild() {
        NodeExample n = new NodeExample();

        assertTrue(r.addChild(n));

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(r.getRootNode(), n);

        assertEquals(1, r.getRootNode().getOutgoingEdges().size());
        assertTrue(r.getRootNode().getOutgoingEdges().contains(e));
        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));

        assertEquals(2, r.getNodeCount());
        assertEquals(1, r.getEdgeCount());
    }

    @Test
    public void testAddDirectEdgeImplTwice() {
        NodeExample n = new NodeExample();

        assertTrue(r.addChild(n));
        assertFalse(r.addChild(n));

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(r.getRootNode(), n);

        assertEquals(1, r.getRootNode().getOutgoingEdges().size());
        assertTrue(r.getRootNode().getOutgoingEdges().contains(e));
        assertEquals(1, n.getIncomingEdges().size());
        assertTrue(n.getIncomingEdges().contains(e));

        assertEquals(2, r.getNodeCount());
        assertEquals(1, r.getEdgeCount());
    }

    @Test
    public void testAddDirectEdgeImplWithTwoDirectEdges() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();

        r.addChild(n);
        n.addChild(o);

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(r.getRootNode(), n);
        DirectEdgeExampleImpl f = new DirectEdgeExampleImpl(n, o);
        ImplicitEdgeExampleImpl g = new ImplicitEdgeExampleImpl(r.getRootNode(), o, e, f, f, 1);

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

    @Test
    public void testAddDirectEdgeImplWithThreeDirectEdgesFrontToBack() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();

        r.addChild(n);
        n.addChild(o);
        o.addChild(p);

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(r.getRootNode(), n);
        DirectEdgeExampleImpl f = new DirectEdgeExampleImpl(n, o);
        DirectEdgeExampleImpl g = new DirectEdgeExampleImpl(o, p);

        ImplicitEdgeExampleImpl h = new ImplicitEdgeExampleImpl(r.getRootNode(), o, e, f, f, 1);
        ImplicitEdgeExampleImpl i = new ImplicitEdgeExampleImpl(n, p, f, g, g, 1);
        ImplicitEdgeExampleImpl j = new ImplicitEdgeExampleImpl(r.getRootNode(), p, h, g, g, 2);

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

    @Test
    public void testAddDirectEdgeImplWithThreeDirectEdgesBackToFront() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();

        o.addChild(p);
        n.addChild(o);
        r.addChild(n);

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(r.getRootNode(), n);
        DirectEdgeExampleImpl f = new DirectEdgeExampleImpl(n, o);
        DirectEdgeExampleImpl g = new DirectEdgeExampleImpl(o, p);

        ImplicitEdgeExampleImpl h = new ImplicitEdgeExampleImpl(r.getRootNode(), o, e, e, f, 1);
        ImplicitEdgeExampleImpl i = new ImplicitEdgeExampleImpl(n, p, f, f, g, 1);
        ImplicitEdgeExampleImpl j = new ImplicitEdgeExampleImpl(r.getRootNode(), p, e, e, i, 2);

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

    @Test
    public void testAddDirectEdgeImplWithThreeDirectEdgesMiddleFrontBack() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();

        n.addChild(o);
        r.addChild(n);
        o.addChild(p);

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(r.getRootNode(), n);
        DirectEdgeExampleImpl f = new DirectEdgeExampleImpl(n, o);
        DirectEdgeExampleImpl g = new DirectEdgeExampleImpl(o, p);

        ImplicitEdgeExampleImpl h = new ImplicitEdgeExampleImpl(r.getRootNode(), o, e, e, f, 1);
        ImplicitEdgeExampleImpl i = new ImplicitEdgeExampleImpl(n, p, f, g, g, 1);
        ImplicitEdgeExampleImpl j = new ImplicitEdgeExampleImpl(r.getRootNode(), p, h, g, g, 2);

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

    @Test
    public void testAddDirectEdgeImplWithThreeDirectEdgesFrontBackMiddle() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();

        r.addChild(n);
        o.addChild(p);
        n.addChild(o);

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(r.getRootNode(), n);
        DirectEdgeExampleImpl f = new DirectEdgeExampleImpl(n, o);
        DirectEdgeExampleImpl g = new DirectEdgeExampleImpl(o, p);

        ImplicitEdgeExampleImpl h = new ImplicitEdgeExampleImpl(r.getRootNode(), o, e, f, f, 1);
        ImplicitEdgeExampleImpl i = new ImplicitEdgeExampleImpl(n, p, f, f, g, 1);
        ImplicitEdgeExampleImpl j = new ImplicitEdgeExampleImpl(r.getRootNode(), p, e, f, g, 2);

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

    @Test
    public void testRemoveChild() {
        NodeExample n = new NodeExample();

        r.addChild(n);

        assertTrue(r.removeChild(n));

        assertEquals(0, r.getRootNode().getOutgoingEdges().size());

        assertEquals(1, r.getNodeCount());
        assertEquals(0, r.getEdgeCount());
    }

    @Test
    public void testRemoveChildWithImplicitEdgeImpl() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();

        r.addChild(n);
        n.addChild(o);

        assertTrue(r.removeChild(n));

        assertEquals(0, r.getRootNode().getOutgoingEdges().size());
        assertEquals(0, n.getIncomingEdges().size());

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(n, o);
        assertEquals(1, n.getOutgoingEdges().size());
        assertTrue(n.getOutgoingEdges().contains(e));
        assertEquals(1, o.getIncomingEdges().size());
        assertTrue(o.getIncomingEdges().contains(e));

        assertEquals(1, r.getNodeCount());
        assertEquals(0, r.getEdgeCount());
    }

    @Test
    public void testRemoveChildStillReachableWithImplicitEdgeImpl() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();

        r.addChild(n);
        n.addChild(o);
        r.addChild(o);

        assertTrue(r.removeChild(o));

        DirectEdgeExampleImpl e = new DirectEdgeExampleImpl(r.getRootNode(), n);
        DirectEdgeExampleImpl f = new DirectEdgeExampleImpl(n, o);
        ImplicitEdgeExampleImpl h = new ImplicitEdgeExampleImpl(r.getRootNode(), o, e, f, f, 1);

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

    @Test
    public void testRemoveChildWithNonAddedEdgeImpl() {
        NodeExample n = new NodeExample();
        assertFalse(r.removeChild(n));
    }

    @Test
    public void testAddDirectEdgeImplToNullEndNodeImpl() {
        assertThrows(GraphLogicException.class, () -> r.addChild(null));
    }

    @Test
    public void testGraphTraversalOrder() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();

        n.addChild(p);
        r.addChild(n);
        o.addChild(p);
        r.addChild(o);

        Iterator<NodeExample> iterator = r.iterator();
        assertEquals(r.getRootNode(), iterator.next());
        assertEquals(n, iterator.next());
        assertEquals(p, iterator.next());
        assertEquals(o, iterator.next());
        assertEquals(p, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCalculateNodeCoordinates() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();

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

    @Test
    public void testCalculateNodeCoordinatesWithRootPassthrough() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();

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

    @Test
    public void testCalculateNodeCoordinatesWith_A_AB_B_Setup() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();
        NodeExample q = new NodeExample();
        NodeExample s = new NodeExample();

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

    @Test
    public void testCalculateNodeCoordinatesWith_A_BA_B_Setup() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();
        NodeExample q = new NodeExample();
        NodeExample s = new NodeExample();

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

    @Test
    public void testCalculateNodeCoordinatesWith_G_A_BA_B_H_Setup() {
        NodeExample g = new NodeExample();
        NodeExample i = new NodeExample();
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();
        NodeExample q = new NodeExample();
        NodeExample s = new NodeExample();
        NodeExample h = new NodeExample();
        NodeExample j = new NodeExample();

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

    @Test
    public void testCalculateNodeCoordinatesWith_1_3_2_Setup() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();
        NodeExample q = new NodeExample();
        NodeExample s = new NodeExample();

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

    @Test
    public void testCalculateNodeCoordinatesWith_1_3_3_Setup() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();
        NodeExample q = new NodeExample();
        NodeExample s = new NodeExample();
        NodeExample t = new NodeExample();

        r.addChild(n);
        r.addChild(o);
        r.addChild(p);

        n.addChild(q);
        n.addChild(s);

        o.addChild(q);

        p.addChild(s);
        p.addChild(t);

        r.calculateNodeCoordinates();

        assertEquals(0.0, r.getRootNode().getY(), .01);
        assertEquals(2.0, r.getRootNode().getX(), .01);

        assertEquals(0.0, o.getY(), .01);
        assertEquals(0.0, o.getX(), .01);

        assertEquals(0.0, n.getY(), .01);
        assertEquals(1.5, n.getX(), .01);

        assertEquals(0.0, p.getY(), .01);
        assertEquals(3.5, p.getX(), .01);

        assertEquals(1.0, q.getY(), .01);
        assertEquals(0.5, q.getX(), .01);

        assertEquals(1.0, s.getY(), .01);
        assertEquals(2.5, s.getX(), .01);

        assertEquals(1.0, t.getY(), .01);
        assertEquals(4.0, t.getX(), .01);
    }

    @Test
    public void testCalculateNodeCoordinatesWith_1_2_2_2_Setup() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();
        NodeExample q = new NodeExample();
        NodeExample s = new NodeExample();
        NodeExample t = new NodeExample();

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

    @Test
    public void testCopy() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();

        r.addChild(n);
        n.addChild(o);

        DirectedAcyclicGraph<NodeExample, EdgeExample> copy = r.copy();
        NodeExample rootCopy = copy.rootNode;
        assertEquals(2, rootCopy.getOutgoingEdges().size());
        assertTrue(!rootCopy.collectChildren().contains(n));
        assertTrue(!rootCopy.collectChildren().contains(o));
    }

    @Test
    public void testCalculateNodeCoordinatesWithCollision() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();
        NodeExample q = new NodeExample();
        NodeExample s = new NodeExample();

        r.addChild(n);
        r.addChild(o);
        n.addChild(p);
        p.addChild(q);
        p.addChild(s);

        o.addChild(q);

        r.calculateNodeCoordinates();

        assertEquals(0.0, r.getRootNode().getY(), .01);
        assertEquals(1.0, r.getRootNode().getX(), .01);

        assertEquals(0.0, n.getY(), .01);
        assertEquals(0.5, n.getX(), .01);

        assertEquals(0.0, o.getY(), .01);
        assertEquals(2.0, o.getX(), .01);

        assertEquals(1.0, p.getY(), .01);
        assertEquals(0.5, p.getX(), .01);

        assertEquals(2.0, q.getY(), .01);
        assertEquals(1.0, q.getX(), .01);

        assertEquals(2.0, s.getY(), .01);
        assertEquals(2.0, s.getX(), .01);
    }

    @Test
    public void testCalculateNodeCoordinatesWithOverlap() {
        NodeExample n = new NodeExample();
        NodeExample o = new NodeExample();
        NodeExample p = new NodeExample();
        NodeExample q = new NodeExample();
        NodeExample s = new NodeExample();

        r.addChild(n);
        r.addChild(o);

        n.addChild(p);
        n.addChild(q);
        n.addChild(s);

        r.addChild(s);

        r.calculateNodeCoordinates();

        assertEquals(0.0, r.getRootNode().getY(), .01);
        assertEquals(2.0, r.getRootNode().getX(), .01);

        assertEquals(0.0, n.getY(), .01);
        assertEquals(1.0, n.getX(), .01);

        assertEquals(0.0, o.getY(), .01);
        assertEquals(4.0, o.getX(), .01);

        assertEquals(1.0, p.getY(), .01);
        assertEquals(2.5, p.getX(), .01);

        assertEquals(1.0, q.getY(), .01);
        assertEquals(3.5, q.getX(), .01);

        assertEquals(1.0, s.getY(), .01);
        assertEquals(1.5, s.getX(), .01);
    }
}
