# dags

A Java library for building and manipulating **directed acyclic graphs** (DAGs).

## What it does

When you add a direct edge A → B to a graph, the library automatically computes and maintains all **implicit (transitive) edges** — so if A → B and B → C already exist, adding A → B automatically creates the implicit edge A → C. Removing an edge cascades correctly: all implicit edges that depended on it are detached.

Key features:
- Automatic implicit edge generation and cascade cleanup
- Cycle detection at edge-addition time (throws `GraphLogicException`)
- Graph layout via a Graham-Coffman crossing-minimization algorithm (`calculateNodeCoordinates()`)
- Deep graph clone support
- Type-safe self-referential generics throughout

## Concepts

| Term | Meaning |
|---|---|
| **Direct edge** | A user-created edge between two nodes (`hops = 0`) |
| **Implicit edge** | A library-maintained transitive edge derived from direct edges (`hops > 0`) |
| **Root node** | The single entry point of a `DirectedAcyclicGraph`; has no incoming edges |
| **Orphaned node** | A node with no incoming edges |

## Quick start

Extend the three abstract classes to create a concrete graph implementation:

```java
// 1. Your edge marker interface
public interface MyEdge extends Edge<MyNode, MyEdge> {}

// 2. Your node
public class MyNode extends AbstractNode<MyNode, MyEdge> {
    @Override protected MyNode getThis() { return this; }
    @Override protected MyEdge buildDirectEdge(MyNode start, MyNode end) {
        return new MyDirectEdge(start, end);
    }
    @Override protected MyEdge buildImplicitEdge(MyNode start, MyNode end,
            MyEdge entry, MyEdge direct, MyEdge exit, int hops) {
        return new MyImplicitEdge(start, end, entry, direct, exit, hops);
    }
    @Override public MyNode clone() { return new MyNode(); }
}

// 3. Your graph
public class MyGraph extends DirectedAcyclicGraph<MyNode, MyEdge> {
    public MyGraph() { super(new MyNode()); }
    @Override protected DirectedAcyclicGraph<MyNode, MyEdge> constructThis() {
        return new MyGraph();
    }
}

// 4. Use it
MyGraph graph = new MyGraph();
MyNode a = new MyNode();
MyNode b = new MyNode();
MyNode c = new MyNode();

graph.addChild(a);
a.addChild(b);
b.addChild(c);
// a now has implicit edge a→c (hops=1)

graph.calculateNodeCoordinates(); // assigns x/y for visualization
```

## Requirements

- Java 21+
- Maven 3.8+

Install Java 21 via [SDKMAN](https://sdkman.io/):
```bash
sdk install java 21-tem
```

Or via Homebrew:
```bash
brew install --cask temurin@21
```

## Building

```bash
mvn compile
```

## Running tests

```bash
mvn test
```

Coverage report is generated at `target/site/jacoco/index.html`.

## Project structure

```
src/main/java/com/nickmacinnis/dags/
  Node.java                    — node interface
  Edge.java                    — edge interface
  AbstractNode.java            — core node logic (cycle detection, implicit edges)
  AbstractEdge.java            — core edge logic (attach/detach lifecycle)
  DirectEdge.java              — base class for direct (user-created) edges
  ImplicitEdge.java            — base class for calculated transitive edges
  DirectedAcyclicGraph.java    — graph container with layout algorithm
  GraphLogicException.java     — thrown on DAG contract violations

src/test/java/com/nickmacinnis/dags/
  example/                     — concrete implementations used by tests
  AbstractNodeTest.java
  DirectedAcyclicGraphTest.java
  DirectEdgeTest.java
  ImplicitEdgeTest.java
```
