# GraphColarCSP

#  Problem 2 — CSP: Graph Coloring with Backtracking, MRV, LCV, and AC-3

##  Overview
This project implements a **Constraint Satisfaction Problem (CSP)** solver for **graph coloring** using advanced AI search techniques.  
The program assigns colors to graph vertices such that **no two connected nodes share the same color**.

The solver uses:
- **Backtracking Search**
- **MRV (Minimum Remaining Values)**
- **LCV (Least Constraining Value)**
- **AC-3 (Arc Consistency)**

The implementation reads a graph from a text file, applies the CSP algorithms, and prints either a valid coloring solution or `failure`.

---

##  Concept Summary

**Goal:** Assign a color (from 1..k) to each vertex.  
**Constraint:** For every edge (u, v), `color[u] != color[v]`.

### Techniques used:
| Technique | Description |
| **Backtracking** | Systematically explores color assignments. |
| **MRV (Minimum Remaining Values)** | Chooses the variable with the fewest legal colors left. |
| **LCV (Least Constraining Value)** | Chooses the color that least restricts neighbor options. |
| **AC-3 (Arc Consistency)** | Prunes impossible values from domains to maintain consistency. |

---

##  Input Format

A simple text file (e.g., `csp_small.txt`):
(Problem 2 — CSP: Graph Coloring with Backtracking, MRV, LCV, and AC-3 solved)
