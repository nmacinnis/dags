# CLAUDE.md

## Build and test commands

```bash
mvn compile          # compile only
mvn test             # compile + run all tests
mvn verify           # compile + test + SpotBugs static analysis
mvn test -pl .       # same, explicit project root
```

Coverage report: `target/site/jacoco/index.html` (generated on every `mvn test`).

## Architecture

This is a DAG library with **automatic implicit edge management**. Understanding this is critical before editing anything.

### The implicit edge system

When a direct edge A → B is added:
1. The direct edge is attached to A's outgoing list and B's incoming list.
2. For every incoming edge P → A, an implicit edge P → B is created (hops = P→A hops + 1).
3. For every outgoing edge B → C, an implicit edge A → C is created (hops = B→C hops + 1).
4. For every combination of P → A and B → C, an implicit edge P → C is created (hops = combined + 2).

When a direct edge is detached, all implicit edges that depend on it cascade-detach recursively via `collectAttachedEdges()`. Each implicit edge tracks three references: `entryEdge`, `directEdge`, `exitEdge` — used for both routing and cascade management.

**CME hazard**: `AbstractEdge.detach()` iterates its implicit edge sets while detaching elements. Detaching an implicit edge calls back into `detachOutgoingEdge`/`detachIncomingEdge`/`detachDependentEdge`, which mutates the live set. Always copy the set before iterating — `new LinkedHashSet<>(outgoingImplicitEdges)` etc.

### Self-referential generics

Every class uses the pattern `N extends Node<N, E>, E extends Edge<N, E>`. This forces concrete implementations to specify their own type. Do not try to simplify this — it is what makes `getThis()` and `buildDirectEdge()` type-safe at the concrete level.

`AbstractNode` and `AbstractEdge` provide a concrete `getThis()` using an `@SuppressWarnings("unchecked")` cast — safe by construction due to the generic constraint. Concrete subclasses do **not** need to override `getThis()`.

Concrete implementations must override:
- `buildDirectEdge(N, N)` — factory for direct edges
- `buildImplicitEdge(N, N, E, E, E, int)` — factory for implicit edges
- `copy()` — returns a new empty node of the same concrete type (edges are rebuilt by the graph)
- `constructThis()` (on `DirectedAcyclicGraph`) — factory for a new empty graph of the same type

### Class responsibilities

| Class | Responsibility |
|---|---|
| `AbstractNode` | Adds/removes edges, cycle detection, implicit edge generation, DFT/BFT/grid traversal |
| `AbstractEdge` | Tracks `incoming/outgoing/dependent` implicit edges; attach/detach lifecycle; sealed base |
| `DirectEdge` | Hops = 0; entry/direct/exit all point to self; `non-sealed` |
| `ImplicitEdge` | Hops > 0; attach/detach also registers with entry/exit/direct edges; `non-sealed` |
| `DirectedAcyclicGraph` | Root container; Graham-Coffman layout; copy; removeNode cascade |

### Exception model

`GraphLogicException` extends `RuntimeException` (unchecked). It is thrown when:
- An edge would create a cycle
- An edge points to a null end node

No method needs `throws GraphLogicException` in its signature.

### Sealed hierarchy

`AbstractEdge` is `sealed ... permits DirectEdge, ImplicitEdge`. Both subclasses are `non-sealed` to allow user extension. Use `instanceof DirectEdge<?, ?>` (with wildcards) rather than pattern-matching on the sealed type.

### Cycle detection

Checked in `AbstractNode.addDirectEdge()` before any edges are created:
1. Null end node → exception
2. Self-referent edge → exception
3. Any incoming edge whose start node equals end node → cycle → exception

Implicit edge addition does not re-check cycles because the invariant is maintained by construction.

### Layout algorithm

`calculateNodeCoordinates()` in `DirectedAcyclicGraph`:
1. Set Y = depth (max hop count from any incoming edge)
2. Run Graham-Coffman two-pass sort to minimize crossings (`untangle()`)
3. Compute X by averaging over all grid paths
4. Resolve X collisions (same Y, X within 1.0) by nudging right

## Code conventions

- No comments unless the WHY is non-obvious
- Prefer `instanceof DirectEdge<?, ?>` (with wildcard) for raw-type safety in Java 21
- `new ArrayList<>()` / `new LinkedHashSet<>()` — diamond operator everywhere
- `Objects.equals()` / `Objects.hash()` in `equals()`/`hashCode()` implementations
- Anonymous `Comparator` classes → lambdas
- JUnit 5 (`@Test` from `org.junit.jupiter.api`; `assertThrows` for expected exceptions)
- No `throws` declarations on test methods — `GraphLogicException` is unchecked
- SpotBugs exclusions live in `spotbugs-exclude.xml`; only exclude with documented justification
