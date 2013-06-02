package com.nickmacinnis.dags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The root node of the graph, which has only outgoing edges.
 * The root node additionally tracks the contents of the graph
 * as a whole.
 * @author nmacinnis
 */
public abstract class DirectedAcyclicGraph<N extends Node<N, E>, E extends Edge<N, E>> implements Iterable<N>, Cloneable {
    protected N rootNode;

    public DirectedAcyclicGraph(N rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * Adds the node as a child of the root node
     * @return true if addition was successful
     * @throws GraphLogicException
     */
    public boolean addChild(N endNode)
            throws GraphLogicException {
        return rootNode.addChild(endNode);
    }

    /**
     * Removes the node from the root node's children
     * @return true if removal was successful
     */
    public boolean removeChild(N endNode) {
        return rootNode.removeChild(endNode);
    }

    /**
     * Removes the node from the graph
     * @param node - node to remove from the graph
     */
    public void removeNode(N node) {
        Set<N> allNodes = collectChildren();
        detachSingleNode(node);
        Set<N> remainingNodes = collectChildren();
        allNodes.removeAll(remainingNodes);
        // "deorphanize"
        for (N orphan : allNodes) {
            detachSingleNode(orphan);
        }
    }

    /**
     * Clean up a single node by detaching all edges from it
     */
    private void detachSingleNode(N node) {
        for(E edge : new LinkedHashSet<E>(node.getIncomingEdges())) {
            edge.detach();
        }
        for(E edge : new LinkedHashSet<E>(node.getOutgoingEdges())) {
            edge.detach();
        }
    }

    /**
     * @return number of nodes in the graph
     */
    public int getNodeCount() {
        return rootNode.collectChildren().size() + 1;
    }

    /**
     * @return number of direct edges in the graph
     */
    public int getEdgeCount() {
        return rootNode.collectEdges().size();
    }

    @Override
    public Iterator<N> iterator() {
        return rootNode.iterator();
    }

    /**
     * @return the unique set of all non-root nodes in the graph
     */
    public Set<N> collectChildren() {
        return rootNode.collectChildren();
    }

    /**
     * @return the unique set of all direct edges in the graph
     */
    public Set<E> collectDirectEdges() {
        return rootNode.collectDirectEdges();
    }

    /**
     * @return the unique set of all edges in the graph
     */
    public Set<E> collectEdges() {
        return rootNode.collectEdges();
    }

    /**
     * Rearrange the graph to remove most unnecessary edge crossings, then
     * generate coordinates for each node.
     */
    public void calculateNodeCoordinates() {
        for (N node : rootNode.collectChildren()) {
            node.setY(node.calculateDepth());
        }
        untangle();
        List<List<N>> rootGrid = rootNode.generateGrid();
        Map<N, Integer> nodeOccurrences = new HashMap<N, Integer>();
        for (int i = 0; i < rootGrid.size(); i++) {
            List<N> row = rootGrid.get(i);
            for (int j = 0; j < row.size(); j++) {
                N node = row.get(j);
                int occurrences = 1;
                if (nodeOccurrences.containsKey(node)) {
                    occurrences = nodeOccurrences.get(node) + 1;
                }
                nodeOccurrences.put(node, occurrences);
                double oldX = node.getX();
                node.setX(oldX + (i - oldX) / occurrences);
            }
        }
        Set<N> positionedNodes = new LinkedHashSet<N>();
        //remove collisions
        for (N node : rootNode.collectChildren()) {
            N overlappingNode = findOverlappingNode(node, positionedNodes);
            while (null != overlappingNode) {
                node.setX(overlappingNode.getX() + 1);
                overlappingNode = findOverlappingNode(node, positionedNodes);
            }
            positionedNodes.add(node);
        }
    }

    /**
     * Identify a node in the set of already positioned nodes which has the same
     * Y coordinate as the candidate node and which has X coordinate within 1 of the
     * candidate node.
     * @return The first node found which overlaps, or null if none found
     */
    private N findOverlappingNode(N candidateNode, Set<N> positionedNodes) {
        for (N positionedNode : positionedNodes) {
            if(candidateNode.getY() == positionedNode.getY() &&
                    Math.abs(candidateNode.getX() - positionedNode.getX()) < 1) {
                return positionedNode;
            }
        }
        return null;
    }

    /**
     * Apply Graham-Coffman algorithm to minimize the number of crossed edges in the graph.
     * First, sort the nodes into a list starting at the root in ascending order of parent most recently added to this list.
     * Then, re-sort the nodes into a new list starting from the end of the previous list, in ascending order of child most recently added to this list.
     * Finally, reverse the last list to get the reordered graph.
     * A child node _c with parents _a and _b will fall after a node having only parent _a and before a node having only parent _b.
     */
    protected void untangle() {
        List<N> initialList = new ArrayList<N>();
        final Map<N, List<N>> nodeParents = new HashMap<N, List<N>>();
        final Map<N, List<N>> nodeChildren = new HashMap<N, List<N>>();
        initialList.add(rootNode);
        for (N node : rootNode.collectChildren()) {
            initialList.add(node);
        }

        for (N node : initialList) {
            List<N> parents = new ArrayList<N>();
            for (E edge : node.getIncomingEdges()) {
                if (edge instanceof DirectEdge) {
                    parents.add(edge.getStartNode());
                }
            }
            nodeParents.put(node, parents);
            List<N> children = new ArrayList<N>();
            for (E edge : node.getOutgoingEdges()) {
                if (edge instanceof DirectEdge) {
                    children.add(edge.getEndNode());
                }
            }
            nodeChildren.put(node, children);
        }

        final List<N> firstPassSortedNodes = new ArrayList<N>();

        final Comparator<N> firstPassComparator = new Comparator<N>() {
            @Override
            public int compare(N n1, N n2) {
                List<N> n1Parents = nodeParents.get(n1);
                for (N parent : n1Parents) {
                    if (!firstPassSortedNodes.contains(parent)) {
                        return 1;
                    }
                }
                List<N> n2Parents = nodeParents.get(n2);
                for (N parent : n2Parents) {
                    if (!firstPassSortedNodes.contains(parent)) {
                        return -1;
                    }
                }
                int n1UnmarkedParents = n1Parents.size();
                int n2UnmarkedParents = n2Parents.size();
                for (int i = firstPassSortedNodes.size(); i > 0; i--) {
                    N recentNode = firstPassSortedNodes.get(i - 1);
                    boolean n1HasMostRecentNode = n1Parents.contains(recentNode);
                    if (n1HasMostRecentNode) {
                        n1UnmarkedParents--;
                    }
                    boolean n2HasMostRecentNode = n2Parents.contains(recentNode);
                    if (n2HasMostRecentNode) {
                        n2UnmarkedParents--;
                    }
                    if (n1HasMostRecentNode && !n2HasMostRecentNode) {
                        return 1;
                    } else if (n2HasMostRecentNode && !n1HasMostRecentNode) {
                        return -1;
                    } else if (n1UnmarkedParents == 0 && n2UnmarkedParents == 0) {
                        return 0;
                    } else if (n1UnmarkedParents == 0 && n2UnmarkedParents != 0) {
                        return 1;
                    } else if (n2UnmarkedParents == 0 && n1UnmarkedParents != 0) {
                        return -1;
                    }
                }

                return 0;
            }

        };

        while (!initialList.isEmpty()) {
            N nextNode = Collections.min(initialList, firstPassComparator);
            firstPassSortedNodes.add(nextNode);
            initialList.remove(nextNode);
        }
        Collections.reverse(firstPassSortedNodes);
        final List<N> secondPassSortedNodes = new ArrayList<N>();

        Comparator<N> secondPassComparator = new Comparator<N>() {
            @Override
            public int compare(N n1, N n2) {
                List<N> n1Children = nodeChildren.get(n1);
                for (N child : n1Children) {
                    if (!secondPassSortedNodes.contains(child)) {
                        return 1;
                    }
                }
                List<N> n2Children = nodeChildren.get(n2);
                for (N child : n2Children) {
                    if (!secondPassSortedNodes.contains(child)) {
                        return -1;
                    }
                }
                int n1UnmarkedChildren = n1Children.size();
                int n2UnmarkedChildren = n2Children.size();
                for (int i = secondPassSortedNodes.size(); i > 0; i--) {
                    N recentNode = secondPassSortedNodes.get(i - 1);
                    boolean n1HasMostRecentNode = n1Children.contains(recentNode);
                    if (n1HasMostRecentNode) {
                        n1UnmarkedChildren--;
                    }
                    boolean n2HasMostRecentNode = n2Children.contains(recentNode);
                    if (n2HasMostRecentNode) {
                        n2UnmarkedChildren--;
                    }
                    if (n1HasMostRecentNode && !n2HasMostRecentNode) {
                        return 1;
                    } else if (n2HasMostRecentNode && !n1HasMostRecentNode) {
                        return -1;
                    } else if (n1UnmarkedChildren == 0 && n2UnmarkedChildren == 0) {
                        return 0;
                    } else if (n1UnmarkedChildren == 0 && n2UnmarkedChildren != 0) {
                        return 1;
                    } else if (n2UnmarkedChildren == 0 && n1UnmarkedChildren != 0) {
                        return -1;
                    }
                }

                return 0;
            }

        };
        while (!firstPassSortedNodes.isEmpty()) {
            N nextNode = Collections.min(firstPassSortedNodes, secondPassComparator);
            secondPassSortedNodes.add(nextNode);
            firstPassSortedNodes.remove(nextNode);
        }
        Collections.reverse(secondPassSortedNodes);

        //now each node sorts its edges in the order of the nodes in this list

        Comparator<E> outgoingEdgeComparator = new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                Integer o1Index = secondPassSortedNodes.indexOf(o1.getEndNode());
                Integer o2Index = secondPassSortedNodes.indexOf(o2.getEndNode());
                return o1Index.compareTo(o2Index);
            }
        };

        for (N node : this) {
            Collections.sort(node.getOutgoingEdges(), outgoingEdgeComparator);
        }

        //now finally regenerate the node ordering based on traversal so that pass-through edges
        // won't distort everything.
        final List<N> nodesOrderedByTraversal = new ArrayList<N>();
        for (N node : rootNode.dft()) {
            if (!nodesOrderedByTraversal.contains(node)) {
                nodesOrderedByTraversal.add(node);
            }
        }

        //and reorder all the edges according to this list
        Comparator<E> incomingEdgeComparator = new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                Integer o1Index = nodesOrderedByTraversal.indexOf(o1.getStartNode());
                Integer o2Index = nodesOrderedByTraversal.indexOf(o2.getStartNode());
                return o1Index.compareTo(o2Index);
            }
        };

        outgoingEdgeComparator = new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                Integer o1Index = nodesOrderedByTraversal.indexOf(o1.getEndNode());
                Integer o2Index = nodesOrderedByTraversal.indexOf(o2.getEndNode());
                return o1Index.compareTo(o2Index);
            }
        };
        for (N node : this) {
            Collections.sort(node.getIncomingEdges(), incomingEdgeComparator);
            Collections.sort(node.getOutgoingEdges(), outgoingEdgeComparator);
        }
    }

    /**
     * @return this
     */
    protected abstract DirectedAcyclicGraph<N, E> constructThis();

    @Override
    public DirectedAcyclicGraph<N, E> clone()
            throws CloneNotSupportedException {
        DirectedAcyclicGraph<N, E> clone = constructThis();
        Set<N> nodes = collectChildren();
        Map<N, N> clonedNodes = new HashMap<N, N>();
        clonedNodes.put(rootNode, clone.rootNode);
        for (N node : nodes) {
            clonedNodes.put(node, node.clone());
        }
        Set<E> edges = collectEdges();
        for (E edge : edges) {
            if (edge instanceof DirectEdge) {
                N startNode = clonedNodes.get(edge.getStartNode());
                N endNode = clonedNodes.get(edge.getEndNode());
                try {
                    startNode.addChild(endNode);
                } catch (GraphLogicException e) {
                    // doubtful
                }
            }

        }

        return clone;
    }

}
