# 📚 DSA Bookshelf — Master Index

> **Goal:** Build deep intuition for problem-solving — from cracking product-based company interviews to mastering advanced computer science concepts.
>
> **Language:** Java | **Author:** Senpai (AI) × You
>
> **Inspired by:** Our own roadmap + UC Berkeley CS61B curriculum
>
> **Last Updated:** September 2026

---

## 🗺️ How to Use This Bookshelf

1. **Read in order** — each book builds on the previous one.
2. **Complete all patterns** in a book before moving to the next.
3. **Solve practice problems** listed in each chapter before moving on.
4. **Come back to this index** to track your progress and see what's next.

### Progress Key
| Symbol | Meaning |
|--------|---------|
| ✅ | Completed |
| 🔄 | In Progress |
| ⏳ | Up Next |
| 🔒 | Locked (complete prerequisites first) |

---

## 🏁 Phase 1 — Foundations
> *Master these before anything else. Every DSA problem traces back to these.*

| # | Book | File | Status | Topics |
|---|------|------|--------|--------|
| 01 | **Arrays & Strings** | [01-arrays-and-strings.md](./01-arrays-and-strings.md) | 🔄 In Progress | Array memory model, String internals, Two Pointers, Sliding Window, Prefix Sum, Kadane's, Dutch Flag, Cyclic Sort, KMP, Rolling Hash |

---

## 🎯 Phase 2 — Core DSA (Product-Based Company Essentials)
> *Bread and butter of every technical interview at Amazon, Google, Microsoft, Flipkart, Swiggy, etc.*
> *This phase also aligns fully with the UC Berkeley CS61B curriculum.*

| # | Book | File | Status | Topics |
|---|------|------|--------|--------|
| 02 | **Java OOP & Design** | `02-java-oop-and-design.md` | ⏳ | Interface vs inheritance, Polymorphism, Generics, Comparator/Comparable, Encapsulation, Packages — *CS61B Week 2–4* |
| 03 | **Asymptotics & Complexity** | `03-asymptotics-and-complexity.md` | 🔒 | Big-O, Big-Theta, Big-Omega, Amortized analysis, Recursion tree method, Master theorem — *CS61B Week 5* |
| 04 | **Hashing & HashMaps** | `04-hashing-and-hashmaps.md` | 🔒 | HashMap, HashSet, LinkedHashMap, TreeMap internals, Collision (chaining vs open addressing), Load factor, Two Sum family, Subarray Sum, Group Anagrams, Longest Consecutive Sequence — *CS61B Week 8* |
| 05 | **Linked List** | `05-linked-list.md` | 🔒 | Singly & Doubly LL, Fast-Slow pointers, Reversal, Merge, Cycle detection, LRU Cache — *CS61B Week 3* |
| 06 | **Stack, Queue & Deque** | `06-stack-queue-deque.md` | 🔒 | Stack, Queue, Deque (ArrayDeque), Monotonic Stack, Next Greater Element, Valid Parentheses, Sliding Window Maximum — *CS61B Week 3* |
| 07 | **Disjoint Sets (Union-Find)** | `07-disjoint-sets.md` | 🔒 | Quick-find, Quick-union, Weighted quick-union, Path compression, Cycle detection in graphs, Number of connected components — *CS61B Lab 05* |
| 08 | **Recursion & Backtracking** | `08-recursion-and-backtracking.md` | 🔒 | Call stack, Recursion tree, Subsets, Permutations, Combinations, N-Queens, Sudoku Solver |
| 09 | **Trees & BST** | `09-trees-and-bst.md` | 🔒 | Binary Tree, BST operations, DFS (in/pre/post order), BFS (level order), Diameter, LCA, Height, Morris traversal — *CS61B Week 6–7* |
| 10 | **Balanced Trees** | `10-balanced-trees.md` | 🔒 | B-Trees (2-3 and 2-3-4), Left-Leaning Red-Black Trees (LLRBs), AVL Trees, Rotations, Why balance matters — *CS61B Week 7, Lab 07* |
| 11 | **Heaps & Priority Queue** | `11-heaps-and-priority-queue.md` | 🔒 | Min/Max Heap, PriorityQueue API, Heapify, K-th largest/smallest, Top K elements, Merge K sorted lists, Median from stream — *CS61B Week 9* |
| 12 | **Tries** | `12-tries.md` | 🔒 | Trie insert/search/delete, Word Search, Auto-complete, Prefix matching, XOR Trie (Max XOR) — *CS61B Week 10* |
| 13 | **Binary Search** | `13-binary-search.md` | 🔒 | Search space concept, Binary search on answer, Rotated array, Finding boundaries, Peak element, Aggressive cows |
| 14 | **Sorting Algorithms** | `14-sorting-algorithms.md` | 🔒 | Insertion sort, Selection sort, Shell sort, Mergesort, Quicksort, Heapsort, Counting sort, Radix sort (MSD/LSD), Sorting lower bound proof — *CS61B Week 11* |
| 15 | **Graphs** | `15-graphs.md` | 🔒 | Adjacency list/matrix, BFS, DFS, Cycle detection, Topological Sort (Kahn's + DFS), Dijkstra's, A* (A-star), Bellman-Ford, MST (Prim's & Kruskal's), DAGs — *CS61B Week 12–13* |
| 16 | **Dynamic Programming** | `16-dynamic-programming.md` | 🔒 | Memoization vs Tabulation, 1D DP, 2D DP, DP on strings (LCS, Edit Distance), DP on subsequences, Knapsack family, DP on stocks, DP on grids |

---

## 🚀 Phase 3 — Advanced DSA
> *These separate senior engineers and competitive programmers from the pack.*
> *Includes CS61B advanced topics + beyond.*

| # | Book | File | Status | Topics |
|---|------|------|--------|--------|
| 17 | **Bit Manipulation** | `17-bit-manipulation.md` | 🔒 | Bit operators, XOR tricks, Power of 2, Subsets using bits, Brian Kernighan's algorithm, Bitmask DP intro |
| 18 | **Segment Tree & BIT** | `18-segment-tree-and-bit.md` | 🔒 | Range queries, Point update, Segment tree build/query/update, Lazy propagation, Binary Indexed Tree (Fenwick Tree) |
| 19 | **Advanced Graph Algorithms** | `19-advanced-graphs.md` | 🔒 | Floyd-Warshall, Bridges & Articulation Points (Tarjan's), SCC (Kosaraju's), Euler Path/Circuit, Network Flow (Ford-Fulkerson) |
| 20 | **Advanced Dynamic Programming** | `20-advanced-dp.md` | 🔒 | DP on Trees, Digit DP, Bitmask DP, DP on Intervals, Matrix Chain Multiplication, Palindrome partitioning |
| 21 | **Advanced String Algorithms** | `21-advanced-strings.md` | 🔒 | Suffix Array, Suffix Automaton, Aho-Corasick, Z-Algorithm, Manacher's Algorithm |

---

## 🧠 Phase 4 — CS Deep Dives (Widening Your Horizon)
> *Go beyond DSA into the foundations of CS. Inspired by CS61B's final weeks on software engineering, compression, and complexity theory.*

| # | Book | File | Status | Topics |
|---|------|------|--------|--------|
| 22 | **Mathematical Algorithms** | `22-math-algorithms.md` | 🔒 | Number Theory, GCD/LCM (Euclidean), Sieve of Eratosthenes, Modular Arithmetic, Fast Exponentiation, Combinatorics, Catalan numbers |
| 23 | **Complexity Theory** | `23-complexity-theory.md` | 🔒 | P vs NP, NP-completeness, Reductions, Decidability intro, Cook-Levin theorem concept — *CS61B Final Weeks* |
| 24 | **Compression Algorithms** | `24-compression-algorithms.md` | 🔒 | Huffman encoding, LZ77/LZ78, Run-length encoding, Burrows-Wheeler transform, How ZIP/GZIP work — *CS61B Final Weeks* |
| 25 | **Software Engineering & Design Patterns** | `25-software-engineering.md` | 🔒 | SOLID principles, Design patterns (Factory, Observer, Strategy, Decorator), Modularity, Testing (JUnit, TDD), Debugging techniques — *CS61B Software Engineering theme* |
| 26 | **Advanced Trees** | `26-advanced-trees.md` | 🔒 | Splay Tree, Treap, Skip List, B+ Tree (how databases index data) |
| 27 | **Concurrency & Parallel Algorithms** | `27-concurrency-algorithms.md` | 🔒 | Thread-safe data structures, Lock-free algorithms, ConcurrentHashMap internals, Parallel merge sort |
| 28 | **Computational Geometry** | `28-computational-geometry.md` | 🔒 | Convex Hull, Line intersection, Point in polygon, Closest pair of points |
| 29 | **Randomized Algorithms** | `29-randomized-algorithms.md` | 🔒 | Randomized QuickSort, Monte Carlo vs Las Vegas, Bloom Filters, Skip Lists, Treap |

---

## 📊 Roadmap Summary (Visual)

```
PHASE 1 — Foundations
└── 01. Arrays & Strings 🔄

PHASE 2 — Core DSA (Interview Ready) ← CS61B aligns here
├── 02. Java OOP & Design              [CS61B Wk 2-4]
├── 03. Asymptotics & Complexity       [CS61B Wk 5]
├── 04. Hashing & HashMaps             [CS61B Wk 8]
├── 05. Linked List                    [CS61B Wk 3]
├── 06. Stack, Queue & Deque           [CS61B Wk 3]
├── 07. Disjoint Sets (Union-Find)     [CS61B Lab 5]
├── 08. Recursion & Backtracking
├── 09. Trees & BST                    [CS61B Wk 6-7]
├── 10. Balanced Trees (LLRB, B-Tree)  [CS61B Wk 7]
├── 11. Heaps & Priority Queue         [CS61B Wk 9]
├── 12. Tries                          [CS61B Wk 10]
├── 13. Binary Search
├── 14. Sorting Algorithms             [CS61B Wk 11]
├── 15. Graphs (BFS, DFS, Dijkstra)    [CS61B Wk 12-13]
└── 16. Dynamic Programming
         ↓
    [INTERVIEW READY — Product Based Companies]

PHASE 3 — Advanced DSA
├── 17. Bit Manipulation
├── 18. Segment Tree & BIT
├── 19. Advanced Graphs
├── 20. Advanced DP
└── 21. Advanced String Algorithms
         ↓
    [COMPETITIVE PROGRAMMING / FAANG Senior Level]

PHASE 4 — CS Deep Dives  ← CS61B final weeks + beyond
├── 22. Mathematical Algorithms
├── 23. Complexity Theory              [CS61B Final]
├── 24. Compression Algorithms         [CS61B Final]
├── 25. Software Engineering & Design  [CS61B Theme]
├── 26. Advanced Trees
├── 27. Concurrency & Parallel Algo
├── 28. Computational Geometry
└── 29. Randomized Algorithms
         ↓
    [CS FUNDAMENTALS MASTERY]
```

---

## 🆚 Our Shelf vs. CS61B — What We Added

| Category | CS61B Covers | Our Shelf Adds |
|---|---|---|
| **Foundations** | Java OOP, Generics, Testing | Dedicated books on patterns + API mastery |
| **Complexity** | Big-O, Amortized analysis | Master theorem, Recursion tree — deeper |
| **Trees** | BST, B-Tree, LLRB, Heaps, Tries | AVL, Splay, Treap, Skip Lists (Book 26) |
| **Graphs** | BFS, DFS, Dijkstra, A*, MST, Topo Sort | Tarjan's, Kosaraju's, Network Flow (Book 19) |
| **Sorting** | All major sorts including Radix | Same — fully covered in Book 14 |
| **Hashing** | Chaining, Open addressing | DSU-based problems, advanced combos |
| **Disjoint Sets** | QuickUnion + Path compression | Full book with all graph applications |
| **Backtracking** | Not deeply covered | Full dedicated book (Book 08) |
| **DP** | Not in CS61B | 2 full dedicated books (Books 16 + 20) |
| **Bit Manipulation** | Not in CS61B | Full book (Book 17) |
| **Complexity Theory** | P vs NP, Reductions intro | Full conceptual book (Book 23) |
| **Compression** | Huffman, LZ, BWT | Full book with all major algorithms (Book 24) |
| **Software Engineering** | Design patterns, modularity, testing | Dedicated book with SOLID, TDD (Book 25) |
| **Geometry** | Not in CS61B | Computational Geometry (Book 28) |
| **Randomized Algos** | Not in CS61B | Bloom Filters, Treap, Monte Carlo (Book 29) |

> **Summary:** CS61B is an excellent university course. Our shelf covers everything CS61B does, plus adds interview-specific pattern training, DP, Bit Manipulation, Advanced Graphs, and Phase 4 CS deep dives that go well beyond a standard university curriculum.

---

## 🏢 Company-Wise Focus Guide

| Company Tier | Must Master | Good to Know |
|---|---|---|
| **Tier 3 (Startups)** | Books 01–09 | — |
| **Tier 2 (Zomato, Razorpay, CRED)** | Books 01–13 | Book 16 basics |
| **Tier 1 (Google, Amazon, Microsoft)** | Books 01–16 | Books 17–19 |
| **Quant / Trading Firms** | Books 01–16, 22 | Books 17–21 |
| **Competitive Programming (ICPC, CF)** | All Phases | Phase 4 |

---

## 📈 Estimated Timeline

| Phase | Books | Estimated Time (2–3 hrs/day) |
|---|---|---|
| Phase 1 | 1 book | 3–4 weeks |
| Phase 2 | 15 books | 6–9 months |
| Phase 3 | 5 books | 3–4 months |
| Phase 4 | 8 books | 4–6 months |

> ⚡ **Pro tip:** Consistency beats intensity. 2–3 focused hours daily beats a 10-hour weekend session.

---

## 📝 Your Milestones

| Date | Milestone |
|------|-----------|
| Sep 2026 | 📖 Started Book 01 — Arrays & Strings |
| | |
| | |
| | |

---

*"The journey of a thousand miles begins with a single step." — You've taken it. Keep going.* 🚀


---

## 🏫 CS61B Inspired Practice — Homeworks & Projects

> These are adapted from UC Berkeley CS61B's homework and project assignments across the last 3 semesters (FA23, SP24, FA24). They are real-world coding challenges that build intuition you cannot get from LeetCode alone. Each one is mapped to the relevant book in this series.
>
> **How to use:** After completing the relevant book, attempt the corresponding project/homework here. These are open-source — specs and starter code are publicly available at `fa24.datastructur.es`.

---

### 📝 Homeworks

| HW | Name | DSA Concept | Relevant Book | Semesters Offered | Spec Link |
|---|---|---|---|---|---|
| HW0A | Java Syntax Basics | Variables, Loops, Conditionals, Functions | Book 01 (Arrays & Strings) | FA23, SP24, FA24 | [fa24](https://fa24.datastructur.es/homeworks/hw0/hw0a/) |
| HW0B | Java Data Structures Intro | Arrays, Lists, Maps, Conditionals in Java | Book 01, Book 04 | FA23, SP24, FA24 | [fa24](https://fa24.datastructur.es/homeworks/hw0/hw0b/) |
| HW1 | Arithmetic Interpreter / Java Practice | OOP, Interfaces, Inheritance | Book 02 (Java OOP) | FA23, SP24 | [sp24](https://sp24.datastructur.es/homeworks/hw1/) |
| HW2 | Percolation | Disjoint Sets (Union-Find) | Book 07 (Disjoint Sets) | FA23, SP24, FA24 | [fa24](https://fa24.datastructur.es/homeworks/hw2/) |
| HW3 | Hashing | Hash Tables, Custom hashCode() | Book 04 (Hashing) | FA23, SP24 | [sp24](https://sp24.datastructur.es/homeworks/hw3/) |
| HW4 | Midterm Review | Mixed — Arrays, Lists, Trees, Sorting | Books 01–14 | FA23, SP24, FA24 | [fa24](https://fa24.datastructur.es/homeworks/hw4/) |

---

### 🚀 Projects

#### Project 0: 2048 — The Java Warmup
- **Semesters:** FA23, SP24, FA24 (consistent across all recent semesters)
- **Spec:** [fa24.datastructur.es/projects/proj0](https://fa24.datastructur.es/projects/proj0/)
- **What you build:** The core game logic for 2048 — tile merging, board state management, win/lose detection.
- **DSA Concepts:** 2D Arrays, Matrix traversal, State machines
- **Relevant Books:** Book 01 — Arrays & Strings (Matrix patterns chapter)
- **Why it matters:** Your first large Java project. You learn to work with a real codebase, navigate existing code, and implement logic without writing everything from scratch. Teaches the real-world skill of reading and extending code.
- **Difficulty:** 🟢 Easy (great starter project)
- **Key Skills Practiced:**
  - 2D array indexing and traversal
  - Conditional logic for tile merging rules
  - Iterating in all 4 directions (up, down, left, right)
  - Testing with JUnit

---

#### Project 1A: LinkedListDeque61B — Build a Deque from Scratch
- **Semesters:** FA23, SP24, FA24
- **Spec:** [fa24.datastructur.es/projects/proj1a](https://fa24.datastructur.es/projects/proj1a/)
- **What you build:** A double-ended queue (Deque) using a circular doubly linked list.
- **DSA Concepts:** Linked List, Doubly Linked List, Sentinel nodes
- **Relevant Books:** Book 05 — Linked List
- **Why it matters:** Forces you to deeply understand pointer manipulation. The circular sentinel node trick is a clean engineering pattern that eliminates edge cases.
- **Difficulty:** 🟡 Medium
- **Key Skills Practiced:**
  - Node class design with prev/next pointers
  - Circular doubly linked list with sentinel head
  - addFirst, addLast, removeFirst, removeLast in O(1)
  - Writing your own JUnit tests (required by the spec)

---

#### Project 1B: ArrayDeque61B — Circular Array Deque
- **Semesters:** FA23, SP24, FA24
- **Spec:** [fa24.datastructur.es/projects/proj1b](https://fa24.datastructur.es/projects/proj1b/)
- **What you build:** A double-ended queue using a resizing circular array (ring buffer).
- **DSA Concepts:** Circular arrays, Dynamic resizing (amortized O(1)), Array indexing tricks
- **Relevant Books:** Book 01 — Arrays & Strings, Book 03 — Asymptotics
- **Why it matters:** The circular array trick (using modulo for wrap-around) is one of the most elegant array techniques. Resizing teaches amortized analysis in a hands-on way.
- **Difficulty:** 🟡 Medium
- **Key Skills Practiced:**
  - Circular array with front/back pointers
  - Modular arithmetic for wrap-around: `(index + 1) % capacity`
  - Dynamic resizing: double when full, halve when 25% full
  - Amortized O(1) operations

---

#### Project 1C: Deque Variants & MaxArrayDeque
- **Semesters:** FA24 (introduced as a separate part)
- **Spec:** [fa24.datastructur.es/projects/proj1c](https://fa24.datastructur.es/projects/proj1c/)
- **What you build:** MaxArrayDeque (a Deque that tracks the maximum using a Comparator), plus Guitar Hero (using Deques to simulate Karplus-Strong string synthesis).
- **DSA Concepts:** Generics, Comparator<T>, Interface design
- **Relevant Books:** Book 02 — Java OOP, Book 06 — Stack & Queue
- **Why it matters:** Teaches you to write generic, reusable data structures. The Guitar Hero component shows that data structures are everywhere — even in audio processing.
- **Difficulty:** 🟡 Medium

---

#### Project 2A: Ngordnet (Part 1) — WordNet & TimeSeries
- **Semesters:** FA23, SP24, FA24
- **Spec:** [fa24.datastructur.es/projects/proj2a](https://fa24.datastructur.es/projects/proj2a/)
- **What you build:** A web app back-end for exploring historical word frequency data. You implement a `TimeSeries` (extending `TreeMap<Integer, Double>`) and a `NGramMap` that loads and queries massive word-frequency datasets.
- **DSA Concepts:** TreeMap, HashMap, File I/O, Large data handling
- **Relevant Books:** Book 04 — Hashing, Book 09 — Trees & BST
- **Why it matters:** First exposure to working with real, large datasets. You learn that data structure choice directly impacts performance — using the wrong structure here means your queries take seconds instead of milliseconds.
- **Difficulty:** 🟡 Medium
- **Key Skills Practiced:**
  - TreeMap for sorted time-series data
  - HashMap for O(1) word lookups
  - Parsing large CSV/text files efficiently
  - API design (implementing a given interface)

---

#### Project 2B: Ngordnet (Part 2) — Hyponyms & Graph Traversal
- **Semesters:** FA23, SP24, FA24
- **Spec:** [fa24.datastructur.es/projects/proj2b](https://fa24.datastructur.es/projects/proj2b/)
- **What you build:** Extend the Ngordnet back-end to support finding "hyponyms" — words that are more specific versions of a given word (e.g., "dog" is a hyponym of "animal"). Requires building a graph from WordNet data and doing BFS/DFS to find all descendants.
- **DSA Concepts:** Graph (DAG), BFS/DFS traversal, Set operations
- **Relevant Books:** Book 15 — Graphs
- **Why it matters:** Real-world graph problem. WordNet is an actual linguistic database used in NLP. You're essentially building a simplified search engine feature.
- **Difficulty:** 🔴 Hard
- **Key Skills Practiced:**
  - Building an adjacency list from raw text data
  - BFS/DFS to find all reachable nodes (hyponyms)
  - Set intersection for multi-word queries
  - Combining graph + hash map + tree map in one solution

---

#### Project 3: Build Your Own World (BYOW)
- **Semesters:** FA23, SP24, FA24 (the crown jewel of CS61B — every semester)
- **Spec:** [fa24.datastructur.es/projects/proj3](https://fa24.datastructur.es/projects/proj3/)
- **What you build:** A complete 2D game engine that procedurally generates random tile-based worlds. The world must be different every time (based on a seed) but reproducible with the same seed. Includes a player character that can move around.
- **DSA Concepts:** 2D Arrays, Pseudorandom generation, Graph connectivity, Software design, State persistence
- **Relevant Books:** Books 01, 07, 15, 25
- **Why it matters:** This is a 2000–3000 line project. You design the entire architecture yourself. There is no skeleton — you start from near-scratch. This is the closest thing to a real software engineering project in an undergraduate DS course.
- **Difficulty:** 🔴 Hard (the hardest CS61B project)
- **Key Skills Practiced:**
  - Procedural world generation (BSP trees or random room placement)
  - Ensuring connectivity using graph algorithms
  - Saving/loading world state (serialization)
  - Software design — separating concerns, modular code
  - Collaborative coding (done in pairs)

---

### 📐 Labs (Quick Reference)

CS61B labs are shorter, 1-2 hour guided exercises done weekly. Key ones to replicate independently:

| Lab | Name | Concept | Relevant Book |
|---|---|---|---|
| Lab 01 | Java Setup & Hello World | Environment, First Java program | Pre-Book 01 |
| Lab 02 | Debugging Part 1 | IntelliJ debugger, Breakpoints | All books |
| Lab 03 | Debugging Part 2 | Advanced debugging techniques | All books |
| Lab 04 | Git & Version Control | Git workflow | Pre-Book 01 |
| Lab 05 | Disjoint Sets | Union-Find implementation | Book 07 |
| Lab 06 | BSTMap | BST-backed Map implementation | Book 09 |
| Lab 07 | LLRBs | Left-Leaning Red-Black Tree | Book 10 |
| Lab 08 | Hashmaps | HashMap with separate chaining | Book 04 |
| Lab 09 | Conway's Game of Life | 2D Arrays, Simulation | Book 01 |
| Lab 10 | Tetris | Deque, Game state, OOP | Books 02, 06 |

---


### 🔄 FA25 & FA26 Curriculum Update Note
*Note: Starting in Fall 2025, CS61B slightly restructured their project numbering, but the **core assignments remain identical**.*
* **Percolation** (formerly HW2) is now **Mini-Project 3**.
* **LinkedListDeque** & **ArrayDeque** (formerly Proj1A/1B) are now **Mini-Projects 1 & 2**.
* **Ngordnet** (formerly Proj2) is now **Project 4** (NGrams & WordNet).
* Homeworks have been expanded into smaller weekly assignments (HW01-HW12) covering the exact same foundational DSA concepts (BSTs, HashMaps, IntLists, etc.). 
* You can confidently use the FA24/SP24 specs as they represent the finalized form of the "modern" CS61B era.

### 🗓️ Semester Cross-Reference

| Assignment | FA23 | SP24 | FA24 |
|---|---|---|---|
| HW0 (Java Basics) | ✅ | ✅ | ✅ |
| HW1 (OOP Practice) | ✅ | ✅ | — |
| HW2 (Percolation) | ✅ | ✅ | ✅ |
| HW3 (Hashing) | ✅ | ✅ | — |
| HW4 (Midterm Review) | ✅ | ✅ | ✅ |
| Project 0 (2048) | ✅ | ✅ | ✅ |
| Project 1A (LinkedListDeque) | ✅ | ✅ | ✅ |
| Project 1B (ArrayDeque) | ✅ | ✅ | ✅ |
| Project 1C (MaxDeque + Guitar) | — | — | ✅ |
| Project 2A (Ngordnet Part 1) | ✅ | ✅ | ✅ |
| Project 2B (Ngordnet Part 2) | ✅ | ✅ | ✅ |
| Project 3 (BYOW) | ✅ | ✅ | ✅ |