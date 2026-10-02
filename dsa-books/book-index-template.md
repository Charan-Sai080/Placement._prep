# DSA Book Series — Index Template & Style Guide

> **Purpose:** This file is a reference for Senpai (AI) to maintain consistent structure, formatting, and coverage across all DSA books in this series. Every new book must follow this template exactly.

---

## Series Naming Convention

| Book Number | Data Structure | File Name |
|---|---|---|
| 01 | Arrays & Strings | `01-arrays-and-strings.md` |
| 02 | Hashing & HashMaps | `02-hashing-and-hashmaps.md` |
| 03 | Linked List | `03-linked-list.md` |
| 04 | Stack & Queue | `04-stack-and-queue.md` |
| 05 | Recursion & Backtracking | `05-recursion-and-backtracking.md` |
| 06 | Binary Search | `06-binary-search.md` |
| 07 | Trees & BST | `07-trees-and-bst.md` |
| 08 | Heaps & Priority Queue | `08-heaps-and-priority-queue.md` |
| 09 | Graphs | `09-graphs.md` |
| 10 | Dynamic Programming | `10-dynamic-programming.md` |
| 11 | Trie | `11-trie.md` |
| 12 | Segment Tree & BIT | `12-segment-tree-and-bit.md` |

---

## Standard Book Structure (Must Follow in Every Book)

Each book must contain the following sections in this exact order:

```
# Book Title

## Preface
## Chapter 1: Concept & Internal Working
## Chapter 2: Java API — Complete Reference
   ### 2.x Each Class/Interface
       - Class overview
       - Constructor signatures
       - Method table (signature + description)
       - Code snippet for each method group
## Chapter 3: Patterns
   ### Pattern N: [Pattern Name]
       - What is it?
       - When to use it? (trigger words / problem types)
       - Visual diagram (ASCII)
       - Template code
       - Worked example (problem → thought process → code → dry run)
       - Common mistakes
       - Practice problems list
## Chapter 4: Mixed Patterns & Advanced Combos
## Chapter 5: Interview Cheat Sheet
## Appendix: Quick Reference Card
```

---

## Formatting Rules

### Code Blocks
- Always use fenced code blocks with language identifier for syntax highlighting
- Java code → triple-backtick java
- Pseudocode → triple-backtick text  
- Terminal/Output → triple-backtick bash
- ASCII diagrams → triple-backtick text

### API Method Signature Format
Always present method signatures in this format:
```
returnType methodName(paramType paramName, ...)
```
Never omit throws clauses if relevant. Include modifiers (static, final).

### Pattern Section Format
Every pattern must follow this exact sub-structure:
1. **Concept** — What and Why
2. **Trigger Words** — How to recognize this pattern in a problem statement
3. **Visual** — ASCII art showing the mechanism
4. **Template** — Reusable code skeleton
5. **Worked Example** — Full problem to solution
6. **Dry Run** — Step-by-step trace table
7. **Time & Space Complexity**
8. **Common Mistakes**
9. **Practice Problems** — with difficulty tags

### Complexity Notation
Always present as:
- Time: O(...)
- Space: O(...)
With explanation of why.

### Difficulty Tags
- 🟢 Easy
- 🟡 Medium  
- 🔴 Hard

---

## Coverage Checklist (Must complete before submitting a book)

- [ ] All internal memory concepts explained
- [ ] Every public method of every relevant class covered with signature
- [ ] All major patterns covered (refer to pattern list per DS)
- [ ] Every pattern has a worked example with dry run
- [ ] Interview cheat sheet at end
- [ ] Quick reference card at end
- [ ] Code is syntactically correct Java
- [ ] No pattern is left without practice problems

---

## Pattern Master List Per Data Structure

### Arrays & Strings (Book 01)
- Two Pointers (Opposite direction)
- Two Pointers (Same direction / Fast-Slow)
- Sliding Window (Fixed size)
- Sliding Window (Variable size)
- Prefix Sum
- Kadane's Algorithm (Max Subarray)
- Dutch National Flag (3-way partition)
- Cyclic Sort
- Binary Search on Answer
- Matrix Traversal (Spiral, Diagonal, Layer by Layer)
- String Two Pointers / Palindrome
- Frequency Map / Anagram
- KMP Algorithm
- Rolling Hash
- Sorting-based tricks

### Hashing (Book 02)
- Frequency counting
- Two Sum pattern
- Subarray with given sum (using prefix + hash)
- Group Anagrams
- Longest consecutive sequence

### Linked List (Book 03)
- Fast and Slow pointers
- Reversal
- Merge
- Cycle detection
- In-place modification

### Stack & Queue (Book 04)
- Monotonic Stack
- Next Greater Element
- Valid Parentheses family
- BFS with Queue
- Sliding Window Maximum

---

## Author Notes

- **Student:** Learning Java DSA with goal of cracking product-based company interviews
- **Current Progress:** Java theory complete up to Collections
- **Language:** Java
- **Style:** Teach intuition first, code second. Always explain WHY before HOW.
- **Tone:** Like a senior engineer mentoring a junior — direct, thorough, encouraging
