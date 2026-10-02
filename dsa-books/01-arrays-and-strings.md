# Book 01 — Arrays & Strings (Java)
### *Senpai DSA Series*

> **"You cannot build a skyscraper without understanding the ground it stands on."**
> Arrays are that ground for every data structure and algorithm you will ever learn.

---

## Preface

### Who This Book Is For

This book is written for developers who:
- Know Java syntax and OOP fundamentals
- Have gone through Java Collections theory
- Want to crack product-based company (FAANG/MAANG/Unicorn) interviews
- Need a single, exhaustive reference that goes from *"what is an array"* all the way to *"how do I solve Leetcode Hard problems systematically"*

You do not need prior DSA knowledge. You DO need patience — this book is long because the subject deserves it.

### How to Use This Book

1. **Read Chapter 1 and 2 once, carefully.** Understand the internals before touching code.
2. **Chapter 3 and 4 are your Java API reference.** Bookmark these. Come back every time you forget a method.
3. **Chapters 5 and 6 are the heart of the book.** For every pattern:
   - Read the concept.
   - Draw the ASCII diagram on paper.
   - Write the template code from memory.
   - Solve the worked example yourself, then compare.
   - Do at least 3 practice problems before moving on.
4. **Chapter 7** shows you how patterns combine — this is where interview-level thinking lives.
5. **Chapter 8 and the Appendix** are your revision sheets. Print them before an interview.

### A Note on Learning Style

This book teaches **intuition first, code second**. Every pattern starts with a human-level explanation of *why* it works before showing a single line of code. If you cannot explain a pattern in plain English, you cannot apply it under pressure. That is the standard we hold ourselves to here.

---

## Chapter 1: Arrays — Concept & Internal Working

### 1.1 What Is an Array?

An array is the simplest and most fundamental data structure. At its core, it is a **contiguous block of memory** where elements of the **same type** are stored one after another.

The word *contiguous* is crucial. It means there are no gaps. Element 0 lives right next to element 1, which lives right next to element 2, and so on. This physical layout in RAM is what gives arrays their superpower: **O(1) random access**.

#### How Index-Based Access Works (The Math)

Given:
- **Base address** of the array = `B` (the memory address of element at index 0)
- **Size of each element** = `S` bytes (e.g., `int` = 4 bytes)
- **Index** you want = `i`

The memory address of element at index `i` is:

```text
address(i) = B + (i × S)
```

Example: If `int[] arr` starts at memory address `1000`, and `int` is 4 bytes:
- `arr[0]` → address `1000`
- `arr[1]` → address `1004`
- `arr[2]` → address `1008`
- `arr[5]` → address `1020`

The CPU computes this in one arithmetic operation. It does not scan from index 0. This is why array access is O(1) regardless of array size.

```text
Memory Layout for int[] arr = {10, 20, 30, 40, 50}:

Index:    [  0  ] [  1  ] [  2  ] [  3  ] [  4  ]
Value:    [  10 ] [  20 ] [  30 ] [  40 ] [  50 ]
Address:  [ 1000] [ 1004] [ 1008] [ 1012] [ 1016]
           ↑
           Base address B = 1000
           Element size S = 4 bytes (int)
```

### 1.2 Stack vs Heap for Arrays in Java

In Java, memory is divided into two main regions:

| Region | What lives here | Lifetime |
|--------|-----------------|----------|
| **Stack** | Local variables, method call frames, primitive values | Until method returns |
| **Heap** | All objects (including arrays) | Until garbage collected |

**Arrays in Java are always objects, and objects always live on the heap.**

When you write:
```java
int[] arr = new int[5];
```

What actually happens:
1. **On the heap:** A block of memory is allocated for 5 integers (20 bytes) + array metadata (length field, type info, etc.)
2. **On the stack:** A local variable `arr` is created. It holds a **reference (pointer)** to the heap location — not the data itself.

```text
Stack                    Heap
┌─────────┐             ┌──────────────────────────────┐
│  arr    │──reference─▶│  [10] [20] [30] [40] [50]    │
│ (ref)   │             │  length = 5                  │
└─────────┘             └──────────────────────────────┘
```

**Important consequence:** When you pass an array to a method, you are passing the reference, not a copy. Modifications inside the method affect the original array.

```java
public static void doubleAll(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        arr[i] *= 2;  // modifies the original array on the heap
    }
}

public static void main(String[] args) {
    int[] nums = {1, 2, 3};
    doubleAll(nums);
    System.out.println(Arrays.toString(nums)); // [2, 4, 6] — original is changed
}
```

### 1.3 Primitive Arrays vs Object Arrays

Java has two kinds of arrays:

#### Primitive Arrays
```java
int[] intArr = new int[3];       // stores actual int values (4 bytes each)
double[] dblArr = new double[3]; // stores actual double values (8 bytes each)
char[] charArr = new char[3];    // stores actual char values (2 bytes each)
```
Memory layout is truly contiguous with actual values stored directly.

#### Object Arrays
```java
String[] strArr = new String[3];    // stores references (pointers) to String objects
Integer[] intObjArr = new Integer[3]; // stores references to Integer objects
```

Object arrays store **references** (typically 4 or 8 bytes each), not the objects themselves. The actual objects are scattered elsewhere on the heap.

```text
Object Array (String[] strArr):

strArr → [ref₀] [ref₁] [ref₂]
           │       │       │
           ▼       ▼       ▼
        "hello" "world"  null
        (heap)  (heap)
```

**Why this matters:**
- Primitive arrays: cache-friendly (data is contiguous), no boxing overhead
- Object arrays: elements can be null, polymorphism works, but pointer chasing is slower

### 1.4 Time Complexity of Array Operations

| Operation | Time Complexity | Explanation |
|-----------|-----------------|-------------|
| **Access** by index | O(1) | Direct address calculation: B + i×S |
| **Search** (unsorted) | O(n) | Must check each element in worst case |
| **Search** (sorted) | O(log n) | Binary search possible |
| **Insert** at end | O(1) amortized* | Just place element (if space exists) |
| **Insert** at middle/beginning | O(n) | Must shift all subsequent elements right |
| **Delete** at end | O(1) | Just decrement length (logically) |
| **Delete** at middle/beginning | O(n) | Must shift all subsequent elements left |
| **Traverse** | O(n) | Must visit every element |

*For plain Java arrays (fixed size), insert at end is O(1) only if pre-allocated space exists. For ArrayList, it's O(1) amortized because it doubles capacity when full.

#### Insertion Shift Visualization

Inserting `99` at index 2 of `[10, 20, 30, 40, 50]`:

```text
Step 1 — Original:
Index: [ 0  ] [ 1  ] [ 2  ] [ 3  ] [ 4  ]
Value: [ 10 ] [ 20 ] [ 30 ] [ 40 ] [ 50 ]

Step 2 — Shift elements right from index 2 onwards:
Index: [ 0  ] [ 1  ] [ 2  ] [ 3  ] [ 4  ] [ 5  ]
Value: [ 10 ] [ 20 ] [    ] [ 30 ] [ 40 ] [ 50 ]

Step 3 — Place 99 at index 2:
Index: [ 0  ] [ 1  ] [ 2  ] [ 3  ] [ 4  ] [ 5  ]
Value: [ 10 ] [ 20 ] [ 99 ] [ 30 ] [ 40 ] [ 50 ]
```

Elements at index 2, 3, 4 had to shift → O(n) work.

### 1.5 Multi-Dimensional Arrays — Row-Major Order

In Java, a 2D array is actually an **array of arrays** (array of references to row arrays).

```java
int[][] matrix = new int[3][4]; // 3 rows, 4 columns
```

Memory layout:

```text
matrix (reference) → [row0_ref] [row1_ref] [row2_ref]
                          │           │           │
                          ▼           ▼           ▼
                     [0][0][0][0]  [0][0][0][0]  [0][0][0][0]
                      col0123       col0123       col0123
```

Each row is a separate heap object. This is called **row-major order** in Java (rows are stored independently). Rows can even be of different lengths (jagged arrays):

```java
int[][] jagged = new int[3][];
jagged[0] = new int[2];  // row 0 has 2 elements
jagged[1] = new int[4];  // row 1 has 4 elements
jagged[2] = new int[1];  // row 2 has 1 element
```

#### Accessing 2D Array Elements

```java
int[][] grid = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// Access element at row 1, col 2:
int val = grid[1][2]; // returns 6

// Traverse all elements:
for (int row = 0; row < grid.length; row++) {
    for (int col = 0; col < grid[row].length; col++) {
        System.out.print(grid[row][col] + " ");
    }
    System.out.println();
}
// Output:
// 1 2 3
// 4 5 6
// 7 8 9
```

#### 3D Arrays

```java
int[][][] cube = new int[2][3][4]; // 2 layers, 3 rows, 4 columns
cube[1][2][3] = 42; // layer 1, row 2, col 3
```

### 1.6 Limitations of Plain Arrays → Why ArrayList Was Invented

Plain Java arrays have a fatal flaw: **fixed size**. You must declare the size at creation time, and it cannot change.

```java
int[] arr = new int[5]; // forever size 5
```

**Problems this causes:**
1. **Under-allocation:** If you need 6 elements, you're stuck. You must create a new, larger array and copy everything. This is tedious manual work.
2. **Over-allocation:** Guessing too large wastes memory.
3. **No convenient methods:** No `add()`, `remove()`, `contains()`, etc. You must write all manipulation logic yourself.
4. **Insertion/deletion is painful:** You must manually shift elements.

**Solution:** `java.util.ArrayList` — a **dynamic array** backed by a plain array internally, but it handles resizing automatically.

When ArrayList fills up, it creates a new array (typically 1.5× or 2× the old size), copies all elements, and discards the old array. This is the **amortized O(1)** insert at end behavior.

```text
ArrayList growth example (initial capacity = 4):

[A][B][C][D]     ← full at size 4
      ↓ add E
[A][B][C][D][E][ ][ ][ ]  ← new array of size 8, E added
```

---

## Chapter 2: Strings — Concept & Internal Working

### 2.1 String Immutability — What It Means and Why Java Chose It

In Java, `String` objects are **immutable** — once created, their content cannot be changed.

```java
String s = "hello";
s.toUpperCase(); // Does NOT modify s
System.out.println(s); // still "hello"

// toUpperCase() returns a NEW String object
String upper = s.toUpperCase();
System.out.println(upper); // "HELLO"
```

Every String method that appears to "modify" the string actually creates and returns a **new String** with the modification applied.

**Why did Java choose immutability?**

1. **Security:** Strings are used as arguments to class loaders, network connections, file I/O. If they could be mutated, malicious code could change a filename or URL after security checks are done.

2. **String Pool (caching):** The JVM maintains a pool of String literals. Immutability makes it safe to share String objects — if two variables point to the same "hello", neither can change it to affect the other.

3. **Thread safety:** Immutable objects can be freely shared across threads with no synchronization. No race conditions possible.

4. **HashCode caching:** Since the content never changes, a String's hashCode can be computed once and cached. `HashMap` lookups on Strings are thus very fast.

5. **Consistency:** A String passed to a method stays the same after the method returns (no surprise mutations).

### 2.2 String Pool (String Constant Pool)

The **String Constant Pool** (SCP) is a special region in the Java heap (since Java 7; was in PermGen before that) where Java caches String literals.

**Rule:** When you write a String literal in source code, Java checks the pool:
- If the same string already exists in the pool → reuse the existing object
- If not → create a new String in the pool, then return it

```java
String a = "hello";   // creates "hello" in pool, a points to it
String b = "hello";   // "hello" already in pool, b points to SAME object
String c = new String("hello"); // FORCES creation of new object on heap (outside pool)

System.out.println(a == b);      // true  — same object in pool
System.out.println(a == c);      // false — c is a different heap object
System.out.println(a.equals(c)); // true  — same content
```

```text
String Pool (part of Heap):           Regular Heap:
┌─────────────────┐                  ┌─────────────────┐
│  "hello"  ◀──── │ ── a, b point here  │  "hello" ◀──── c │
└─────────────────┘                  └─────────────────┘
```

**`String.intern()`:** Forces a heap string into the pool (or returns existing pool reference):

```java
String x = new String("world"); // on heap, not in pool
String y = x.intern();           // puts "world" in pool (if not there), returns pool ref
String z = "world";              // from pool
System.out.println(y == z); // true — both from pool
```

### 2.3 == vs .equals() — Why This Matters

`==` compares **references** (memory addresses).
`.equals()` compares **contents** (character by character).

```java
String s1 = "Java";
String s2 = "Java";
String s3 = new String("Java");

s1 == s2       // true  (both point to same pool object)
s1 == s3       // false (s3 is a different heap object)
s1.equals(s3)  // true  (same content: "Java")
```

**Golden Rule:** **Always use `.equals()` to compare String content.** Never use `==` for strings in production code.

### 2.4 StringBuilder vs StringBuffer vs String

| Feature | String | StringBuilder | StringBuffer |
|---------|--------|---------------|--------------|
| **Mutability** | Immutable | Mutable | Mutable |
| **Thread Safety** | Yes (immutable) | No | Yes (synchronized) |
| **Performance** | Slow for concatenation in loops | Fast | Slower than StringBuilder |
| **Use case** | Constants, keys, single values | String building in single thread | String building in multi-thread |

**Why String concatenation in a loop is slow:**

```java
// BAD — creates a new String object on every iteration
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i; // creates 1000 intermediate String objects!
}

// GOOD — StringBuilder mutates in place
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i); // no new objects, just internal char array grows
}
String result = sb.toString();
```

The first loop is O(n²) in time because each `+=` copies the growing string. The second is O(n).

### 2.5 char[] vs String — Internal Representation

Internally, Java's `String` stores characters in a `char[]` (or `byte[]` since Java 9's Compact Strings optimization — Latin-1 strings use 1 byte per char instead of 2).

```java
String s = "hello";
char[] chars = s.toCharArray(); // get the underlying chars
// chars = ['h', 'e', 'l', 'l', 'o']
```

**Why use `char[]` instead of `String` for passwords?**
- `String` is immutable: the password content stays in memory (in the String Pool even) until GC collects it — a security risk.
- `char[]` can be explicitly zeroed after use: `Arrays.fill(password, '\0');`

### 2.6 Unicode and char in Java (UTF-16)

Java's `char` type is a **16-bit unsigned integer** representing a Unicode code point in the range U+0000 to U+FFFF (Basic Multilingual Plane).

Java Strings use **UTF-16** encoding internally. Characters outside the BMP (emoji, rare CJK characters) require two `char` values (a "surrogate pair").

```java
char c = 'A';           // Unicode U+0041, value = 65
char heart = '\u2665';  // Unicode heart symbol ♥
int code = (int) c;     // 65
char fromCode = (char) 65; // 'A'

// Be careful with emoji — they need two chars (surrogate pair)
String emoji = "😀";
System.out.println(emoji.length()); // 2 (not 1!)
System.out.println(emoji.codePointCount(0, emoji.length())); // 1 (actual characters)
```

**Common char operations in DSA:**

```java
// Check if lowercase letter
char ch = 's';
boolean isLower = (ch >= 'a' && ch <= 'z'); // true

// Convert to index (a=0, b=1, ..., z=25)
int idx = ch - 'a'; // 18

// Convert between cases
char upper = Character.toUpperCase(ch); // 'S'
char lower = Character.toLowerCase('S'); // 's'

// Check character type
Character.isLetter(ch);    // true
Character.isDigit('5');    // true
Character.isLetterOrDigit(ch); // true
Character.isAlphabetic(ch); // true
```

---

## Chapter 3: Java API — Arrays

### 3.1 Primitive Arrays — Complete Operations Guide

#### Declaration

```java
// Declare without initializing (reference is null)
int[] arr;
double[] prices;
String[] names;

// Multi-dimensional declaration
int[][] matrix;
int[][][] cube;
```

#### Initialization

```java
// Method 1: Allocate with default values (0 for int, false for boolean, null for objects)
int[] arr = new int[5]; // [0, 0, 0, 0, 0]

// Method 2: Array initializer literal
int[] arr = {10, 20, 30, 40, 50};

// Method 3: new with initializer
int[] arr = new int[]{10, 20, 30};

// 2D array:
int[][] matrix = new int[3][4]; // 3 rows, 4 columns, all zeros
int[][] matrix2 = {{1,2,3}, {4,5,6}, {7,8,9}}; // literal init

// Jagged 2D array:
int[][] jagged = new int[3][];
jagged[0] = new int[2];
jagged[1] = new int[5];
jagged[2] = new int[3];
```

#### The `.length` Property

`.length` is a **field** (not a method — no parentheses) that holds the number of elements.

```java
int[] arr = {10, 20, 30, 40, 50};
System.out.println(arr.length); // 5

int[][] matrix = new int[3][4];
System.out.println(matrix.length);    // 3 (number of rows)
System.out.println(matrix[0].length); // 4 (columns in row 0)
```

#### Accessing Elements

```java
int[] arr = {10, 20, 30, 40, 50};

// Read:
int first = arr[0];  // 10
int last  = arr[arr.length - 1]; // 50

// Write:
arr[2] = 99;
// arr is now {10, 20, 99, 40, 50}

// ArrayIndexOutOfBoundsException if out of range:
// int x = arr[10]; // throws exception — valid indices are 0 to 4
```

#### Iterating Over Arrays

```java
int[] arr = {10, 20, 30, 40, 50};

// Method 1: Classic for loop (when index is needed)
for (int i = 0; i < arr.length; i++) {
    System.out.println("Index " + i + ": " + arr[i]);
}

// Method 2: Enhanced for-each (when index is NOT needed)
for (int val : arr) {
    System.out.println(val);
}

// Method 3: while loop
int i = 0;
while (i < arr.length) {
    System.out.println(arr[i]);
    i++;
}

// Method 4: Java 8 streams
Arrays.stream(arr).forEach(System.out::println);
```

#### Iterating 2D Arrays

```java
int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};

// Row by row:
for (int row = 0; row < matrix.length; row++) {
    for (int col = 0; col < matrix[row].length; col++) {
        System.out.print(matrix[row][col] + " ");
    }
    System.out.println();
}

// Enhanced for-each on 2D:
for (int[] row : matrix) {
    for (int val : row) {
        System.out.print(val + " ");
    }
    System.out.println();
}
```

#### Cloning Arrays

```java
int[] original = {1, 2, 3, 4, 5};

// Shallow clone — creates a new array with same elements
int[] copy = original.clone();
copy[0] = 999;

System.out.println(original[0]); // 1 — original unaffected (int is primitive)
System.out.println(copy[0]);     // 999

// For 2D arrays, clone() is SHALLOW — inner arrays are shared!
int[][] matrix = {{1,2},{3,4}};
int[][] matClone = matrix.clone();
matClone[0][0] = 999;
System.out.println(matrix[0][0]); // 999 — original IS affected!
// To deep clone 2D: clone each row separately
int[][] deepClone = new int[matrix.length][];
for (int i = 0; i < matrix.length; i++) {
    deepClone[i] = matrix[i].clone();
}
```

#### System.arraycopy()

```java
// Signature:
// static void System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)

int[] src  = {1, 2, 3, 4, 5};
int[] dest = new int[5];

// Copy 3 elements starting at src[1] into dest starting at dest[0]
System.arraycopy(src, 1, dest, 0, 3);
// dest = {2, 3, 4, 0, 0}

System.out.println(Arrays.toString(dest)); // [2, 3, 4, 0, 0]

// Can also copy within the same array (handles overlap correctly):
int[] arr = {1, 2, 3, 4, 5};
System.arraycopy(arr, 0, arr, 1, 4); // shift right by 1
// arr = {1, 1, 2, 3, 4}
```

---

### 3.2 `java.util.Arrays` Class — Complete API

`Arrays` is a utility class with only static methods. You never instantiate it. Import: `import java.util.Arrays;`

---

#### `static void sort(int[] a)`

**Signature:** `static void sort(int[] a)`  
**Description:** Sorts the entire array in ascending order. Uses **Dual-Pivot Quicksort** for primitive arrays (O(n log n) average, O(n²) worst case). Modifies the array in-place.

```java
int[] arr = {5, 2, 8, 1, 9, 3};
Arrays.sort(arr);
System.out.println(Arrays.toString(arr)); // [1, 2, 3, 5, 8, 9]
```

---

#### `static void sort(int[] a, int fromIndex, int toIndex)`

**Signature:** `static void sort(int[] a, int fromIndex, int toIndex)`  
**Description:** Sorts only the subarray from `fromIndex` (inclusive) to `toIndex` (exclusive).

```java
int[] arr = {5, 2, 8, 1, 9, 3};
Arrays.sort(arr, 1, 4); // sort indices 1,2,3 only
// Before: [5, 2, 8, 1, 9, 3]
// After:  [5, 1, 2, 8, 9, 3]
System.out.println(Arrays.toString(arr)); // [5, 1, 2, 8, 9, 3]
```

---

#### `static <T> void sort(T[] a, Comparator<? super T> c)`

**Signature:** `static <T> void sort(T[] a, Comparator<? super T> c)`  
**Description:** Sorts an object array using a custom comparator. Uses **TimSort** (O(n log n) guaranteed, stable). Cannot be used with primitive arrays (use wrapper types).

```java
// Sort strings by length
String[] words = {"banana", "apple", "fig", "cherry", "date"};
Arrays.sort(words, (a, b) -> a.length() - b.length());
System.out.println(Arrays.toString(words));
// [fig, date, apple, banana, cherry]

// Sort integers in descending order (must use Integer[], not int[])
Integer[] nums = {5, 2, 8, 1, 9};
Arrays.sort(nums, (a, b) -> b - a);
System.out.println(Arrays.toString(nums)); // [9, 8, 5, 2, 1]

// Sort 2D array by first column
int[][] matrix = {{3,1},{1,5},{2,3}};
Arrays.sort(matrix, (a, b) -> a[0] - b[0]);
// matrix: [[1,5],[2,3],[3,1]]
```

---

#### `static int binarySearch(int[] a, int key)`

**Signature:** `static int binarySearch(int[] a, int key)`  
**Description:** Searches the sorted array for `key`. Returns the **index** if found, or `-(insertion point) - 1` if not found. **Array MUST be sorted first.**

```java
int[] arr = {1, 3, 5, 7, 9, 11};
int idx = Arrays.binarySearch(arr, 7);
System.out.println(idx); // 3

int notFound = Arrays.binarySearch(arr, 6);
System.out.println(notFound); // -4  (insertion point is 3, so -(3)-1 = -4)

// WRONG — array not sorted, behavior undefined:
int[] unsorted = {5, 1, 3};
// Arrays.binarySearch(unsorted, 3); — DON'T DO THIS
```

---

#### `static int binarySearch(int[] a, int fromIndex, int toIndex, int key)`

**Signature:** `static int binarySearch(int[] a, int fromIndex, int toIndex, int key)`  
**Description:** Binary search within the subarray `[fromIndex, toIndex)`.

```java
int[] arr = {1, 3, 5, 7, 9, 11, 13, 15};
// Search for 9 within indices 2 to 6 (values 5,7,9,11)
int idx = Arrays.binarySearch(arr, 2, 6, 9);
System.out.println(idx); // 4
```

---

#### `static boolean equals(int[] a, int[] a2)`

**Signature:** `static boolean equals(int[] a, int[] a2)`  
**Description:** Returns `true` if two arrays have the same length and all corresponding elements are equal.

```java
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};
int[] c = {1, 2, 4};

System.out.println(Arrays.equals(a, b)); // true
System.out.println(Arrays.equals(a, c)); // false
System.out.println(a == b);              // false — different objects
```

---

#### `static void fill(int[] a, int val)`

**Signature:** `static void fill(int[] a, int val)`  
**Description:** Fills the entire array with the given value.

```java
int[] arr = new int[5];
Arrays.fill(arr, 7);
System.out.println(Arrays.toString(arr)); // [7, 7, 7, 7, 7]

// Common use: initialize boolean array to true
boolean[] visited = new boolean[10];
Arrays.fill(visited, true);

// Initialize with -1 (common in DP)
int[] dp = new int[10];
Arrays.fill(dp, -1);
```

---

#### `static void fill(int[] a, int fromIndex, int toIndex, int val)`

**Signature:** `static void fill(int[] a, int fromIndex, int toIndex, int val)`  
**Description:** Fills only the subarray `[fromIndex, toIndex)` with the value.

```java
int[] arr = {0, 0, 0, 0, 0, 0};
Arrays.fill(arr, 2, 5, 9); // fill indices 2,3,4 with 9
System.out.println(Arrays.toString(arr)); // [0, 0, 9, 9, 9, 0]
```

---

#### `static int[] copyOf(int[] original, int newLength)`

**Signature:** `static int[] copyOf(int[] original, int newLength)`  
**Description:** Copies the array into a new array of the given length. If `newLength > original.length`, pads with zeros. If shorter, truncates.

```java
int[] arr = {1, 2, 3, 4, 5};

// Extend (pads with 0)
int[] extended = Arrays.copyOf(arr, 8);
System.out.println(Arrays.toString(extended)); // [1, 2, 3, 4, 5, 0, 0, 0]

// Truncate
int[] truncated = Arrays.copyOf(arr, 3);
System.out.println(Arrays.toString(truncated)); // [1, 2, 3]

// Same length (full copy)
int[] copy = Arrays.copyOf(arr, arr.length);
System.out.println(Arrays.toString(copy)); // [1, 2, 3, 4, 5]
```

---

#### `static int[] copyOfRange(int[] original, int from, int to)`

**Signature:** `static int[] copyOfRange(int[] original, int from, int to)`  
**Description:** Copies the subarray from index `from` (inclusive) to `to` (exclusive).

```java
int[] arr = {10, 20, 30, 40, 50, 60};
int[] sub = Arrays.copyOfRange(arr, 1, 4);
System.out.println(Arrays.toString(sub)); // [20, 30, 40]

// If 'to' is beyond the array length, pads with 0:
int[] extended = Arrays.copyOfRange(arr, 3, 10);
System.out.println(Arrays.toString(extended)); // [40, 50, 60, 0, 0, 0, 0]
```

---

#### `static String toString(int[] a)`

**Signature:** `static String toString(int[] a)`  
**Description:** Returns a string representation like `[1, 2, 3, 4, 5]`. Works for all primitive array types.

```java
int[] arr = {1, 2, 3};
System.out.println(Arrays.toString(arr));          // [1, 2, 3]
System.out.println(Arrays.toString(new double[]{1.1, 2.2})); // [1.1, 2.2]
System.out.println(Arrays.toString(null));         // null
```

---

#### `static String deepToString(Object[] a)`

**Signature:** `static String deepToString(Object[] a)`  
**Description:** Returns a deep string representation for nested (multi-dimensional) arrays. Regular `toString` would print `[[I@7ef88735, ...]` (memory addresses) for 2D arrays.

```java
int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};

// WRONG — prints references:
System.out.println(Arrays.toString(matrix)); // [[I@hashcode, ...]

// RIGHT:
System.out.println(Arrays.deepToString(matrix)); // [[1, 2, 3], [4, 5, 6], [7, 8, 9]]

// Works for 3D too:
int[][][] cube = {{{1,2},{3,4}},{{5,6},{7,8}}};
System.out.println(Arrays.deepToString(cube)); // [[[1, 2], [3, 4]], [[5, 6], [7, 8]]]
```

---

#### Deep Copy of 2D Arrays

There is no `Arrays.deepCopyOf()` method. You must do it manually:

```java
int[][] original = {{1,2,3},{4,5,6},{7,8,9}};

// Method 1: Manual loop
int[][] deep = new int[original.length][];
for (int i = 0; i < original.length; i++) {
    deep[i] = Arrays.copyOf(original[i], original[i].length);
}
deep[0][0] = 999;
System.out.println(original[0][0]); // 1 — unaffected

// Method 2: Stream (Java 8+)
int[][] deep2 = Arrays.stream(original)
                      .map(int[]::clone)
                      .toArray(int[][]::new);
```

---

#### `static <T> List<T> asList(T... a)`

**Signature:** `static <T> List<T> asList(T... a)`  
**Description:** Returns a **fixed-size** `List` backed by the array. Changes to the list reflect in the array and vice versa. **Cannot add or remove elements** (size is fixed), but `set()` works.

```java
// Create a List from array values:
List<String> list = Arrays.asList("apple", "banana", "cherry");
System.out.println(list.get(1)); // banana

list.set(0, "avocado"); // OK — modifies the backed array
// list.add("date");    // UnsupportedOperationException!

// To get a truly mutable ArrayList:
List<String> mutable = new ArrayList<>(Arrays.asList("a", "b", "c"));
mutable.add("d"); // OK now

// GOTCHA: Does NOT work with primitive arrays!
int[] primitiveArr = {1, 2, 3};
// Arrays.asList(primitiveArr) → List<int[]> with ONE element (the array itself)!
// Use Integer[] instead:
Integer[] boxedArr = {1, 2, 3};
List<Integer> intList = Arrays.asList(boxedArr); // works correctly
```

---

#### `static IntStream stream(int[] array)` and `static <T> Stream<T> stream(T[] array)`

**Signature:**
- `static IntStream stream(int[] array)`
- `static <T> Stream<T> stream(T[] array)`

**Description:** Converts an array to a Java 8 Stream for functional operations.

```java
int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};

// Sum
int sum = Arrays.stream(arr).sum(); // 31

// Max
int max = Arrays.stream(arr).max().getAsInt(); // 9

// Filter and collect
int[] evens = Arrays.stream(arr).filter(x -> x % 2 == 0).toArray();
System.out.println(Arrays.toString(evens)); // [4, 2, 6]

// Sort with stream
int[] sorted = Arrays.stream(arr).sorted().toArray();

// Object array to Stream:
String[] words = {"hello", "world"};
long count = Arrays.stream(words).filter(w -> w.length() > 4).count(); // 2

// Convert int[] to Integer[] via stream:
Integer[] boxed = Arrays.stream(arr).boxed().toArray(Integer[]::new);

// int[] to List<Integer>:
List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());
```

---

### 3.3 `java.util.ArrayList` — Complete API

`ArrayList` is the go-to dynamic array in Java. Import: `import java.util.ArrayList;`

`ArrayList<E>` is generic — `E` is the element type. **Cannot store primitive types directly** — use `Integer`, `Double`, `Character`, etc.

#### Constructors

---

##### `ArrayList()`

**Signature:** `ArrayList()`  
**Description:** Creates an empty ArrayList with default initial capacity of **10**.

```java
ArrayList<Integer> list = new ArrayList<>();
System.out.println(list.size()); // 0
```

---

##### `ArrayList(int initialCapacity)`

**Signature:** `ArrayList(int initialCapacity)`  
**Description:** Creates an empty ArrayList with the given initial capacity. Use this when you know approximately how many elements you'll have — avoids repeated resizing.

```java
ArrayList<String> list = new ArrayList<>(1000); // pre-allocate for 1000 elements
// Still size 0, but won't resize until 1000 elements are added
```

---

##### `ArrayList(Collection<? extends E> c)`

**Signature:** `ArrayList(Collection<? extends E> c)`  
**Description:** Creates an ArrayList from another collection. Elements are added in the collection's iteration order.

```java
LinkedList<String> linked = new LinkedList<>();
linked.add("a"); linked.add("b"); linked.add("c");

ArrayList<String> list = new ArrayList<>(linked);
System.out.println(list); // [a, b, c]

// Convert array to ArrayList:
String[] arr = {"x", "y", "z"};
ArrayList<String> fromArr = new ArrayList<>(Arrays.asList(arr));
```

---

#### Core Methods

---

##### `boolean add(E e)`

**Signature:** `boolean add(E e)`  
**Description:** Appends element to the end. Returns `true` (always — per `Collection` contract). Amortized O(1) — occasionally O(n) when resizing.

```java
ArrayList<Integer> list = new ArrayList<>();
list.add(10);
list.add(20);
list.add(30);
System.out.println(list); // [10, 20, 30]
```

---

##### `void add(int index, E element)`

**Signature:** `void add(int index, E element)`  
**Description:** Inserts element at the given index, shifting all elements at and after `index` one position right. O(n) due to shifting.

```java
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(10, 20, 30, 40));
list.add(2, 99); // insert 99 at index 2
System.out.println(list); // [10, 20, 99, 30, 40]
```

---

##### `boolean addAll(Collection<? extends E> c)`

**Signature:** `boolean addAll(Collection<? extends E> c)`  
**Description:** Appends all elements of the given collection to the end of this list. Returns `true` if the list changed.

```java
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
List<Integer> toAdd = Arrays.asList(4, 5, 6);
list.addAll(toAdd);
System.out.println(list); // [1, 2, 3, 4, 5, 6]
```

---

##### `boolean addAll(int index, Collection<? extends E> c)`

**Signature:** `boolean addAll(int index, Collection<? extends E> c)`  
**Description:** Inserts all elements of collection starting at the given index, shifting existing elements right.

```java
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
List<Integer> toInsert = Arrays.asList(10, 11);
list.addAll(1, toInsert); // insert at index 1
System.out.println(list); // [1, 10, 11, 2, 3]
```

---

##### `E get(int index)`

**Signature:** `E get(int index)`  
**Description:** Returns the element at the given index. O(1) — direct array access. Throws `IndexOutOfBoundsException` if index is invalid.

```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
String elem = list.get(2);
System.out.println(elem); // c

// Get last element:
String last = list.get(list.size() - 1); // d
```

---

##### `E set(int index, E element)`

**Signature:** `E set(int index, E element)`  
**Description:** Replaces the element at position `index` with `element`. Returns the old element. O(1).

```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
String old = list.set(1, "BETA"); // replace index 1
System.out.println(old);  // b
System.out.println(list); // [a, BETA, c]
```

---

##### `E remove(int index)`

**Signature:** `E remove(int index)`  
**Description:** Removes and returns the element at the given index. Shifts subsequent elements left. O(n).

```java
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50));
int removed = list.remove(2); // removes index 2
System.out.println(removed); // 30
System.out.println(list);    // [10, 20, 40, 50]
```

---

##### `boolean remove(Object o)`

**Signature:** `boolean remove(Object o)`  
**Description:** Removes the **first occurrence** of the specified object. Returns `true` if found and removed. O(n) for finding + O(n) for shifting.

```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "b", "d"));
boolean removed = list.remove("b"); // removes first "b"
System.out.println(removed); // true
System.out.println(list);    // [a, c, b, d]

// IMPORTANT: For Integer lists, to remove by VALUE (not index), cast to Object:
ArrayList<Integer> nums = new ArrayList<>(Arrays.asList(10, 20, 30));
nums.remove(Integer.valueOf(20)); // removes value 20
// nums.remove(1) would remove INDEX 1 (which is value 20 here — but this is by index, not value!)
```

---

##### `boolean removeAll(Collection<?> c)`

**Signature:** `boolean removeAll(Collection<?> c)`  
**Description:** Removes all elements that are in the given collection. Returns `true` if the list changed.

```java
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
list.removeAll(Arrays.asList(2, 4, 6)); // remove evens
System.out.println(list); // [1, 3, 5]
```

---

##### `boolean retainAll(Collection<?> c)`

**Signature:** `boolean retainAll(Collection<?> c)`  
**Description:** Retains only elements that are in the given collection (intersection). Returns `true` if list changed.

```java
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
list.retainAll(Arrays.asList(2, 4, 6)); // keep only evens
System.out.println(list); // [2, 4, 6]
```

---

##### `void clear()`

**Signature:** `void clear()`  
**Description:** Removes all elements. Size becomes 0, but internal capacity is unchanged.

```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
list.clear();
System.out.println(list.size()); // 0
System.out.println(list);        // []
```

---

##### `int size()`

**Signature:** `int size()`  
**Description:** Returns the number of elements currently in the list. O(1).

```java
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
System.out.println(list.size()); // 4
list.add(5);
System.out.println(list.size()); // 5
```

---

##### `boolean isEmpty()`

**Signature:** `boolean isEmpty()`  
**Description:** Returns `true` if the list has no elements (`size() == 0`).

```java
ArrayList<String> list = new ArrayList<>();
System.out.println(list.isEmpty()); // true
list.add("hello");
System.out.println(list.isEmpty()); // false
```

---

##### `boolean contains(Object o)`

**Signature:** `boolean contains(Object o)`  
**Description:** Returns `true` if the list contains the element. Uses `.equals()` for comparison. O(n) linear scan.

```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
System.out.println(list.contains("banana")); // true
System.out.println(list.contains("grape"));  // false
```

---

##### `int indexOf(Object o)`

**Signature:** `int indexOf(Object o)`  
**Description:** Returns the index of the **first** occurrence of the element, or `-1` if not found. O(n).

```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "b", "a"));
System.out.println(list.indexOf("b")); // 1
System.out.println(list.indexOf("z")); // -1
```

---

##### `int lastIndexOf(Object o)`

**Signature:** `int lastIndexOf(Object o)`  
**Description:** Returns the index of the **last** occurrence of the element, or `-1` if not found. O(n).

```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "b", "a"));
System.out.println(list.lastIndexOf("b")); // 3
System.out.println(list.lastIndexOf("a")); // 4
```

---

##### `List<E> subList(int fromIndex, int toIndex)`

**Signature:** `List<E> subList(int fromIndex, int toIndex)`  
**Description:** Returns a **view** of the portion of this list from `fromIndex` (inclusive) to `toIndex` (exclusive). The returned list is backed by the original — changes affect both. Useful for passing a slice to a method.

```java
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));
List<Integer> sub = list.subList(2, 5); // [2, 3, 4]
System.out.println(sub); // [2, 3, 4]

sub.set(0, 99); // modifies original too!
System.out.println(list); // [0, 1, 99, 3, 4, 5, 6]

// To get an independent copy:
List<Integer> independent = new ArrayList<>(list.subList(2, 5));
```

---

##### `Object[] toArray()`

**Signature:** `Object[] toArray()`  
**Description:** Returns an `Object[]` containing all elements in order. The returned array is independent (not backed by the list).

```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
Object[] arr = list.toArray();
System.out.println(Arrays.toString(arr)); // [a, b, c]
// Elements are Objects, need casting:
String s = (String) arr[0]; // "a"
```

---

##### `<T> T[] toArray(T[] a)`

**Signature:** `<T> T[] toArray(T[] a)`  
**Description:** Returns a typed array. If `a` is large enough, fills it and returns it. If not, allocates a new array of the same type.

```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

// Pattern 1: pass an empty array of the right type
String[] arr1 = list.toArray(new String[0]);
System.out.println(Arrays.toString(arr1)); // [a, b, c]

// Pattern 2: pre-size the array
String[] arr2 = list.toArray(new String[list.size()]);
System.out.println(Arrays.toString(arr2)); // [a, b, c]
```

---

##### `Iterator<E> iterator()`

**Signature:** `Iterator<E> iterator()`  
**Description:** Returns an iterator over the elements in proper sequence. Used for safe removal while iterating.

```java
ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    int val = it.next();
    if (val % 2 == 0) {
        it.remove(); // safe removal during iteration
    }
}
System.out.println(list); // [1, 3, 5]
```

---

##### `ListIterator<E> listIterator()`

**Signature:** `ListIterator<E> listIterator()`  
**Description:** Returns a list iterator that can traverse in both directions and add/remove/set elements.

```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) {
    String val = lit.next();
    lit.set(val.toUpperCase()); // replace each element with uppercase
}
System.out.println(list); // [A, B, C]

// Traverse backwards:
while (lit.hasPrevious()) {
    System.out.print(lit.previous() + " "); // C B A
}
```

---

##### `void sort(Comparator<? super E> c)`

**Signature:** `void sort(Comparator<? super E> c)`  
**Description:** Sorts this list using the given comparator (or natural order if `null`). Backed by `Arrays.sort()` (TimSort). Stable, O(n log n).

```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("banana", "apple", "cherry", "date"));

// Natural order (alphabetical):
list.sort(null);
System.out.println(list); // [apple, banana, cherry, date]

// Custom order — by length:
list.sort((a, b) -> a.length() - b.length());
System.out.println(list); // [date, apple, banana, cherry]

// Reverse order:
list.sort(Comparator.reverseOrder());
System.out.println(list); // [date, cherry, banana, apple]
```

---

##### `void ensureCapacity(int minCapacity)`

**Signature:** `void ensureCapacity(int minCapacity)`  
**Description:** Increases the capacity of the ArrayList to at least `minCapacity`. Use this if you know in advance you'll add many elements — avoids incremental resizing.

```java
ArrayList<Integer> list = new ArrayList<>(); // initial capacity 10
list.ensureCapacity(1000); // now pre-allocated for 1000 elements

// Good for:
for (int i = 0; i < 1000; i++) {
    list.add(i); // no resizing needed
}
```

---

##### `void trimToSize()`

**Signature:** `void trimToSize()`  
**Description:** Trims the internal array capacity to the current size. Frees unused memory. Use when you've finished building the list and want to minimize memory footprint.

```java
ArrayList<Integer> list = new ArrayList<>(1000); // capacity 1000
list.add(1); list.add(2); list.add(3); // only 3 elements
list.trimToSize(); // now capacity == 3, releases the rest
```

---

##### `Object clone()`

**Signature:** `Object clone()`  
**Description:** Returns a **shallow** copy of this ArrayList. The list structure is copied, but the elements themselves are not deep-copied (references to same objects).

```java
ArrayList<String> original = new ArrayList<>(Arrays.asList("a", "b", "c"));
ArrayList<String> copy = (ArrayList<String>) original.clone();

copy.add("d");
System.out.println(original); // [a, b, c] — unaffected (structure is separate)
System.out.println(copy);     // [a, b, c, d]

// But: if elements are mutable objects (e.g., int[]), both lists share them:
ArrayList<int[]> lists = new ArrayList<>();
lists.add(new int[]{1, 2, 3});
ArrayList<int[]> cloned = (ArrayList<int[]>) lists.clone();
cloned.get(0)[0] = 999; // modifies the SAME array object
System.out.println(lists.get(0)[0]); // 999
```

---

## Chapter 4: Java API — Strings

### 4.1 `java.lang.String` — Complete API

`String` is a final class in `java.lang`. Import is automatic.

#### Constructors

---

##### `String()`

**Signature:** `String()`  
**Description:** Creates an empty string `""`. Rarely used directly; prefer string literals.

```java
String s = new String(); // ""
System.out.println(s.length()); // 0
```

---

##### `String(String original)`

**Signature:** `String(String original)`  
**Description:** Creates a new String with the same content. This new object is NOT in the String pool (forces heap allocation). Rarely needed.

```java
String s1 = "hello";
String s2 = new String(s1); // new object on heap, same content
System.out.println(s1.equals(s2)); // true
System.out.println(s1 == s2);      // false
```

---

##### `String(char[] value)`

**Signature:** `String(char[] value)`  
**Description:** Creates a String from a char array. The array content is copied.

```java
char[] chars = {'J', 'a', 'v', 'a'};
String s = new String(chars); // "Java"
System.out.println(s); // Java

// Modifying chars after does NOT affect s:
chars[0] = 'X';
System.out.println(s); // still "Java"
```

---

##### `String(char[] value, int offset, int count)`

**Signature:** `String(char[] value, int offset, int count)`  
**Description:** Creates a String from a portion of a char array, starting at `offset` for `count` characters.

```java
char[] chars = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
String s = new String(chars, 6, 5); // "World" (start at index 6, take 5 chars)
System.out.println(s); // World
```

---

##### `String(byte[] bytes)`

**Signature:** `String(byte[] bytes)`  
**Description:** Creates a String from byte array using the platform's default charset.

```java
byte[] bytes = {72, 101, 108, 108, 111}; // ASCII codes for "Hello"
String s = new String(bytes);
System.out.println(s); // Hello
```

---

##### `String(byte[] bytes, String charsetName)` throws `UnsupportedEncodingException`

**Signature:** `String(byte[] bytes, String charsetName) throws UnsupportedEncodingException`  
**Description:** Creates a String from byte array using the specified charset.

```java
byte[] utf8bytes = "Hello".getBytes("UTF-8");
try {
    String s = new String(utf8bytes, "UTF-8");
    System.out.println(s); // Hello
} catch (UnsupportedEncodingException e) {
    e.printStackTrace();
}
```

---

##### `String(StringBuilder builder)`

**Signature:** `String(StringBuilder builder)`  
**Description:** Creates a String from a StringBuilder. Equivalent to calling `builder.toString()`.

```java
StringBuilder sb = new StringBuilder("Hello World");
String s = new String(sb);
System.out.println(s); // Hello World
```

---

#### Core String Methods

---

##### `int length()`

**Signature:** `int length()`  
**Description:** Returns the number of **char** values (UTF-16 code units) in the string. Note: emoji/surrogate pairs count as 2.

```java
String s = "Hello";
System.out.println(s.length()); // 5

String s2 = "";
System.out.println(s2.length()); // 0

String emoji = "Hi😀";
System.out.println(emoji.length()); // 4 (H=1, i=1, 😀=2)
```

---

##### `boolean isEmpty()`

**Signature:** `boolean isEmpty()`  
**Description:** Returns `true` if `length() == 0`.

```java
System.out.println("".isEmpty());       // true
System.out.println("  ".isEmpty());     // false (spaces exist)
System.out.println("hello".isEmpty()); // false
```

---

##### `boolean isBlank()` *(Java 11+)*

**Signature:** `boolean isBlank()`  
**Description:** Returns `true` if the string is empty or contains only whitespace characters. More useful than `isEmpty()` for user input validation.

```java
System.out.println("".isBlank());       // true
System.out.println("  ".isBlank());     // true  ← key difference from isEmpty()
System.out.println("  a  ".isBlank()); // false
System.out.println("hello".isBlank()); // false
```

---

##### `char charAt(int index)`

**Signature:** `char charAt(int index)`  
**Description:** Returns the `char` at the given index. Throws `StringIndexOutOfBoundsException` if out of range.

```java
String s = "Hello";
char c = s.charAt(1); // 'e'
char first = s.charAt(0); // 'H'
char last = s.charAt(s.length() - 1); // 'o'

// Iterate characters:
for (int i = 0; i < s.length(); i++) {
    System.out.print(s.charAt(i) + " "); // H e l l o
}
```

---

##### `int codePointAt(int index)`

**Signature:** `int codePointAt(int index)`  
**Description:** Returns the Unicode code point at the given index. Handles surrogate pairs (returns the full code point, not just the char value).

```java
String s = "A😀B";
System.out.println(s.codePointAt(0)); // 65 (A)
System.out.println(s.codePointAt(1)); // 128512 (😀 full code point)
System.out.println((int)'A');         // 65
```

---

##### `int indexOf(int ch)`

**Signature:** `int indexOf(int ch)`  
**Description:** Returns the index of the first occurrence of character `ch`, or `-1` if not found.

```java
String s = "Hello World";
System.out.println(s.indexOf('o'));  // 4
System.out.println(s.indexOf('z'));  // -1
System.out.println(s.indexOf('l'));  // 2 (first occurrence)
```

---

##### `int indexOf(int ch, int fromIndex)`

**Signature:** `int indexOf(int ch, int fromIndex)`  
**Description:** Returns index of first occurrence of `ch` at or after `fromIndex`.

```java
String s = "Hello World";
System.out.println(s.indexOf('l', 3));  // 3 (search from index 3 onwards)
System.out.println(s.indexOf('l', 4));  // 9 (skips the 'l' at index 3)
System.out.println(s.indexOf('o', 5));  // 7 ("World"'s 'o')
```

---

##### `int indexOf(String str)`

**Signature:** `int indexOf(String str)`  
**Description:** Returns the index of the first occurrence of the substring `str`, or `-1` if not found.

```java
String s = "Hello World Hello";
System.out.println(s.indexOf("Hello")); // 0
System.out.println(s.indexOf("World")); // 6
System.out.println(s.indexOf("Java"));  // -1
```

---

##### `int indexOf(String str, int fromIndex)`

**Signature:** `int indexOf(String str, int fromIndex)`  
**Description:** Returns index of first occurrence of `str` at or after `fromIndex`.

```java
String s = "Hello World Hello";
System.out.println(s.indexOf("Hello", 1)); // 12 (skips index 0)
```

---

##### `int lastIndexOf(int ch)`

**Signature:** `int lastIndexOf(int ch)`  
**Description:** Returns the index of the **last** occurrence of character `ch`, or `-1`.

```java
String s = "Hello World";
System.out.println(s.lastIndexOf('l')); // 9
System.out.println(s.lastIndexOf('o')); // 7
```

---

##### `int lastIndexOf(String str)`

**Signature:** `int lastIndexOf(String str)`  
**Description:** Returns the index of the **last** occurrence of substring `str`.

```java
String s = "Hello World Hello";
System.out.println(s.lastIndexOf("Hello")); // 12
System.out.println(s.lastIndexOf("World")); // 6
```

---

##### `boolean contains(CharSequence s)`

**Signature:** `boolean contains(CharSequence s)`  
**Description:** Returns `true` if the string contains the specified sequence. Accepts `String`, `StringBuilder`, `StringBuffer`.

```java
String s = "Hello World";
System.out.println(s.contains("World")); // true
System.out.println(s.contains("world")); // false (case-sensitive)
System.out.println(s.contains(""));     // true (empty string is always contained)

// Case-insensitive check:
System.out.println(s.toLowerCase().contains("world")); // true
```

---

##### `boolean startsWith(String prefix)`

**Signature:** `boolean startsWith(String prefix)`  
**Description:** Returns `true` if the string starts with `prefix`.

```java
String s = "Hello World";
System.out.println(s.startsWith("Hello")); // true
System.out.println(s.startsWith("World")); // false
System.out.println(s.startsWith(""));     // true
```

---

##### `boolean startsWith(String prefix, int toffset)`

**Signature:** `boolean startsWith(String prefix, int toffset)`  
**Description:** Returns `true` if the substring starting at `toffset` starts with `prefix`.

```java
String s = "Hello World";
System.out.println(s.startsWith("World", 6)); // true (substring from index 6 is "World")
System.out.println(s.startsWith("Hello", 0)); // true
System.out.println(s.startsWith("Hello", 1)); // false
```

---

##### `boolean endsWith(String suffix)`

**Signature:** `boolean endsWith(String suffix)`  
**Description:** Returns `true` if the string ends with `suffix`.

```java
String s = "Hello World";
System.out.println(s.endsWith("World")); // true
System.out.println(s.endsWith("Hello")); // false
System.out.println(s.endsWith(""));     // true
```

---

##### `String substring(int beginIndex)`

**Signature:** `String substring(int beginIndex)`  
**Description:** Returns the substring from `beginIndex` to the end of the string. The new string is a fresh object.

```java
String s = "Hello World";
System.out.println(s.substring(6)); // "World"
System.out.println(s.substring(0)); // "Hello World" (full string)
```

---

##### `String substring(int beginIndex, int endIndex)`

**Signature:** `String substring(int beginIndex, int endIndex)`  
**Description:** Returns the substring from `beginIndex` (inclusive) to `endIndex` (exclusive). Length = `endIndex - beginIndex`.

```java
String s = "Hello World";
System.out.println(s.substring(0, 5)); // "Hello"
System.out.println(s.substring(6, 11)); // "World"
System.out.println(s.substring(2, 4)); // "ll"
```

---

##### `String replace(char oldChar, char newChar)`

**Signature:** `String replace(char oldChar, char newChar)`  
**Description:** Returns a new string with all occurrences of `oldChar` replaced by `newChar`.

```java
String s = "Hello World";
String replaced = s.replace('l', 'x');
System.out.println(replaced); // "Hexxo Worxd"
System.out.println(s);        // "Hello World" (original unchanged)
```

---

##### `String replace(CharSequence target, CharSequence replacement)`

**Signature:** `String replace(CharSequence target, CharSequence replacement)`  
**Description:** Returns a new string with all occurrences of the `target` sequence replaced by `replacement`. Not a regex method — treats target literally.

```java
String s = "foo bar foo baz foo";
String result = s.replace("foo", "qux");
System.out.println(result); // "qux bar qux baz qux"

// Replace with empty string (deletion):
String noSpace = "Hello World".replace(" ", "");
System.out.println(noSpace); // "HelloWorld"
```

---

##### `String replaceAll(String regex, String replacement)`

**Signature:** `String replaceAll(String regex, String replacement)`  
**Description:** Returns a new string with all substrings matching the **regex** replaced by `replacement`. More powerful than `replace()` but slower.

```java
String s = "Hello123World456";
// Remove all digits:
String noDigits = s.replaceAll("\\d+", "");
System.out.println(noDigits); // "HelloWorld"

// Replace one or more spaces with single space:
String normalized = "Hello   World   Java".replaceAll("\\s+", " ");
System.out.println(normalized); // "Hello World Java"

// Remove non-alphanumeric characters:
String clean = "H@ell#o W*orl&d".replaceAll("[^a-zA-Z0-9]", "");
System.out.println(clean); // "HelloWorld"
```

---

##### `String replaceFirst(String regex, String replacement)`

**Signature:** `String replaceFirst(String regex, String replacement)`  
**Description:** Returns a new string with only the **first** substring matching the regex replaced.

```java
String s = "aabbcc";
System.out.println(s.replaceFirst("b", "X")); // "aaXbcc"
System.out.println(s.replaceAll("b", "X"));   // "aaXXcc"
```

---

##### `String[] split(String regex)`

**Signature:** `String[] split(String regex)`  
**Description:** Splits the string around matches of the given regex. Returns a String array. Trailing empty strings are NOT included by default.

```java
String csv = "apple,banana,cherry,date";
String[] fruits = csv.split(",");
System.out.println(Arrays.toString(fruits)); // [apple, banana, cherry, date]
System.out.println(fruits.length); // 4

// Split on whitespace:
String sentence = "Hello World Java";
String[] words = sentence.split("\\s+");
System.out.println(Arrays.toString(words)); // [Hello, World, Java]

// Split on dot (must escape . in regex):
String ip = "192.168.1.1";
String[] parts = ip.split("\\.");
System.out.println(Arrays.toString(parts)); // [192, 168, 1, 1]
```

---

##### `String[] split(String regex, int limit)`

**Signature:** `String[] split(String regex, int limit)`  
**Description:** Splits with a limit on the number of resulting parts. If `limit > 0`, at most `limit` parts are returned (last part may contain delimiters). If `limit < 0`, includes trailing empty strings.

```java
String s = "a,b,c,d,e";
System.out.println(Arrays.toString(s.split(",", 3)));  // [a, b, c,d,e] — max 3 parts
System.out.println(Arrays.toString(s.split(",", -1))); // [a, b, c, d, e] — all, keep trailing

String trailingComma = "a,b,c,";
System.out.println(Arrays.toString(trailingComma.split(","))); // [a, b, c] — trailing empty dropped
System.out.println(Arrays.toString(trailingComma.split(",", -1))); // [a, b, c, ] — kept
```

---

##### `String toLowerCase()`

**Signature:** `String toLowerCase()`  
**Description:** Returns a new string with all characters converted to lowercase using the default locale.

```java
String s = "Hello WORLD 123";
System.out.println(s.toLowerCase()); // "hello world 123"
```

---

##### `String toUpperCase()`

**Signature:** `String toUpperCase()`  
**Description:** Returns a new string with all characters converted to uppercase.

```java
String s = "Hello World";
System.out.println(s.toUpperCase()); // "HELLO WORLD"
```

---

##### `String trim()`

**Signature:** `String trim()`  
**Description:** Returns a string with leading and trailing ASCII whitespace (characters ≤ '\u0020') removed. Does NOT handle Unicode whitespace (use `strip()` for that).

```java
String s = "   Hello World   ";
System.out.println(s.trim()); // "Hello World"
System.out.println("|" + "  hi  ".trim() + "|"); // |hi|
```

---

##### `String strip()` *(Java 11+)*

**Signature:** `String strip()`  
**Description:** Like `trim()` but uses Unicode-aware whitespace stripping. Handles all Unicode whitespace characters.

```java
String s = "   Hello   ";
System.out.println(s.strip()); // "Hello"
// For most practical purposes, strip() == trim() for standard ASCII whitespace
```

---

##### `String stripLeading()` *(Java 11+)*

**Signature:** `String stripLeading()`  
**Description:** Removes leading whitespace only.

```java
String s = "   Hello   ";
System.out.println(s.stripLeading());  // "Hello   "
System.out.println(s.stripTrailing()); // "   Hello"
```

---

##### `String stripTrailing()` *(Java 11+)*

**Signature:** `String stripTrailing()`  
**Description:** Removes trailing whitespace only.

```java
String s = "   Hello   ";
System.out.println("|" + s.stripTrailing() + "|"); // |   Hello|
```

---

##### `String concat(String str)`

**Signature:** `String concat(String str)`  
**Description:** Returns a new string that is `this` string followed by `str`. Equivalent to `this + str`. Returns `this` if `str` is empty.

```java
String s = "Hello";
String result = s.concat(" World");
System.out.println(result); // "Hello World"

// For multiple concatenations, use StringBuilder instead
```

---

##### `boolean equals(Object anObject)`

**Signature:** `boolean equals(Object anObject)`  
**Description:** Returns `true` if the given object is a String with the same character sequence. Case-sensitive.

```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = "hello";

System.out.println(s1.equals(s2)); // true
System.out.println(s1.equals(s3)); // false (case-sensitive)
System.out.println(s1.equals(null)); // false (does not throw NPE)
System.out.println(s1.equals(42));   // false (not a String)
```

---

##### `boolean equalsIgnoreCase(String anotherString)`

**Signature:** `boolean equalsIgnoreCase(String anotherString)`  
**Description:** Case-insensitive comparison.

```java
System.out.println("Hello".equalsIgnoreCase("hello")); // true
System.out.println("JAVA".equalsIgnoreCase("java"));   // true
System.out.println("Hello".equalsIgnoreCase("World")); // false
```

---

##### `int compareTo(String anotherString)`

**Signature:** `int compareTo(String anotherString)`  
**Description:** Lexicographic comparison. Returns:
- Negative if `this` < `other`
- Zero if equal
- Positive if `this` > `other`

```java
System.out.println("apple".compareTo("banana")); // negative (a < b)
System.out.println("banana".compareTo("apple")); // positive
System.out.println("apple".compareTo("apple"));  // 0

// Useful for custom sorting:
String[] words = {"banana", "apple", "cherry"};
Arrays.sort(words); // uses compareTo internally
System.out.println(Arrays.toString(words)); // [apple, banana, cherry]
```

---

##### `int compareToIgnoreCase(String str)`

**Signature:** `int compareToIgnoreCase(String str)`  
**Description:** Case-insensitive lexicographic comparison.

```java
System.out.println("Hello".compareToIgnoreCase("hello")); // 0
System.out.println("Apple".compareToIgnoreCase("banana")); // negative
```

---

##### `char[] toCharArray()`

**Signature:** `char[] toCharArray()`  
**Description:** Returns a new char array containing the characters of this string. The array is independent — modifying it does not affect the string.

```java
String s = "Hello";
char[] chars = s.toCharArray(); // ['H','e','l','l','o']

// Sort characters:
Arrays.sort(chars);
String sorted = new String(chars);
System.out.println(sorted); // "Hello" → "Helo" (sorted chars: H,e,l,l,o → H,e,l,l,o)

// Anagram check using toCharArray:
String a = "listen", b = "silent";
char[] ca = a.toCharArray(), cb = b.toCharArray();
Arrays.sort(ca); Arrays.sort(cb);
System.out.println(Arrays.equals(ca, cb)); // true — they're anagrams
```

---

##### `static String valueOf(...)` — All Overloads

**Signature (all overloads):**
- `static String valueOf(boolean b)`
- `static String valueOf(char c)`
- `static String valueOf(char[] data)`
- `static String valueOf(int i)`
- `static String valueOf(long l)`
- `static String valueOf(float f)`
- `static String valueOf(double d)`
- `static String valueOf(Object obj)` — calls `obj.toString()`, returns `"null"` if obj is null

**Description:** Converts any primitive or object to its String representation.

```java
System.out.println(String.valueOf(42));       // "42"
System.out.println(String.valueOf(3.14));     // "3.14"
System.out.println(String.valueOf(true));     // "true"
System.out.println(String.valueOf('A'));      // "A"
System.out.println(String.valueOf(null));     // "null" (safe for null, no NPE)

Object obj = null;
System.out.println(String.valueOf(obj));     // "null" (safe)
// vs:
// System.out.println(obj.toString()); // NullPointerException!
```

---

##### `static String format(String format, Object... args)`

**Signature:** `static String format(String format, Object... args)`  
**Description:** Returns a formatted string using printf-style format specifiers.

Common format specifiers:
- `%d` — integer
- `%f` — floating point (`%.2f` for 2 decimal places)
- `%s` — string
- `%c` — character
- `%n` — newline (platform-independent)
- `%5d` — right-aligned in width 5

```java
String name = "Alice";
int age = 30;
double score = 95.678;

String result = String.format("Name: %s, Age: %d, Score: %.2f", name, age, score);
System.out.println(result); // Name: Alice, Age: 30, Score: 95.68

// Padding and alignment:
String padded = String.format("|%10s|%-10s|", "right", "left");
System.out.println(padded); // |     right|left      |

// Zero-padding numbers:
String num = String.format("%05d", 42); // "00042"
System.out.println(num); // 00042
```

---

##### `String intern()`

**Signature:** `String intern()`  
**Description:** Returns a canonical representation (from the String pool). If a string equal to this one is already in the pool, returns the pooled reference. Otherwise, adds this string to the pool. Useful for memory deduplication when working with many repeated strings.

```java
String s1 = new String("hello"); // on heap, not in pool
String s2 = new String("hello"); // different heap object
System.out.println(s1 == s2); // false

String i1 = s1.intern(); // adds to pool (or gets existing pool ref)
String i2 = s2.intern(); // same pool ref
System.out.println(i1 == i2); // true
```

---

##### `int hashCode()`

**Signature:** `int hashCode()`  
**Description:** Returns the hash code. Computed as: `s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]`. The same string always returns the same hash. Used internally by HashMap.

```java
String s1 = "hello";
String s2 = "hello";
System.out.println(s1.hashCode()); // 99162322
System.out.println(s2.hashCode()); // 99162322 (same)
System.out.println("hello".hashCode()); // 99162322

// Two different strings CAN have same hashCode (hash collision):
System.out.println("Aa".hashCode()); // 2112
System.out.println("BB".hashCode()); // 2112 — collision!
```

---

##### `boolean matches(String regex)`

**Signature:** `boolean matches(String regex)`  
**Description:** Returns `true` if the **entire** string matches the given regex. Equivalent to `Pattern.matches(regex, str)`.

```java
System.out.println("12345".matches("\\d+"));    // true
System.out.println("12a45".matches("\\d+"));    // false
System.out.println("hello".matches("[a-z]+"));  // true
System.out.println("hello world".matches("[a-z]+")); // false (space not matched)
System.out.println("hello world".matches("[a-z ]+"));// true
```

---

##### `String repeat(int count)` *(Java 11+)*

**Signature:** `String repeat(int count)`  
**Description:** Returns a string that is this string repeated `count` times. Returns empty string if `count == 0`.

```java
System.out.println("ab".repeat(3));    // "ababab"
System.out.println("-".repeat(20));    // "--------------------"
System.out.println("ha".repeat(0));   // ""
// Useful for creating separator lines, padding, etc.
```

---

##### `static String join(CharSequence delimiter, CharSequence... elements)` / `join(CharSequence delimiter, Iterable<? extends CharSequence> elements)`

**Signature:**
- `static String join(CharSequence delimiter, CharSequence... elements)`
- `static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements)`

**Description:** Joins strings with a delimiter between each element.

```java
// Varargs version:
String result = String.join(", ", "apple", "banana", "cherry");
System.out.println(result); // "apple, banana, cherry"

// Iterable version:
List<String> words = Arrays.asList("Java", "is", "awesome");
String sentence = String.join(" ", words);
System.out.println(sentence); // "Java is awesome"

// Path joining:
String path = String.join("/", "usr", "local", "bin");
System.out.println(path); // "usr/local/bin"
```

---

##### `Stream<String> lines()` *(Java 11+)*

**Signature:** `Stream<String> lines()`  
**Description:** Returns a Stream of lines extracted from the string, separated by line terminators (`\n`, `\r`, or `\r\n`).

```java
String text = "line1\nline2\nline3";
text.lines().forEach(System.out::println);
// line1
// line2
// line3

long count = text.lines().count(); // 3

// Filter blank lines:
String multiLine = "hello\n\nworld\n  \njava";
List<String> nonBlank = multiLine.lines()
                                 .filter(l -> !l.isBlank())
                                 .collect(Collectors.toList());
System.out.println(nonBlank); // [hello, world, java]
```

---

##### `byte[] getBytes()`

**Signature:** `byte[] getBytes()`  
**Description:** Encodes the string into bytes using the platform's default charset.

```java
String s = "Hello";
byte[] bytes = s.getBytes();
System.out.println(Arrays.toString(bytes)); // [72, 101, 108, 108, 111]
```

---

##### `byte[] getBytes(String charsetName)` throws `UnsupportedEncodingException`

**Signature:** `byte[] getBytes(String charsetName) throws UnsupportedEncodingException`  
**Description:** Encodes using the specified charset.

```java
try {
    byte[] utf8 = "Hello".getBytes("UTF-8");
    byte[] utf16 = "Hello".getBytes("UTF-16");
    System.out.println(Arrays.toString(utf8));  // [72, 101, 108, 108, 111]
    System.out.println(utf8.length);   // 5
    System.out.println(utf16.length);  // 12 (2 bytes per char + BOM)
} catch (UnsupportedEncodingException e) {
    e.printStackTrace();
}
```

---

### 4.2 `java.lang.StringBuilder` — Complete API

`StringBuilder` is a mutable sequence of characters. Use it for building strings in loops or when you need efficient in-place modification.

#### Constructors

---

##### `StringBuilder()`

**Signature:** `StringBuilder()`  
**Description:** Creates a StringBuilder with no characters and an initial capacity of 16 characters.

```java
StringBuilder sb = new StringBuilder();
System.out.println(sb.capacity()); // 16
System.out.println(sb.length());   // 0
```

---

##### `StringBuilder(int capacity)`

**Signature:** `StringBuilder(int capacity)`  
**Description:** Creates a StringBuilder with the given initial capacity.

```java
StringBuilder sb = new StringBuilder(100); // pre-allocate 100 chars
```

---

##### `StringBuilder(String str)`

**Signature:** `StringBuilder(String str)`  
**Description:** Creates a StringBuilder initialized with the given string. Initial capacity = `str.length() + 16`.

```java
StringBuilder sb = new StringBuilder("Hello");
System.out.println(sb);          // Hello
System.out.println(sb.length()); // 5
System.out.println(sb.capacity()); // 21 (5 + 16)
```

---

##### `StringBuilder(CharSequence seq)`

**Signature:** `StringBuilder(CharSequence seq)`  
**Description:** Creates a StringBuilder from any CharSequence (String, StringBuffer, etc.).

```java
CharSequence cs = "World";
StringBuilder sb = new StringBuilder(cs);
System.out.println(sb); // World
```

---

#### StringBuilder Methods

---

##### `StringBuilder append(String str)` — and all overloads

**Signature (all overloads):**
- `StringBuilder append(String str)`
- `StringBuilder append(boolean b)`
- `StringBuilder append(char c)`
- `StringBuilder append(char[] str)`
- `StringBuilder append(char[] str, int offset, int len)`
- `StringBuilder append(int i)`
- `StringBuilder append(long lng)`
- `StringBuilder append(float f)`
- `StringBuilder append(double d)`
- `StringBuilder append(Object obj)` — calls `toString()`
- `StringBuilder append(CharSequence s)`
- `StringBuilder append(CharSequence s, int start, int end)`
- `StringBuilder append(StringBuilder sb)`

**Description:** Appends the string representation of the argument to this StringBuilder. Returns `this` (enables method chaining). This is the core method — O(1) amortized.

```java
StringBuilder sb = new StringBuilder();
sb.append("Hello")
  .append(' ')
  .append("World")
  .append('!')
  .append(42)
  .append(true);
System.out.println(sb); // Hello World!42true
System.out.println(sb.length()); // 16

// Append char array:
char[] chars = {'J','a','v','a'};
sb.append(chars);
System.out.println(sb); // Hello World!42trueJava

// Append partial char array:
sb.append(chars, 0, 2); // append 'J','a' from index 0, length 2
System.out.println(sb); // Hello World!42trueJavaJa
```

---

##### `StringBuilder insert(int offset, String str)` — and overloads

**Signature (key overloads):**
- `StringBuilder insert(int offset, String str)`
- `StringBuilder insert(int offset, char c)`
- `StringBuilder insert(int offset, int i)`
- `StringBuilder insert(int offset, boolean b)`
- `StringBuilder insert(int offset, char[] str)`
- `StringBuilder insert(int dstOffset, CharSequence s, int start, int end)`

**Description:** Inserts the string representation of the second argument at `offset`. Everything from `offset` onwards is shifted right. O(n) due to shifting.

```java
StringBuilder sb = new StringBuilder("Hello World");
sb.insert(5, " Beautiful");
System.out.println(sb); // Hello Beautiful World

sb.insert(0, ">> "); // insert at beginning
System.out.println(sb); // >> Hello Beautiful World

sb.insert(sb.length(), " <<"); // insert at end (same as append)
System.out.println(sb); // >> Hello Beautiful World <<
```

---

##### `StringBuilder delete(int start, int end)`

**Signature:** `StringBuilder delete(int start, int end)`  
**Description:** Removes characters from `start` (inclusive) to `end` (exclusive). O(n).

```java
StringBuilder sb = new StringBuilder("Hello Beautiful World");
sb.delete(5, 15); // delete " Beautiful" (indices 5 to 14)
System.out.println(sb); // Hello World

sb.delete(0, 5); // delete "Hello"
System.out.println(sb); // " World"
```

---

##### `StringBuilder deleteCharAt(int index)`

**Signature:** `StringBuilder deleteCharAt(int index)`  
**Description:** Removes the character at the given index. O(n).

```java
StringBuilder sb = new StringBuilder("Hello");
sb.deleteCharAt(2); // remove 'l' at index 2
System.out.println(sb); // Helo

// Common pattern: remove last character
sb.deleteCharAt(sb.length() - 1);
System.out.println(sb); // Hel
```

---

##### `StringBuilder replace(int start, int end, String str)`

**Signature:** `StringBuilder replace(int start, int end, String str)`  
**Description:** Replaces characters from `start` (inclusive) to `end` (exclusive) with `str`. The new string can be of different length.

```java
StringBuilder sb = new StringBuilder("Hello World");
sb.replace(6, 11, "Java"); // replace "World" with "Java"
System.out.println(sb); // Hello Java
```

---

##### `StringBuilder reverse()`

**Signature:** `StringBuilder reverse()`  
**Description:** Reverses the character sequence. O(n). Note: handles surrogate pairs correctly.

```java
StringBuilder sb = new StringBuilder("Hello World");
sb.reverse();
System.out.println(sb); // dlroW olleH

// Check palindrome pattern:
String s = "racecar";
String reversed = new StringBuilder(s).reverse().toString();
System.out.println(s.equals(reversed)); // true
```

---

##### `String toString()`

**Signature:** `String toString()`  
**Description:** Returns a String representation of the StringBuilder content.

```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");
String result = sb.toString(); // "Hello World"
System.out.println(result.getClass().getSimpleName()); // String
```

---

##### `int length()`

**Signature:** `int length()`  
**Description:** Returns the number of characters currently stored.

```java
StringBuilder sb = new StringBuilder("Hello");
System.out.println(sb.length()); // 5
sb.append(" World");
System.out.println(sb.length()); // 11
```

---

##### `char charAt(int index)`

**Signature:** `char charAt(int index)`  
**Description:** Returns the char at the given index.

```java
StringBuilder sb = new StringBuilder("Hello");
System.out.println(sb.charAt(1)); // 'e'
System.out.println(sb.charAt(4)); // 'o'
```

---

##### `void setCharAt(int index, char ch)`

**Signature:** `void setCharAt(int index, char ch)`  
**Description:** Sets the char at the given index to `ch`. O(1). This is one of the operations that makes StringBuilder more powerful than String — direct character modification.

```java
StringBuilder sb = new StringBuilder("Hello");
sb.setCharAt(0, 'J'); // change 'H' to 'J'
System.out.println(sb); // Jello

// Useful pattern: toggle case of a char
char c = sb.charAt(1);
sb.setCharAt(1, Character.toUpperCase(c)); // 'e' → 'E'
System.out.println(sb); // JEllo
```

---

##### `int indexOf(String str)` and `int indexOf(String str, int fromIndex)`

**Signature:**
- `int indexOf(String str)`
- `int indexOf(String str, int fromIndex)`

**Description:** Returns the index of the first occurrence of `str`, optionally starting from `fromIndex`. Returns `-1` if not found.

```java
StringBuilder sb = new StringBuilder("Hello World Hello");
System.out.println(sb.indexOf("Hello"));    // 0
System.out.println(sb.indexOf("Hello", 1)); // 12
System.out.println(sb.indexOf("Java"));     // -1
```

---

##### `int lastIndexOf(String str)` and `int lastIndexOf(String str, int fromIndex)`

**Signature:**
- `int lastIndexOf(String str)`
- `int lastIndexOf(String str, int fromIndex)`

**Description:** Returns the index of the last occurrence.

```java
StringBuilder sb = new StringBuilder("Hello World Hello");
System.out.println(sb.lastIndexOf("Hello")); // 12
System.out.println(sb.lastIndexOf("Hello", 10)); // 0 (search backwards from 10)
```

---

##### `String substring(int start)` and `String substring(int start, int end)`

**Signature:**
- `String substring(int start)`
- `String substring(int start, int end)`

**Description:** Returns a substring as a new String.

```java
StringBuilder sb = new StringBuilder("Hello World");
System.out.println(sb.substring(6));     // "World"
System.out.println(sb.substring(0, 5)); // "Hello"
```

---

##### `int capacity()`

**Signature:** `int capacity()`  
**Description:** Returns the current capacity (number of characters that can be stored without resizing). Capacity ≥ length always.

```java
StringBuilder sb = new StringBuilder(); // default capacity 16
System.out.println(sb.capacity()); // 16
sb.append("Hello World!!!!!!!!!"); // 20 chars, exceeds 16
System.out.println(sb.capacity()); // auto-grown: (16+1)*2 = 34, or similar
```

---

##### `void ensureCapacity(int minimumCapacity)`

**Signature:** `void ensureCapacity(int minimumCapacity)`  
**Description:** Ensures the capacity is at least `minimumCapacity`. If current capacity is less, grows to `max(minimumCapacity, (currentCapacity+1)*2)`.

```java
StringBuilder sb = new StringBuilder(); // capacity 16
sb.ensureCapacity(100); // grow to at least 100
System.out.println(sb.capacity()); // 100 (or larger)
```

---

### 4.3 StringBuffer vs StringBuilder — Comparison Table

| Feature | StringBuilder | StringBuffer |
|---------|---------------|--------------|
| **Package** | `java.lang` | `java.lang` |
| **Thread-safe** | ❌ No | ✅ Yes (all methods synchronized) |
| **Performance** | Faster (no synchronization overhead) | Slower |
| **When to use** | Single-threaded code (99% of DSA problems) | Multi-threaded string building |
| **API** | Identical to StringBuffer | Identical to StringBuilder |
| **Introduced** | Java 5 | Java 1.0 |

**Rule of thumb for interviews:** Always use `StringBuilder`. Thread safety is a runtime concern that interviewers don't test via StringBuffer. `StringBuffer` is the old API — `StringBuilder` replaced it for single-threaded code.

```java
// StringBuffer usage (identical API, just thread-safe):
StringBuffer sbf = new StringBuffer("Hello");
sbf.append(" World");
System.out.println(sbf.toString()); // Hello World

// The only time to prefer StringBuffer:
// When multiple threads are building a string (very rare in DSA context)
```

---

### 4.4 String Conversion Methods

Converting between String and primitive types is a fundamental operation in DSA. Here is the complete reference:

#### String → Primitive / Wrapper

```java
// String → int
int i1 = Integer.parseInt("42");         // 42
int i2 = Integer.parseInt("-17");        // -17
int i3 = Integer.parseInt("FF", 16);     // 255 (hex to decimal)
int i4 = Integer.valueOf("42");          // 42 (returns Integer, auto-unboxed)

// String → long
long l1 = Long.parseLong("1234567890123");

// String → double
double d1 = Double.parseDouble("3.14");

// String → boolean
boolean b1 = Boolean.parseBoolean("true");  // true
boolean b2 = Boolean.parseBoolean("True");  // true (case-insensitive)
boolean b3 = Boolean.parseBoolean("yes");   // false (only "true" returns true)

// String → char (no direct method — access the char)
char c1 = "A".charAt(0); // 'A'
```

#### Primitive / Wrapper → String

```java
// int → String (multiple ways)
String s1 = String.valueOf(42);          // "42"
String s2 = Integer.toString(42);        // "42"
String s3 = "" + 42;                     // "42" (string concat, less clean)
String s4 = Integer.toString(255, 16);   // "ff" (decimal to hex)
String s5 = Integer.toBinaryString(10);  // "1010"
String s6 = Integer.toOctalString(8);    // "10"
String s7 = Integer.toHexString(255);    // "ff"

// double → String
String sd = String.valueOf(3.14);        // "3.14"
String sd2 = Double.toString(3.14);      // "3.14"

// char → String
String sc = String.valueOf('A');         // "A"
String sc2 = Character.toString('A');    // "A"

// char[] → String
char[] arr = {'H','e','l','l','o'};
String fromArr = new String(arr);        // "Hello"
String fromArr2 = String.valueOf(arr);   // "Hello"
```

#### Number Formatting

```java
// Format with leading zeros:
String padded = String.format("%05d", 42); // "00042"

// Format decimal places:
String formatted = String.format("%.3f", 3.14159); // "3.142"

// Integer to hex/binary:
System.out.println(Integer.toHexString(255));    // ff
System.out.println(Integer.toBinaryString(10));  // 1010
System.out.println(Integer.toOctalString(8));    // 10

// Parse hex string to int:
int fromHex = Integer.parseInt("ff", 16); // 255
int fromBin = Integer.parseInt("1010", 2); // 10
```

---

## Chapter 5: Patterns — Arrays

> **Before every pattern:** Read the concept, draw the diagram on paper, write the template from memory, then code the example. Shortcuts lead to pattern blindness in interviews.

---

### Pattern 1: Two Pointers (Opposite Direction)

#### 1. Concept

The **Opposite Direction Two Pointers** pattern places one pointer at the start (`left = 0`) and one at the end (`right = n - 1`) of a sorted array (or sometimes any array). Both pointers move toward each other — `left` moves right, `right` moves left — until they meet or cross.

**Why it works:** On a sorted array, if you want to find two elements with a certain sum:
- If the sum is too small, move `left` right (increase the smaller element)
- If the sum is too large, move `right` left (decrease the larger element)
- This eliminates elements systematically without nested loops

This converts what would be an O(n²) brute-force nested loop into an O(n) scan.

#### 2. Trigger Words

Look for these patterns in problem statements:
- "sorted array"
- "find two elements that..."
- "two numbers that sum to target"
- "check if palindrome"
- "remove duplicates"
- "move zeros to end"
- "container with most water"
- "pair with given sum/difference"
- "squares of sorted array"

#### 3. Visual

```text
Sorted Array: [1, 3, 5, 7, 9, 11]
               ↑                ↑
              left             right
              
Target sum = 12

Step 1: arr[left]=1 + arr[right]=11 = 12 ✓ FOUND!

Another example, target = 10:
Step 1: arr[left]=1, arr[right]=11 → sum=12 > 10 → right--
Step 2: arr[left]=1, arr[right]=9  → sum=10 = 10 ✓ FOUND!

Another example, target = 8:
Step 1: arr[left]=1, arr[right]=11 → sum=12 > 8 → right--
Step 2: arr[left]=1, arr[right]=9  → sum=10 > 8 → right--
Step 3: arr[left]=1, arr[right]=7  → sum=8  = 8 ✓ FOUND!

Pointer movement visualized:
[1,  3,  5,  7,  9, 11]
 L→             ←R      (pointers converge until they meet)
```

#### 4. Template

```java
public static int[] twoPointersOpposite(int[] arr, int target) {
    int left = 0;
    int right = arr.length - 1;

    while (left < right) {
        int sum = arr[left] + arr[right];

        if (sum == target) {
            return new int[]{left, right}; // or arr[left], arr[right]
        } else if (sum < target) {
            left++;   // need larger sum → move left pointer right
        } else {
            right--;  // need smaller sum → move right pointer left
        }
    }
    return new int[]{-1, -1}; // no pair found
}
```

#### 5. Worked Example — Two Sum II (LeetCode 167)

**Problem:** Given a 1-indexed sorted array `numbers` and a target, return the indices `[index1, index2]` of the two numbers that add up to target. Exactly one solution guaranteed.

**Thought Process:**
- Array is sorted → Two pointers are natural
- One pointer at start (smallest), one at end (largest)
- If sum too small → move left right (need bigger number)
- If sum too big → move right left (need smaller number)
- Guaranteed solution → loop will always find it

```java
public int[] twoSum(int[] numbers, int target) {
    int left = 0;
    int right = numbers.length - 1;

    while (left < right) {
        int sum = numbers[left] + numbers[right];

        if (sum == target) {
            // Problem is 1-indexed, so add 1 to both
            return new int[]{left + 1, right + 1};
        } else if (sum < target) {
            left++;
        } else {
            right--;
        }
    }
    // Problem guarantees a solution, but compiler needs a return
    return new int[]{-1, -1};
}
```

#### 6. Dry Run

Input: `numbers = [2, 7, 11, 15]`, `target = 9`

| Step | left | right | numbers[left] | numbers[right] | sum | Action |
|------|------|-------|---------------|----------------|-----|--------|
| 1 | 0 | 3 | 2 | 15 | 17 | sum > 9 → right-- |
| 2 | 0 | 2 | 2 | 11 | 13 | sum > 9 → right-- |
| 3 | 0 | 1 | 2 | 7 | 9 | sum == 9 → FOUND! |

**Output:** `[1, 2]` (1-indexed)

#### 7. Time & Space Complexity

- **Time:** O(n) — each element is visited at most once (each pointer moves at most n times total)
- **Space:** O(1) — only two pointer variables, no extra data structure

#### 8. Common Mistakes

1. **Not sorting first:** This pattern only works on sorted arrays. If the problem doesn't mention sorted, you must sort first (adds O(n log n)).
2. **Using `left <= right` instead of `left < right`:** When `left == right`, they're the same element — can't use one element twice. Use `left < right`.
3. **Wrong index adjustment:** When sum < target, move `left++` (not `right--`). Mixing these up gives wrong results.
4. **Off-by-one in 1-indexed problems:** LeetCode 167 is 1-indexed; add 1 to both indices when returning.

#### 9. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 167 — Two Sum II | 🟢 Easy | Basic two pointers on sorted array |
| LeetCode 125 — Valid Palindrome | 🟢 Easy | Pointers from both ends, skip non-alphanumeric |
| LeetCode 977 — Squares of Sorted Array | 🟢 Easy | Largest squares are at ends, work inward |
| LeetCode 15 — 3Sum | 🟡 Medium | Sort + fix one element + two pointers |
| LeetCode 11 — Container With Most Water | 🟡 Medium | Move pointer with smaller height |
| LeetCode 42 — Trapping Rain Water | 🔴 Hard | Track max from left and right simultaneously |

---

### Pattern 2: Two Pointers (Same Direction / Fast-Slow)

#### 1. Concept

The **Same Direction Two Pointers** (also called **Fast-Slow Pointers**) pattern uses two pointers that both start at or near the beginning and move in the **same direction**, but at different speeds or under different conditions.

The **slow pointer** marks the position where the next "valid" element should go. The **fast pointer** scans through all elements to find valid ones. When the fast pointer finds a valid element, it places it at the slow pointer's position, and both advance.

This is primarily used for **in-place array modification** — removing duplicates, moving zeros, removing specific values — without extra space.

#### 2. Trigger Words

- "in-place"
- "remove duplicates"
- "remove element"
- "move zeros to end"
- "compress array"
- "two pointers, both moving forward"
- "overwrite invalid elements"

#### 3. Visual

```text
Problem: Remove duplicates from [1, 1, 2, 3, 3, 4]
Result should be [1, 2, 3, 4] in-place

slow: marks where next unique element goes
fast: scans every element

Initial state:
[1, 1, 2, 3, 3, 4]
 ↑
slow=0, fast=0

Fast scans forward:
fast=1: arr[fast]=1 == arr[slow]=1 → DUPLICATE, skip (fast++)
fast=2: arr[fast]=2 != arr[slow]=1 → UNIQUE, slow++, arr[slow]=arr[fast]
fast=3: arr[fast]=3 != arr[slow]=2 → UNIQUE, slow++, arr[slow]=arr[fast]
fast=4: arr[fast]=3 == arr[slow]=3 → DUPLICATE, skip (fast++)
fast=5: arr[fast]=4 != arr[slow]=3 → UNIQUE, slow++, arr[slow]=arr[fast]

Final: [1, 2, 3, 4, 3, 4] — first 4 elements are the answer
       slow=3, return slow+1 = 4 (length of result)

Visual timeline:
Step 0: [1̲, 1, 2, 3, 3, 4]   slow=0, fast=0
Step 1: [1̲, 1, 2, 3, 3, 4]   slow=0, fast=1 (dup, skip)
Step 2: [1, 2̲, 2, 3, 3, 4]   slow=1, fast=2 (unique, place)
Step 3: [1, 2, 3̲, 3, 3, 4]   slow=2, fast=3 (unique, place)
Step 4: [1, 2, 3̲, 3, 3, 4]   slow=2, fast=4 (dup, skip)
Step 5: [1, 2, 3, 4̲, 3, 4]   slow=3, fast=5 (unique, place)
```

#### 4. Template

```java
// Generic fast-slow template for "keep valid elements in-place"
public int fastSlow(int[] arr) {
    if (arr.length == 0) return 0;

    int slow = 0; // marks position for next valid element

    for (int fast = 0; fast < arr.length; fast++) {
        if (isValid(arr, slow, fast)) { // define your condition here
            arr[slow] = arr[fast];
            slow++;
        }
        // if not valid, fast keeps moving, slow stays
    }
    return slow; // new length of the valid portion
}
```

#### 5. Worked Example — Remove Duplicates from Sorted Array (LeetCode 26)

**Problem:** Given a sorted integer array, remove duplicates in-place. Return the count of unique elements. The first `k` elements of the array should contain the unique values in original order.

```java
public int removeDuplicates(int[] nums) {
    if (nums.length == 0) return 0;

    int slow = 0; // slow points to the last placed unique element

    for (int fast = 1; fast < nums.length; fast++) {
        // If current element is different from the last unique one
        if (nums[fast] != nums[slow]) {
            slow++; // advance slow to the next position
            nums[slow] = nums[fast]; // place the unique element here
        }
        // If nums[fast] == nums[slow], it's a duplicate — skip it (fast advances, slow stays)
    }

    return slow + 1; // number of unique elements (slow is 0-indexed)
}
```

#### 6. Dry Run

Input: `nums = [0, 0, 1, 1, 1, 2, 2, 3, 3, 4]`

| fast | nums[fast] | nums[slow] | Action | slow | Array state |
|------|-----------|-----------|--------|------|-------------|
| 1 | 0 | 0 | Duplicate, skip | 0 | [**0**, 0, 1, 1, 1, 2, 2, 3, 3, 4] |
| 2 | 1 | 0 | Unique: slow++, place | 1 | [0, **1**, 1, 1, 1, 2, 2, 3, 3, 4] |
| 3 | 1 | 1 | Duplicate, skip | 1 | [0, **1**, 1, 1, 1, 2, 2, 3, 3, 4] |
| 4 | 1 | 1 | Duplicate, skip | 1 | [0, **1**, 1, 1, 1, 2, 2, 3, 3, 4] |
| 5 | 2 | 1 | Unique: slow++, place | 2 | [0, 1, **2**, 1, 1, 2, 2, 3, 3, 4] |
| 6 | 2 | 2 | Duplicate, skip | 2 | [0, 1, **2**, 1, 1, 2, 2, 3, 3, 4] |
| 7 | 3 | 2 | Unique: slow++, place | 3 | [0, 1, 2, **3**, 1, 2, 2, 3, 3, 4] |
| 8 | 3 | 3 | Duplicate, skip | 3 | [0, 1, 2, **3**, 1, 2, 2, 3, 3, 4] |
| 9 | 4 | 3 | Unique: slow++, place | 4 | [0, 1, 2, 3, **4**, 2, 2, 3, 3, 4] |

**Return:** `slow + 1 = 5`. The first 5 elements `[0, 1, 2, 3, 4]` are the answer.

#### 7. Time & Space Complexity

- **Time:** O(n) — fast pointer visits every element exactly once
- **Space:** O(1) — in-place, only two integer variables

#### 8. Common Mistakes

1. **Starting fast at 0 instead of 1:** In the "remove duplicates" version, you compare `nums[fast]` with `nums[slow]`. If both start at 0, you compare an element with itself — always equal. Start `fast = 1`.
2. **Returning `slow` instead of `slow + 1`:** `slow` is the 0-based index of the last valid element. The count is `slow + 1`.
3. **Not handling empty array:** Always add a guard: `if (nums.length == 0) return 0;`
4. **Confusing the slow pointer's role:** Slow always points to the LAST PLACED element (or position TO PLACE next, depending on variant). Be consistent.

#### 9. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 26 — Remove Duplicates from Sorted Array | 🟢 Easy | Classic fast-slow |
| LeetCode 27 — Remove Element | 🟢 Easy | Slow pointer for non-target elements |
| LeetCode 283 — Move Zeroes | 🟢 Easy | Slow pointer for non-zero elements |
| LeetCode 80 — Remove Duplicates II (allow ≤2 copies) | 🟡 Medium | Check `nums[fast] != nums[slow-2]` |
| LeetCode 88 — Merge Sorted Array | 🟢 Easy | Two pointers from the END (reverse) |
| LeetCode 344 — Reverse String | 🟢 Easy | Two pointers converging (opposite variant) |

---

### Pattern 3: Sliding Window (Fixed Size)

#### 1. Concept

The **Fixed-Size Sliding Window** pattern maintains a window of exactly `k` elements. As the window slides right by one position: one element enters from the right, one exits from the left.

Instead of recomputing the window's aggregate (sum, max, etc.) from scratch for every position (O(k) per position = O(n×k) total), you **add the incoming element and subtract the outgoing element**. This updates the window in O(1), giving O(n) total.

Think of it like a physical window frame sliding across a row of numbers. You don't look at all numbers through the window every time — you just add what entered and remove what left.

#### 2. Trigger Words

- "subarray of size k"
- "window of size k"
- "exactly k elements"
- "consecutive k elements"
- "maximum/minimum/sum of size k subarray"
- "average of k elements"
- "first negative number in window of size k"

#### 3. Visual

```text
Array: [2, 1, 5, 1, 3, 2]   k = 3

Window slides right, size always = 3:

Position 0: [2, 1, 5] 1, 3, 2    sum = 8
Position 1: 2, [1, 5, 1] 3, 2    sum = 7   (8 - 2 + 1 = 7)
Position 2: 2, 1, [5, 1, 3] 2    sum = 9   (7 - 1 + 3 = 9)
Position 3: 2, 1, 5, [1, 3, 2]   sum = 6   (9 - 5 + 2 = 6)

           outgoing      incoming
              ↓               ↓
  [..., X | a, b, c, d | Y, ...]
             window
  
  New sum = old sum - X + Y
```

#### 4. Template

```java
public static double maxAverage(int[] arr, int k) {
    // Step 1: Compute sum of first window
    int windowSum = 0;
    for (int i = 0; i < k; i++) {
        windowSum += arr[i];
    }

    int maxSum = windowSum;

    // Step 2: Slide the window
    for (int i = k; i < arr.length; i++) {
        windowSum += arr[i];         // add incoming element (right)
        windowSum -= arr[i - k];    // remove outgoing element (left)
        maxSum = Math.max(maxSum, windowSum);
    }

    return (double) maxSum / k;
}
```

#### 5. Worked Example — Maximum Average Subarray of Size k (LeetCode 643)

**Problem:** Given an integer array `nums` and an integer `k`, find the contiguous subarray of length `k` with the maximum average. Return the maximum average.

```java
public double findMaxAverage(int[] nums, int k) {
    // Build first window
    int windowSum = 0;
    for (int i = 0; i < k; i++) {
        windowSum += nums[i];
    }

    int maxSum = windowSum;

    // Slide window from position k to end
    for (int i = k; i < nums.length; i++) {
        windowSum += nums[i];      // new element enters window
        windowSum -= nums[i - k]; // oldest element leaves window
        maxSum = Math.max(maxSum, windowSum);
    }

    return (double) maxSum / k;
}
```

#### 6. Dry Run

Input: `nums = [1, 12, -5, -6, 50, 3]`, `k = 4`

First window (indices 0–3): `1 + 12 + (-5) + (-6) = 2`, `maxSum = 2`

| i | Incoming nums[i] | Outgoing nums[i-k] | windowSum | maxSum |
|---|------------------|--------------------|-----------|--------|
| 4 | 50 | nums[0]=1 | 2 + 50 - 1 = 51 | 51 |
| 5 | 3 | nums[1]=12 | 51 + 3 - 12 = 42 | 51 |

**Return:** `51.0 / 4 = 12.75`

#### 7. Time & Space Complexity

- **Time:** O(n) — one pass to build first window (O(k)), one pass to slide (O(n-k)). Total O(n).
- **Space:** O(1) — only `windowSum` and `maxSum` variables

#### 8. Common Mistakes

1. **Integer overflow:** If elements are large and k is large, `windowSum` can overflow `int`. Use `long` if needed.
2. **Building first window in the slide loop:** Some solutions try to handle the first window in the main loop. It's cleaner to build it separately, then slide.
3. **Off-by-one in outgoing element index:** The outgoing element when `i = k` is `arr[0]`, when `i = k+1` is `arr[1]`, ... in general it's `arr[i-k]`. Double-check this.
4. **Forgetting to initialize with first window result:** If you start tracking `maxSum` only from the second window, you might miss the case where the first window IS the maximum.

#### 9. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 643 — Max Average Subarray I | 🟢 Easy | Direct fixed-window sum |
| LeetCode 1343 — Number of Subarrays of Size k with Average ≥ threshold | 🟡 Medium | Count windows meeting condition |
| "First Negative in Every Window of Size k" | 🟡 Medium | Use deque or track count |
| LeetCode 239 — Sliding Window Maximum | 🔴 Hard | Monotonic deque for window max |
| "Maximum Sum of k Consecutive Elements" | 🟢 Easy | Basic fixed window |
| "Count distinct elements in every window" | 🟡 Medium | HashMap + sliding window |

---

### Pattern 4: Sliding Window (Variable Size)

#### 1. Concept

The **Variable-Size Sliding Window** pattern uses a window whose size is not fixed. The window grows (right pointer advances) to include elements, and shrinks (left pointer advances) when a condition is violated.

The key invariant: the window always satisfies (or just violated) some condition. You expand until violation, then shrink until valid again.

This eliminates the O(n²) brute force of trying every possible subarray, bringing it to O(n).

**Types of variable window problems:**
1. **Find the minimum-length window** that satisfies a condition (shrink aggressively)
2. **Find the maximum-length window** that satisfies a condition (shrink only when violated)

#### 2. Trigger Words

- "minimum length subarray with sum ≥ target"
- "longest subarray with..."
- "smallest subarray whose..."
- "window that contains..."
- "at most k distinct..."
- "without repeating characters"

#### 3. Visual

```text
Problem: Minimum size subarray with sum ≥ 7
Array: [2, 3, 1, 2, 4, 3]

left=0, right=0, windowSum=0, minLen=∞

Expand right until sum ≥ 7, then shrink left:

right=0: sum=2  < 7, expand
right=1: sum=5  < 7, expand
right=2: sum=6  < 7, expand
right=3: sum=8  ≥ 7! window=[0..3]=[2,3,1,2], len=4, minLen=4
  left++: sum=6  < 7, stop shrinking
right=4: sum=10 ≥ 7! window=[1..4]=[3,1,2,4], len=4, minLen=4
  left++: sum=7  ≥ 7! window=[2..4]=[1,2,4], len=3, minLen=3
  left++: sum=6  < 7, stop shrinking
right=5: sum=9  ≥ 7! window=[3..5]=[2,4,3], len=3, minLen=3
  left++: sum=7  ≥ 7! window=[4..5]=[4,3], len=2, minLen=2
  left++: sum=3  < 7, stop shrinking

Answer: minLen = 2 (subarray [4,3])

Window growth/shrink visualization:
[2, 3, 1, 2, 4, 3]
 L  →  →  R          expand
 L  →  R             shrink
    L  →  →  R       expand
    L  →  R          shrink
       L  →  R       shrink again
```

#### 4. Template

```java
// Template 1: Minimum window satisfying condition
public int minWindow(int[] arr, int target) {
    int left = 0;
    int windowSum = 0;
    int minLen = Integer.MAX_VALUE;

    for (int right = 0; right < arr.length; right++) {
        windowSum += arr[right]; // expand window

        // Shrink window from left while condition is still satisfied
        while (windowSum >= target) {
            minLen = Math.min(minLen, right - left + 1);
            windowSum -= arr[left]; // shrink from left
            left++;
        }
    }

    return minLen == Integer.MAX_VALUE ? 0 : minLen;
}

// Template 2: Maximum window satisfying condition
public int maxWindow(int[] arr, int k) {
    int left = 0;
    int result = 0;
    // some data structure to track window state

    for (int right = 0; right < arr.length; right++) {
        // add arr[right] to window state

        // If window violates condition, shrink from left
        while (windowViolatesCondition()) {
            // remove arr[left] from window state
            left++;
        }

        // Window is valid — update result
        result = Math.max(result, right - left + 1);
    }
    return result;
}
```

#### 5. Worked Example — Minimum Size Subarray Sum (LeetCode 209)

**Problem:** Given an array of positive integers and a target, return the minimum length of a contiguous subarray whose sum ≥ target. Return 0 if no such subarray exists.

```java
public int minSubArrayLen(int target, int[] nums) {
    int left = 0;
    int windowSum = 0;
    int minLen = Integer.MAX_VALUE;

    for (int right = 0; right < nums.length; right++) {
        windowSum += nums[right]; // expand: add right element to window

        // As long as sum is sufficient, try to shrink from left
        while (windowSum >= target) {
            // Current window [left, right] is valid — record its length
            int currentLen = right - left + 1;
            minLen = Math.min(minLen, currentLen);

            // Shrink: remove left element from window
            windowSum -= nums[left];
            left++;
        }
        // After while loop: windowSum < target (window is invalid — expand again)
    }

    return minLen == Integer.MAX_VALUE ? 0 : minLen;
}
```

#### 6. Dry Run

Input: `target = 7`, `nums = [2, 3, 1, 2, 4, 3]`

| right | nums[right] | windowSum after add | while condition | Action | left | minLen |
|-------|-------------|---------------------|-----------------|--------|------|--------|
| 0 | 2 | 2 | 2 < 7, skip | — | 0 | ∞ |
| 1 | 3 | 5 | 5 < 7, skip | — | 0 | ∞ |
| 2 | 1 | 6 | 6 < 7, skip | — | 0 | ∞ |
| 3 | 2 | 8 | 8 ≥ 7 → enter while | len=4, minLen=4, sum=8-2=6, left=1 | 1 | 4 |
| | | 6 | 6 < 7 → exit while | — | 1 | 4 |
| 4 | 4 | 10 | 10 ≥ 7 → enter while | len=4, minLen=4, sum=10-3=7, left=2 | 2 | 4 |
| | | 7 | 7 ≥ 7 → stay in while | len=3, minLen=3, sum=7-1=6, left=3 | 3 | 3 |
| | | 6 | 6 < 7 → exit while | — | 3 | 3 |
| 5 | 3 | 9 | 9 ≥ 7 → enter while | len=3, minLen=3, sum=9-2=7, left=4 | 4 | 3 |
| | | 7 | 7 ≥ 7 → stay in while | len=2, minLen=2, sum=7-4=3, left=5 | 5 | 2 |
| | | 3 | 3 < 7 → exit while | — | 5 | 2 |

**Return:** `2` (subarray `[4,3]`)

#### 7. Time & Space Complexity

- **Time:** O(n) — though there's a nested while loop, each element is added to the window exactly once (when `right` passes it) and removed at most once (when `left` passes it). Total operations = 2n → O(n).
- **Space:** O(1) — only `left`, `windowSum`, `minLen` variables

#### 8. Common Mistakes

1. **O(n²) thinking:** Beginners compute `windowSum` from scratch in the outer loop and inner loop. You must reuse the `windowSum` variable — add and subtract.
2. **Using `if` instead of `while` for shrinking:** If you use `if`, you only shrink once per step. You must keep shrinking while the condition holds. Use `while`.
3. **Not handling the case where no valid subarray exists:** Return 0, not `Integer.MAX_VALUE`.
4. **Using this pattern on arrays with negative numbers without modification:** The basic template assumes all elements are positive (so adding to window always increases sum). Negative numbers break the monotonicity assumption.

#### 9. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 209 — Minimum Size Subarray Sum | 🟡 Medium | Classic variable window |
| LeetCode 3 — Longest Substring Without Repeating Characters | 🟡 Medium | HashSet tracks chars in window |
| LeetCode 76 — Minimum Window Substring | 🔴 Hard | HashMap for char frequency |
| LeetCode 904 — Fruit Into Baskets | 🟡 Medium | At most 2 distinct elements |
| LeetCode 424 — Longest Repeating Character Replacement | 🟡 Medium | Track max frequency in window |
| LeetCode 1004 — Max Consecutive Ones III | 🟡 Medium | At most k zeros allowed |

---

### Pattern 5: Prefix Sum

#### 1. Concept

A **Prefix Sum** array `prefix[i]` stores the sum of all elements from index 0 to index `i-1` (or 0 to `i`, depending on convention). Once built, it lets you answer any **range sum query in O(1)** instead of O(n).

**The key identity:**
```
sum(arr[i..j]) = prefix[j+1] - prefix[i]
```
Where `prefix[0] = 0`, `prefix[1] = arr[0]`, `prefix[2] = arr[0] + arr[1]`, etc.

By precomputing the prefix sums in O(n), you can answer thousands of range queries in O(1) each, instead of O(n) per query.

**Extended use:** Prefix sum combined with a HashMap enables finding subarrays with a given sum in O(n) — one of the most powerful patterns in DSA.

#### 2. Trigger Words

- "range sum"
- "sum of elements from i to j"
- "subarray sum"
- "subarray sum equals k"
- "number of subarrays with sum..."
- "prefix"
- "cumulative sum"
- "difference array"

#### 3. Visual

```text
Array:  [3, 1, 4, 1, 5, 9, 2, 6]
Index:   0  1  2  3  4  5  6  7

Prefix Sum Array (prefix[i] = sum of first i elements):
prefix[0] = 0          (empty prefix)
prefix[1] = 3          (just arr[0])
prefix[2] = 4          (arr[0]+arr[1])
prefix[3] = 8
prefix[4] = 9
prefix[5] = 14
prefix[6] = 23
prefix[7] = 25
prefix[8] = 31

Query: sum(arr[2..5]) = sum of {4,1,5,9} = 19
     = prefix[6] - prefix[2]
     = 23 - 4
     = 19 ✓

Formula:
prefix:  [0,  3,  4,  8,  9, 14, 23, 25, 31]
          ↑                   ↑
      prefix[i]           prefix[j+1]
     
sum(i..j) = prefix[j+1] - prefix[i]
```

#### 4. Template

```java
// Build prefix sum array
public static int[] buildPrefix(int[] arr) {
    int n = arr.length;
    int[] prefix = new int[n + 1]; // prefix[0] = 0 (important!)
    for (int i = 0; i < n; i++) {
        prefix[i + 1] = prefix[i] + arr[i];
    }
    return prefix;
}

// Range sum query O(1)
public static int rangeSum(int[] prefix, int i, int j) {
    // sum of arr[i..j] inclusive
    return prefix[j + 1] - prefix[i];
}

// Pattern: Subarray sum equals k using prefix + HashMap
public static int subarraySum(int[] nums, int k) {
    Map<Integer, Integer> prefixCount = new HashMap<>();
    prefixCount.put(0, 1); // empty prefix sum = 0, seen once

    int count = 0;
    int currentSum = 0;

    for (int num : nums) {
        currentSum += num;

        // If (currentSum - k) was seen before, those subarrays sum to k
        int needed = currentSum - k;
        count += prefixCount.getOrDefault(needed, 0);

        // Record this prefix sum
        prefixCount.put(currentSum, prefixCount.getOrDefault(currentSum, 0) + 1);
    }
    return count;
}
```

#### 5. Worked Example — Subarray Sum Equals K (LeetCode 560)

**Problem:** Given an integer array `nums` and an integer `k`, return the total number of subarrays whose sum equals `k`.

**Key Insight:**
If `prefix[j] - prefix[i] = k`, then the subarray `arr[i..j-1]` has sum `k`.
Rearranging: `prefix[i] = prefix[j] - k`.

So for each position `j`, we want to know **how many times** the prefix sum `prefix[j] - k` has appeared earlier. A HashMap tracks this in O(1) per step.

```java
public int subarraySum(int[] nums, int k) {
    // prefixCount: maps prefix sum value → how many times it's been seen
    Map<Integer, Integer> prefixCount = new HashMap<>();
    prefixCount.put(0, 1); // "empty prefix" has sum 0, occurs once

    int count = 0;
    int currentSum = 0;

    for (int i = 0; i < nums.length; i++) {
        currentSum += nums[i]; // running prefix sum

        // How many previous prefix sums equal (currentSum - k)?
        // Each such prefix sum creates a subarray ending at i with sum k
        int needed = currentSum - k;
        if (prefixCount.containsKey(needed)) {
            count += prefixCount.get(needed);
        }

        // Update the map with current prefix sum
        prefixCount.put(currentSum, prefixCount.getOrDefault(currentSum, 0) + 1);
    }

    return count;
}
```

#### 6. Dry Run

Input: `nums = [1, 1, 1]`, `k = 2`

Initial: `prefixCount = {0: 1}`, `count = 0`, `currentSum = 0`

| i | nums[i] | currentSum | needed = currentSum-k | prefixCount.get(needed) | count | prefixCount after |
|---|---------|------------|----------------------|------------------------|-------|-------------------|
| 0 | 1 | 1 | 1-2 = -1 | 0 (not found) | 0 | {0:1, 1:1} |
| 1 | 1 | 2 | 2-2 = 0 | 1 (found!) | 1 | {0:1, 1:1, 2:1} |
| 2 | 1 | 3 | 3-2 = 1 | 1 (found!) | 2 | {0:1, 1:1, 2:1, 3:1} |

**Return:** `2` (subarrays: `[1,1]` at indices 0-1 and `[1,1]` at indices 1-2)

#### 7. Time & Space Complexity

**Simple prefix array:**
- Time: O(n) to build, O(1) per query
- Space: O(n) for prefix array

**Subarray Sum Equals K (prefix + HashMap):**
- Time: O(n) — one pass, HashMap operations are O(1) average
- Space: O(n) — HashMap can store up to n+1 distinct prefix sums

#### 8. Common Mistakes

1. **Off-by-one:** `prefix[i]` represents sum of the FIRST `i` elements (0 to i-1). So `prefix[0] = 0`. Range sum is `prefix[j+1] - prefix[i]`, not `prefix[j] - prefix[i-1]`.
2. **Forgetting to initialize HashMap with `{0: 1}`:** This handles subarrays starting from index 0. Without it, you miss subarrays that start at the beginning.
3. **Not using prefix + HashMap:** A common mistake is building the prefix array and then iterating with two loops — still O(n²). The HashMap is what makes it O(n).
4. **Overflow:** When elements can be large, use `long` for `currentSum`.

#### 9. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 303 — Range Sum Query | 🟢 Easy | Direct prefix sum |
| LeetCode 560 — Subarray Sum Equals K | 🟡 Medium | Prefix + HashMap |
| LeetCode 724 — Find Pivot Index | 🟢 Easy | leftSum = totalSum - leftSum - arr[i] |
| LeetCode 525 — Contiguous Array (equal 0s and 1s) | 🟡 Medium | Replace 0 with -1, find sum=0 subarray |
| LeetCode 1248 — Count Number of Nice Subarrays | 🟡 Medium | Prefix of odd counts |
| LeetCode 974 — Subarray Sums Divisible by K | 🟡 Medium | prefix%k pattern |

---

---

### Pattern 6: Kadane's Algorithm (Maximum Subarray Sum)

#### 1. Concept

**Kadane's Algorithm** solves the "maximum subarray sum" problem in O(n). The key insight is a decision made at each element:

> **At index `i`, you choose:** Start a new subarray at `i`, OR extend the subarray ending at `i-1`.

You extend if the existing subarray's sum is positive (it helps). You start fresh if the existing sum is negative (it hurts).

```
maxEndingHere = max(arr[i], maxEndingHere + arr[i])
```

If `maxEndingHere + arr[i] < arr[i]`, it means `maxEndingHere < 0`, so we drop the old subarray and start fresh at `arr[i]`.

We track the global maximum seen so far:
```
maxSoFar = max(maxSoFar, maxEndingHere)
```

The algorithm is a brilliant example of **greedy thinking** — at each step, make the locally optimal choice (extend or restart), which leads to the globally optimal answer.

#### 2. Trigger Words

- "maximum subarray sum"
- "contiguous subarray with largest sum"
- "largest sum of consecutive elements"
- "Best Time to Buy and Sell Stock" (variation)
- "circular array maximum sum" (variation)

#### 3. Visual

```text
Array: [-2, 1, -3, 4, -1, 2, 1, -5, 4]

At each index, decide: extend or restart?

Index: [ -2 ] [  1 ] [ -3 ] [  4 ] [ -1 ] [  2 ] [  1 ] [ -5 ] [  4 ]
maxHere: -2     1     -2     4      3      5      6      1      5
maxSoFar:-2     1      1     4      4      5      6      6      6

Detailed:
i=0: maxHere = max(-2, 0+(-2)) = -2   maxSoFar=-2
i=1: maxHere = max( 1,-2+ 1 )  =  1   maxSoFar= 1
i=2: maxHere = max(-3, 1+(-3)) = -2   maxSoFar= 1
i=3: maxHere = max( 4,-2+ 4 )  =  4   maxSoFar= 4
i=4: maxHere = max(-1, 4+(-1)) =  3   maxSoFar= 4
i=5: maxHere = max( 2, 3+ 2 )  =  5   maxSoFar= 5
i=6: maxHere = max( 1, 5+ 1 )  =  6   maxSoFar= 6 ← answer!
i=7: maxHere = max(-5, 6+(-5)) =  1   maxSoFar= 6
i=8: maxHere = max( 4, 1+ 4 )  =  5   maxSoFar= 6

Optimal subarray: [4, -1, 2, 1] with sum 6
```

#### 4. Template

```java
// Basic Kadane's — returns maximum subarray sum
public static int maxSubArray(int[] nums) {
    int maxEndingHere = nums[0]; // max subarray sum ending at current index
    int maxSoFar = nums[0];      // global maximum

    for (int i = 1; i < nums.length; i++) {
        // Extend existing subarray or start fresh at nums[i]
        maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
        // Update global maximum
        maxSoFar = Math.max(maxSoFar, maxEndingHere);
    }
    return maxSoFar;
}

// Extended: also track the subarray indices
public static int[] maxSubArrayWithIndices(int[] nums) {
    int maxEndingHere = nums[0];
    int maxSoFar = nums[0];
    int start = 0, end = 0;     // indices of max subarray
    int tempStart = 0;           // start of current candidate subarray

    for (int i = 1; i < nums.length; i++) {
        if (nums[i] > maxEndingHere + nums[i]) {
            // Start fresh
            maxEndingHere = nums[i];
            tempStart = i;
        } else {
            // Extend
            maxEndingHere += nums[i];
        }

        if (maxEndingHere > maxSoFar) {
            maxSoFar = maxEndingHere;
            start = tempStart;
            end = i;
        }
    }
    return new int[]{maxSoFar, start, end};
}
```

#### 5. Worked Example — Maximum Subarray (LeetCode 53)

**Problem:** Given an integer array, find the contiguous subarray (containing at least one number) which has the largest sum and return its sum.

```java
public int maxSubArray(int[] nums) {
    int maxEndingHere = nums[0];
    int maxSoFar = nums[0];

    for (int i = 1; i < nums.length; i++) {
        // Core decision: start fresh or extend?
        maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
        maxSoFar = Math.max(maxSoFar, maxEndingHere);
    }
    return maxSoFar;
}
```

#### 6. Dry Run

Input: `nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]`

| i | nums[i] | maxEndingHere + nums[i] | maxEndingHere (max of above) | maxSoFar |
|---|---------|------------------------|------------------------------|----------|
| 0 | -2 | — (first element) | -2 | -2 |
| 1 | 1 | -2+1 = -1 | max(1, -1) = **1** | 1 |
| 2 | -3 | 1+(-3) = -2 | max(-3, -2) = **-2** | 1 |
| 3 | 4 | -2+4 = 2 | max(4, 2) = **4** | 4 |
| 4 | -1 | 4+(-1) = 3 | max(-1, 3) = **3** | 4 |
| 5 | 2 | 3+2 = 5 | max(2, 5) = **5** | 5 |
| 6 | 1 | 5+1 = 6 | max(1, 6) = **6** | **6** |
| 7 | -5 | 6+(-5) = 1 | max(-5, 1) = **1** | 6 |
| 8 | 4 | 1+4 = 5 | max(4, 5) = **5** | 6 |

**Return:** `6`

#### 7. Time & Space Complexity

- **Time:** O(n) — single pass through the array
- **Space:** O(1) — only two variables, no extra memory

#### 8. Variation: Maximum Product Subarray (LeetCode 152)

Products are trickier than sums because of **negative numbers**: a negative × negative = positive. So the minimum product so far can become the maximum when multiplied by a negative number.

**Key insight:** Track both the maximum AND minimum product ending at current position.

```java
public int maxProduct(int[] nums) {
    int maxHere = nums[0];
    int minHere = nums[0]; // need this for negative × negative case
    int maxSoFar = nums[0];

    for (int i = 1; i < nums.length; i++) {
        // When nums[i] is negative, max and min swap roles
        int tempMax = maxHere;
        maxHere = Math.max(nums[i], Math.max(maxHere * nums[i], minHere * nums[i]));
        minHere = Math.min(nums[i], Math.min(tempMax * nums[i], minHere * nums[i]));
        maxSoFar = Math.max(maxSoFar, maxHere);
    }
    return maxSoFar;
}
```

#### 9. Common Mistakes

1. **Starting `maxEndingHere` at 0 instead of `nums[0]`:** If all elements are negative, the answer is the largest negative number (not 0). Always start with `nums[0]`.
2. **Starting the loop at `i = 0` and re-processing index 0:** Initialize with index 0 outside the loop, start the loop at `i = 1`.
3. **Confusing "maximum ending here" with "maximum so far":** These are different. `maxEndingHere` can decrease; `maxSoFar` never does.
4. **Forgetting to handle the single-element array:** The template handles it correctly — `maxEndingHere = maxSoFar = nums[0]`, loop doesn't execute.

#### 10. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 53 — Maximum Subarray | 🟢 Easy | Classic Kadane's |
| LeetCode 152 — Maximum Product Subarray | 🟡 Medium | Track min and max both |
| LeetCode 918 — Maximum Sum Circular Subarray | 🟡 Medium | Max of (linear max, total - min subarray) |
| LeetCode 121 — Best Time to Buy and Sell Stock | 🟢 Easy | Track minPrice so far |
| LeetCode 1749 — Maximum Absolute Sum of Any Subarray | 🟡 Medium | Max(maxSubarraySum, -minSubarraySum) |

---

### Pattern 7: Dutch National Flag (3-Way Partition)

#### 1. Concept

The **Dutch National Flag** (DNF) algorithm, invented by Edsger Dijkstra, partitions an array into three sections in a **single pass** with O(1) extra space. The three sections typically represent three categories of elements (e.g., 0s, 1s, 2s, or elements less than, equal to, and greater than a pivot).

Three pointers are used:
- `low`: everything before `low` is in group 1 (e.g., all 0s)
- `mid`: the current element being examined
- `high`: everything after `high` is in group 3 (e.g., all 2s)
- Between `low` and `high` (exclusive) are group 2 elements (1s) and unexamined elements

The invariant maintained: `[0..low-1]` = all 0s, `[low..mid-1]` = all 1s, `[high+1..n-1]` = all 2s, `[mid..high]` = unexamined.

#### 2. Trigger Words

- "sort 0s, 1s, and 2s"
- "three-way partition"
- "sort colors"
- "Dutch national flag"
- "partition into three groups"
- "move elements of 3 types to their sections"

#### 3. Visual

```text
Initial: [2, 0, 1, 2, 1, 0]
          ↑           ↑
         mid          high     low=0

Goal: [0s | 1s | 2s]

Invariants at each step:
[0..low-1] = 0s (done)
[low..mid-1] = 1s (done)
[mid..high] = unknown (to process)
[high+1..n-1] = 2s (done)

Step visualization:
Start: [2, 0, 1, 2, 1, 0]  low=0, mid=0, high=5
       mid=0, arr[mid]=2 → swap(mid,high), high--
       [0, 0, 1, 2, 1, 2]  low=0, mid=0, high=4
       mid=0, arr[mid]=0 → swap(mid,low), low++, mid++
       [0, 0, 1, 2, 1, 2]  low=1, mid=1, high=4
       mid=1, arr[mid]=0 → swap(mid,low), low++, mid++
       [0, 0, 1, 2, 1, 2]  low=2, mid=2, high=4
       mid=2, arr[mid]=1 → mid++
       [0, 0, 1, 2, 1, 2]  low=2, mid=3, high=4
       mid=3, arr[mid]=2 → swap(mid,high), high--
       [0, 0, 1, 1, 2, 2]  low=2, mid=3, high=3
       mid=3, arr[mid]=1 → mid++
       [0, 0, 1, 1, 2, 2]  low=2, mid=4, high=3
       mid(4) > high(3) → DONE!
Result: [0, 0, 1, 1, 2, 2] ✓
```

#### 4. Template

```java
public static void dutchNationalFlag(int[] arr) {
    int low = 0;
    int mid = 0;
    int high = arr.length - 1;

    while (mid <= high) {
        if (arr[mid] == 0) {
            // 0 belongs at the left section
            swap(arr, low, mid);
            low++;
            mid++;
            // Both advance: the element at low (now swapped) was already processed (it was 1)
        } else if (arr[mid] == 1) {
            // 1 is in the correct section (between low and high)
            mid++;
        } else {
            // arr[mid] == 2: belongs at the right section
            swap(arr, mid, high);
            high--;
            // DO NOT increment mid: the element now at mid (swapped from high) hasn't been examined
        }
    }
}

private static void swap(int[] arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}
```

#### 5. Worked Example — Sort Colors (LeetCode 75)

**Problem:** Given an array with values 0, 1, and 2 (representing red, white, and blue), sort them in-place so all 0s come first, then 1s, then 2s. Do it in a single pass with constant space.

```java
public void sortColors(int[] nums) {
    int low = 0;
    int mid = 0;
    int high = nums.length - 1;

    while (mid <= high) {
        switch (nums[mid]) {
            case 0:
                // Swap with low boundary, advance both
                int tmp0 = nums[low]; nums[low] = nums[mid]; nums[mid] = tmp0;
                low++;
                mid++;
                break;
            case 1:
                // Already in correct zone
                mid++;
                break;
            case 2:
                // Swap with high boundary, only advance high (not mid)
                int tmp2 = nums[high]; nums[high] = nums[mid]; nums[mid] = tmp2;
                high--;
                break;
        }
    }
}
```

#### 6. Dry Run

Input: `nums = [2, 0, 2, 1, 1, 0]`

| Step | mid | high | low | nums[mid] | Action | Array |
|------|-----|------|-----|-----------|--------|-------|
| Start | 0 | 5 | 0 | 2 | swap(0,5), high-- | [0, 0, 2, 1, 1, **2**] |
| 1 | 0 | 4 | 0 | 0 | swap(0,0), low++, mid++ | [**0**, 0, 2, 1, 1, 2] |
| 2 | 1 | 4 | 1 | 0 | swap(1,1), low++, mid++ | [0, **0**, 2, 1, 1, 2] |
| 3 | 2 | 4 | 2 | 2 | swap(2,4), high-- | [0, 0, 1, 1, **2**, 2] |
| 4 | 2 | 3 | 2 | 1 | mid++ | [0, 0, **1**, 1, 2, 2] |
| 5 | 3 | 3 | 2 | 1 | mid++ | [0, 0, 1, **1**, 2, 2] |
| 6 | 4 > 3 | — | — | — | STOP (mid > high) | [0, 0, 1, 1, 2, 2] |

**Result:** `[0, 0, 1, 1, 2, 2]`

#### 7. Time & Space Complexity

- **Time:** O(n) — `mid` advances every step except when swapping with `high`, and `high` decreases every time that happens. Total operations bounded by n.
- **Space:** O(1) — only 3 pointer variables

#### 8. Common Mistakes

1. **Incrementing `mid` after swapping with `high`:** When you swap `arr[mid]` with `arr[high]`, the new element at `mid` came from `high` and has not been examined yet. **Don't increment `mid`**.
2. **Incrementing only `mid` when swapping with `low`:** After swapping `arr[mid]` with `arr[low]`, the element now at `mid` was previously at `low`. Since `low..mid-1` is the "1s zone" (already processed), the element that was at `low` must be a 1 (or a 0 if `low==mid`). It's safe to increment both `low` and `mid`.
3. **Loop condition:** Use `mid <= high` (not `mid < high`). The element at `mid == high` still needs to be processed.

#### 9. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 75 — Sort Colors | 🟡 Medium | Classic DNF |
| "Segregate 0s and 1s" | 🟢 Easy | 2-partition (simpler case) |
| LeetCode 905 — Sort Array By Parity | 🟢 Easy | Even left, odd right (2-partition) |
| LeetCode 2161 — Partition Array According to Given Pivot | 🟡 Medium | 3-partition with pivot value |
| LeetCode 283 — Move Zeroes | 🟢 Easy | 2-partition: zeros and non-zeros |

---

### Pattern 8: Cyclic Sort

#### 1. Concept

**Cyclic Sort** is a sorting algorithm designed specifically for arrays that contain **numbers in the range 1 to N** (or 0 to N-1). It places each number at its correct index in a single linear scan.

The key insight: if the array contains numbers 1 to N, then number `k` should be at index `k-1`. If `arr[i]` is not at index `i` (i.e., `arr[i] != i+1`), swap it to its correct position (`arr[arr[i]-1]`).

Once sorted, missing numbers appear as positions where `arr[i] != i+1`.

**Why it's O(n):** Even though there's a while loop inside the for loop, each element is swapped to its correct position at most once. After a swap, either the element at `i` is now correct (loop advances), or a new wrong element arrives but it too will eventually be swapped to its correct position. Total swaps ≤ n.

#### 2. Trigger Words

- "array of size N with numbers 1 to N"
- "find the missing number"
- "find all duplicates"
- "find the duplicate number"
- "numbers from 1 to N (or 0 to N-1)"
- "each number appears once except..."
- "place each number at index i-1"

#### 3. Visual

```text
Array: [3, 1, 5, 4, 2]  (contains 1..5, should sort to [1,2,3,4,5])

Step 1: i=0, arr[0]=3, correct index = 3-1 = 2
        arr[0] != arr[2] → swap(0, 2)
        [5, 1, 3, 4, 2]  now arr[0]=5, correct index = 4
        arr[0] != arr[4] → swap(0, 4)
        [2, 1, 3, 4, 5]  now arr[0]=2, correct index = 1
        arr[0] != arr[1] → swap(0, 1)
        [1, 2, 3, 4, 5]  now arr[0]=1, correct index = 0 ✓ → i++

Step 2: i=1, arr[1]=2, correct index = 1 ✓ → i++
Step 3: i=2, arr[2]=3, correct index = 2 ✓ → i++
Step 4: i=3, arr[3]=4, correct index = 3 ✓ → i++
Step 5: i=4, arr[4]=5, correct index = 4 ✓ → i++

Result: [1, 2, 3, 4, 5]

"Cyclic" because swapping creates cycles:
3 → index 2 → has value 5 → index 4 → has value 2 → index 1 → has value 1 → index 0 (done)
```

#### 4. Template

```java
// Cyclic sort for arrays containing numbers 1 to N
public static void cyclicSort(int[] nums) {
    int i = 0;
    while (i < nums.length) {
        // Where should nums[i] go?
        int correctIndex = nums[i] - 1; // number k belongs at index k-1

        if (nums[i] != nums[correctIndex]) {
            // nums[i] is not at its correct position — swap it there
            int temp = nums[i];
            nums[i] = nums[correctIndex];
            nums[correctIndex] = temp;
            // DON'T increment i — the new element at i might also be wrong
        } else {
            // nums[i] is at its correct position (or it's a duplicate — same value at correct index)
            i++;
        }
    }
}
```

#### 5. Worked Example — Find the Missing Number (LeetCode 268)

**Problem:** Given an array of n distinct numbers in the range [0, n], find the one number in range [0, n] that is missing.

**Approach:** Use cyclic sort. Numbers 0 to n-1 should be at their corresponding index. Number n can't fit (array size is n), so its slot will be the "extra" position. After sorting, find the index where `arr[i] != i`.

```java
public int missingNumber(int[] nums) {
    int i = 0;
    int n = nums.length;

    // Cyclic sort: place each number at index == number
    // (different from 1..N: here 0..N-1, so number k at index k)
    while (i < n) {
        int correctIndex = nums[i]; // number k should be at index k

        if (nums[i] < n && nums[i] != nums[correctIndex]) {
            // nums[i] is in range [0, n-1] and not at correct position
            int temp = nums[i];
            nums[i] = nums[correctIndex];
            nums[correctIndex] = temp;
        } else {
            i++;
        }
    }

    // Find the missing number: the index where arr[i] != i
    for (i = 0; i < n; i++) {
        if (nums[i] != i) {
            return i;
        }
    }
    return n; // missing number is n itself
}
```

#### 6. Dry Run

Input: `nums = [3, 0, 1]`, `n = 3` (range 0..3, one missing)

**Cyclic Sort Phase:**

| i | nums[i] | correctIndex | Condition | Action | Array |
|---|---------|-------------|-----------|--------|-------|
| 0 | 3 | 3 | nums[i]=3 = n → else | i++ | [3,0,1] |
| 1 | 0 | 0 | 0 < 3 && 0 ≠ nums[0]=3 → swap | swap(1,0) | [0,3,1] |
| 1 | 3 | 3 | nums[i]=3 = n → else | i++ | [0,3,1] |
| 2 | 1 | 1 | 1 < 3 && 1 = nums[1]=3? No → swap | swap(2,1) | [0,1,3] |
| 2 | 3 | 3 | nums[i]=3 = n → else | i++ | [0,1,3] |

After sort: `[0, 1, 3]`

**Find Missing Phase:**

| i | nums[i] | nums[i] == i? |
|---|---------|---------------|
| 0 | 0 | ✓ |
| 1 | 1 | ✓ |
| 2 | 3 | ✗ → return 2 |

**Return:** `2` ✓

#### 7. Time & Space Complexity

- **Time:** O(n) — each element is moved to its correct position at most once. The outer while loop iterates at most 2n times.
- **Space:** O(1) — in-place sorting, only pointer variables

#### 8. Common Mistakes

1. **Swapping when `nums[i] == nums[correctIndex]` (duplicate case):** If both positions have the same value, swapping forever creates an infinite loop. The check `nums[i] != nums[correctIndex]` prevents this — it handles duplicates.
2. **Using `i++` inside the swap branch:** After a swap, the new element at `i` might also be wrong. Don't advance `i` after a swap — let the while loop re-check `i`.
3. **Wrong range handling for 0..N vs 1..N:** For 1..N: `correctIndex = nums[i] - 1`. For 0..N-1: `correctIndex = nums[i]`. Know which range you're dealing with.
4. **Not guarding against out-of-bounds:** For missing/duplicate problems, elements might equal `n` (can't be placed). Guard: `if (nums[i] < n && ...)`.

#### 9. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 268 — Missing Number | 🟢 Easy | Cyclic sort 0..N, find wrong index |
| LeetCode 448 — Find All Numbers Disappeared in Array | 🟢 Easy | Cyclic sort, then scan for mismatches |
| LeetCode 287 — Find the Duplicate Number | 🟡 Medium | Cyclic sort (or Floyd's cycle) |
| LeetCode 442 — Find All Duplicates in Array | 🟡 Medium | After sort, find indices where arr[i]!=i+1 (both i and arr[i]-1 valid) |
| "First Missing Positive" (LC 41) | 🔴 Hard | Cyclic sort 1..N, find first arr[i]!=i+1 |

---

### Pattern 9: Binary Search on Answer

#### 1. Concept

**Binary Search on Answer** is a powerful technique for optimization problems where:
1. The answer lies within a **known range** `[lo, hi]`
2. There is a **monotonic property**: if answer `m` is feasible, all answers `≥ m` (or `≤ m`) are also feasible
3. You can write a **`canAchieve(mid)`** function to check feasibility

Instead of searching for an element in an array, you search for the **optimal value** in the answer space. For each candidate mid-value, you check if it's achievable. Binary search finds the optimal in O(log(range)) calls to `canAchieve`, each of which costs O(n).

**Total time:** O(n log(range)) — far better than brute-force O(n × range).

#### 2. Trigger Words

- "minimize the maximum"
- "maximize the minimum"
- "minimum number of days/operations to..."
- "what is the minimum speed/rate/capacity..."
- "given a constraint, find the optimal value"
- "koko eating bananas"
- "split array"
- "ship packages"
- "allocate books"

#### 3. Visual

```text
Problem: Koko eats bananas. Piles = [3,6,7,11]. She has 8 hours.
What's the minimum eating speed k such that she can finish in time?

Answer space: k can range from 1 to max(piles) = 11

lo=1, hi=11 (the search space for k)

Is k feasible? canEat(k) = sum(ceil(pile/k) for each pile) <= H

k=1:  ceil(3/1)+ceil(6/1)+ceil(7/1)+ceil(11/1) = 3+6+7+11 = 27 > 8 → NO
k=6:  ceil(3/6)+ceil(6/6)+ceil(7/6)+ceil(11/6) = 1+1+2+2 = 6 ≤ 8 → YES!
k=3:  ceil(3/3)+ceil(6/3)+ceil(7/3)+ceil(11/3) = 1+2+3+4 = 10 > 8 → NO
k=4:  ceil(3/4)+ceil(6/4)+ceil(7/4)+ceil(11/4) = 1+2+2+3 = 8 ≤ 8 → YES!
k=4 is feasible. Can we do k=3? NO. So answer = 4.

Binary search on k:
lo=1, hi=11
mid=6 → feasible → hi=6 (try smaller k)
lo=1, hi=6, mid=3 → NOT feasible → lo=4
lo=4, hi=6, mid=5 → feasible → hi=5
lo=4, hi=5, mid=4 → feasible → hi=4
lo=4, hi=4 → STOP, answer = lo = 4

Search space reduction:
[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
          ↑            ↑
         lo           hi
           ↓ binary search narrows it
         [4] ← answer
```

#### 4. Template

```java
// Template: Binary Search on Answer
// Problem: Find minimum k such that canAchieve(k) is true
public int binarySearchOnAnswer(int[] arr, int H) {
    int lo = 1;                    // minimum possible answer
    int hi = getMaxPossible(arr);  // maximum possible answer

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;  // prevent overflow

        if (canAchieve(arr, mid, H)) {
            // mid is feasible — try to find something smaller (minimize)
            hi = mid;
        } else {
            // mid is not feasible — must use larger value
            lo = mid + 1;
        }
    }
    return lo; // lo == hi at termination, this is the answer
}

private boolean canAchieve(int[] arr, int mid, int H) {
    // Check if mid is feasible for the given constraint H
    // Implementation depends on the specific problem
    int totalOperations = 0;
    for (int val : arr) {
        totalOperations += (val + mid - 1) / mid; // ceiling division: ceil(val/mid)
    }
    return totalOperations <= H;
}
```

#### 5. Worked Example — Koko Eating Bananas (LeetCode 875)

**Problem:** Koko has piles of bananas. She can eat at speed k (bananas per hour). She has H hours. What is the minimum integer k such that she can eat all bananas within H hours?

```java
public int minEatingSpeed(int[] piles, int h) {
    int lo = 1;
    int hi = 0;
    for (int pile : piles) hi = Math.max(hi, pile); // hi = max pile

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;

        if (canFinish(piles, mid, h)) {
            hi = mid; // mid works, try smaller speed
        } else {
            lo = mid + 1; // mid doesn't work, need faster speed
        }
    }
    return lo;
}

private boolean canFinish(int[] piles, int speed, int h) {
    int hoursNeeded = 0;
    for (int pile : piles) {
        hoursNeeded += (pile + speed - 1) / speed; // ceiling division
        // Equivalent to: Math.ceil((double) pile / speed)
    }
    return hoursNeeded <= h;
}
```

#### 6. Dry Run

Input: `piles = [3, 6, 7, 11]`, `h = 8`

Initial: `lo = 1`, `hi = 11`

| Iteration | lo | hi | mid | canFinish(piles, mid, 8) | Action |
|-----------|----|----|-----|--------------------------|--------|
| 1 | 1 | 11 | 6 | ceil(3/6)+ceil(6/6)+ceil(7/6)+ceil(11/6)=1+1+2+2=6 ≤ 8 ✓ | hi=6 |
| 2 | 1 | 6 | 3 | ceil(3/3)+ceil(6/3)+ceil(7/3)+ceil(11/3)=1+2+3+4=10 > 8 ✗ | lo=4 |
| 3 | 4 | 6 | 5 | ceil(3/5)+ceil(6/5)+ceil(7/5)+ceil(11/5)=1+2+2+3=8 ≤ 8 ✓ | hi=5 |
| 4 | 4 | 5 | 4 | ceil(3/4)+ceil(6/4)+ceil(7/4)+ceil(11/4)=1+2+2+3=8 ≤ 8 ✓ | hi=4 |
| 5 | 4 | 4 | — | lo==hi → STOP | — |

**Return:** `4`

#### 7. Time & Space Complexity

- **Time:** O(n log m) where `n = piles.length` and `m = max pile size`. Binary search over range 1..m takes O(log m) iterations, each calling `canFinish` in O(n).
- **Space:** O(1)

#### 8. Common Mistakes

1. **Infinite loop with `lo = mid` instead of `lo = mid + 1`:** When `lo < hi` and `canAchieve` is false, set `lo = mid + 1` (not `lo = mid`). If you set `lo = mid`, `mid = (lo+hi)/2 = lo` again when `hi = lo+1`, creating an infinite loop.
2. **Integer overflow in mid calculation:** Use `mid = lo + (hi - lo) / 2` instead of `mid = (lo + hi) / 2`. If `lo` and `hi` are large ints, `(lo + hi)` overflows.
3. **Wrong answer space boundaries:** Identify `lo` and `hi` correctly. For Koko, `lo = 1` (minimum speed), `hi = max(piles)` (at max speed, each pile takes 1 hour).
4. **Ceiling division error:** `ceil(a/b)` in integers is `(a + b - 1) / b`. Forgetting this gives wrong `hoursNeeded`.

#### 9. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 875 — Koko Eating Bananas | 🟡 Medium | Min speed, answer range 1..max(piles) |
| LeetCode 1011 — Capacity to Ship Packages | 🟡 Medium | Min capacity, range max(weight)..sum(weights) |
| LeetCode 410 — Split Array Largest Sum | 🔴 Hard | Min largest sum, range max(arr)..sum(arr) |
| LeetCode 1482 — Minimum Number of Days to Make m Bouquets | 🟡 Medium | Binary search on days |
| LeetCode 1283 — Find the Smallest Divisor | 🟡 Medium | Sum/divisor <= threshold |
| "Allocate Minimum Pages" (GFG) | 🔴 Hard | Min max pages, binary search |

---

### Pattern 10: Matrix Traversal

Matrix traversal problems require moving through a 2D grid in specific patterns. The three most common patterns are spiral traversal, diagonal traversal, and layer-by-layer (rotation).

---

#### 10A: Spiral Traversal

##### Concept

Traverse the matrix in a spiral order: left→right along top row, top→bottom along right column, right→left along bottom row, bottom→top along left column, then move inward and repeat.

Maintain four boundaries: `top`, `bottom`, `left`, `right`. After traversing each edge, shrink the boundary.

##### Visual

```text
Matrix:
[1,  2,  3,  4 ]
[5,  6,  7,  8 ]
[9,  10, 11, 12]
[13, 14, 15, 16]

Spiral order:
→ 1, 2, 3, 4        (top row, left→right)
↓ 8, 12, 16         (right col, top→bottom)
← 15, 14, 13        (bottom row, right→left)
↑ 9, 5              (left col, bottom→top)
→ 6, 7              (next top row)
↓ 11                (next right col)
← 10                (next bottom row)
Result: [1,2,3,4,8,12,16,15,14,13,9,5,6,7,11,10]

Boundary shrink:
Layer 1: top=0,bottom=3,left=0,right=3
After traversal: top=1,bottom=2,left=1,right=2
Layer 2: top=1,bottom=2,left=1,right=2
...
```

##### Code

```java
public List<Integer> spiralOrder(int[][] matrix) {
    List<Integer> result = new ArrayList<>();
    if (matrix == null || matrix.length == 0) return result;

    int top = 0, bottom = matrix.length - 1;
    int left = 0, right = matrix[0].length - 1;

    while (top <= bottom && left <= right) {
        // Traverse top row (left → right)
        for (int col = left; col <= right; col++) {
            result.add(matrix[top][col]);
        }
        top++; // top row done, shrink top boundary

        // Traverse right column (top → bottom)
        for (int row = top; row <= bottom; row++) {
            result.add(matrix[row][right]);
        }
        right--; // right column done, shrink right boundary

        // Traverse bottom row (right → left) — only if rows remain
        if (top <= bottom) {
            for (int col = right; col >= left; col--) {
                result.add(matrix[bottom][col]);
            }
            bottom--; // bottom row done, shrink bottom boundary
        }

        // Traverse left column (bottom → top) — only if columns remain
        if (left <= right) {
            for (int row = bottom; row >= top; row--) {
                result.add(matrix[row][left]);
            }
            left++; // left column done, shrink left boundary
        }
    }
    return result;
}
```

##### Dry Run

For a 3×3 matrix `[[1,2,3],[4,5,6],[7,8,9]]`:

| Phase | Boundary | Elements Added |
|-------|----------|----------------|
| Top row L→R | top=0,bot=2,left=0,right=2 | 1,2,3; top→1 |
| Right col T→B | top=1,bot=2,left=0,right=2 | 6,9; right→1 |
| Bottom row R→L | top=1,bot=2,left=0,right=1 | 8,7; bot→1 |
| Left col B→T | top=1,bot=1,left=0,right=1 | 4; left→1 |
| Top row L→R | top=1,bot=1,left=1,right=1 | 5; top→2 |
| Loop exits: top(2) > bot(1) | — | — |

Result: `[1,2,3,6,9,8,7,4,5]` ✓

**Complexity:** Time O(m×n), Space O(1) excluding output

---

#### 10B: Diagonal Traversal

##### Concept

Traverse all diagonals of the matrix, alternating direction (up-right, then down-left).

```java
public int[] findDiagonalOrder(int[][] matrix) {
    if (matrix == null || matrix.length == 0) return new int[0];
    int m = matrix.length, n = matrix[0].length;
    int[] result = new int[m * n];
    int idx = 0;
    int row = 0, col = 0;
    boolean goingUp = true;

    while (idx < m * n) {
        result[idx++] = matrix[row][col];

        if (goingUp) {
            // Moving up-right: row--, col++
            if (col == n - 1) {
                // Hit right wall: go down
                row++;
                goingUp = false;
            } else if (row == 0) {
                // Hit top wall: go right
                col++;
                goingUp = false;
            } else {
                row--;
                col++;
            }
        } else {
            // Moving down-left: row++, col--
            if (row == m - 1) {
                // Hit bottom wall: go right
                col++;
                goingUp = true;
            } else if (col == 0) {
                // Hit left wall: go down
                row++;
                goingUp = true;
            } else {
                row++;
                col--;
            }
        }
    }
    return result;
}
```

**Complexity:** Time O(m×n), Space O(1) excluding output

---

#### 10C: Layer-by-Layer — Rotate Matrix 90° Clockwise

##### Concept

To rotate a matrix 90° clockwise in-place:
1. **Transpose** the matrix (swap `matrix[i][j]` with `matrix[j][i]`)
2. **Reverse each row**

These two operations together achieve a 90° clockwise rotation.

##### Visual

```text
Original:        After Transpose:    After Row Reverse:
1 2 3            1 4 7               7 4 1
4 5 6    →       2 5 8      →        8 5 2
7 8 9            3 6 9               9 6 3

(= 90° clockwise rotation)
```

##### Code

```java
public void rotate(int[][] matrix) {
    int n = matrix.length;

    // Step 1: Transpose (swap matrix[i][j] with matrix[j][i])
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) { // j starts at i+1 to avoid double-swap
            int temp = matrix[i][j];
            matrix[i][j] = matrix[j][i];
            matrix[j][i] = temp;
        }
    }

    // Step 2: Reverse each row
    for (int i = 0; i < n; i++) {
        int left = 0, right = n - 1;
        while (left < right) {
            int temp = matrix[i][left];
            matrix[i][left] = matrix[i][right];
            matrix[i][right] = temp;
            left++;
            right--;
        }
    }
}
```

##### Dry Run

Input: `[[1,2,3],[4,5,6],[7,8,9]]`

After Transpose:
```
[1, 4, 7]
[2, 5, 8]
[3, 6, 9]
```

After Reverse each row:
```
[7, 4, 1]
[8, 5, 2]
[9, 6, 3]
```

**Result:** 90° clockwise rotation ✓

**Complexity:** Time O(n²), Space O(1) — in-place

---

#### Practice Problems for Matrix Traversal

| Problem | Difficulty | Pattern |
|---------|------------|---------|
| LeetCode 54 — Spiral Matrix | 🟡 Medium | Spiral order traversal |
| LeetCode 59 — Spiral Matrix II | 🟡 Medium | Fill matrix in spiral order |
| LeetCode 498 — Diagonal Traverse | 🟡 Medium | Alternating diagonal |
| LeetCode 48 — Rotate Image | 🟡 Medium | Transpose + reverse |
| LeetCode 73 — Set Matrix Zeroes | 🟡 Medium | In-place matrix modification |
| LeetCode 74 — Search a 2D Matrix | 🟡 Medium | Treat as sorted 1D array |

---

## Chapter 6: Patterns — Strings

---

### Pattern 11: String Two Pointers & Palindrome

#### 1. Concept

Just like the array two-pointer technique, **String Two Pointers** places one pointer at each end of the string and moves them toward each other. For palindrome problems, the check is: `s[left] == s[right]` at every step. If they differ, the string is not a palindrome.

**Expand-Around-Center** is the palindrome variant that works for finding the longest palindromic substring. Instead of converging, you start at a center and expand outward as long as characters match.

#### 2. Trigger Words

- "palindrome"
- "valid palindrome"
- "is this string a palindrome?"
- "longest palindromic substring"
- "check if string can be palindrome"
- "remove characters to make palindrome"

#### 3. Visual

```text
Valid Palindrome Check: "racecar"

  l                 r
  ↓                 ↓
[ r, a, c, e, c, a, r ]
   r == r ✓ → l++, r--

     l           r
     ↓           ↓
[ r, a, c, e, c, a, r ]
     a == a ✓ → l++, r--

       l       r
       ↓       ↓
[ r, a, c, e, c, a, r ]
       c == c ✓ → l++, r--

         l   r
         ↓   ↓
[ r, a, c, e, c, a, r ]
         e == e ✓ → l++, r--
         l > r → PALINDROME ✓

Expand-Around-Center for "abacaba":
Center at index 3 ('c'):
  Expand: 'b'=='b' ✓ → expand
  Expand: 'a'=='a' ✓ → expand
  Expand: left<0 → stop
  Length = 7 (entire string)

Center between indices 3,4 (even-length):
  'c' != 'a' → stop immediately
  Length = 0
```

#### 4. Template

```java
// Check if entire string is a palindrome (ignoring non-alphanumeric)
public boolean isPalindrome(String s) {
    int left = 0, right = s.length() - 1;

    while (left < right) {
        // Skip non-alphanumeric characters
        while (left < right && !Character.isLetterOrDigit(s.charAt(left))) left++;
        while (left < right && !Character.isLetterOrDigit(s.charAt(right))) right--;

        if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
            return false;
        }
        left++;
        right--;
    }
    return true;
}

// Find longest palindromic substring (expand around center)
public String longestPalindrome(String s) {
    int start = 0, maxLen = 1;

    for (int center = 0; center < s.length(); center++) {
        // Odd-length palindromes (single character center)
        int len1 = expandAroundCenter(s, center, center);
        // Even-length palindromes (two character center)
        int len2 = expandAroundCenter(s, center, center + 1);
        int len = Math.max(len1, len2);

        if (len > maxLen) {
            maxLen = len;
            start = center - (len - 1) / 2;
        }
    }
    return s.substring(start, start + maxLen);
}

private int expandAroundCenter(String s, int left, int right) {
    while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
        left--;
        right++;
    }
    // When loop exits: s[left] != s[right] (or out of bounds)
    // The palindrome length is right - left - 1
    return right - left - 1;
}
```

#### 5. Worked Example — Valid Palindrome (LeetCode 125)

**Problem:** A phrase is a palindrome if, after converting all uppercase to lowercase and removing all non-alphanumeric characters, it reads the same forward and backward.

```java
public boolean isPalindrome(String s) {
    int left = 0, right = s.length() - 1;

    while (left < right) {
        // Advance left past non-alphanumeric
        while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
            left++;
        }
        // Advance right past non-alphanumeric
        while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
            right--;
        }

        // Compare characters (case-insensitive)
        if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
            return false; // mismatch → not a palindrome
        }
        left++;
        right--;
    }
    return true; // all chars matched
}
```

#### 6. Dry Run

Input: `s = "A man, a plan, a canal: Panama"`

Cleaned (conceptually): `"amanaplanacanalpanama"` (21 chars)

| Step | left char | right char | Match? | Action |
|------|-----------|------------|--------|--------|
| 1 | 'A'→'a' | 'a' | ✓ | l++, r-- |
| 2 | 'm' | 'm' | ✓ | l++, r-- |
| 3 | 'a' | 'a' | ✓ | l++, r-- |
| 4 | 'n' | 'n' | ✓ | l++, r-- |
| 5 | 'a' | 'a' | ✓ | l++, r-- |
| ... | ... | ... | ✓ | continues |
| end | left ≥ right | — | — | return true |

**Return:** `true`

#### 7. Time & Space Complexity

- **Time:** O(n) — each character visited at most once by each pointer
- **Space:** O(1) — only pointer variables (no extra string created)

#### 8. Longest Palindromic Substring — Dry Run

Input: `s = "babad"`

| center | Odd expand (c,c) | Even expand (c,c+1) | Best at this center |
|--------|-----------------|---------------------|---------------------|
| 0 'b' | expand('b','b')→'b', len=1 | expand('b','a')→mismatch, len=0 | 1 |
| 1 'a' | 'a'→'bab'→len=3 | 'a','b'→mismatch, len=0 | 3 (start=0) |
| 2 'b' | 'b'→'aba'→len=3 | 'b','a'→mismatch, len=0 | 3 (start=1) |
| 3 'a' | 'a'→len=1 | 'a','d'→mismatch, len=0 | 1 |
| 4 'd' | 'd'→len=1 | out of bounds, len=0 | 1 |

MaxLen = 3 (either "bab" or "aba" — both valid)

**Return:** `"bab"` (first longest found)

**Complexity:** Time O(n²), Space O(1)

#### 9. Common Mistakes

1. **Not skipping non-alphanumeric:** LeetCode 125 requires skipping spaces and punctuation. Use `Character.isLetterOrDigit()`.
2. **Case sensitivity:** Use `Character.toLowerCase()` before comparing.
3. **Even-length palindromes:** Always check BOTH odd (single center) and even (pair center) in expand-around-center. Missing even-length check misses palindromes like "abba".
4. **Off-by-one in expand:** When expand exits, `right - left - 1` is the length. Don't write `right - left + 1`.

#### 10. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 125 — Valid Palindrome | 🟢 Easy | Two pointers, skip non-alnum |
| LeetCode 5 — Longest Palindromic Substring | 🟡 Medium | Expand around center |
| LeetCode 680 — Valid Palindrome II | 🟢 Easy | Allow one deletion |
| LeetCode 647 — Palindromic Substrings | 🟡 Medium | Count all palindromes with expand |
| LeetCode 234 — Palindrome Linked List | 🟢 Easy | Find middle, reverse, compare |
| LeetCode 131 — Palindrome Partitioning | 🟡 Medium | Backtracking + palindrome check |

---

### Pattern 12: Frequency Map / Anagram Pattern

#### 1. Concept

The **Frequency Map** pattern uses either a fixed-size `int[26]` array (for lowercase letters) or a `HashMap<Character, Integer>` to count character frequencies. It's the backbone of anagram detection, permutation checks, and character-count comparison problems.

**Why int[26] instead of HashMap for lowercase letters?**
- `int[26]` has O(26) = O(1) space — fixed, regardless of string length
- No boxing overhead (char → Character)
- Cache-friendly (contiguous memory)
- Simpler code

The pattern is: count frequencies of one string, then "consume" them by processing the second string (increment for one, decrement for the other). If all counts are zero at the end → anagram.

#### 2. Trigger Words

- "anagram"
- "permutation"
- "same characters with same frequency"
- "character count"
- "group by same letters"
- "frequency"
- "rearrange letters"

#### 3. Visual

```text
Anagram check: "listen" vs "silent"

Frequency of "listen": [l:1, i:1, s:1, t:1, e:1, n:1]
int[26] (index = char - 'a'):
 e  i  l  n  s  t
[0, 0, 0, 0, 0, 0, ..., 1, ..., 1, ..., 1, ..., 1, ..., 1, ..., 1, ...]
 a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z

Step 1: Increment for "listen": l+1, i+1, s+1, t+1, e+1, n+1
Step 2: Decrement for "silent": s-1, i-1, l-1, e-1, n-1, t-1

Result: all zeros → ANAGRAM ✓

Visual before decrement:      After decrement:
e: 1                          e: 0
i: 1                          i: 0
l: 1                          l: 0
n: 1                          n: 0
s: 1                          s: 0
t: 1                          t: 0
All others: 0                 All: 0 → ANAGRAM
```

#### 4. Template

```java
// Template 1: Anagram check with int[26]
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;

    int[] freq = new int[26]; // freq[i] = count of (char)(i + 'a')

    for (char c : s.toCharArray()) freq[c - 'a']++;  // increment for s
    for (char c : t.toCharArray()) freq[c - 'a']--;  // decrement for t

    for (int count : freq) {
        if (count != 0) return false; // mismatch in frequency
    }
    return true;
}

// Template 2: Group anagrams with HashMap
public List<List<String>> groupAnagrams(String[] strs) {
    Map<String, List<String>> map = new HashMap<>();

    for (String str : strs) {
        char[] chars = str.toCharArray();
        Arrays.sort(chars); // sort to get canonical form
        String key = new String(chars);

        map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
    }
    return new ArrayList<>(map.values());
}
```

#### 5. Worked Example 1 — Valid Anagram (LeetCode 242)

```java
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;

    int[] count = new int[26];

    for (int i = 0; i < s.length(); i++) {
        count[s.charAt(i) - 'a']++;
        count[t.charAt(i) - 'a']--;
    }

    for (int c : count) {
        if (c != 0) return false;
    }
    return true;
}
```

#### 6. Worked Example 2 — Group Anagrams (LeetCode 49)

**Problem:** Given an array of strings, group the anagrams together.

```java
public List<List<String>> groupAnagrams(String[] strs) {
    // Key: sorted version of word → value: list of original words
    Map<String, List<String>> groups = new HashMap<>();

    for (String word : strs) {
        // Sort the characters to create a canonical key
        char[] arr = word.toCharArray();
        Arrays.sort(arr);
        String key = new String(arr); // e.g., "eat" → "aet", "tea" → "aet"

        // Add word to its group
        if (!groups.containsKey(key)) {
            groups.put(key, new ArrayList<>());
        }
        groups.get(key).add(word);
    }

    return new ArrayList<>(groups.values());
}
```

#### 7. Dry Run

Input: `strs = ["eat","tea","tan","ate","nat","bat"]`

| Word | Sorted Key | Map after processing |
|------|-----------|---------------------|
| "eat" | "aet" | {"aet": ["eat"]} |
| "tea" | "aet" | {"aet": ["eat","tea"]} |
| "tan" | "ant" | {"aet": ["eat","tea"], "ant": ["tan"]} |
| "ate" | "aet" | {"aet": ["eat","tea","ate"], "ant": ["tan"]} |
| "nat" | "ant" | {"aet": [...], "ant": ["tan","nat"]} |
| "bat" | "abt" | {"aet": [...], "ant": [...], "abt": ["bat"]} |

**Output:** `[["eat","tea","ate"], ["tan","nat"], ["bat"]]`

#### 8. Time & Space Complexity

**Anagram check:** Time O(n), Space O(1) (int[26] is constant)
**Group Anagrams:** Time O(n × k log k) where k = max word length (sorting each word), Space O(n × k) for the map

#### 9. Common Mistakes

1. **Using HashMap when int[26] suffices:** For lowercase English letters, `int[26]` is faster and simpler.
2. **Checking only one string's frequency:** You must account for both strings. Use increment for one, decrement for the other, then check all zeros.
3. **Not handling different lengths:** Two strings of different lengths can never be anagrams. Early return saves time.
4. **Modifying the original string for sorting:** `word.toCharArray()` creates a copy — you're sorting the char array, not the string (which is immutable anyway).

#### 10. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 242 — Valid Anagram | 🟢 Easy | int[26] frequency check |
| LeetCode 49 — Group Anagrams | 🟡 Medium | Sort-based canonical key |
| LeetCode 383 — Ransom Note | 🟢 Easy | Frequency: magazine must cover ransom |
| LeetCode 438 — Find All Anagrams in a String | 🟡 Medium | Sliding window + frequency |
| LeetCode 567 — Permutation in String | 🟡 Medium | Sliding window + frequency comparison |
| LeetCode 1002 — Find Common Characters | 🟢 Easy | Min frequency across all strings |

---

### Pattern 13: Sliding Window on Strings

#### 1. Concept

Sliding window on strings works identically to the array variable-size sliding window, but the "window state" is typically a **character frequency map**. As the window slides right, you add the new character; when the window becomes invalid, you remove characters from the left.

The trick for string windows: tracking when the window is "valid" (contains required characters with required frequencies) using an integer counter like `formed` or `satisfied` rather than comparing entire maps each step.

#### 2. Trigger Words

- "longest substring without..."
- "minimum window containing..."
- "substring containing all characters of..."
- "window with at most k distinct..."
- "find substring..."

#### 3. Visual

```text
Longest Substring Without Repeating Characters: "abcabcbb"

Window expands right; when duplicate found, shrink left until no duplicate.

[a]bcabcbb  window={a:1}, len=1
[ab]cabcbb  window={a:1,b:1}, len=2
[abc]abcbb  window={a:1,b:1,c:1}, len=3
[abca]bcbb  'a' already in window! → shrink left
 [bca]bcbb  window={a:1,b:1,c:1} (removed a), 'a' now unique → re-add? already added
 
Wait — let's redo:
"abcabcbb"
 lL  R
 
r=0: 'a' add → {a:1}, maxLen=1
r=1: 'b' add → {a:1,b:1}, maxLen=2
r=2: 'c' add → {a:1,b:1,c:1}, maxLen=3
r=3: 'a' DUPLICATE → shrink: remove nums[l]='a' → {b:1,c:1}, l=1
     'a' now unique → add 'a' → {b:1,c:1,a:1}, maxLen=3
r=4: 'b' DUPLICATE → shrink: remove 'b' → {c:1,a:1}, l=2
     add 'b' → {c:1,a:1,b:1}, maxLen=3
r=5: 'c' DUPLICATE → shrink: remove 'c' → {a:1,b:1}, l=3
     add 'c' → {a:1,b:1,c:1}, maxLen=3
r=6: 'b' DUPLICATE → shrink: remove 'a'→{b:1,c:1}, l=4, still dup
     shrink: remove 'b'→{c:1}, l=5
     add 'b' → {c:1,b:1}, maxLen=3
r=7: 'b' DUPLICATE → shrink: remove 'c'→{b:1}, l=6, still dup
     shrink: remove 'b'→{}, l=7
     add 'b' → {b:1}, maxLen=3
DONE. Answer = 3 ("abc")
```

#### 4. Template

```java
// Longest substring without repeating characters
public int lengthOfLongestSubstring(String s) {
    Map<Character, Integer> freq = new HashMap<>();
    int left = 0, maxLen = 0;

    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        freq.put(c, freq.getOrDefault(c, 0) + 1); // add to window

        // Shrink window while there's a duplicate
        while (freq.get(c) > 1) {
            char leftChar = s.charAt(left);
            freq.put(leftChar, freq.get(leftChar) - 1);
            if (freq.get(leftChar) == 0) freq.remove(leftChar);
            left++;
        }

        maxLen = Math.max(maxLen, right - left + 1);
    }
    return maxLen;
}
```

#### 5. Worked Example — Longest Substring Without Repeating Characters (LeetCode 3)

```java
public int lengthOfLongestSubstring(String s) {
    Set<Character> window = new HashSet<>(); // tracks chars in current window
    int left = 0, maxLen = 0;

    for (int right = 0; right < s.length(); right++) {
        // Shrink window until s[right] is not a duplicate
        while (window.contains(s.charAt(right))) {
            window.remove(s.charAt(left));
            left++;
        }

        window.add(s.charAt(right));
        maxLen = Math.max(maxLen, right - left + 1);
    }
    return maxLen;
}
```

#### 6. Dry Run

Input: `s = "abcabcbb"`

| right | char | Window before add | Shrink? | Window after | left | maxLen |
|-------|------|------------------|---------|--------------|------|--------|
| 0 | 'a' | {} | No | {a} | 0 | 1 |
| 1 | 'b' | {a} | No | {a,b} | 0 | 2 |
| 2 | 'c' | {a,b} | No | {a,b,c} | 0 | 3 |
| 3 | 'a' | {a,b,c} | Yes: remove 'a', left=1 | {b,c,a} | 1 | 3 |
| 4 | 'b' | {b,c,a} | Yes: remove 'b', left=2 | {c,a,b} | 2 | 3 |
| 5 | 'c' | {c,a,b} | Yes: remove 'c', left=3 | {a,b,c} | 3 | 3 |
| 6 | 'b' | {a,b,c} | Yes: remove 'a',left=4; then remove 'b',left=5 | {c,b} | 5 | 3 |
| 7 | 'b' | {c,b} | Yes: remove 'c',left=6; remove 'b',left=7 | {b} | 7 | 3 |

**Return:** `3`

#### 7. Advanced Variant — Minimum Window Substring (LeetCode 76)

**Problem:** Given strings `s` and `t`, find the minimum window in `s` that contains all characters of `t`.

```java
public String minWindow(String s, String t) {
    if (s.isEmpty() || t.isEmpty()) return "";

    // Count required characters from t
    Map<Character, Integer> need = new HashMap<>();
    for (char c : t.toCharArray()) need.put(c, need.getOrDefault(c, 0) + 1);

    int required = need.size(); // number of distinct chars needed
    int formed = 0;             // number of distinct chars currently satisfied

    Map<Character, Integer> window = new HashMap<>();
    int left = 0;
    int minLen = Integer.MAX_VALUE;
    int minLeft = 0;

    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        window.put(c, window.getOrDefault(c, 0) + 1);

        // Check if current char's requirement is just met
        if (need.containsKey(c) && window.get(c).intValue() == need.get(c).intValue()) {
            formed++;
        }

        // Try to shrink window while all requirements are satisfied
        while (left <= right && formed == required) {
            // Record if this is the smallest window so far
            int windowLen = right - left + 1;
            if (windowLen < minLen) {
                minLen = windowLen;
                minLeft = left;
            }

            // Shrink from left
            char leftChar = s.charAt(left);
            window.put(leftChar, window.get(leftChar) - 1);
            if (need.containsKey(leftChar) && window.get(leftChar) < need.get(leftChar)) {
                formed--; // a required char is no longer satisfied
            }
            left++;
        }
    }

    return minLen == Integer.MAX_VALUE ? "" : s.substring(minLeft, minLeft + minLen);
}
```

**Complexity:** Time O(|s| + |t|), Space O(|s| + |t|)

#### 8. Time & Space Complexity

**Longest Substring Without Repeating:**
- Time: O(n) — each character added and removed at most once
- Space: O(min(m, n)) where m = charset size, n = string length

**Minimum Window Substring:**
- Time: O(|s| + |t|)
- Space: O(|s| + |t|)

#### 9. Common Mistakes

1. **O(n²) loop:** Recounting frequencies from scratch each time is O(n²). Maintain the frequency map incrementally.
2. **Not handling `formed == required` correctly:** Use integer equality (`intValue()`), not `==` on Integer objects (auto-boxing pitfall).
3. **Returning empty string vs no solution:** Return `""` (not `null`) when no window exists, unless the problem specifies otherwise.

#### 10. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 3 — Longest Substring Without Repeating Chars | 🟡 Medium | HashSet window |
| LeetCode 76 — Minimum Window Substring | 🔴 Hard | formed/required counter |
| LeetCode 438 — Find All Anagrams in a String | 🟡 Medium | Fixed window with freq compare |
| LeetCode 567 — Permutation in String | 🟡 Medium | Fixed window freq match |
| LeetCode 340 — Longest Substring with At Most K Distinct | 🟡 Medium | Window size by map size |
| LeetCode 1004 — Max Consecutive Ones III | 🟡 Medium | At most k zeros |

---

### Pattern 14: KMP Algorithm (Pattern Matching)

#### 1. Concept

**KMP (Knuth-Morris-Pratt)** solves the string pattern matching problem — given text T and pattern P, find all occurrences of P in T — in O(n+m) time instead of the naive O(n×m).

**Naive approach fails when:** The pattern has repeated prefixes. When a mismatch occurs at position `j`, naive restarts from `j=0` in the pattern, potentially repeating comparisons.

**KMP's insight:** When a mismatch occurs, we've already processed part of the pattern. The **LPS (Longest Proper Prefix which is also Suffix)** array tells us how far back in the pattern we need to reset, instead of resetting to 0 every time.

**LPS array:** For each position `i` in the pattern, `lps[i]` = length of the longest proper prefix of `pattern[0..i]` that is also a suffix of `pattern[0..i]`.

Example: pattern = "AABAAB"
- lps[0] = 0 (A — no proper prefix/suffix)
- lps[1] = 1 (AA — "A" is both prefix and suffix)
- lps[2] = 0 (AAB — no proper prefix/suffix)
- lps[3] = 1 (AABA — "A" is prefix, "A" is suffix)
- lps[4] = 2 (AABAA — "AA" is prefix and suffix)
- lps[5] = 3 (AABAAB — "AAB" is both prefix and suffix)

When a mismatch occurs at `pattern[j]`, instead of resetting j to 0, we reset j to `lps[j-1]`. This "skips" the common prefix/suffix we've already matched.

#### 2. Trigger Words

- "find pattern in text"
- "find all occurrences of pattern"
- "string matching"
- "find substring indices"
- "how many times does pattern appear in text"

#### 3. Visual

```text
Text:    "AABABAABAAB"
Pattern: "AABAAB"

LPS for "AABAAB":
Pattern: A  A  B  A  A  B
Index:   0  1  2  3  4  5
LPS:     0  1  0  1  2  3

KMP Search:
Text:    A  A  B  A  B  A  A  B  A  A  B
Index:   0  1  2  3  4  5  6  7  8  9  10
Pattern: A  A  B  A  A  B
LPS:     0  1  0  1  2  3

i=0,j=0: T[0]='A'==P[0]='A' → i++,j++
i=1,j=1: T[1]='A'==P[1]='A' → i++,j++
i=2,j=2: T[2]='B'==P[2]='B' → i++,j++
i=3,j=3: T[3]='A'==P[3]='A' → i++,j++
i=4,j=4: T[4]='B'!=P[4]='A' → MISMATCH, j=lps[j-1]=lps[3]=1
i=4,j=1: T[4]='B'!=P[1]='A' → MISMATCH, j=lps[j-1]=lps[0]=0
i=4,j=0: T[4]='B'!=P[0]='A' → MISMATCH, j=0 (can't go further), i++
i=5,j=0: T[5]='A'==P[0]='A' → i++,j++
i=6,j=1: T[6]='A'==P[1]='A' → i++,j++
i=7,j=2: T[7]='B'==P[2]='B' → i++,j++
i=8,j=3: T[8]='A'==P[3]='A' → i++,j++
i=9,j=4: T[9]='A'==P[4]='A' → i++,j++
i=10,j=5: T[10]='B'==P[5]='B' → i++,j++ (j==6=pattern.length)
MATCH FOUND at index 10-6+1 = 5! j=lps[j-1]=lps[5]=3
(continue searching with j=3)
```

#### 4. Code

```java
public class KMP {
    // Build the LPS (Longest Proper Prefix which is also Suffix) array
    private static int[] buildLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        lps[0] = 0; // always 0

        int len = 0; // length of previous longest prefix-suffix
        int i = 1;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                // Characters match — extend the prefix-suffix
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    // Fall back: try shorter prefix-suffix
                    len = lps[len - 1];
                    // DON'T increment i — try the same i with shorter prefix
                } else {
                    // len == 0: no prefix-suffix possible
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    // KMP Search: find all occurrences of pattern in text
    public static List<Integer> search(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        int n = text.length(), m = pattern.length();

        if (m == 0) return occurrences;

        int[] lps = buildLPS(pattern);

        int i = 0; // index into text
        int j = 0; // index into pattern

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                // Full pattern matched
                occurrences.add(i - j); // match starts at index i - m
                j = lps[j - 1]; // reset j to find overlapping matches
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                // Mismatch after j matches
                if (j != 0) {
                    j = lps[j - 1]; // fall back using LPS
                } else {
                    i++; // no prefix to fall back to, advance text
                }
            }
        }
        return occurrences;
    }

    public static void main(String[] args) {
        String text = "AABABAABAAB";
        String pattern = "AABAAB";
        List<Integer> result = search(text, pattern);
        System.out.println("Pattern found at indices: " + result);
        // Output: [5]
    }
}
```

#### 5. LPS Construction Dry Run

Pattern: `"AABAAB"`, m=6

| i | pattern[i] | len | pattern[len] | Match? | lps[i] | Action |
|---|-----------|-----|-------------|--------|--------|--------|
| 1 | 'A' | 0 | 'A' | ✓ | 1 | len=1, i=2 |
| 2 | 'B' | 1 | 'A' | ✗ | — | len=lps[0]=0 |
| 2 | 'B' | 0 | 'A' | ✗ | 0 | i=3 |
| 3 | 'A' | 0 | 'A' | ✓ | 1 | len=1, i=4 |
| 4 | 'A' | 1 | 'A' | ✓ | 2 | len=2, i=5 |
| 5 | 'B' | 2 | 'B' | ✓ | 3 | len=3, i=6 (done) |

**LPS = [0, 1, 0, 1, 2, 3]** ✓

#### 6. Time & Space Complexity

- **LPS construction:** O(m)
- **KMP search:** O(n) — `i` only increases; `j` decreases via LPS but total j-decrements ≤ total j-increments ≤ n
- **Total:** O(n + m)
- **Space:** O(m) for LPS array

#### 7. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 28 — Find Index of First Occurrence | 🟢 Easy | KMP or built-in indexOf |
| LeetCode 686 — Repeated String Match | 🟡 Medium | KMP on repeated string |
| LeetCode 459 — Repeated Substring Pattern | 🟡 Medium | KMP: is s a repetition of prefix? |
| LeetCode 214 — Shortest Palindrome | 🔴 Hard | KMP to find longest palindrome prefix |

---

### Pattern 15: Rolling Hash (Rabin-Karp)

#### 1. Concept

**Rolling Hash** (Rabin-Karp algorithm) is an alternative to KMP for string matching. Instead of comparing characters, it compares **hash values** of windows.

**Polynomial Rolling Hash:**
For a window of length k, the hash is:
```
hash = (s[0] × p^(k-1) + s[1] × p^(k-2) + ... + s[k-1]) mod M
```
where `p` is a prime base (e.g., 31 or 131) and `M` is a large prime (e.g., 10^9+7).

**Rolling:** When sliding the window right by 1:
```
new_hash = (old_hash - s[left] × p^(k-1)) × p + s[right+1]
```

This computes the new hash in O(1) instead of O(k) — the "rolling" part.

**When to use Rolling Hash over KMP:**
- When you need to find **any** of multiple patterns (multiple pattern matching)
- Repeated DNA sequences (same hash → same window content)
- 2D pattern matching in matrices
- Cases where hash collisions are acceptable

#### 2. Worked Example — Repeated DNA Sequences (LeetCode 187)

**Problem:** Given a DNA string `s`, find all 10-letter substrings that appear more than once.

```java
public List<String> findRepeatedDnaSequences(String s) {
    List<String> result = new ArrayList<>();
    if (s.length() <= 10) return result;

    Map<String, Integer> seen = new HashMap<>();

    for (int i = 0; i <= s.length() - 10; i++) {
        String window = s.substring(i, i + 10);
        seen.put(window, seen.getOrDefault(window, 0) + 1);
        if (seen.get(window) == 2) { // add exactly when seen twice (avoid duplicates)
            result.add(window);
        }
    }
    return result;
}

// Rolling hash version for O(n) time:
public List<String> findRepeatedDnaSequencesHash(String s) {
    int n = s.length();
    if (n <= 10) return new ArrayList<>();

    // Encode: A=0, C=1, G=2, T=3
    Map<Character, Integer> charMap = Map.of('A',0,'C',1,'G',2,'T',3);
    int base = 4, mod = 1_000_000_007, k = 10;

    // Precompute base^(k-1) for rolling
    long pow = 1;
    for (int i = 0; i < k - 1; i++) pow = (pow * base) % mod;

    Map<Long, Integer> hashCount = new HashMap<>();
    List<String> result = new ArrayList<>();
    Set<String> added = new HashSet<>();

    long hash = 0;
    // Initial window
    for (int i = 0; i < k; i++) {
        hash = (hash * base + charMap.get(s.charAt(i))) % mod;
    }
    hashCount.put(hash, 1);

    // Slide window
    for (int i = k; i < n; i++) {
        // Roll: remove leftmost, add rightmost
        hash = (hash - charMap.get(s.charAt(i - k)) * pow % mod + mod) % mod;
        hash = (hash * base + charMap.get(s.charAt(i))) % mod;

        int count = hashCount.getOrDefault(hash, 0);
        hashCount.put(hash, count + 1);

        if (count >= 1) {
            // Hash collision possible — verify with actual string
            String sub = s.substring(i - k + 1, i + 1);
            if (!added.contains(sub)) {
                result.add(sub);
                added.add(sub);
            }
        }
    }
    return result;
}
```

#### 3. Time & Space Complexity

- **Naive (substring comparison):** O(n × k) — k for each substring comparison
- **Rolling hash:** O(n) average, O(n × k) worst case (due to hash collisions requiring string verification)
- **Space:** O(n) for hash map

#### 4. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 187 — Repeated DNA Sequences | 🟡 Medium | Rolling hash or HashSet of substrings |
| LeetCode 1044 — Longest Duplicate Substring | 🔴 Hard | Binary search + rolling hash |
| LeetCode 718 — Maximum Length of Repeated Subarray | 🟡 Medium | Rolling hash or DP |

---

### Pattern 16: Sorting-Based String Tricks

#### 1. Concept

Sorting a string or array of strings **normalizes** them, creating a canonical form that makes comparison trivial. Multiple problems that seem complex become simple after sorting.

**Cases:**
1. **Anagram detection:** Sort both strings — if equal strings, they're anagrams
2. **Group Anagrams:** Sort each string to use as a HashMap key
3. **Custom string comparator:** Define `compare(a, b)` to achieve a specific global order
4. **Relative sort:** Sort array relative to a reference order

**The key insight for "Largest Number" (LC 179):**
When deciding whether string `a` or `b` should come first, compare `a+b` vs `b+a`. If `a+b > b+a`, then `a` should come first.

Example: `a="9"`, `b="34"` → `"934"` vs `"349"` → `"934" > "349"` → put `"9"` first.

#### 2. Worked Example — Largest Number (LeetCode 179)

**Problem:** Given a list of non-negative integers, arrange them so that they form the largest number. Return the result as a string.

```java
public String largestNumber(int[] nums) {
    // Convert int[] to String[] for sorting
    String[] strs = new String[nums.length];
    for (int i = 0; i < nums.length; i++) {
        strs[i] = String.valueOf(nums[i]);
    }

    // Custom comparator: prefer the arrangement that gives larger concatenation
    Arrays.sort(strs, (a, b) -> (b + a).compareTo(a + b));
    // If b+a > a+b: b should come BEFORE a → return positive (b first)

    // Edge case: if largest number is 0 (e.g., [0, 0])
    if (strs[0].equals("0")) return "0";

    // Build result
    StringBuilder sb = new StringBuilder();
    for (String s : strs) sb.append(s);
    return sb.toString();
}
```

#### 3. Dry Run

Input: `nums = [10, 2]`

| Comparison | a+b | b+a | Winner |
|-----------|-----|-----|--------|
| "10" vs "2" | "102" | "210" | "2" before "10" (210 > 102) |

Sorted: `["2", "10"]`
Result: `"210"` ✓

Input: `nums = [3, 30, 34, 5, 9]`

| After sort | Sorted order | Concatenation |
|-----------|-------------|---------------|
| Compare all pairs | ["9","5","34","3","30"] | "9534330" |

**Return:** `"9534330"` ✓

#### 4. Time & Space Complexity

- **Time:** O(n × k × log n) where k = average string length (comparison takes O(k), sort takes O(n log n))
- **Space:** O(n × k) for string conversion

#### 5. Practice Problems

| Problem | Difficulty | Key Insight |
|---------|------------|-------------|
| LeetCode 179 — Largest Number | 🟡 Medium | Custom comparator: compare b+a vs a+b |
| LeetCode 1122 — Relative Sort Array | 🟢 Easy | Sort by position in reference array |
| LeetCode 791 — Custom Sort String | 🟡 Medium | Sort string by custom character order |
| LeetCode 242 — Valid Anagram | 🟢 Easy | Sort both strings, compare |
| LeetCode 49 — Group Anagrams | 🟡 Medium | Sort each string as HashMap key |

---

## Chapter 7: Mixed Patterns & Advanced Combos

These problems require combining two or more patterns. Recognizing which patterns to combine is the hallmark of interview-level problem solving.

---

### Combo 1: Two Pointers + Sorting — 3Sum (LeetCode 15)

**Problem:** Find all unique triplets in an array that sum to zero.

**Why two patterns?**
- Brute force is O(n³) — three nested loops
- Sort the array → reduces to "for each element, find two others summing to its negative" → Two Pointers in O(n) per element → Total O(n²)

**Strategy:**
1. Sort the array
2. For each `i`, use two pointers on the remaining subarray to find pairs summing to `-nums[i]`
3. Skip duplicate values to avoid duplicate triplets

```java
public List<List<Integer>> threeSum(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    Arrays.sort(nums); // sort first — O(n log n)

    for (int i = 0; i < nums.length - 2; i++) {
        // Skip duplicate starting values
        if (i > 0 && nums[i] == nums[i - 1]) continue;

        // Early termination: if smallest possible triplet > 0, no solution
        if (nums[i] > 0) break;

        int left = i + 1, right = nums.length - 1;
        int target = -nums[i]; // need pair summing to this

        while (left < right) {
            int sum = nums[left] + nums[right];

            if (sum == target) {
                result.add(Arrays.asList(nums[i], nums[left], nums[right]));

                // Skip duplicates for left and right
                while (left < right && nums[left] == nums[left + 1]) left++;
                while (left < right && nums[right] == nums[right - 1]) right--;

                left++;
                right--;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
    }
    return result;
}
```

**Dry Run for `nums = [-1, 0, 1, 2, -1, -4]`:**

After sorting: `[-4, -1, -1, 0, 1, 2]`

| i | nums[i] | target | left | right | nums[l]+nums[r] | Action |
|---|---------|--------|------|-------|-----------------|--------|
| 0 | -4 | 4 | 1 | 5 | -1+2=1 < 4 | left++ |
| 0 | -4 | 4 | 2 | 5 | -1+2=1 < 4 | left++ |
| 0 | -4 | 4 | 3 | 5 | 0+2=2 < 4 | left++ |
| 0 | -4 | 4 | 4 | 5 | 1+2=3 < 4 | left++ |
| 0 | -4 | 4 | 5 | 5 | left>=right, stop | — |
| 1 | -1 | 1 | 2 | 5 | -1+2=1 = 1 ✓ | add [-1,-1,2], skip dups, left=3,right=4 |
| 1 | -1 | 1 | 3 | 4 | 0+1=1 = 1 ✓ | add [-1,0,1], left=4, right=3 (stop) |
| 2 | -1 | — | — | — | skip (dup of i=1) | continue |
| 3 | 0 | 0 | 4 | 5 | 1+2=3 > 0 | right-- |
| 3 | 0 | 0 | 4 | 4 | left>=right, stop | — |

**Result:** `[[-1,-1,2], [-1,0,1]]` ✓

**Complexity:** Time O(n²), Space O(1) excluding output

---

### Combo 2: Prefix Sum + HashMap — Subarray Sum Equals K (LeetCode 560)

**Problem:** Count subarrays with sum exactly equal to `k`.

**Why two patterns?**
- Sliding window doesn't work (negative numbers can exist)
- Brute force O(n²) — two loops
- Prefix sum + HashMap finds all subarrays in O(n)

The insight: if `prefix[j] - prefix[i] = k`, then subarray `[i, j-1]` has sum k. So for each j, we want to count how many previous prefix sums equal `prefix[j] - k`.

```java
public int subarraySum(int[] nums, int k) {
    Map<Integer, Integer> prefixCount = new HashMap<>();
    prefixCount.put(0, 1); // empty prefix (handles subarrays starting at index 0)

    int count = 0, sum = 0;

    for (int num : nums) {
        sum += num; // running prefix sum

        // Count how many previous sums equal (sum - k)
        count += prefixCount.getOrDefault(sum - k, 0);

        // Update prefix sum frequency map
        prefixCount.put(sum, prefixCount.getOrDefault(sum, 0) + 1);
    }
    return count;
}
```

**Why `prefixCount.put(0, 1)` is critical:**
If `nums = [3, 4]` and `k = 7`:
- At i=1: sum = 7, need = 7-7=0, count += prefixCount.get(0) = 1 ✓
- Without the initialization, this subarray [3,4] would be missed.

**Complexity:** Time O(n), Space O(n)

---

### Combo 3: Sliding Window + Frequency Map — Permutation in String (LeetCode 567)

**Problem:** Given two strings `s1` and `s2`, return true if any permutation of `s1` is a substring of `s2`.

**Why two patterns?**
- A permutation of `s1` has the same character frequency as `s1`
- Find a window of size `s1.length()` in `s2` with matching frequency → Fixed-size sliding window
- Track matching character count using `int[26]` arrays

```java
public boolean checkInclusion(String s1, String s2) {
    if (s1.length() > s2.length()) return false;

    int[] need = new int[26]; // frequency of chars in s1
    int[] have = new int[26]; // frequency of chars in current window of s2
    int k = s1.length();
    int matches = 0; // how many characters have matching frequency

    // Build frequency for s1
    for (char c : s1.toCharArray()) need[c - 'a']++;

    // Build initial window for first k characters of s2
    for (int i = 0; i < k; i++) {
        int idx = s2.charAt(i) - 'a';
        have[idx]++;
        // Check if this char's count now matches (or starts matching)
        if (have[idx] == need[idx]) matches++;
        // Note: if have[idx] > need[idx], it "overshot" — not a match
    }

    if (matches == 26) return true; // all 26 char types match

    // Slide the window
    for (int i = k; i < s2.length(); i++) {
        // Add incoming character (right)
        int inIdx = s2.charAt(i) - 'a';
        have[inIdx]++;
        if (have[inIdx] == need[inIdx]) matches++;
        else if (have[inIdx] == need[inIdx] + 1) matches--; // overshot: went from match to over

        // Remove outgoing character (left)
        int outIdx = s2.charAt(i - k) - 'a';
        have[outIdx]--;
        if (have[outIdx] == need[outIdx]) matches++;       // became a match
        else if (have[outIdx] == need[outIdx] - 1) matches--; // went below match

        if (matches == 26) return true;
    }
    return false;
}
```

**Dry Run for `s1 = "ab"`, `s2 = "eidbaooo"`:**

`need = {a:1, b:1}`, k=2

Initial window "ei": `have = {e:1, i:1}`, matches for {e:0,i:0} vs need{a,b} → matches=0

| i | Incoming s2[i] | have changes | Outgoing s2[i-2] | matches | Result |
|---|---------------|-------------|-----------------|---------|--------|
| 2 | 'd' | d:1 | 'e': e:0 | 0 | — |
| 3 | 'b' | b:1, matches++ | 'i': i:0 | 1 | — |
| 4 | 'a' | a:1, matches++ | 'd': d:0 | 2 | — |
| — | — | — | — | 2≠26 | — |

Wait — we compare `matches == 26`, but actually we should compare with the count of distinct chars that have `need[i] > 0`. Let me use the correct approach that tracks `matched` as count of chars where have==need:

Actually for this problem the standard approach counts chars with `need[i]>0` as `required`, and `matched` as how many of those are satisfied. Let me show the simpler correct version:

```java
public boolean checkInclusion(String s1, String s2) {
    if (s1.length() > s2.length()) return false;

    int[] need = new int[26], have = new int[26];
    for (char c : s1.toCharArray()) need[c - 'a']++;

    int k = s1.length();
    int required = 0;
    for (int f : need) if (f > 0) required++;

    int satisfied = 0;

    // Initial window
    for (int i = 0; i < k; i++) {
        int c = s2.charAt(i) - 'a';
        have[c]++;
        if (have[c] == need[c]) satisfied++;
    }
    if (satisfied == required) return true;

    // Slide
    for (int i = k; i < s2.length(); i++) {
        int in = s2.charAt(i) - 'a';
        have[in]++;
        if (have[in] == need[in]) satisfied++;
        else if (have[in] == need[in] + 1) satisfied--;

        int out = s2.charAt(i - k) - 'a';
        if (have[out] == need[out]) satisfied--;
        else if (have[out] == need[out] + 1) satisfied++;  // going back to match from overshoot
        have[out]--;
        if (have[out] == need[out]) satisfied++;

        if (satisfied == required) return true;
    }
    return false;
}
```

**Complexity:** Time O(|s1| + |s2|), Space O(1) — int[26] is constant

---

### Combo 4: Binary Search + Prefix Sum — Split Array Largest Sum (LeetCode 410)

**Problem:** Split array into `m` non-empty continuous subarrays to minimize the largest subarray sum.

**Why two patterns?**
- The answer (minimum possible largest sum) lies in range `[max(nums), sum(nums)]`
- Monotonic property: if we can split with largest sum ≤ X, we can also split with largest sum ≤ X+1
- Binary search on the answer X; for each X, greedily check if array can be split into ≤ m parts using prefix sums/greedy

```java
public int splitArray(int[] nums, int m) {
    // Binary search: answer is in [max element, sum of all]
    long lo = 0, hi = 0;
    for (int num : nums) {
        lo = Math.max(lo, num);  // minimum possible: one subarray must be at least max(nums)
        hi += num;               // maximum possible: one subarray = entire array
    }

    while (lo < hi) {
        long mid = lo + (hi - lo) / 2;

        if (canSplit(nums, m, mid)) {
            hi = mid; // mid works, try smaller
        } else {
            lo = mid + 1; // mid too small, increase
        }
    }
    return (int) lo;
}

// Check if we can split nums into at most m subarrays with each sum ≤ maxSum
private boolean canSplit(int[] nums, int m, long maxSum) {
    int parts = 1;      // start with 1 subarray
    long currentSum = 0;

    for (int num : nums) {
        if (currentSum + num > maxSum) {
            // Current element doesn't fit — start a new subarray
            parts++;
            currentSum = num;

            if (parts > m) return false; // too many parts
        } else {
            currentSum += num;
        }
    }
    return true; // successfully split into ≤ m parts
}
```

**Example:** `nums = [7,2,5,10,8]`, `m = 2`

Answer range: `lo = 10` (max element), `hi = 32` (sum)

Binary search → `lo = hi = 18` → answer is `18`

- With maxSum=18: `[7,2,5]` (sum=14) | `[10,8]` (sum=18) → 2 parts ✓

**Complexity:** Time O(n log(sum)), Space O(1)

---

## Chapter 8: Interview Cheat Sheet

When you see a problem, scan this table. Match the problem description to the pattern. Trust the pattern.

### Array Problems

| Problem Type | Pattern | Key Variable to Track | Time Complexity |
|-------------|---------|----------------------|-----------------|
| Two elements sum to target (sorted array) | Two Pointers (Opposite) | left, right pointers | O(n) |
| Remove duplicates / compress array in-place | Two Pointers (Same Dir) | slow pointer = write position | O(n) |
| Max/Sum/Avg of subarray of fixed size k | Sliding Window (Fixed) | windowSum, add incoming, subtract outgoing | O(n) |
| Min/Max length subarray with sum ≥ target | Sliding Window (Variable) | left, right, windowSum; while valid, shrink | O(n) |
| Range sum queries | Prefix Sum | prefix[i+1] = prefix[i] + arr[i] | O(n) build, O(1) query |
| Subarrays with sum = k | Prefix + HashMap | prefixCount.put(0,1), count += get(sum-k) | O(n) |
| Maximum sum of contiguous subarray | Kadane's | maxHere = max(arr[i], maxHere+arr[i]) | O(n) |
| Sort 0s, 1s, 2s | Dutch National Flag | low, mid, high — 3 regions | O(n) |
| Array 1..N, find missing/duplicate | Cyclic Sort | place arr[i] at index arr[i]-1 | O(n) |
| Minimize maximum / Maximize minimum | Binary Search on Answer | lo=min possible, hi=max possible, check mid | O(n log range) |
| Matrix spiral / rotation / diagonal | Matrix Traversal | boundary variables / transpose+reverse | O(m×n) |
| Triplets summing to 0 | Sort + Two Pointers | Fix one, two pointers on rest | O(n²) |

### String Problems

| Problem Type | Pattern | Key Variable to Track | Time Complexity |
|-------------|---------|----------------------|-----------------|
| Check palindrome | Two Pointers | left, right converge | O(n) |
| Longest palindromic substring | Expand Around Center | expandAroundCenter(i,i) and (i,i+1) | O(n²) |
| Anagram check / character counting | Frequency Map | int[26] or HashMap | O(n) |
| Longest substring without repeating | Sliding Window + Set | HashSet window, shrink on duplicate | O(n) |
| Minimum window containing all chars | Sliding Window + Map | formed/required counter | O(n) |
| Pattern occurrence in text | KMP | LPS array, j = lps[j-1] on mismatch | O(n+m) |
| Repeated window hashes | Rolling Hash | hash = hash*p + char, roll by subtract+multiply | O(n) avg |
| Arrange strings for largest number | Sort with Custom Comparator | compare b+a vs a+b | O(n log n × k) |
| Group by same character content | Sort each string as key | sorted string → HashMap key | O(n × k log k) |

### When Patterns Combine

| Combined Pattern | Use When |
|-----------------|----------|
| Sort + Two Pointers | Multiple elements with sum condition AND duplicates need to be skipped |
| Prefix Sum + HashMap | Subarray sum with negative numbers possible |
| Sliding Window + Frequency Map | Substring with character constraints |
| Binary Search + Greedy Check | "Minimize max" or "maximize min" optimization |
| Prefix Sum + Binary Search | Count subarrays with sum in range (sorted prefix array) |

---

## Appendix: Quick Reference Card

### All String Methods — One Table

| Method | Signature | Returns | Time |
|--------|-----------|---------|------|
| length | `length()` | int | O(1) |
| isEmpty | `isEmpty()` | boolean | O(1) |
| isBlank | `isBlank()` | boolean | O(n) |
| charAt | `charAt(int i)` | char | O(1) |
| indexOf | `indexOf(char/String)` | int | O(n) |
| lastIndexOf | `lastIndexOf(char/String)` | int | O(n) |
| contains | `contains(CharSequence)` | boolean | O(n) |
| startsWith | `startsWith(String)` | boolean | O(m) |
| endsWith | `endsWith(String)` | boolean | O(m) |
| substring | `substring(begin[, end])` | String | O(k) |
| replace | `replace(old, new)` | String | O(n) |
| replaceAll | `replaceAll(regex, rep)` | String | O(n) |
| split | `split(regex[, limit])` | String[] | O(n) |
| toLowerCase | `toLowerCase()` | String | O(n) |
| toUpperCase | `toUpperCase()` | String | O(n) |
| trim | `trim()` | String | O(n) |
| strip | `strip()` | String | O(n) |
| concat | `concat(String)` | String | O(n) |
| equals | `equals(Object)` | boolean | O(n) |
| equalsIgnoreCase | `equalsIgnoreCase(String)` | boolean | O(n) |
| compareTo | `compareTo(String)` | int | O(n) |
| toCharArray | `toCharArray()` | char[] | O(n) |
| valueOf | `valueOf(primitive/Object)` | String | O(n) |
| format | `format(format, args...)` | String | O(n) |
| intern | `intern()` | String | O(n) |
| matches | `matches(regex)` | boolean | O(n) |
| repeat | `repeat(int)` | String | O(n) |
| join | `join(delim, elements)` | String | O(n) |
| lines | `lines()` | Stream<String> | O(n) |
| getBytes | `getBytes()` | byte[] | O(n) |

---

### All StringBuilder Methods — One Table

| Method | Signature | Returns | Time |
|--------|-----------|---------|------|
| append | `append(any)` | StringBuilder | O(n) amortized |
| insert | `insert(offset, str)` | StringBuilder | O(n) |
| delete | `delete(start, end)` | StringBuilder | O(n) |
| deleteCharAt | `deleteCharAt(index)` | StringBuilder | O(n) |
| replace | `replace(start, end, str)` | StringBuilder | O(n) |
| reverse | `reverse()` | StringBuilder | O(n) |
| toString | `toString()` | String | O(n) |
| length | `length()` | int | O(1) |
| charAt | `charAt(int)` | char | O(1) |
| setCharAt | `setCharAt(int, char)` | void | O(1) |
| indexOf | `indexOf(String[, from])` | int | O(n) |
| lastIndexOf | `lastIndexOf(String)` | int | O(n) |
| substring | `substring(start[, end])` | String | O(k) |
| capacity | `capacity()` | int | O(1) |
| ensureCapacity | `ensureCapacity(int)` | void | O(n) |

---

### All Arrays Utility Methods — One Table

| Method | Signature | Returns | Time |
|--------|-----------|---------|------|
| sort | `sort(arr)` | void | O(n log n) |
| sort | `sort(arr, from, to)` | void | O(k log k) |
| sort | `sort(T[], Comparator)` | void | O(n log n) |
| binarySearch | `binarySearch(arr, key)` | int | O(log n) |
| equals | `equals(arr1, arr2)` | boolean | O(n) |
| fill | `fill(arr, val)` | void | O(n) |
| fill | `fill(arr, from, to, val)` | void | O(k) |
| copyOf | `copyOf(arr, len)` | array | O(n) |
| copyOfRange | `copyOfRange(arr, from, to)` | array | O(k) |
| toString | `toString(arr)` | String | O(n) |
| deepToString | `deepToString(Object[])` | String | O(n) |
| asList | `asList(T...)` | List<T> | O(n) |
| stream | `stream(arr)` | Stream/IntStream | O(1) |

---

### All ArrayList Methods — One Table

| Method | Signature | Returns | Time |
|--------|-----------|---------|------|
| add | `add(E)` | boolean | O(1) amortized |
| add | `add(int, E)` | void | O(n) |
| addAll | `addAll(Collection)` | boolean | O(k) |
| get | `get(int)` | E | O(1) |
| set | `set(int, E)` | E | O(1) |
| remove | `remove(int)` | E | O(n) |
| remove | `remove(Object)` | boolean | O(n) |
| removeAll | `removeAll(Collection)` | boolean | O(n×m) |
| retainAll | `retainAll(Collection)` | boolean | O(n×m) |
| clear | `clear()` | void | O(n) |
| size | `size()` | int | O(1) |
| isEmpty | `isEmpty()` | boolean | O(1) |
| contains | `contains(Object)` | boolean | O(n) |
| indexOf | `indexOf(Object)` | int | O(n) |
| lastIndexOf | `lastIndexOf(Object)` | int | O(n) |
| subList | `subList(from, to)` | List<E> | O(1) |
| toArray | `toArray()` | Object[] | O(n) |
| toArray | `toArray(T[])` | T[] | O(n) |
| iterator | `iterator()` | Iterator<E> | O(1) |
| listIterator | `listIterator()` | ListIterator<E> | O(1) |
| sort | `sort(Comparator)` | void | O(n log n) |
| ensureCapacity | `ensureCapacity(int)` | void | O(n) |
| trimToSize | `trimToSize()` | void | O(n) |
| clone | `clone()` | Object | O(n) |

---

### Pattern Recognition Flowchart (Text-Based Decision Tree)

```text
START: What does the problem involve?
│
├─ ARRAY (numbers, elements, indices)
│   │
│   ├─ Is the array SORTED (or can be sorted)?
│   │   ├─ Find 2 elements with sum → TWO POINTERS (OPPOSITE)
│   │   ├─ Find 3 elements with sum → SORT + TWO POINTERS (fix one, TP on rest)
│   │   ├─ Find missing/duplicate in 1..N → CYCLIC SORT
│   │   └─ Remove duplicates in-place → TWO POINTERS (SAME DIRECTION)
│   │
│   ├─ SUBARRAY problems:
│   │   ├─ Fixed size window → SLIDING WINDOW (FIXED)
│   │   │   └─ Max/sum/avg of window of size k
│   │   ├─ Variable size window (POSITIVE numbers) → SLIDING WINDOW (VARIABLE)
│   │   │   └─ Min/max length with sum ≥ target
│   │   ├─ Subarray with exact sum k (possibly negatives) → PREFIX SUM + HASHMAP
│   │   │   └─ prefixCount.put(0,1), count += get(sum - k)
│   │   └─ Maximum sum of any subarray → KADANE'S ALGORITHM
│   │
│   ├─ OPTIMIZATION ("minimize maximum" / "maximize minimum"):
│   │   └─ BINARY SEARCH ON ANSWER
│   │       └─ Define lo, hi, write canAchieve(mid)
│   │
│   ├─ Partition into groups (0s, 1s, 2s):
│   │   └─ DUTCH NATIONAL FLAG (3 pointers: low, mid, high)
│   │
│   └─ MATRIX (2D array):
│       ├─ Spiral order → SPIRAL TRAVERSAL (4 boundary pointers)
│       ├─ Rotate 90° → TRANSPOSE + REVERSE ROWS
│       └─ Diagonal → UP-RIGHT / DOWN-LEFT alternation
│
└─ STRING
    │
    ├─ Palindrome check → TWO POINTERS (left, right converge)
    │   └─ Longest palindromic substring → EXPAND AROUND CENTER
    │
    ├─ Character frequency / anagram:
    │   ├─ Check if anagram → int[26] frequency, increment/decrement
    │   ├─ Group anagrams → sort each string, use as HashMap key
    │   └─ Find anagram window → SLIDING WINDOW + FREQUENCY MAP
    │
    ├─ SUBSTRING problems:
    │   ├─ Fixed window (permutation in string) → SLIDING WINDOW (FIXED) + freq array
    │   ├─ Variable window (no repeating chars) → SLIDING WINDOW + HashSet
    │   └─ Minimum window (all chars of t) → SLIDING WINDOW + formed/required counter
    │
    ├─ Pattern matching (find P in T):
    │   ├─ Single pattern → KMP (O(n+m))
    │   └─ Multiple patterns / rolling window → ROLLING HASH
    │
    └─ Ordering / arrangement:
        ├─ Sort with custom rule → COMPARATOR (e.g., largest number: b+a vs a+b)
        └─ Character-based sort → int[26] frequency, reconstruct
```

---

## Final Words

You now have everything you need to tackle Arrays and Strings at the interview level. 

**The path forward:**
1. **Practice is non-negotiable.** Reading patterns builds intuition; solving problems builds reflex.
2. **For each problem:** verbalize the pattern name before coding.
3. **For each solution:** analyze the time and space complexity before moving on.
4. **Target:** 3-5 problems per pattern, then move to mixed-pattern problems.

**Recommended problem order by pattern:**
```
Two Pointers      → LC 167, 125, 977, 15, 11
Fast-Slow         → LC 26, 27, 283, 80
Fixed Window      → LC 643, 1343
Variable Window   → LC 209, 3, 904
Prefix Sum        → LC 303, 560, 724
Kadane's          → LC 53, 152, 918
DNF               → LC 75, 905
Cyclic Sort       → LC 268, 448, 287
Binary Search Ans → LC 875, 1011, 410
Matrix            → LC 54, 48, 498
String Palindrome → LC 125, 5, 680
Anagram           → LC 242, 49, 438
String Window     → LC 3, 76, 567
KMP               → LC 28, 459
```

Good luck. You've got this.

---

*Senpai DSA Series — Book 01: Arrays & Strings*  
*Next: Book 02 — Hashing & HashMaps*


---

# Chapter 9: CS61B Practice — Arrays & Strings

> This chapter contains practice problems and projects from UC Berkeley's CS61B course (FA23, SP24, FA24) that directly test Arrays and Strings concepts. These go beyond LeetCode — they simulate real software engineering.

---

## 9.1 Homework: HW0A — Java Syntax Basics

**Source:** [fa24.datastructur.es/homeworks/hw0/hw0a](https://fa24.datastructur.es/homeworks/hw0/hw0a/)
**Semesters:** FA23, SP24, FA24

### What It Tests
- Java syntax: variables, types, conditionals, loops
- Writing and calling methods
- Working with arrays

### Problems Included in HW0A

**Problem 1: Java Loops — Collatz Conjecture**
Write a method that, given a positive integer n, prints the Collatz sequence starting from n:
- If n is even: next = n / 2
- If n is odd: next = 3 * n + 1
- Stop when n == 1

```java
public class Collatz {
    /**
     * Returns the next Collatz number after n.
     * If n is even, returns n/2.
     * If n is odd, returns 3*n + 1.
     */
    public static int nextCollatz(int n) {
        if (n % 2 == 0) {
            return n / 2;
        } else {
            return 3 * n + 1;
        }
    }

    /**
     * Prints all numbers in the Collatz sequence starting from n.
     * Example: n=5 → 5 16 8 4 2 1
     */
    public static void collatzSequence(int n) {
        System.out.print(n);
        while (n != 1) {
            n = nextCollatz(n);
            System.out.print(" " + n);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        collatzSequence(5);  // 5 16 8 4 2 1
        collatzSequence(10); // 10 5 16 8 4 2 1
    }
}
```

**Dry Run for n=5:**

| Step | n | Operation |
|------|---|----------|
| 1 | 5 | odd → 3*5+1 = 16 |
| 2 | 16 | even → 16/2 = 8 |
| 3 | 8 | even → 8/2 = 4 |
| 4 | 4 | even → 4/2 = 2 |
| 5 | 2 | even → 2/2 = 1 |
| 6 | 1 | stop |

---

**Problem 2: Array Operations — Draw Triangle**
Write a method that draws a triangle of the given size using `*` characters:

```java
public class DrawTriangle {
    /**
     * Draws a right triangle of height n.
     * n=4 output:
     * *
     * **
     * ***
     * ****
     */
    public static void drawTriangle(int n) {
        for (int row = 1; row <= n; row++) {
            for (int col = 0; col < row; col++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        drawTriangle(4);
    }
}
```

**Key Insight:** Two nested loops — outer loop controls the row number, inner loop controls how many `*` to print. This is a classic 2D array / nested loop pattern.

---

**Problem 3: String Manipulation — windowPosSum**
Given an array `a`, return a new array where each element is replaced by the sum of itself and the next `n` elements (wrapping around). This is a sliding window variant.

```java
public class WindowPosSum {
    /**
     * Replaces each element a[i] with the sum of a[i] through a[i+n],
     * but only adding elements that are positive.
     * Only elements where a[i] > 0 get this treatment.
     *
     * Example: a=[1,-1,6,10,0,3], n=3
     * i=0: a[0]=1 (positive), sum 1+(-1)+6+10 = 16
     * i=1: a[1]=-1 (not positive, skip)
     * i=2: a[2]=6, sum 6+10+0+3 = 19
     * ...
     * Result: [16, -1, 19, 13, 0, 3]
     */
    public static void windowPosSum(int[] a, int n) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] > 0) {  // only positive elements
                int sum = 0;
                for (int j = i; j <= i + n && j < a.length; j++) {
                    sum += a[j];
                }
                a[i] = sum;
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {1, -1, 6, 10, 0, 3};
        windowPosSum(a, 3);
        System.out.println(java.util.Arrays.toString(a));
        // Output: [16, -1, 19, 13, 0, 3]
    }
}
```

**Pattern Connection:** This is the **Fixed Sliding Window** pattern (Chapter 5, Pattern 3) combined with conditional logic.

---

## 9.2 Homework: HW0B — Java Data Structures Intro

**Source:** [fa24.datastructur.es/homeworks/hw0/hw0b](https://fa24.datastructur.es/homeworks/hw0/hw0b/)
**Semesters:** FA23, SP24, FA24

### What It Tests
- Java Lists vs Arrays
- String manipulation
- Control flow with data structures

### Key Problems

**Problem: Flatten a 2D List**
Given a `List<List<Integer>>`, return a `List<Integer>` containing all elements.

```java
import java.util.ArrayList;
import java.util.List;

public class Flatten {
    /**
     * Flattens a 2D list into a 1D list.
     * Input:  [[1,2,3],[4,5],[6]]
     * Output: [1,2,3,4,5,6]
     */
    public static List<Integer> flatten(List<List<Integer>> l) {
        List<Integer> result = new ArrayList<>();
        for (List<Integer> innerList : l) {
            for (int num : innerList) {
                result.add(num);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<List<Integer>> input = new ArrayList<>();
        input.add(List.of(1, 2, 3));
        input.add(List.of(4, 5));
        input.add(List.of(6));
        System.out.println(flatten(input)); // [1, 2, 3, 4, 5, 6]
    }
}
```

**Pattern Connection:** Nested iteration — foundational to matrix traversal (Chapter 5, Pattern 10).

---

**Problem: Skip Iterating**
Given an array, return a new list containing every n-th element starting from index 0.

```java
import java.util.ArrayList;
import java.util.List;

public class SkipIterate {
    /**
     * Returns every n-th element from the array.
     * a=[0,1,2,3,4,5,6,7,8,9], n=3 → [0,3,6,9]
     */
    public static List<Integer> skipIterate(int[] a, int n) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < a.length; i += n) {
            result.add(a[i]);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] a = {0,1,2,3,4,5,6,7,8,9};
        System.out.println(skipIterate(a, 3)); // [0, 3, 6, 9]
    }
}
```

---

## 9.3 Project 0: 2048 — Array Matrix Mastery

**Source:** [fa24.datastructur.es/projects/proj0](https://fa24.datastructur.es/projects/proj0/)
**Semesters:** FA23, SP24, FA24
**Difficulty:** 🟢 Easy

### What You Build
The game logic for 2048 — a sliding tile puzzle on a 4×4 grid.
- Tiles slide in a direction (up/down/left/right)
- Tiles of the same value merge into one tile of double the value
- Game ends when no moves are possible, or a 2048 tile appears

### DSA Concepts Applied
- **2D Arrays** (Book 01 — Chapter 1, Matrix Traversal Pattern)
- **Directional Iteration** (the core challenge)
- **State Machines** (tracking board state, score, game-over condition)

### Core Methods to Implement

```java
public class Model {
    private int[][] board; // The 4x4 game board
    private int score;
    private int maxScore;
    private boolean gameOver;

    /**
     * The heart of 2048: tilt the board in one direction.
     * All tiles slide as far as possible in the given direction.
     * Tiles of equal value that collide merge into one.
     *
     * Strategy: Always write the "tilt up" logic first,
     * then rotate the board to simulate other directions.
     */
    public boolean tilt(Side side) {
        boolean changed = false;
        // Rotate board so we always process "up"
        board = rotate(board, side);

        // Process each column independently (for UP direction)
        for (int col = 0; col < board[0].length; col++) {
            changed |= tiltColumn(col);
        }

        // Rotate back
        board = rotateBack(board, side);
        return changed;
    }

    /**
     * Tilt a single column upward.
     * Tiles move up as far as possible.
     * Two adjacent tiles of same value merge (each tile can only merge once).
     */
    private boolean tiltColumn(int col) {
        boolean changed = false;
        int nextFree = 0;       // next free row to place a tile
        int lastMergedRow = -1; // row of last merged tile (can't merge again)

        for (int row = 0; row < board.length; row++) {
            if (board[row][col] != 0) {
                int val = board[row][col];
                if (nextFree > 0
                    && board[nextFree - 1][col] == val
                    && (nextFree - 1) != lastMergedRow) {
                    // Merge!
                    board[nextFree - 1][col] *= 2;
                    score += board[nextFree - 1][col];
                    lastMergedRow = nextFree - 1;
                    board[row][col] = 0;
                    changed = true;
                } else {
                    // Move tile up
                    if (row != nextFree) {
                        board[nextFree][col] = val;
                        board[row][col] = 0;
                        changed = true;
                    }
                    nextFree++;
                }
            }
        }
        return changed;
    }
}
```

### Pattern Connections
| 2048 Mechanic | DSA Pattern |
|---|---|
| Tiles sliding up in a column | Two Pointers (same direction) — Pattern 2 |
| Processing each column independently | Matrix Column Traversal — Pattern 10 |
| Detecting if board changed | State tracking with boolean flag |
| Rotating board to handle all 4 directions | Matrix Rotation — Pattern 10 |

### Self-Assessment Checklist
- [ ] Can implement tilt for a single direction (up)
- [ ] Handles the "merge only once per move" rule correctly
- [ ] Correctly handles empty tiles (value 0)
- [ ] All 4 directions work by rotating board
- [ ] Score tracking works
- [ ] Win (2048 tile appears) and lose (no moves possible) detected correctly

---

## 9.4 Project 1B: ArrayDeque61B — Circular Array Mastery

**Source:** [fa24.datastructur.es/projects/proj1b](https://fa24.datastructur.es/projects/proj1b/)
**Semesters:** FA23, SP24, FA24
**Difficulty:** 🟡 Medium

### What You Build
A double-ended queue using a circular array (ring buffer). This is how Java's `ArrayDeque` actually works internally.

### The Circular Array Trick

```text
Initial state (capacity=8):
Index:  [0] [1] [2] [3] [4] [5] [6] [7]
Value:  [ ]  [A] [B] [C] [ ] [ ] [ ] [ ]
              ↑                   ↑
            front               back

After addFirst(Z):     After addLast(D):
[0] [1] [2] [3] [4]    [0] [1] [2] [3] [4] [5]
[Z] [A] [B] [C] [ ]    [Z] [A] [B] [C] [D] [ ]
 ↑               ↑      ↑                   ↑
front           back   front               back

Wrap-around (front goes to index 7):
Index: [0] [1] [2] [3] [4] [5] [6] [7]
Value: [A] [B] [C] [ ] [ ] [ ] [ ] [Z]
                                     ↑
                                    front  
```

### Core Implementation

```java
public class ArrayDeque61B<T> {
    private T[] data;
    private int front;   // index of first element
    private int back;    // index AFTER last element
    private int size;
    private static final int INITIAL_CAPACITY = 8;
    private static final double MIN_USAGE_RATIO = 0.25;

    @SuppressWarnings("unchecked")
    public ArrayDeque61B() {
        data = (T[]) new Object[INITIAL_CAPACITY];
        front = 0;
        back = 0;
        size = 0;
    }

    /**
     * Adds an element to the front of the deque.
     * The key trick: front moves BACKWARD (with wrap-around).
     * Time: O(1) amortized
     */
    public void addFirst(T x) {
        if (size == data.length) resize(data.length * 2);
        front = (front - 1 + data.length) % data.length; // move front back
        data[front] = x;
        size++;
    }

    /**
     * Adds an element to the back of the deque.
     * Time: O(1) amortized
     */
    public void addLast(T x) {
        if (size == data.length) resize(data.length * 2);
        data[back] = x;
        back = (back + 1) % data.length; // move back forward
        size++;
    }

    /**
     * Removes and returns the element at the front.
     * Time: O(1) amortized
     */
    public T removeFirst() {
        if (size == 0) return null;
        T val = data[front];
        data[front] = null;  // avoid loitering
        front = (front + 1) % data.length;
        size--;
        // Shrink if usage < 25% (only for capacity > 8)
        if (size > 0 && data.length > INITIAL_CAPACITY
                && (double) size / data.length < MIN_USAGE_RATIO) {
            resize(data.length / 2);
        }
        return val;
    }

    /**
     * Removes and returns the element at the back.
     * Time: O(1) amortized
     */
    public T removeLast() {
        if (size == 0) return null;
        back = (back - 1 + data.length) % data.length;
        T val = data[back];
        data[back] = null;  // avoid loitering
        size--;
        if (size > 0 && data.length > INITIAL_CAPACITY
                && (double) size / data.length < MIN_USAGE_RATIO) {
            resize(data.length / 2);
        }
        return val;
    }

    /**
     * Gets element at index i (0 = front).
     * Time: O(1)
     */
    public T get(int i) {
        return data[(front + i) % data.length];
    }

    /**
     * Resize to a new capacity, re-laying out elements from front.
     * Time: O(n)
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        T[] newData = (T[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = get(i); // copy elements in order
        }
        data = newData;
        front = 0;
        back = size;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
}
```

### Complexity Analysis
| Operation | Time Complexity | Why |
|---|---|---|
| addFirst | O(1) amortized | Array doubling: O(n) cost spread over n adds |
| addLast | O(1) amortized | Same |
| removeFirst | O(1) amortized | Array halving: O(n) cost spread over n removes |
| removeLast | O(1) amortized | Same |
| get(i) | O(1) | Direct array index: `(front + i) % length` |
| resize | O(n) | Must copy all n elements |

### Pattern Connections
| Concept | Pattern |
|---|---|
| Circular indexing | Modular arithmetic: `(i + 1) % length` |
| Dynamic resizing | Amortized O(1) — Chapter 3, Asymptotics |
| Shrinking | Keep usage ratio ≥ 25% to avoid wasted space |

### Self-Assessment Checklist
- [ ] Circular array with modulo indexing works correctly
- [ ] addFirst/addLast both work including wrap-around
- [ ] removeFirst/removeLast handle empty deque (return null)
- [ ] Resizing doubles capacity when full
- [ ] Resizing halves capacity when usage < 25%
- [ ] get(i) works correctly as `(front + i) % length`
- [ ] Memory loitering avoided (set removed slots to null)

---

## 9.5 Lab 09: Conway's Game of Life — 2D Array Simulation

**Source:** [fa24.datastructur.es/labs/lab09](https://fa24.datastructur.es/labs/lab09/)
**Semesters:** FA24
**Difficulty:** 🟢 Easy to 🟡 Medium

### What You Build
Conway's Game of Life — a cellular automaton on a 2D grid. Each cell is alive (1) or dead (0). Each generation, cells live or die based on their 8 neighbors.

### Rules
1. A live cell with 2 or 3 live neighbors **survives**
2. A dead cell with exactly 3 live neighbors **becomes alive** (reproduction)
3. All other live cells **die** (under/overpopulation)
4. All other dead cells **stay dead**

### Core Implementation

```java
public class GameOfLife {
    /**
     * Given the current board state, returns the next generation.
     * Uses a NEW board to avoid modifying cells that haven't been processed yet.
     *
     * @param board current state (1 = alive, 0 = dead)
     * @return next generation board
     */
    public static int[][] nextGeneration(int[][] board) {
        int rows = board.length;
        int cols = board[0].length;
        int[][] next = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int liveNeighbors = countLiveNeighbors(board, r, c);
                if (board[r][c] == 1) {
                    // Rule 1: live cell survives with 2 or 3 neighbors
                    next[r][c] = (liveNeighbors == 2 || liveNeighbors == 3) ? 1 : 0;
                } else {
                    // Rule 2: dead cell becomes alive with exactly 3 neighbors
                    next[r][c] = (liveNeighbors == 3) ? 1 : 0;
                }
            }
        }
        return next;
    }

    /**
     * Counts live neighbors of cell (r, c) in all 8 directions.
     * Handles boundary conditions safely.
     */
    private static int countLiveNeighbors(int[][] board, int r, int c) {
        int count = 0;
        // 8 directions: all combinations of (-1, 0, 1) for row and col
        int[] dr = {-1, -1, -1,  0, 0,  1, 1, 1};
        int[] dc = {-1,  0,  1, -1, 1, -1, 0, 1};

        for (int d = 0; d < 8; d++) {
            int nr = r + dr[d];
            int nc = c + dc[d];
            // Check bounds before accessing
            if (nr >= 0 && nr < board.length && nc >= 0 && nc < board[0].length) {
                count += board[nr][nc];
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int[][] board = {
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
        };
        // Blinker pattern — oscillates between vertical and horizontal
        int[][] next = nextGeneration(board);
        for (int[] row : next) {
            System.out.println(java.util.Arrays.toString(row));
        }
        // Output:
        // [0, 0, 0]
        // [1, 1, 1]
        // [0, 0, 0]
    }
}
```

### Pattern Connections
| Game of Life Concept | DSA Pattern |
|---|---|
| 8-direction neighbor check | Matrix traversal with direction arrays — Pattern 10 |
| New board = next gen, old board = current | Snapshot technique (avoids in-place mutation bugs) |
| Boundary checking | Common matrix edge-case handling |

---

## 9.6 Practice Problem Set (Arrays & Strings — CS61B Style)

These problems are inspired by CS61B homework and lab problem styles — they go slightly beyond standard LeetCode because they require designing clean, well-tested Java code.

### Set A: Array Problems

1. **Rotate Matrix 90°** 🟡 — Rotate an N×N matrix in-place clockwise. (Spiral Matrix pattern)
2. **Pascal's Triangle** 🟢 — Generate the first N rows. (2D Array + prefix sum intuition)
3. **Longest Increasing Subarray** 🟢 — Find the length of the longest contiguous increasing subarray. (Sliding window variant)
4. **Maximum Circular Subarray Sum** 🔴 — Max subarray sum in a circular array. (Kadane's + prefix sum combo)
5. **Product of Array Except Self** 🟡 — Without division. (Prefix + suffix product — Prefix Sum pattern)
6. **Trapping Rain Water** 🔴 — Classic Two Pointers or Prefix Sum.
7. **Container With Most Water** 🟡 — Two Pointers (opposite direction).
8. **Jump Game** 🟡 — Can you reach the end? (Greedy / Sliding Window)
9. **Set Matrix Zeroes** 🟡 — In-place matrix modification.
10. **Spiral Matrix** 🟡 — Print in spiral order (Matrix Traversal Pattern 10)

### Set B: String Problems

1. **Valid Anagram** 🟢 — Frequency Map pattern (Pattern 12)
2. **Longest Substring Without Repeating Characters** 🟡 — Sliding Window (Pattern 13)
3. **Valid Palindrome II** 🟡 — Two Pointers + at most 1 deletion
4. **Minimum Window Substring** 🔴 — Variable Sliding Window + Frequency Map combo
5. **Group Anagrams** 🟡 — Sorting + HashMap
6. **Encode and Decode Strings** 🟡 — Design a codec (String manipulation)
7. **Longest Palindromic Substring** 🟡 — Expand Around Center (Pattern 11)
8. **Count Palindromic Substrings** 🟡 — Expand Around Center variation
9. **Find All Anagrams in a String** 🟡 — Sliding Window + Frequency Map
10. **Repeated DNA Sequences** 🟡 — Rolling Hash (Pattern 15)

### Set C: CS61B Style (Design + Implement)

1. **Implement ArrayList from scratch** 🟡 — Resizing array, add, get, remove, indexOf
2. **Implement a circular buffer** 🟡 — Like ArrayDeque61B above
3. **Implement String.split() manually** 🟡 — No built-in split allowed
4. **Implement StringBuilder manually** 🟡 — Char array with dynamic resizing
5. **2D Game of Life** 🟡 — From Lab 09 above
6. **2048 Tilt Logic** 🟢 — From Project 0 above

---

*End of Chapter 9 — CS61B Practice for Arrays & Strings*
