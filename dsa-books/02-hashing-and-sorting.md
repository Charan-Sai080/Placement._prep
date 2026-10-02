# Book 02 — Hashing & Sorting (Java)
### *Senpai DSA Series*

> **"To find a needle in a haystack, you either need a map of the hay, or you need to sort the hay first."**
> Hashing gives you the map. Sorting organizes the hay. Together, they unlock the ability to solve almost any array problem efficiently.

---

## Preface

Array problems often suffer from the curse of O(n²). When you need to find pairs, duplicates, or check conditions across elements, the naive approach is to compare everything with everything. This is where you hit a wall in interviews. You cannot avoid hashing when solving these problems because a Hash Table fundamentally changes the game: it turns an O(n) linear scan into an O(1) instant lookup. When you trade a little bit of space (O(n)) for time (O(n) instead of O(n²)), you unlock the optimal solutions for a vast category of problems.

But Hashing isn't a silver bullet. It doesn't help when you need to maintain order, find the "closest" elements, or process overlapping ranges. This is why sorting unlocks a suite of other techniques. By organizing the data (costing O(n log n)), you enable Two Pointers, Binary Search, and Interval Merging. Sorting exposes the structure of the data, making adjacent elements meaningful and allowing you to make greedy, confident decisions.

How to use this book: This is a combined book because Hashing and Sorting are the two primary tools you apply *to* Arrays and Strings (Book 01). First, learn the concept (Chapter 1 and 4). Understand *how* a HashMap resolves collisions and *why* Quicksort works. Second, learn the Java API (Chapter 2 and 6) so you never stumble on syntax during an interview. Third, study the Patterns (Chapter 3 and 7) — these are the blueprints. Finally, practice. 

The relationship between Book 01 and this book is simple: Book 01 taught you how to walk on the data structure. This book gives you the tools to sprint, organize, and query it instantly.

---

## PART 1: HASHING

### Chapter 1: Concept & Internal Working — Hash Tables

**1.1 The Problem Hashing Solves**

Imagine you are looking for a specific book in a massive, unorganized library of 10,000 books. 
To find your book, you have to look at the first book, then the second, then the third... until you find it. If your book is the last one, you checked 10,000 books. This is an O(n) array scan. It is slow and painful.

Now, imagine the library has a magical catalog index. You give the index the title of the book, and it instantly tells you: *"Aisle 4, Shelf B, Position 12."* You walk straight there. You checked exactly 1 book. This is O(1) time. 

This magical index is a **Hash Table**. Hashing solves the problem of *search time*. By taking a key (like a book title or a string) and passing it through a mathematical function, we instantly calculate the exact index in an array where the value lives. 

**1.2 Hash Functions**

The heart of a Hash Table is the **Hash Function**. A hash function takes an input (the key) and returns an integer (the hash code). 

What makes a good hash function?
1. **Determinism:** The same input must ALWAYS produce the same output. `hash("apple")` must always be `2`.
2. **Uniform Distribution:** It should spread the keys evenly across all available buckets. If every key hashes to `2`, we just built a slow Linked List.
3. **Speed:** It must be fast to compute (O(1)).
4. **Avalanche Effect:** A small change in the input should cause a massive change in the output. `hash("cat")` and `hash("bat")` should be completely different numbers.

For an integer key, the simplest hash function is modulo arithmetic:
`hash = key % tableSize`
If our table size is 10, `key 15` goes to bucket 5, and `key 22` goes to bucket 2.

In Java, every object inherits the `hashCode()` method from `Object`. 
The strict contract of `hashCode()` is: **If two objects are equal according to `equals()`, they MUST return the same `hashCode()`.** But two unequal objects CAN have the same `hashCode()` (this is a collision, which we will cover next).

How does Java hash a String? It uses a polynomial rolling hash. Here is the actual formula conceptually:
```java
int hash = 0;
for (char c : s.toCharArray()) {
    hash = 31 * hash + c;
}
```

Why the magic number `31`?
- **It is prime:** Multiplying by a prime number reduces overlapping patterns (collisions) in the generated hashes.
- **It is odd:** Multiplying by an even number would shift bits left and eventually fill the rightmost bits with zeros, causing information loss (since integer overflow simply drops the highest bits).
- **It is optimized by the JIT compiler:** `31 * x` can be perfectly optimized using bitwise shifts and subtraction: `(x << 5) - x`. The CPU loves this.

*The Risk of Negative Hashes:* 
Since integers in Java are signed, `hashCode()` can overflow and return a negative number. You cannot use a negative number as an array index! 
Always normalize it when building a custom table:
`int index = Math.abs(key.hashCode() % capacity);`
Or, even better, strip the sign bit using bitwise AND:
`int index = (key.hashCode() & 0x7fffffff) % capacity;`

**1.3 Collisions**

A collision happens when two *different* keys produce the *same* bucket index. 
Why are they unavoidable? The **Pigeonhole Principle**. If you have 10 pigeonholes (buckets) and 11 pigeons (keys), at least one hole must contain 2 pigeons. Since the universe of possible Strings is infinite, but our array size is finite, collisions are a mathematical certainty.

ASCII diagram of the problem:
```text
Keys: "apple", "grape", "mango"
hash("apple") % 4 = 2
hash("grape") % 4 = 2  ← COLLISION!
hash("mango") % 4 = 0

Bucket 0: [mango]
Bucket 1: [empty]
Bucket 2: [apple] → [grape]   ← need to handle this!
Bucket 3: [empty]
```

**Strategy 1: Separate Chaining (Java's HashMap approach)**

Instead of storing the element directly in the bucket, each bucket holds a **Linked List** (or a Tree in modern Java). When a collision occurs, we just append the new key-value pair to the end of the list in that bucket.

```text
Bucket 0: → ["mango","v1"] → null
Bucket 1: → null
Bucket 2: → ["apple","v2"] → ["grape","v3"] → null
Bucket 3: → null
```
- **Worst Case:** If the hash function is terrible, all keys land in Bucket 0. The Hash Table degrades into a single Linked List, making operations O(n).
- **Average Case:** With a good hash function and proper resizing, lists stay very short (0, 1, or 2 elements). Operations remain O(1).

**Strategy 2: Open Addressing (Linear Probing)**

Instead of Linked Lists, we store everything directly in the array. If a collision happens, we just look for the *next* available empty slot. We "probe" linearly: `slot + 1`, `slot + 2`, etc., wrapping around to 0 if we hit the end.

```text
Insert "apple" → hash=2 → slot 2 is free → place there
Insert "grape" → hash=2 → slot 2 taken → try slot 3 → free → place there

Array: [ mango | empty | apple | grape ]
         [0]    [1]     [2]     [3]
```
- **Deletion Problem:** If we delete "apple" (slot 2) by making it empty, and later search for "grape" (hash=2), we will look at slot 2, see it is empty, and wrongly conclude "grape" isn't in the table!
- **Fix:** We use a `TOMBSTONE` marker. When deleting, we mark the slot as `DELETED`. Searches continue past tombstones, but inserts can overwrite them.
- **Clustering Problem:** Linear probing suffers from "primary clustering" — blocks of occupied slots merge together, causing long probe sequences. "Quadratic probing" (checking `slot + 1^2`, `slot + 2^2`) helps fix this.

**1.4 Load Factor & Resizing**

As a Hash Table fills up, collisions become more frequent. Performance drops. We need to expand the table.

- **Load factor (α):** The ratio of elements to capacity. `α = n / capacity`
- **Java HashMap default:** Initial capacity = 16. Load factor threshold = 0.75.
- When `α > 0.75` (e.g., reaching 12 elements in a 16-size table), the table **resizes**: it creates a new array double the size (32), and **rehashes** EVERY key to find its new bucket.

```text
Before resize (capacity=4, size=3, α=0.75):
Bucket 0: [mango]
Bucket 1: [empty]
Bucket 2: [apple → grape]
Bucket 3: [empty]

After resize (capacity=8):
Bucket 0: [empty]
Bucket 1: [empty]
Bucket 2: [apple]        ← rehashed
Bucket 3: [empty]
Bucket 4: [mango]        ← rehashed
Bucket 5: [empty]
Bucket 6: [grape]        ← rehashed (no longer collides!)
Bucket 7: [empty]
```
- **Amortized O(1):** Resizing takes O(n) time because we have to re-insert every element. However, because we *double* the size, resizing happens exponentially less often as the table grows. Averaged out over all insertions, the cost of an insert is strictly O(1).

**1.5 Raw HashMap Implementation — Separate Chaining**

To truly understand a HashMap, you must build one. Here is a complete, compilable implementation using Separate Chaining.

```java
public class MyHashMap<K, V> {
    
    // A node for our Linked List chain
    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;
        
        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private Node<K, V>[] buckets;
    private int size;
    private final double LOAD_FACTOR_THRESHOLD = 0.75;
    
    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = new Node[16]; // Initial capacity
        size = 0;
    }
    
    // Helper to find the correct bucket index safely
    private int getIndex(K key, int capacity) {
        if (key == null) return 0; // null keys always go to bucket 0
        return (key.hashCode() & 0x7fffffff) % capacity;
    }
    
    public void put(K key, V value) {
        if (size >= buckets.length * LOAD_FACTOR_THRESHOLD) {
            resize();
        }
        
        int index = getIndex(key, buckets.length);
        Node<K, V> head = buckets[index];
        
        // Scan the chain to see if key already exists (update case)
        Node<K, V> current = head;
        while (current != null) {
            // Must use .equals() for objects!
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                current.value = value; // Update existing
                return;
            }
            current = current.next;
        }
        
        // Key does not exist. Insert at the FRONT of the linked list (O(1) insertion)
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = head;
        buckets[index] = newNode;
        size++;
    }
    
    public V get(K key) {
        int index = getIndex(key, buckets.length);
        Node<K, V> current = buckets[index];
        
        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                return current.value;
            }
            current = current.next;
        }
        return null; // Not found
    }
    
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public void remove(K key) {
        int index = getIndex(key, buckets.length);
        Node<K, V> current = buckets[index];
        Node<K, V> prev = null;
        
        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                if (prev == null) {
                    // Removing the head of the chain
                    buckets[index] = current.next;
                } else {
                    // Removing from the middle or end
                    prev.next = current.next;
                }
                size--;
                return;
            }
            prev = current;
            current = current.next;
        }
    }
    
    public int size() {
        return size;
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = buckets.length * 2;
        Node<K, V>[] newBuckets = new Node[newCapacity];
        
        // Rehash everything
        for (int i = 0; i < buckets.length; i++) {
            Node<K, V> current = buckets[i];
            while (current != null) {
                Node<K, V> nextNode = current.next; // Save next before altering pointers
                
                // Calculate new index in the larger array
                int newIndex = getIndex(current.key, newCapacity);
                
                // Insert at front of new bucket
                current.next = newBuckets[newIndex];
                newBuckets[newIndex] = current;
                
                current = nextNode;
            }
        }
        buckets = newBuckets;
    }

    // Usage example
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("alice", 30);
        map.put("bob", 25);
        map.put("alice", 31); // update
        System.out.println(map.get("alice")); // 31
        System.out.println(map.get("charlie")); // null
        map.remove("bob");
        System.out.println(map.size()); // 1
    }
}
```

**1.6 Raw HashMap Implementation — Open Addressing (Linear Probing)**

Here is how Open Addressing works in practice. Notice how we use parallel arrays and a `deleted` array to act as our tombstones.

```java
public class MyOpenHashMap<K, V> {
    private K[] keys;
    private V[] values;
    private boolean[] occupied;
    private boolean[] deleted; // TOMBSTONE markers
    private int size;
    private int capacity;
    private final double LOAD_FACTOR = 0.5; // Lower load factor needed for open addressing
    
    @SuppressWarnings("unchecked")
    public MyOpenHashMap() {
        capacity = 16;
        keys = (K[]) new Object[capacity];
        values = (V[]) new Object[capacity];
        occupied = new boolean[capacity];
        deleted = new boolean[capacity];
        size = 0;
    }
    
    private int hash(K key) {
        if (key == null) return 0;
        return (key.hashCode() & 0x7fffffff) % capacity;
    }
    
    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
        
        int idx = hash(key);
        int firstDeleted = -1;
        
        // Probe until we find an empty slot that isn't a tombstone, or we find the key
        while (occupied[idx] || deleted[idx]) {
            if (deleted[idx] && firstDeleted == -1) {
                // Remember the first tombstone we see, we can insert here later
                firstDeleted = idx;
            }
            if (occupied[idx] && ((key == null && keys[idx] == null) || (key != null && key.equals(keys[idx])))) {
                // Key exists, update value
                values[idx] = value;
                return;
            }
            idx = (idx + 1) % capacity; // Linear probe
        }
        
        // Insert at the first available slot (either an empty slot or a recycled tombstone)
        int targetIdx = (firstDeleted != -1) ? firstDeleted : idx;
        keys[targetIdx] = key;
        values[targetIdx] = value;
        occupied[targetIdx] = true;
        deleted[targetIdx] = false;
        size++;
    }
    
    public V get(K key) {
        int idx = hash(key);
        while (occupied[idx] || deleted[idx]) {
            if (occupied[idx] && ((key == null && keys[idx] == null) || (key != null && key.equals(keys[idx])))) {
                return values[idx];
            }
            idx = (idx + 1) % capacity;
        }
        return null;
    }
    
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public void remove(K key) {
        int idx = hash(key);
        while (occupied[idx] || deleted[idx]) {
            if (occupied[idx] && ((key == null && keys[idx] == null) || (key != null && key.equals(keys[idx])))) {
                // Found it. Delete using a TOMBSTONE.
                // We cannot just set occupied=false, because that breaks the probe chain for other keys!
                occupied[idx] = false;
                deleted[idx] = true;
                keys[idx] = null;
                values[idx] = null;
                size--;
                return;
            }
            idx = (idx + 1) % capacity;
        }
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        K[] oldKeys = keys;
        V[] oldValues = values;
        boolean[] oldOccupied = occupied;
        
        keys = (K[]) new Object[capacity];
        values = (V[]) new Object[capacity];
        occupied = new boolean[capacity];
        deleted = new boolean[capacity];
        size = 0; // Will be rebuilt during put()
        
        for (int i = 0; i < oldCapacity; i++) {
            if (oldOccupied[i]) {
                put(oldKeys[i], oldValues[i]); // Re-insert valid keys
            }
        }
    }
}
```

**1.7 Raw HashSet Implementation**

A `HashSet` is essentially a `HashMap` where you only care about the keys. Java's actual `java.util.HashSet` is internally backed by a `HashMap`. It just stores a dummy object as the value for every key to reuse the exact same logic.

```java
public class MyHashSet<K> {
    // A dummy constant value to store in the map
    private static final Object PRESENT = new Object();
    private MyHashMap<K, Object> map = new MyHashMap<>();
    
    public boolean add(K key) { 
        // put returns null if the key was not already present
        if (map.containsKey(key)) {
            return false;
        }
        map.put(key, PRESENT); 
        return true; 
    }
    
    public boolean contains(K key) { 
        return map.containsKey(key); 
    }
    
    public boolean remove(K key) { 
        if (map.containsKey(key)) {
            map.remove(key); 
            return true;
        }
        return false;
    }
    
    public int size() { 
        return map.size(); 
    }
}
```


### Chapter 2: Java Hashing API — Complete Reference

This chapter is your reference guide. Bookmark it. In an interview, you should never waste mental energy trying to remember if the method is `contains()` or `containsKey()`. 

#### 2.1 java.util.HashMap<K, V>

**Class overview:** A hash table based implementation of the `Map` interface. It stores data in key-value pairs. It provides O(1) average time complexity for basic operations (`get` and `put`). It allows exactly one `null` key and multiple `null` values. It is **not** thread-safe and makes no guarantees about the iteration order of the map.

**Constructors:**
```java
HashMap() // Default initial capacity (16) and load factor (0.75).
HashMap(int initialCapacity) // Constructs an empty HashMap with the specified initial capacity and default load factor (0.75).
HashMap(int initialCapacity, float loadFactor) // Custom capacity and load factor.
HashMap(Map<? extends K,? extends V> m) // Constructs a new HashMap with the same mappings as the specified Map.
```

**Methods:**

`V put(K key, V value)` — Associates the specified value with the specified key in this map. Returns the *previous* value associated with the key, or `null` if there was no mapping for the key.
```java
Map<String, Integer> map = new HashMap<>();
map.put("Java", 8); // returns null
Integer oldVal = map.put("Java", 11); // returns 8
```

`V get(Object key)` — Returns the value to which the specified key is mapped, or `null` if this map contains no mapping for the key.
```java
Integer v = map.get("Java"); // returns 11
Integer missing = map.get("Python"); // returns null
```

`V getOrDefault(Object key, V defaultValue)` — Returns the value to which the specified key is mapped, or `defaultValue` if this map contains no mapping for the key.
```java
int count = map.getOrDefault("C++", 0); // returns 0
```

`boolean containsKey(Object key)` — Returns `true` if this map contains a mapping for the specified key.
```java
boolean hasJava = map.containsKey("Java"); // true
```

`boolean containsValue(Object value)` — Returns `true` if this map maps one or more keys to the specified value. **Warning: This is O(n), not O(1)!** It has to scan the whole table.
```java
boolean hasEleven = map.containsValue(11); // true
```

`V remove(Object key)` — Removes the mapping for the specified key from this map if present. Returns the previous value associated with the key.
```java
map.remove("Java"); // removes key and returns 11
```

`boolean remove(Object key, Object value)` — Removes the entry for the specified key only if it is currently mapped to the specified value. (Atomic operation).
```java
map.put("Go", 1);
map.remove("Go", 2); // returns false, "Go" is not mapped to 2
map.remove("Go", 1); // returns true, entry is removed
```

`V putIfAbsent(K key, V value)` — If the specified key is not already associated with a value (or is mapped to `null`), associates it with the given value and returns `null`, else returns the current value.
```java
map.putIfAbsent("Rust", 1); // Returns null, inserts "Rust" -> 1
map.putIfAbsent("Rust", 2); // Returns 1, does NOT update the value
```

`V replace(K key, V value)` — Replaces the entry for the specified key only if it is currently mapped to some value.
```java
map.replace("Rust", 5); // Replaces 1 with 5
```

`boolean replace(K key, V oldValue, V newValue)` — Replaces the entry for the specified key only if currently mapped to the specified value.
```java
map.replace("Rust", 1, 10); // Returns false
map.replace("Rust", 5, 10); // Returns true, updates to 10
```

`V merge(K key, V value, BiFunction<? super V,? super V,? extends V> remappingFunction)` — If the specified key is not already associated with a value, associates it with the given value. Otherwise, replaces the associated value with the results of the given remapping function.
```java
// Great for frequency counting
map.merge("Rust", 1, Integer::sum); // If "Rust" exists, adds 1 to its value. Else inserts 1.
```

`V compute(K key, BiFunction<? super K,? super V,? extends V> remappingFunction)` — Attempts to compute a mapping for the specified key and its current mapped value (or `null` if there is no current mapping).
```java
map.compute("Rust", (k, v) -> (v == null) ? 1 : v + 1);
```

`V computeIfAbsent(K key, Function<? super K,? extends V> mappingFunction)` — If the specified key is not already associated with a value, attempts to compute its value using the given mapping function and enters it into this map.
```java
// Very useful for grouping items into lists
Map<Integer, List<String>> groups = new HashMap<>();
groups.computeIfAbsent(5, k -> new ArrayList<>()).add("apple");
```

`V computeIfPresent(K key, BiFunction<? super K,? super V,? extends V> remappingFunction)` — If the value for the specified key is present and non-null, attempts to compute a new mapping given the key and its current mapped value.
```java
map.computeIfPresent("Rust", (k, v) -> v * 2);
```

`Set<K> keySet()` — Returns a `Set` view of the keys contained in this map.
```java
for (String key : map.keySet()) {
    System.out.println(key);
}
```

`Collection<V> values()` — Returns a `Collection` view of the values contained in this map.
```java
for (Integer val : map.values()) {
    System.out.println(val);
}
```

`Set<Map.Entry<K,V>> entrySet()` — Returns a `Set` view of the mappings contained in this map. This is the most efficient way to iterate over both keys and values.
```java
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " -> " + entry.getValue());
}
```

`void forEach(BiConsumer<? super K,? super V> action)` — Performs the given action for each entry in this map.
```java
map.forEach((k, v) -> System.out.println(k + " -> " + v));
```

`int size()` — Returns the number of key-value mappings.
`boolean isEmpty()` — Returns `true` if this map contains no key-value mappings.
`void clear()` — Removes all of the mappings from this map.

**Frequency Counting Approaches (A comparison):**
```java
String word = "hello";
Map<Character, Integer> freq = new HashMap<>();

// Approach 1: getOrDefault (The most common, very readable)
for (char c : word.toCharArray()) {
    freq.put(c, freq.getOrDefault(c, 0) + 1);
}

// Approach 2: merge (More functional, slightly more concise)
for (char c : word.toCharArray()) {
    freq.merge(c, 1, Integer::sum);
}

// Approach 3: computeIfAbsent (Best for grouping, NOT frequency counting)
// Note: computeIfAbsent expects a Function that takes a key, not a BiFunction that takes an old value.
// It is used like this to group anagrams:
Map<String, List<String>> anagramGroups = new HashMap<>();
anagramGroups.computeIfAbsent(sortedKey, k -> new ArrayList<>()).add(originalWord);
```

#### 2.2 java.util.HashSet<E>

**Class overview:** A collection that contains no duplicate elements. It is backed by a `HashMap` internally. Offers O(1) average time complexity for basic operations.

**Constructors:**
```java
HashSet()
HashSet(int initialCapacity)
HashSet(int initialCapacity, float loadFactor)
HashSet(Collection<? extends E> c) // Constructs a new set containing the elements in the specified collection.
```

**Methods:**

`boolean add(E e)` — Adds the specified element to this set if it is not already present. Returns `true` if the set did not already contain the specified element. (If it returns `false`, it means it was a duplicate!).
```java
Set<Integer> set = new HashSet<>();
set.add(1); // true
set.add(1); // false
```

`boolean remove(Object o)` — Removes the specified element from this set if it is present. Returns `true` if the set contained the specified element.
```java
set.remove(1); // true
```

`boolean contains(Object o)` — Returns `true` if this set contains the specified element.
```java
set.contains(2); // false
```

`int size()` — Returns the number of elements in this set.
`boolean isEmpty()` — Returns `true` if this set contains no elements.
`void clear()` — Removes all of the elements from this set.

`Iterator<E> iterator()` — Returns an iterator over the elements in this set.
```java
Iterator<Integer> it = set.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}
```

`Object[] toArray()` — Returns an array containing all of the elements in this set.
```java
Object[] arr = set.toArray();
Integer[] typedArr = set.toArray(new Integer[0]); // Recommended way for typed array
```

**Set Operations:**
```java
Set<Integer> a = new HashSet<>(Arrays.asList(1, 2, 3, 4));
Set<Integer> b = new HashSet<>(Arrays.asList(3, 4, 5, 6));

// Union
Set<Integer> union = new HashSet<>(a);
union.addAll(b); // {1,2,3,4,5,6}

// Intersection
Set<Integer> intersection = new HashSet<>(a);
intersection.retainAll(b); // {3,4}

// Difference (a - b)
Set<Integer> diff = new HashSet<>(a);
diff.removeAll(b); // {1,2}

// Subset Check
boolean isSubset = a.containsAll(b); // false
```

#### 2.3 java.util.LinkedHashMap<K, V>

**Class overview:** A hash table and linked list implementation of the `Map` interface, with predictable iteration order. It maintains a doubly-linked list running through all of its entries. This linked list defines the iteration ordering, which is normally the order in which keys were inserted into the map (**insertion-order**).

It can also be configured to iterate in **access-order** (from least-recently accessed to most-recently accessed), which is perfect for LRU caches.

**Unique features:**
- Iterating over a `LinkedHashMap` requires time proportional to the *size* of the map, regardless of its capacity. (A `HashMap` iterates over the capacity, which can be slower if it's mostly empty).
- Slightly higher memory footprint than `HashMap` due to the `next` and `prev` pointers.

**The LRU Cache trick:**
By overriding the `removeEldestEntry` method, you can have the map automatically delete the oldest entry when it reaches a certain size.

```java
int capacity = 100;
// Note the 'true' flag at the end for access-order!
LinkedHashMap<Integer, Integer> lruCache = new LinkedHashMap<>(capacity, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > capacity; // evict when over capacity
    }
};

lruCache.put(1, 10);
lruCache.put(2, 20);
lruCache.get(1); // accessing key 1 moves it to the "most recently used" end of the list
// If capacity was 2, inserting a new item now would evict key 2, not key 1!
```

#### 2.4 java.util.TreeMap<K, V>

**Class overview:** A Red-Black tree based `NavigableMap` implementation. The map is sorted according to the natural ordering of its keys, or by a `Comparator` provided at map creation time. All basic operations (`containsKey`, `get`, `put` and `remove`) are **O(log n)** time cost.

**Constructors:**
```java
TreeMap()
TreeMap(Comparator<? super K> comparator)
TreeMap(Map<? extends K,? extends V> m)
```

**Navigation Methods (Unique to NavigableMap):**

`K firstKey()` — Returns the first (lowest) key currently in this map.
`K lastKey()` — Returns the last (highest) key currently in this map.
```java
TreeMap<Integer, String> map = new TreeMap<>();
map.put(10, "A");
map.put(20, "B");
map.put(30, "C");
map.firstKey(); // 10
map.lastKey();  // 30
```

`Map.Entry<K,V> firstEntry()` — Returns a key-value mapping associated with the least key in this map, or `null` if the map is empty.
`Map.Entry<K,V> lastEntry()` — Returns a key-value mapping associated with the greatest key in this map, or `null` if the map is empty.
```java
map.firstEntry().getValue(); // "A"
```

`K floorKey(K key)` — Returns the greatest key less than or equal to the given key, or `null` if there is no such key.
`K ceilingKey(K key)` — Returns the least key greater than or equal to the given key, or `null` if there is no such key.
```java
map.floorKey(25);   // 20 (Greatest key <= 25)
map.ceilingKey(25); // 30 (Least key >= 25)
```

`K lowerKey(K key)` — Returns the greatest key strictly less than the given key, or `null` if there is no such key.
`K higherKey(K key)` — Returns the least key strictly greater than the given key, or `null` if there is no such key.
```java
map.lowerKey(20);  // 10 (Strictly < 20)
map.higherKey(20); // 30 (Strictly > 20)
```

`Map.Entry<K,V> pollFirstEntry()` — Removes and returns a key-value mapping associated with the least key in this map, or `null` if the map is empty.
`Map.Entry<K,V> pollLastEntry()` — Removes and returns a key-value mapping associated with the greatest key in this map, or `null` if the map is empty.

`NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)` — Returns a view of the portion of this map whose keys range from `fromKey` to `toKey`.
`NavigableMap<K,V> headMap(K toKey, boolean inclusive)` — Returns a view of the portion of this map whose keys are less than (or equal to, if inclusive is true) `toKey`.
`NavigableMap<K,V> tailMap(K fromKey, boolean inclusive)` — Returns a view of the portion of this map whose keys are greater than (or equal to, if inclusive is true) `fromKey`.
`NavigableMap<K,V> descendingMap()` — Returns a reverse order view of the mappings contained in this map.

**Practical Example:**
Given events at timestamps, find the most recent event before time T:
```java
TreeMap<Integer, String> events = new TreeMap<>();
events.put(100, "login");
events.put(200, "click");
events.put(300, "logout");

// Most recent event before time 250
Map.Entry<Integer, String> e = events.floorEntry(250);
System.out.println(e.getValue()); // "click"
```

#### 2.5 java.util.TreeSet<E>

**Class overview:** A `NavigableSet` implementation based on a `TreeMap`. The elements are ordered using their natural ordering, or by a `Comparator` provided at set creation time. Provides guaranteed O(log n) time cost for the basic operations (`add`, `remove` and `contains`).

**Navigation Methods:**

`E first()` — Returns the first (lowest) element currently in this set.
`E last()` — Returns the last (highest) element currently in this set.
```java
TreeSet<Integer> set = new TreeSet<>(Arrays.asList(10, 20, 30));
set.first(); // 10
set.last();  // 30
```

`E floor(E e)` — Returns the greatest element in this set less than or equal to the given element, or `null` if there is no such element.
`E ceiling(E e)` — Returns the least element in this set greater than or equal to the given element, or `null` if there is no such element.
`E lower(E e)` — Returns the greatest element in this set strictly less than the given element, or `null` if there is no such element.
`E higher(E e)` — Returns the least element in this set strictly greater than the given element, or `null` if there is no such element.

`E pollFirst()` — Retrieves and removes the first (lowest) element, or returns `null` if this set is empty.
`E pollLast()` — Retrieves and removes the last (highest) element, or returns `null` if this set is empty.

`NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)`
`NavigableSet<E> headSet(E toElement, boolean inclusive)`
`NavigableSet<E> tailSet(E fromElement, boolean inclusive)`
`NavigableSet<E> descendingSet()` — Returns a reverse order view of the elements contained in this set.

#### 2.6 Comparison Table

| Feature | `HashMap` | `LinkedHashMap` | `TreeMap` | `HashSet` | `TreeSet` |
|---|---|---|---|---|---|
| **Ordering** | None | Insertion / Access | Sorted (key) | None | Sorted |
| **`null` keys** | 1 allowed | 1 allowed | NOT allowed | 1 allowed | NOT allowed |
| **get/put/remove** | O(1) avg | O(1) avg | O(log n) | O(1) avg | O(log n) |
| **Thread-safe?** | No | No | No | No | No |
| **Use when...** | You need fast O(1) lookups and order doesn't matter. | You need O(1) lookups but also need to maintain the order elements were added (or accessed). | You need keys always sorted, or need range queries (e.g. "closest value"). | You need to keep track of unique items in O(1) time. | You need unique items always sorted. |


### Chapter 3: Hashing Patterns

#### Pattern 1: Frequency Counting

**1. Concept — What and Why**
The most fundamental use of a HashMap is counting how many times an element appears in an array or string. Instead of iterating through the array for every element to count its occurrences (O(n²)), you iterate once and maintain a running count in a HashMap (O(n)). For strings with only lowercase English letters, a fixed `int[26]` array is faster and uses less memory, acting as a perfect, collision-free Hash Table.

**2. Trigger Words**
"count", "frequency", "most common", "appears k times", "anagram"

**3. Visual**
```text
Input: "hello"

Step 1: 'h' -> freq = {'h':1}
Step 2: 'e' -> freq = {'h':1, 'e':1}
Step 3: 'l' -> freq = {'h':1, 'e':1, 'l':1}
Step 4: 'l' -> freq = {'h':1, 'e':1, 'l':2}
Step 5: 'o' -> freq = {'h':1, 'e':1, 'l':2, 'o':1}
```

**4. Template Code**
```java
Map<Character, Integer> freq = new HashMap<>();
for (char c : s.toCharArray()) {
    freq.put(c, freq.getOrDefault(c, 0) + 1);
}
```

**5. Worked Example — Valid Anagram (LeetCode 242)**
Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`, and `false` otherwise.

*Thought Process:* If `t` is an anagram of `s`, both strings must have the exact same character frequencies. We can count the frequencies of characters in `s` by adding, and then iterate through `t` and subtract. If all frequencies end up as 0, they are anagrams. Since the problem specifies lowercase English letters, we will use the `int[26]` variation.

```java
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;
    
    int[] count = new int[26];
    
    // Count up for s, count down for t
    for (int i = 0; i < s.length(); i++) {
        count[s.charAt(i) - 'a']++;
        count[t.charAt(i) - 'a']--;
    }
    
    // If all are zero, it's an anagram
    for (int c : count) {
        if (c != 0) return false;
    }
    return true;
}
```

**6. Dry Run Table**
Input: `s = "aba"`, `t = "aab"`

| i | `s.charAt(i)` | `t.charAt(i)` | `count['a'-'a']` | `count['b'-'a']` |
|---|---|---|---|---|
| 0 | 'a' (+1) | 'a' (-1) | 0 | 0 |
| 1 | 'b' (+1) | 'a' (-1) | -1 | 1 |
| 2 | 'a' (+1) | 'b' (-1) | 0 | 0 |

Final `count` array is all 0s. Return true.

**7. Time & Space Complexity**
- Time: O(n) where n is the length of the string. We iterate through the string once, and the array once (26 elements).
- Space: O(1) because the `count` array size is fixed at 26, regardless of string length. (If using HashMap for arbitrary characters, space is O(k) where k is unique characters).

**8. Common Mistakes**
- Forgetting to check if the lengths are equal first. Anagrams must have equal lengths.
- Using a HashMap when the character set is restricted (e.g., lowercase letters). Always prefer an array if the universe of keys is small and contiguous.

**9. Practice Problems**
- 🟢 Valid Anagram (LeetCode 242)
- 🟢 First Unique Character in a String (LeetCode 387)
- 🟢 Find All Numbers Disappeared in an Array (LeetCode 448)
- 🟡 Find All Duplicates in an Array (LeetCode 442)
- 🟡 Sort Characters By Frequency (LeetCode 451)

#### Pattern 2: Two Sum Pattern (Complement Lookup)

**1. Concept — What and Why**
Instead of looking forward for a pair that sums to a target (O(n²)), we look backward. As we iterate through the array, we ask: "Have I seen the complement I need?" We store every number we visit in a HashMap. For each new number, we calculate its `complement = target - current_number`. We check if this complement exists in our map. If it does, we found the pair in O(n) time.

**2. Trigger Words**
"pair", "two elements", "sum equals", "target"

**3. Visual**
```text
Target = 9
Array: [2, 7, 11, 15]

i=0, num=2: Complement needed = 9 - 2 = 7. Seen 7? No. Add 2 to map.
i=1, num=7: Complement needed = 9 - 7 = 2. Seen 2? YES! Return pair.
```

**4. Template Code**
```java
Map<Integer, Integer> seen = new HashMap<>(); // value -> index
for (int i = 0; i < nums.length; i++) {
    int complement = target - nums[i];
    if (seen.containsKey(complement)) {
        return new int[]{seen.get(complement), i};
    }
    seen.put(nums[i], i);
}
```

**5. Worked Example — Two Sum (LeetCode 1)**
Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`.

```java
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> seen = new HashMap<>();
    
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        
        if (seen.containsKey(complement)) {
            return new int[] { seen.get(complement), i };
        }
        
        seen.put(nums[i], i);
    }
    
    return new int[] {}; // Should not reach here if guaranteed solution exists
}
```

**6. Dry Run Table**
Input: `nums = [2, 7, 11, 15]`, `target = 9`

| i | `nums[i]` | complement | `seen` map | Action |
|---|---|---|---|---|
| 0 | 2 | 7 | {} | 7 not in map, put (2: 0) |
| 1 | 7 | 2 | {2: 0} | 2 IS in map. Found pair! |

Return `[0, 1]`

**7. Time & Space Complexity**
- Time: O(n). We traverse the array containing n elements exactly once. Each lookup in the table costs O(1) time.
- Space: O(n). The extra space required depends on the number of items stored in the hash table, which stores at most n elements.

**8. Common Mistakes**
- Putting all elements in the map *first*, then doing the lookup pass. This can mistakenly pair an element with itself if `target == 2 * num`. By doing a single pass and looking *backward*, you guarantee you only pair with distinct indices.
- Overflow when `target` and `nums[i]` are very large. `target - nums[i]` can overflow. Use `long` if constraints are large.

**9. Practice Problems**
- 🟢 Two Sum (LeetCode 1)
- 🟡 4Sum II (LeetCode 454)
- 🟡 Number of Pairs of Strings With Concatenation Equal to Target (LeetCode 2023)
- 🟡 Max Number of K-Sum Pairs (LeetCode 1679)
- 🟡 Pairs of Songs With Total Durations Divisible by 60 (LeetCode 1010)

#### Pattern 3: Prefix Sum + HashMap (Subarray Sum)

**1. Concept — What and Why**
A continuous subarray sum can be calculated by subtracting two prefix sums: `Sum(i to j) = PrefixSum(j) - PrefixSum(i-1)`. 
Therefore, if we want a subarray sum to equal `k`, we are looking for a state where:
`PrefixSum(j) - PrefixSum(i-1) = k`
Rearranging this:
`PrefixSum(i-1) = PrefixSum(j) - k`
As we iterate and calculate the current prefix sum `PrefixSum(j)`, we just check our HashMap to see if we have previously encountered the prefix sum `PrefixSum(j) - k`. If we have, it means there is a valid subarray ending at `j`! We map the prefix sum to its *frequency* because there might be multiple previous indices with that same sum (especially if the array has zeros or negative numbers).

**2. Trigger Words**
"subarray sum equals", "number of subarrays", "contiguous subarray"

**3. Visual**
```text
Array: [1, -1, 1, 1, 1, 1], k = 3
Prefix: [1, 0, 1, 2, 3, 4]
                 ^
At index 4, prefixSum is 3. We want k=3. 
We look back in our map for (3 - 3) = 0.
We see prefixSum=0 occurred once. 
So, there is 1 valid subarray ending here.
```

**4. Template Code**
```java
Map<Integer, Integer> prefixCount = new HashMap<>();
prefixCount.put(0, 1); // Crucial: represents an empty prefix
int prefixSum = 0, count = 0;
for (int num : nums) {
    prefixSum += num;
    count += prefixCount.getOrDefault(prefixSum - k, 0);
    prefixCount.put(prefixSum, prefixCount.getOrDefault(prefixSum, 0) + 1);
}
```

**5. Worked Example — Subarray Sum Equals K (LeetCode 560)**
Given an array of integers `nums` and an integer `k`, return the total number of continuous subarrays whose sum equals to `k`.

```java
public int subarraySum(int[] nums, int k) {
    int count = 0;
    int prefixSum = 0;
    Map<Integer, Integer> map = new HashMap<>();
    
    // Initialize map with {0: 1} to handle subarrays starting from index 0
    map.put(0, 1); 
    
    for (int num : nums) {
        prefixSum += num;
        
        // If (prefixSum - k) exists in map, it means we found valid subarrays
        if (map.containsKey(prefixSum - k)) {
            count += map.get(prefixSum - k);
        }
        
        // Record the current prefixSum
        map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
    }
    
    return count;
}
```

**6. Dry Run Table**
Input: `nums = [1, 1, 1]`, `k = 2`
Initial state: `map = {0: 1}`, `count = 0`, `prefixSum = 0`

| i | num | prefixSum | prefixSum - k | map | count |
|---|---|---|---|---|---|
| 0 | 1 | 1 | 1 - 2 = -1 | {0:1, 1:1} | 0 |
| 1 | 1 | 2 | 2 - 2 = 0 | {0:1, 1:1, 2:1} | 0 + 1 = 1 (map has 0) |
| 2 | 1 | 3 | 3 - 2 = 1 | {0:1, 1:1, 2:1, 3:1} | 1 + 1 = 2 (map has 1) |

Return 2.

**7. Time & Space Complexity**
- Time: O(n) to iterate through the array once. HashMap lookups and inserts are O(1) on average.
- Space: O(n) in the worst case where every prefix sum is distinct and stored in the HashMap.

**8. Common Mistakes**
- Forgetting to initialize the map with `{0: 1}`. Without this, valid subarrays that start from index 0 will be missed (because their `prefixSum - k` would be `0`, and the map wouldn't contain it).

**9. Practice Problems**
- 🟡 Subarray Sum Equals K (LeetCode 560)
- 🟡 Subarray Sums Divisible by K (LeetCode 974)
- 🟡 Continuous Subarray Sum (LeetCode 523)
- 🟡 Contiguous Array (LeetCode 525)
- 🟡 Maximum Size Subarray Sum Equals k (LeetCode 325)

#### Pattern 4: Group By Canonical Key

**1. Concept — What and Why**
When you need to group related elements together (e.g., anagrams, items in the same row/column, equivalent expressions), you must invent a **canonical key** — a standardized representation that all equivalent items will share. You then map this canonical key to a List of the original items.
For example, the canonical key for anagrams could be the string sorted alphabetically ("eat", "tea", and "ate" all sort to "aet"). Or it could be a frequency string ("a1e1t1").

**2. Trigger Words**
"group", "anagram", "equivalent", "same characters"

**3. Visual**
```text
Input: ["eat", "tea", "tan", "ate", "nat", "bat"]

"eat" -> sorted: "aet" -> map{"aet": ["eat"]}
"tea" -> sorted: "aet" -> map{"aet": ["eat", "tea"]}
"tan" -> sorted: "ant" -> map{"aet": ["eat", "tea"], "ant": ["tan"]}
"ate" -> sorted: "aet" -> map{"aet": ["eat", "tea", "ate"], "ant": ["tan"]}
```

**4. Template Code**
```java
Map<String, List<String>> groups = new HashMap<>();
for (String word : words) {
    // 1. Generate canonical key
    char[] chars = word.toCharArray();
    Arrays.sort(chars);
    String key = new String(chars);
    
    // 2. Group using computeIfAbsent
    groups.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
}
```

**5. Worked Example — Group Anagrams (LeetCode 49)**
Given an array of strings `strs`, group the anagrams together.

```java
public List<List<String>> groupAnagrams(String[] strs) {
    if (strs == null || strs.length == 0) return new ArrayList<>();
    
    Map<String, List<String>> map = new HashMap<>();
    
    for (String s : strs) {
        // Create frequency-based key (faster than sorting for short strings)
        char[] count = new char[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        
        // Use String.valueOf to convert char array to a String key
        String key = String.valueOf(count);
        
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
    }
    
    return new ArrayList<>(map.values());
}
```

**6. Dry Run Table**
Input: `["eat","tea","tan","ate","nat","bat"]` (using sort-key for simplicity in table)

| word | key (sorted) | map state |
|---|---|---|
| "eat" | "aet" | {"aet": ["eat"]} |
| "tea" | "aet" | {"aet": ["eat", "tea"]} |
| "tan" | "ant" | {"aet": ["eat", "tea"], "ant": ["tan"]} |
| "ate" | "aet" | {"aet": ["eat", "tea", "ate"], "ant": ["tan"]} |
| "nat" | "ant" | {"aet": ["eat", "tea", "ate"], "ant": ["tan", "nat"]} |
| "bat" | "abt" | {"aet": ["eat", "tea", "ate"], "ant": ["tan", "nat"], "abt": ["bat"]} |

**7. Time & Space Complexity**
- Time: O(n * k log k) if sorting keys (n = number of strings, k = max string length). O(n * k) if using frequency array keys.
- Space: O(n * k) to store all strings in the Map.

**8. Common Mistakes**
- Using raw arrays (like `int[]`) as the key in a `HashMap`. Java arrays do not override `hashCode()` or `equals()`! Two different `int[]` with identical contents will hash differently. You must convert the array to a String or a List before using it as a key.

**9. Practice Problems**
- 🟡 Group Anagrams (LeetCode 49)
- 🟡 Group Shifted Strings (LeetCode 249)
- 🟡 Valid Sudoku (LeetCode 36) - (Group by row/col/box)
- 🟡 Find Duplicate File in System (LeetCode 609)
- 🔴 Minimum Number of Frogs Croaking (LeetCode 1419)

#### Pattern 5: Longest Consecutive Sequence

**1. Concept — What and Why**
To find the longest consecutive sequence in an unsorted array in O(n) time, we can't sort it (O(n log n)). Instead, we dump everything into a HashSet to get O(1) lookups. 
The trick to keeping it O(n) overall is to *only start counting sequences from their true beginning*. If a number `n` has `n - 1` in the set, it is in the middle of a sequence, so we ignore it. We only start our `while` loop to count upwards when we find a number where `!set.contains(n - 1)`.

**2. Trigger Words**
"consecutive", "sequence", "consecutive integers", "O(n) time requirement"

**3. Visual**
```text
Array: [100, 4, 200, 1, 3, 2]
Set: {100, 4, 200, 1, 3, 2}

Look at 100: is 99 in set? No. Start sequence! -> [100], length 1.
Look at 4: is 3 in set? Yes. Skip! (we will process this sequence when we hit 1).
Look at 200: is 199 in set? No. Start sequence! -> [200], length 1.
Look at 1: is 0 in set? No. Start sequence! -> [1, 2, 3, 4], length 4.
Look at 3: is 2 in set? Yes. Skip!
Look at 2: is 1 in set? Yes. Skip!
```

**4. Template Code**
```java
Set<Integer> numSet = new HashSet<>();
for (int n : nums) numSet.add(n);
int longest = 0;
for (int n : numSet) {
    if (!numSet.contains(n - 1)) { // n is a sequence start
        int length = 1;
        while (numSet.contains(n + length)) length++;
        longest = Math.max(longest, length);
    }
}
```

**5. Worked Example — Longest Consecutive Sequence (LeetCode 128)**
Given an unsorted array of integers `nums`, return the length of the longest consecutive elements sequence. You must write an algorithm that runs in O(n) time.

```java
public int longestConsecutive(int[] nums) {
    Set<Integer> numSet = new HashSet<>();
    for (int num : nums) {
        numSet.add(num);
    }
    
    int longestStreak = 0;
    
    for (int num : numSet) {
        // Only do work if 'num' is the START of a sequence
        if (!numSet.contains(num - 1)) {
            int currentNum = num;
            int currentStreak = 1;
            
            // Count upwards from the start
            while (numSet.contains(currentNum + 1)) {
                currentNum += 1;
                currentStreak += 1;
            }
            
            longestStreak = Math.max(longestStreak, currentStreak);
        }
    }
    
    return longestStreak;
}
```

**6. Dry Run Table**
Input: `nums = [100, 4, 200, 1, 3, 2]`

| n | `contains(n-1)?` | Action (while loop) | currentStreak | longest |
|---|---|---|---|---|
| 100 | false | loop checks 101 (fails) | 1 | 1 |
| 4 | true | SKIP (not start) | - | 1 |
| 200 | false | loop checks 201 (fails) | 1 | 1 |
| 1 | false | checks 2(y), 3(y), 4(y), 5(n) | 4 | 4 |
| 3 | true | SKIP | - | 4 |
| 2 | true | SKIP | - | 4 |

**7. Time & Space Complexity**
- Time: O(n). Although there is a `while` loop inside a `for` loop, the `while` loop only runs for the *start* of a sequence. Every element in the array is visited at most twice: once in the outer loop, and at most once in the inner `while` loop. O(n + n) = O(n).
- Space: O(n) to store all elements in the HashSet.

**8. Common Mistakes**
- Iterating over the original `nums` array instead of the `HashSet` in the second pass. Iterating over `nums` works but can cause you to redundantly process duplicate numbers.
- Forgetting the crucial `!numSet.contains(num - 1)` check, resulting in O(n²) time complexity for worst-case inputs like `[1, 2, 3, 4, 5]`.

**9. Practice Problems**
- 🟡 Longest Consecutive Sequence (LeetCode 128)

#### Pattern 6: Top K Elements

**1. Concept — What and Why**
Many problems ask for the "top k" most frequent, largest, or closest elements. The standard pipeline is:
1. Build a frequency map (O(n)).
2. Sort the keys based on their values.

If you sort all keys, it takes O(U log U) where U is unique elements. If you use a Min-Heap of size k, it takes O(U log k). 
However, for frequency problems specifically, since the maximum frequency is bounded by `n`, you can use **Bucket Sort** to achieve strictly **O(n)** time. Create an array of Lists where the index is the frequency.

**2. Trigger Words**
"top k", "most frequent", "k most", "k largest"

**3. Visual**
```text
Input: [1, 1, 1, 2, 2, 3], k = 2
Freq Map: {1: 3, 2: 2, 3: 1}

Bucket Array (index = frequency):
Index:  0    1      2      3      4      5      6
       [ ]  [3]    [2]    [1]    [ ]    [ ]    [ ]

Read buckets backwards to get top k:
Bucket 3: add 1
Bucket 2: add 2
Done (found 2 elements).
```

**4. Template Code (Bucket Sort, O(n))**
```java
List<Integer>[] buckets = new List[nums.length + 1];
// bucket[i] = list of numbers that appear exactly i times
```

**5. Worked Example — Top K Frequent Elements (LeetCode 347)**
Given an integer array `nums` and an integer `k`, return the `k` most frequent elements.

*Approach 1: Priority Queue (O(n log k))*
```java
public int[] topKFrequentHeap(int[] nums, int k) {
    Map<Integer, Integer> freq = new HashMap<>();
    for (int n : nums) freq.put(n, freq.getOrDefault(n, 0) + 1);
    
    // Min-heap ordered by frequency ascending
    PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> freq.get(a) - freq.get(b));
    for (int key : freq.keySet()) {
        pq.add(key);
        if (pq.size() > k) pq.poll(); // evict lowest frequency
    }
    
    int[] res = new int[k];
    for (int i = 0; i < k; i++) res[i] = pq.poll();
    return res;
}
```

*Approach 2: Bucket Sort (O(n))*
```java
public int[] topKFrequent(int[] nums, int k) {
    Map<Integer, Integer> freq = new HashMap<>();
    for (int n : nums) freq.put(n, freq.getOrDefault(n, 0) + 1);
    
    // Index is frequency, value is list of numbers with that frequency
    List<Integer>[] buckets = new List[nums.length + 1];
    for (int key : freq.keySet()) {
        int frequency = freq.get(key);
        if (buckets[frequency] == null) {
            buckets[frequency] = new ArrayList<>();
        }
        buckets[frequency].add(key);
    }
    
    int[] res = new int[k];
    int index = 0;
    // Iterate from highest possible frequency down to 0
    for (int i = buckets.length - 1; i >= 0 && index < k; i--) {
        if (buckets[i] != null) {
            for (int num : buckets[i]) {
                res[index++] = num;
                if (index == k) return res;
            }
        }
    }
    return res;
}
```

**6. Dry Run Table (Bucket Sort)**
Input: `nums = [1, 1, 1, 2, 2, 3]`, `k = 2`
Freq: `{1: 3, 2: 2, 3: 1}`

| Index (freq) | `buckets[i]` | Read backwards & build result |
|---|---|---|
| 0 | null | |
| 1 | `[3]` | |
| 2 | `[2]` | i=2: add 2. `res = [1, 2]`. Done! |
| 3 | `[1]` | i=3: add 1. `res = [1]` |
| 4-6 | null | start scanning from i=6 |

**7. Time & Space Complexity**
- Time: O(n) for Bucket Sort. (O(n log k) for Priority Queue).
- Space: O(n) for both map and buckets.

**8. Common Mistakes**
- For Bucket Sort: Sizing the bucket array too small. A number can appear `nums.length` times, so the bucket array must be size `nums.length + 1` to accommodate index `nums.length`.
- Returning elements from buckets in the wrong order. You must iterate backwards from `buckets.length - 1` to get the *most* frequent first.

**9. Practice Problems**
- 🟡 Top K Frequent Elements (LeetCode 347)
- 🟡 Top K Frequent Words (LeetCode 692) (Requires sorting within buckets)
- 🟡 Sort Characters By Frequency (LeetCode 451)

#### Pattern 7: Sliding Window + HashMap

**1. Concept — What and Why**
When you need to find a contiguous subarray or substring that satisfies a certain condition regarding its contents (e.g., "at most 2 distinct characters", "contains all characters of a target string"), you combine Two Pointers (Sliding Window) with a HashMap. The HashMap keeps a real-time inventory (frequency count) of the elements currently inside the window `[left, right]`. When the window violates the constraint, you shrink it from the left, updating the HashMap until it is valid again.

**2. Trigger Words**
"substring", "subarray", "window", "at most k distinct", "contains all"

**3. Visual**
```text
String: "abcabcbb", Goal: longest without repeating

Window: [a] (left=0, right=0) -> map:{a:1} -> valid, len=1
Window: [ab] (left=0, right=1) -> map:{a:1,b:1} -> valid, len=2
Window: [abc] (left=0, right=2) -> map:{a:1,b:1,c:1} -> valid, len=3
Window: [abca] (left=0, right=3) -> map:{a:2,b:1,c:1} -> INVALID! ('a' has freq > 1)
  Shrink left!
Window: [bca] (left=1, right=3) -> map:{a:1,b:1,c:1} -> valid, len=3
```

**4. Template Code**
```java
Map<Character, Integer> window = new HashMap<>();
int left = 0, result = 0;
for (int right = 0; right < s.length(); right++) {
    char c = s.charAt(right);
    window.put(c, window.getOrDefault(c, 0) + 1);
    
    while (/* constraint violated */) {
        char lc = s.charAt(left++);
        window.put(lc, window.get(lc) - 1);
        if (window.get(lc) == 0) window.remove(lc);
    }
    result = Math.max(result, right - left + 1);
}
```

**5. Worked Example — Longest Substring Without Repeating Characters (LeetCode 3)**

```java
public int lengthOfLongestSubstring(String s) {
    Map<Character, Integer> window = new HashMap<>();
    int left = 0, maxLength = 0;
    
    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        window.put(c, window.getOrDefault(c, 0) + 1);
        
        // Constraint: no repeating characters, so no freq > 1
        while (window.get(c) > 1) {
            char leftChar = s.charAt(left);
            window.put(leftChar, window.get(leftChar) - 1);
            if (window.get(leftChar) == 0) {
                window.remove(leftChar);
            }
            left++;
        }
        
        maxLength = Math.max(maxLength, right - left + 1);
    }
    
    return maxLength;
}
```

**6. Dry Run Table**
Input: `s = "abcabcbb"`

| r | `s.charAt(r)` | window map | constraint violated? | Action | `left` | max |
|---|---|---|---|---|---|---|
| 0 | a | {a:1} | No | | 0 | 1 |
| 1 | b | {a:1, b:1} | No | | 0 | 2 |
| 2 | c | {a:1, b:1, c:1} | No | | 0 | 3 |
| 3 | a | {a:2, b:1, c:1} | YES! (a=2) | left=0, remove 'a' -> {a:1,b:1,c:1} | 1 | 3 |
| 4 | b | {a:1, b:2, c:1} | YES! (b=2) | left=1, remove 'b' -> {a:1,b:1,c:1} | 2 | 3 |

**7. Time & Space Complexity**
- Time: O(n). Both `left` and `right` pointers only move forward. Each character is processed at most twice (once added to window, once removed).
- Space: O(min(m, n)) where m is the size of the charset. The map stores at most unique characters.

**8. Common Mistakes**
- Forgetting to clean up the map `if (window.get(leftChar) == 0) window.remove(leftChar);`. If you leave 0-count entries in the map, `map.size()` will no longer accurately reflect the number of *distinct* characters in the window, which breaks logic in problems like "longest substring with K distinct characters".

**9. Practice Problems**
- 🟡 Longest Substring Without Repeating Characters (LeetCode 3)
- 🟡 Longest Repeating Character Replacement (LeetCode 424)
- 🟡 Maximum Erasure Value (LeetCode 1695)
- 🔴 Minimum Window Substring (LeetCode 76)
- 🔴 Subarrays with K Different Integers (LeetCode 992)

---

## PART 2: SORTING

### Chapter 4: Sorting Concepts

**Why Sorting Matters**

In the world of Algorithms, order is everything. Unstructured data forces you to exhaustively search. Structured data allows you to make deductions. 
Sorting transforms the fundamental complexity of problems:
- **Unsorted:** Checking all pairs for a target sum requires looking at every combination → O(n²)
- **Sorted:** Using two pointers closing in from the ends solves the exact same problem → O(n log n) total (sort time + O(n) search)
- **Unsorted:** Finding duplicates requires comparing every element to every other element or using extra memory (O(n) space).
- **Sorted:** Duplicates are adjacent! Finding them takes O(n) time and O(1) space.

**The O(n log n) Lower Bound (For Comparison Sorts)**

Why can't we sort faster than O(n log n) by just comparing elements? Let's prove it using a Decision Tree.

Imagine sorting an array of 3 elements: `[a, b, c]`. There are `3!` (3 factorial = 6) possible ways they could be ordered. Any sorting algorithm that uses comparisons (like `<` or `>`) must ask enough questions to distinguish between these 6 possibilities.

Every comparison `x < y` has a YES or NO answer. This forms a binary decision tree. 
To distinguish between `n!` possible orderings, the tree must have at least `n!` leaves.

```text
              [a < b?]
             /                  YES           NO
        [b < c?]     [a < c?]
        /    \        /         YES      NO   YES      NO
   [a,b,c] [a<c?] [b,a,c] [b,c,a]
            /           YES  NO
        [a,b,c] [c,a,b]
```
A binary tree of height `h` can have at most `2^h` leaves.
Therefore, `2^h >= n!`
Taking the log of both sides: `h >= log₂(n!)`
Using Stirling's approximation, `log₂(n!)` is roughly `n log₂(n)`.
The height of the decision tree `h` is the worst-case number of comparisons. 
**Conclusion: No comparison-based sorting algorithm can be faster than O(n log n) in the worst case.** 

*Note: Algorithms like Counting Sort and Radix Sort bypass this limit and achieve O(n) because they do NOT compare elements against each other. They use the elements directly as array indices.*

**Stability**

A sorting algorithm is **stable** if it preserves the relative order of equal elements. 
Imagine sorting a list of people by age: `[Bob:25, Alice:25, Charlie:30]`. Notice Bob comes before Alice.

- **Stable sort result:** `[Bob:25, Alice:25, Charlie:30]` (Bob stays before Alice).
- **Unstable sort result:** `[Alice:25, Bob:25, Charlie:30]` (They swapped!).

WHY stability matters: Multi-key sorting. If you first sort a spreadsheet by Last Name, and then sort by First Name, a stable sort ensures that all the "Johns" are still ordered alphabetically by their Last Name!

---

### Chapter 5: All Sorting Algorithms — Raw Implementation

This chapter covers the inner workings of every major sorting algorithm. You should be able to write these from memory.

#### 5.1 Bubble Sort

**1. Analogy**
Imagine bubbles rising to the surface of a liquid. Heavy items sink to the bottom, and lighter items float to the top.

**2. Numbered Algorithm Steps**
1. Iterate through the array. Compare adjacent pairs (`arr[i]` and `arr[i+1]`).
2. If they are out of order, swap them.
3. At the end of the first pass, the absolutely largest element will have "bubbled" to the last position.
4. Repeat for the remaining unsorted portion.
5. Optimization: If a pass completes with ZERO swaps, the array is fully sorted. Break early.

**3. ASCII Visual of ONE full pass**
```text
Array: [5, 1, 4, 2, 8]
Compare 5 & 1 -> Swap -> [1, 5, 4, 2, 8]
Compare 5 & 4 -> Swap -> [1, 4, 5, 2, 8]
Compare 5 & 2 -> Swap -> [1, 4, 2, 5, 8]
Compare 5 & 8 -> No swap -> [1, 4, 2, 5, 8]
Pass 1 complete. '8' is in its final position.
```

**4. Complete Java Code**
```java
public void bubbleSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
        boolean swapped = false;
        // Last i elements are already in place
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                // Swap arr[j] and arr[j+1]
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
                swapped = true;
            }
        }
        // If no two elements were swapped by inner loop, then break
        if (!swapped) break;
    }
}
```

**5. Dry Run Trace Table**
Input: `[5, 1, 4, 2, 8]`

| Pass | Array State End of Pass | Swaps Occurred? |
|---|---|---|
| 1 | `[1, 4, 2, 5, 8]` | Yes |
| 2 | `[1, 2, 4, 5, 8]` | Yes |
| 3 | `[1, 2, 4, 5, 8]` | No → Break early |

**6. Complexity**
- Time: Best O(n) (already sorted), Average O(n²), Worst O(n²)
- Space: O(1) in-place

**7. Stability:** Stable. We only swap if strictly greater (`>`), equal elements do not cross paths.
**8. When to use it:** Never in production. Used only for educational purposes or when a few elements are out of place in a tiny array.

#### 5.2 Selection Sort

**1. Analogy**
Imagine you have a hand of cards on a table. Find the absolute smallest card on the table and put it in the first position. Then find the next smallest and put it in the second position. Repeat.

**2. Numbered Algorithm Steps**
1. Maintain two portions: sorted (left) and unsorted (right).
2. Scan the unsorted portion to find the minimum element.
3. Swap that minimum element with the first element of the unsorted portion.
4. Move the sorted boundary one step to the right.

**3. ASCII Visual of ONE full pass**
```text
Array: [64, 25, 12, 22, 11]
Sorted part: []
Unsorted part: [64, 25, 12, 22, 11]
Min is 11. Swap 11 and 64.
Array: [11, 25, 12, 22, 64]
```

**4. Complete Java Code**
```java
public void selectionSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
        // Find the minimum element in unsorted array
        int min_idx = i;
        for (int j = i + 1; j < n; j++) {
            if (arr[j] < arr[min_idx]) {
                min_idx = j;
            }
        }
        // Swap the found minimum element with the first element
        int temp = arr[min_idx];
        arr[min_idx] = arr[i];
        arr[i] = temp;
    }
}
```

**5. Dry Run Trace Table**
Input: `[64, 25, 12, 22, 11]`

| i | min_idx found | Swap performed | Array State |
|---|---|---|---|
| 0 | 4 (value 11) | Swap 11 and 64 | `[11, 25, 12, 22, 64]` |
| 1 | 2 (value 12) | Swap 12 and 25 | `[11, 12, 25, 22, 64]` |
| 2 | 3 (value 22) | Swap 22 and 25 | `[11, 12, 22, 25, 64]` |
| 3 | 3 (value 25) | Swap 25 and 25 | `[11, 12, 22, 25, 64]` |

**6. Complexity**
- Time: Best O(n²), Average O(n²), Worst O(n²) (It ALWAYS scans the remainder)
- Space: O(1) in-place

**7. Stability:** **NOT Stable!** Example: `[4a, 4b, 1]`. Min is 1. Swap 1 with 4a. Array is `[1, 4b, 4a]`. The 4s swapped relative order!
**8. When to use it:** When memory writes (swaps) are extremely costly (e.g., flash memory). It does at most `n` swaps.

#### 5.3 Insertion Sort

**1. Analogy**
Sorting a hand of playing cards. You hold sorted cards in your left hand. You pick up a new card from the table, look at it, and insert it into its correct sorted position in your left hand.

**2. Numbered Algorithm Steps**
1. Assume the first element is already sorted.
2. Pick the next element (the "key").
3. Compare the key with elements in the sorted portion (right to left).
4. Shift elements to the right until you find the correct spot for the key.
5. Insert the key.

**3. ASCII Visual of ONE full pass**
```text
Array: [12, 11, 13, 5, 6]
i=1 (key = 11).
Sorted part: [12]
11 < 12. Shift 12 right.
Insert 11.
Array: [11, 12, 13, 5, 6]
```

**4. Complete Java Code**
```java
public void insertionSort(int[] arr) {
    int n = arr.length;
    for (int i = 1; i < n; ++i) {
        int key = arr[i];
        int j = i - 1;
        
        // Move elements of arr[0..i-1], that are greater than key,
        // to one position ahead of their current position
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j = j - 1;
        }
        arr[j + 1] = key;
    }
}
```

**5. Dry Run Trace Table**
Input: `[12, 11, 13, 5, 6]`

| i | Key | Shifts performed | Array State |
|---|---|---|---|
| 1 | 11 | Shift 12 right. Insert 11 at 0. | `[11, 12, 13, 5, 6]` |
| 2 | 13 | No shifts (13 > 12). | `[11, 12, 13, 5, 6]` |
| 3 | 5 | Shift 13, 12, 11 right. Insert 5 at 0. | `[5, 11, 12, 13, 6]` |
| 4 | 6 | Shift 13, 12, 11 right. Insert 6 at 1. | `[5, 6, 11, 12, 13]` |

**6. Complexity**
- Time: Best O(n) (already sorted - inner loop never runs!), Average O(n²), Worst O(n²)
- Space: O(1) in-place

**7. Stability:** Stable. We only shift if `arr[j] > key`.
**8. When to use it:** For small arrays or nearly sorted arrays. (It is the backbone of TimSort in Java).

#### 5.4 Merge Sort

**1. Analogy**
Divide and Conquer. Divide a messy pile of 100 papers in half. Sort each half of 50 separately. Then take the two sorted piles of 50 and merge them into one sorted pile of 100.

**2. Numbered Algorithm Steps**
1. Divide the array into two halves until subarrays have length 1 (a length 1 array is sorted).
2. Merge two sorted subarrays by comparing their elements one by one.
3. Put the smaller element into a temporary array.
4. Copy the temporary array back into the original array.

**3. ASCII Visual**
```text
         [38,27,43,3]
          /              [38,27]          [43,3]
    /    \           /      [38]  [27]      [43]   [3]
    \    /           \   /
    [27,38]          [3,43]
         \          /
          [3,27,38,43]
```

**4. Complete Java Code**
```java
public void mergeSort(int[] arr, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        
        // Sort first and second halves
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        
        // Merge them
        merge(arr, left, mid, right);
    }
}

private void merge(int[] arr, int left, int mid, int right) {
    int n1 = mid - left + 1;
    int n2 = right - mid;
    
    // Temp arrays
    int[] L = new int[n1];
    int[] R = new int[n2];
    
    // Copy data
    for (int i = 0; i < n1; ++i) L[i] = arr[left + i];
    for (int j = 0; j < n2; ++j) R[j] = arr[mid + 1 + j];
    
    int i = 0, j = 0;
    int k = left;
    
    // Merge back
    while (i < n1 && j < n2) {
        if (L[i] <= R[j]) { // <= ensures stability
            arr[k] = L[i];
            i++;
        } else {
            arr[k] = R[j];
            j++;
        }
        k++;
    }
    
    // Copy remaining elements
    while (i < n1) arr[k++] = L[i++];
    while (j < n2) arr[k++] = R[j++];
}
```

**6. Complexity**
- Time: Best O(n log n), Average O(n log n), Worst O(n log n)
- Space: O(n) for temporary arrays during the merge step.

**7. Stability:** Stable (because of `L[i] <= R[j]`).
**8. When to use it:** When stability is required, or when sorting Linked Lists (Merge Sort on Linked Lists requires O(1) auxiliary space!).

#### 5.5 Quick Sort

**1. Analogy**
Pick a "pivot" element (e.g. the last one). Send everything smaller than the pivot to the left side of the room. Send everything larger to the right side. The pivot is now exactly where it belongs. Now, tell the left and right groups to recursively do the same thing.

**2. Numbered Algorithm Steps**
1. Select a pivot element.
2. Partition the array around the pivot, placing smaller elements to its left and larger to its right.
3. The pivot is now in its final sorted position.
4. Recursively sort the sub-array to the left of the pivot, and the right of the pivot.

**3. Pivot Strategies**
- **Last Element:** Simple (used in Lomuto scheme). Bad if array is already sorted (O(n²)).
- **Random Pivot:** Picks random index, swaps with last. Averts worst-case against malicious inputs.
- **Median of Three:** Picks first, middle, and last, finds median, uses as pivot. Very robust in practice.

**4. Complete Java Code (Lomuto Partition Scheme)**
```java
public void quickSort(int[] arr, int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        
        // Recursively sort elements before and after partition
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}

private int partition(int[] arr, int low, int high) {
    int pivot = arr[high]; // Lomuto chooses last element
    int i = (low - 1); // Index of smaller element
    
    for (int j = low; j < high; j++) {
        // If current element is smaller than the pivot
        if (arr[j] < pivot) {
            i++;
            // Swap arr[i] and arr[j]
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    // Swap arr[i+1] and pivot (arr[high])
    int temp = arr[i + 1];
    arr[i + 1] = arr[high];
    arr[high] = temp;
    
    return i + 1; // Return pivot's final index
}
```

**5. Dry Run Trace Table**
Input: `[10, 7, 8, 9, 1, 5]`
Call: `partition(arr, 0, 5)`. Pivot = 5. `i` starts at -1.

| j | `arr[j]` | `arr[j] < pivot?` | `i` becomes | Array State (after potential swap) |
|---|---|---|---|---|
| 0 | 10 | No (10 > 5) | -1 | `[10, 7, 8, 9, 1, 5]` |
| 1 | 7 | No (7 > 5) | -1 | `[10, 7, 8, 9, 1, 5]` |
| 2 | 8 | No (8 > 5) | -1 | `[10, 7, 8, 9, 1, 5]` |
| 3 | 9 | No (9 > 5) | -1 | `[10, 7, 8, 9, 1, 5]` |
| 4 | 1 | Yes (1 < 5) | 0 | Swap 10 & 1 -> `[1, 7, 8, 9, 10, 5]` |

Loop ends. Swap `arr[i+1]` (which is 7) with pivot 5.
Final array: `[1, 5, 8, 9, 10, 7]`. Return pivot index = 1.

**6. Complexity**
- Time: Best O(n log n), Average O(n log n), Worst O(n²) (occurs if already sorted and using last element as pivot).
- Space: O(log n) average for recursion stack, O(n) worst.

**7. Stability:** **NOT Stable.** Swapping elements across distances breaks relative order. Example: `[2a, 2b, 1]`, pivot 1. Swaps 2a with 1 -> `[1, 2b, 2a]`. 2s are flipped.
**8. When to use it:** General purpose sorting. Faster in practice than Merge Sort due to cache locality and in-place sorting.

#### 5.6 Heap Sort

**1. Analogy**
Organize a tournament where the winner goes to the top. Take the winner (largest element), put them at the end of the line (sorted array). Let the runners-up compete again to find the next winner. Repeat.

**2. Numbered Algorithm Steps**
1. Build a Max-Heap from the unsorted array (O(n)).
2. The largest element is now at `arr[0]`.
3. Swap `arr[0]` with the last element in the heap.
4. Reduce the "heap size" by 1.
5. Call `heapify(0)` to fix the root and find the new max.
6. Repeat steps 3-5 until heap size is 1.

**4. Complete Java Code**
```java
public void heapSort(int[] arr) {
    int n = arr.length;
    
    // Phase 1: Build max heap (rearrange array)
    for (int i = n / 2 - 1; i >= 0; i--) {
        heapify(arr, n, i);
    }
    
    // Phase 2: One by one extract an element from heap
    for (int i = n - 1; i > 0; i--) {
        // Move current root to end
        int temp = arr[0];
        arr[0] = arr[i];
        arr[i] = temp;
        
        // Call max heapify on the reduced heap
        heapify(arr, i, 0);
    }
}

private void heapify(int[] arr, int n, int i) {
    int largest = i; // Initialize largest as root
    int l = 2 * i + 1; // left child = 2*i + 1
    int r = 2 * i + 2; // right child = 2*i + 2
    
    // If left child is larger than root
    if (l < n && arr[l] > arr[largest]) largest = l;
    
    // If right child is larger than largest so far
    if (r < n && arr[r] > arr[largest]) largest = r;
    
    // If largest is not root
    if (largest != i) {
        int swap = arr[i];
        arr[i] = arr[largest];
        arr[largest] = swap;
        
        // Recursively heapify the affected sub-tree
        heapify(arr, n, largest);
    }
}
```

**5. Dry Run Trace Table**
Input: `[12, 11, 13, 5, 6, 7]`

| Step | Heap (array view) | Action |
|---|---|---|
| Build heap | `[13, 11, 12, 5, 6, 7]` | Max element 13 is at root (index 0). |
| Extract 1 | `[12, 11, 7, 5, 6, 13]` | Swap 13 with 7. Heapify(0) brings 12 to root. |
| Extract 2 | `[11, 6, 7, 5, 12, 13]` | Swap 12 with 6. Heapify(0) brings 11 to root. |
| Extract 3 | `[7, 6, 5, 11, 12, 13]` | Swap 11 with 5. Heapify(0) brings 7 to root. |
| Extract 4 | `[6, 5, 7, 11, 12, 13]` | Swap 7 with 5. Heapify(0) brings 6 to root. |
| Extract 5 | `[5, 6, 7, 11, 12, 13]` | Swap 6 with 5. Heapify(0) brings 5 to root. |

**6. Complexity**
- Time: Best, Average, Worst all O(n log n).
- Space: O(1) in-place.

**7. Stability:** **NOT Stable.** Heap operations scramble relative orders.
**8. When to use it:** When memory is tight (requires O(1) space, unlike Merge Sort) and you need a guaranteed O(n log n) worst-case (unlike Quick Sort).

#### 5.7 Counting Sort

**1. Analogy**
You have 100 people and want to sort them by age (0-100). Instead of comparing people, you put 101 labeled buckets on the floor. Ask each person their age and tell them to stand in the corresponding bucket. Then, tell bucket 0 to line up, then bucket 1, etc.

**2. Numbered Algorithm Steps**
(Stable version using prefix sums)
1. Find the max value `k`. Create a `count` array of size `k+1`.
2. Count the occurrences of each element in the input.
3. Transform `count` into a prefix sum array. `count[i]` now holds the actual ending position in the output array for value `i`.
4. Iterate through the original array *backwards* (for stability), place each element in the output array using the prefix sum index, and decrement the prefix sum.

**4. Complete Java Code (Stable Version)**
```java
public void countingSort(int[] arr) {
    if (arr.length <= 1) return;
    
    int max = Arrays.stream(arr).max().getAsInt();
    
    int[] count = new int[max + 1];
    int[] output = new int[arr.length];
    
    // Store count of each character
    for (int i = 0; i < arr.length; i++) {
        count[arr[i]]++;
    }
    
    // Change count[i] so that count[i] now contains actual
    // position of this character in output array (prefix sums)
    for (int i = 1; i <= max; i++) {
        count[i] += count[i - 1];
    }
    
    // Build the output array backwards for stability
    for (int i = arr.length - 1; i >= 0; i--) {
        output[count[arr[i]] - 1] = arr[i];
        count[arr[i]]--;
    }
    
    // Copy output back to arr
    for (int i = 0; i < arr.length; i++) arr[i] = output[i];
}
```

**5. Dry Run Trace Table**
Input: `[4, 2, 2, 8, 3, 3, 1]` -> max = 8.

| Count array | Prefix Sum | Output |
|---|---|---|
| `[0,1,2,2,1,0,0,0,1]` | `[0,1,3,5,6,6,6,6,7]` | `[1, 2, 2, 3, 3, 4, 8]` |

**6. Complexity**
- Time: O(n + k) where k is the max element.
- Space: O(n + k).

**7. Stability:** Stable (because we iterate backward during output placement).
**8. When to use it:** When integers are within a known, small range (e.g., characters `a-z`, ages `0-120`).

#### 5.8 Radix Sort

**1. Analogy**
Sort by the last digit (LSD). Then sort by the second-to-last digit. Continue to the most significant digit. It works like an old mechanical punch-card sorter. Crucial requirement: the underlying sort per digit *must* be stable (we use Counting Sort).

**4. Complete Java Code**
```java
public void radixSort(int[] arr) {
    int max = Arrays.stream(arr).max().getAsInt();
    
    // Do counting sort for every digit. Exp is 10^i
    for (int exp = 1; max / exp > 0; exp *= 10) {
        countSortByDigit(arr, exp);
    }
}

private void countSortByDigit(int[] arr, int exp) {
    int[] output = new int[arr.length];
    int[] count = new int[10]; // Base 10
    
    // Store count of occurrences in count[]
    for (int i = 0; i < arr.length; i++) {
        count[(arr[i] / exp) % 10]++;
    }
    
    // Prefix sums
    for (int i = 1; i < 10; i++) {
        count[i] += count[i - 1];
    }
    
    // Build output backward for stability
    for (int i = arr.length - 1; i >= 0; i--) {
        int digit = (arr[i] / exp) % 10;
        output[count[digit] - 1] = arr[i];
        count[digit]--;
    }
    
    for (int i = 0; i < arr.length; i++) arr[i] = output[i];
}
```

**6. Complexity**
- Time: O(d * (n + b)) where d is max digits, b is base (10).
- Space: O(n + b).

#### 5.9 Bucket Sort

**1. Analogy**
For floating-point numbers distributed evenly between `[0, 1)`. Create `n` empty buckets. Multiply each number by `n` to find its bucket. Put it in. Sort each individual bucket using Insertion Sort. Concatenate.

**4. Complete Java Code**
```java
public void bucketSort(float[] arr) {
    int n = arr.length;
    if (n <= 0) return;
    
    // 1. Create n empty buckets
    List<Float>[] buckets = new List[n];
    for (int i = 0; i < n; i++) {
        buckets[i] = new ArrayList<Float>();
    }
    
    // 2. Put array elements in different buckets
    for (int i = 0; i < n; i++) {
        int bucketIndex = (int) (arr[i] * n); // maps to [0, n-1]
        buckets[bucketIndex].add(arr[i]);
    }
    
    // 3. Sort individual buckets
    for (int i = 0; i < n; i++) {
        Collections.sort(buckets[i]); // typically uses insertion sort for small lists
    }
    
    // 4. Concatenate all buckets into arr[]
    int index = 0;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < buckets[i].size(); j++) {
            arr[index++] = buckets[i].get(j);
        }
    }
}
```

**6. Complexity**
- Time: Average O(n). Worst O(n²) (if all elements hash to the same bucket).
- Space: O(n).

---

**Sorting Summary Table**

| Algorithm | Best | Average | Worst | Space | Stable | Use When |
|---|---|---|---|---|---|---|
| Bubble | O(n) | O(n²) | O(n²) | O(1) | Yes | Never in production. |
| Selection | O(n²) | O(n²) | O(n²) | O(1) | No | When memory writes are very costly. |
| Insertion | O(n) | O(n²) | O(n²) | O(1) | Yes | Array is nearly sorted or very small. |
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) | Yes | Stability is required. Sorting Linked Lists. |
| Quick Sort | O(n log n) | O(n log n) | O(n²) | O(log n) | No | General purpose, large arrays in memory. |
| Heap Sort | O(n log n) | O(n log n) | O(n log n) | O(1) | No | Need O(n log n) guarantee with O(1) space. |
| Counting | O(n+k) | O(n+k) | O(n+k) | O(k) | Yes | Integers with small known range [0, k]. |
| Radix Sort | O(d·n) | O(d·n) | O(d·n) | O(n+b) | Yes | When range k is larger than n, but digits are small. |
| Bucket Sort | O(n) | O(n) | O(n²) | O(n) | Yes | Uniformly distributed floating point decimals. |


### Chapter 6: Java Sorting API — Complete Reference

#### 6.1 Arrays Class — Sorting

Java provides highly optimized sorting routines for primitive arrays and object arrays inside `java.util.Arrays`.

**For Primitives:** Java uses **Dual-Pivot Quicksort**. It is faster in practice than standard Quicksort, but it is **NOT stable**. (Stability doesn't matter for primitives anyway, since a `5` is identical to another `5`).

- `static void sort(int[] a)` — Sorts the entire array.
```java
int[] arr = {5, 2, 8, 1};
Arrays.sort(arr); // [1, 2, 5, 8]
```
- `static void sort(int[] a, int fromIndex, int toIndex)` — Sorts a subarray from `fromIndex` (inclusive) to `toIndex` (exclusive).
```java
int[] arr = {5, 2, 8, 1};
Arrays.sort(arr, 1, 3); // [5, 2, 8, 1] -> arr[1] and arr[2] are already sorted
```
- Similar overloads exist for `long[]`, `double[]`, `char[]`, `byte[]`, `short[]`, `float[]`.
- `static void parallelSort(int[] a)` — Divides the array into sub-arrays that are sorted in parallel by multiple threads. Good for arrays > 8192 elements.

**For Objects:** Java uses **TimSort**. TimSort is a hybrid of Merge Sort and Insertion Sort. It is **stable** and has a best-case of **O(n)** if the array is already partially sorted!

- `static <T extends Comparable<? super T>> void sort(T[] a)` — Sorts objects using their natural ordering.
```java
String[] arr = {"banana", "apple", "cherry"};
Arrays.sort(arr); // ["apple", "banana", "cherry"]
```
- `static <T> void sort(T[] a, Comparator<? super T> c)` — Sorts objects using a custom comparator.
```java
String[] arr = {"banana", "apple", "cherry"};
Arrays.sort(arr, (s1, s2) -> s1.length() - s2.length()); // ["apple", "banana", "cherry"]
```
- `static <T> void sort(T[] a, int fromIndex, int toIndex, Comparator<? super T> c)` — Sorts a subarray of objects using a comparator.

#### 6.2 Collections Class — Sorting & Related

`java.util.Collections` provides algorithms that operate on collections, primarily Lists.

- `static <T extends Comparable<? super T>> void sort(List<T> list)` — Sorts the list using natural order.
```java
List<Integer> list = Arrays.asList(3, 1, 2);
Collections.sort(list); // [1, 2, 3]
```
- `static <T> void sort(List<T> list, Comparator<? super T> c)` — Sorts the list using a comparator.
- `static void reverse(List<?> list)` — Reverses the order of the elements. (O(n)).
```java
Collections.reverse(list); // [3, 2, 1]
```
- `static void shuffle(List<?> list)` — Randomly permutes the list using Fisher-Yates algorithm. (O(n)).
- `static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll)` — Finds the minimum element based on natural order.
- `static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll)` — Finds the maximum element based on natural order.
- Both `min` and `max` have overloads taking a `Comparator`.

*Note: Since Java 8, `List` interface itself has a `sort` method. `list.sort(comparator)` is preferred over `Collections.sort(list, comparator)`.*

#### 6.3 The Comparator Interface — Complete Guide

A `Comparator` is a standalone object that encapsulates ordering logic. It dictates how two objects should be compared.

**Basic Usage:**
```java
// Anonymous class (old style, Java 7-)
Comparator<String> byLength = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.length() - b.length();
    }
};

// Lambda (modern, Java 8+)
Comparator<String> byLength = (a, b) -> a.length() - b.length();
```

**The Overflow Bug — IMPORTANT:**
Never use subtraction for comparison if integers can be very large or negative!
```java
// WRONG! If a = Integer.MAX_VALUE and b = -1, a - b will overflow and return a negative number!
Comparator<Integer> wrong = (a, b) -> a - b; 

// CORRECT. Uses safe comparison logic.
Comparator<Integer> correct = (a, b) -> Integer.compare(a, b);
Comparator<Integer> correct2 = Integer::compare; // Method reference
```

**Comparator Factory Methods (Java 8+):**
These methods allow you to elegantly chain comparisons.

- `static <T,U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor)` — Extracts a sort key.
- `static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor)` — Prevents boxing overhead.
- `static <T> Comparator<T> comparingDouble(ToDoubleFunction<? super T> keyExtractor)`
- `static <T> Comparator<T> comparingLong(ToLongFunction<? super T> keyExtractor)`
- `default Comparator<T> reversed()` — Reverses the comparator.
- `default Comparator<T> thenComparing(Comparator<? super T> other)` — Chained tie-breaker.
- `default <U extends Comparable<? super U>> Comparator<T> thenComparing(Function<? super T, ? extends U> keyExtractor)`
- `default Comparator<T> thenComparingInt(ToIntFunction<? super T> keyExtractor)`
- `static <T extends Comparable<? super T>> Comparator<T> naturalOrder()`
- `static <T extends Comparable<? super T>> Comparator<T> reverseOrder()`
- `static <T> Comparator<T> nullsFirst(Comparator<? super T> comparator)` — Treats nulls as less than non-nulls.
- `static <T> Comparator<T> nullsLast(Comparator<? super T> comparator)`

**Example of Chaining:**
Sort students: first by GPA descending, then by name ascending, putting null names last.
```java
students.sort(
    Comparator.comparingDouble(Student::getGpa).reversed()
              .thenComparing(Comparator.comparing(Student::getName, Comparator.nullsLast(Comparator.naturalOrder())))
);
```

#### 6.4 The Comparable Interface

`Comparable` is implemented *by the class itself* to define its default, "natural" ordering.

```java
public class Student implements Comparable<Student> {
    String name;
    double gpa;
    
    @Override
    public int compareTo(Student other) {
        // Natural order = ascending GPA
        return Double.compare(this.gpa, other.gpa); 
    }
}
```
**Contract:**
1. Consistent with `equals()`: If `compareTo` returns 0, `equals()` should return true (highly recommended).
2. Transitive: If `A > B` and `B > C`, then `A > C`.

**When to use which?**
- Use `Comparable` when there is ONE obvious, universal way to sort the class (e.g., Integer, String, Date).
- Use `Comparator` for one-off, specific sorting (e.g., sort Strings by length instead of alphabetically), or when you don't own the class and can't modify it.

---

### Chapter 7: Sorting Patterns

#### Pattern 1: Sort + Two Pointers

**1. Concept — What and Why**
Many problems ask for pairs or triplets that meet a condition (e.g., sum to zero). The naive approach is O(n³). If we sort the array first (O(n log n)), we can fix one element and use a left and right pointer closing in to find the remaining pair in O(n) time. The sorting enables us to make intelligent decisions: if the sum is too small, move left pointer right. If too large, move right pointer left. Total time: O(n log n + n²) = O(n²).

**2. Trigger Words**
"triplet", "pair with sum", "three numbers", "closest sum"

**3. Template Code**
```java
Arrays.sort(nums);
for (int i = 0; i < nums.length - 2; i++) {
    if (i > 0 && nums[i] == nums[i - 1]) continue; // Skip duplicates
    int left = i + 1, right = nums.length - 1;
    while (left < right) {
        int sum = nums[i] + nums[left] + nums[right];
        if (sum == target) {
            // Found! Move both, skip duplicates
            left++; right--;
        } else if (sum < target) left++;
        else right--;
    }
}
```

**4. Worked Example — 3Sum (LeetCode 15)**
```java
public List<List<Integer>> threeSum(int[] nums) {
    List<List<Integer>> res = new ArrayList<>();
    Arrays.sort(nums);
    
    for (int i = 0; i < nums.length - 2; i++) {
        if (i > 0 && nums[i] == nums[i - 1]) continue;
        
        int left = i + 1;
        int right = nums.length - 1;
        
        while (left < right) {
            int sum = nums[i] + nums[left] + nums[right];
            if (sum == 0) {
                res.add(Arrays.asList(nums[i], nums[left], nums[right]));
                left++; right--;
                while (left < right && nums[left] == nums[left - 1]) left++;
                while (left < right && nums[right] == nums[right + 1]) right--;
            } else if (sum < 0) {
                left++;
            } else {
                right--;
            }
        }
    }
    return res;
}
```

**5. Dry Run Table**
Input: `[-1, 0, 1, 2, -1, -4]` → Sorted: `[-4, -1, -1, 0, 1, 2]`

| i | `nums[i]` | L | R | sum | Action |
|---|---|---|---|---|---|
| 0 | -4 | 1 (-1) | 5 (2) | -4+-1+2 = -3 | <0, L++ |
| 0 | -4 | 2 (-1) | 5 (2) | -4+-1+2 = -3 | <0, L++ |
| 1 | -1 | 2 (-1) | 5 (2) | -1+-1+2 = 0 | Found! L++, R--, skip dups |
| 2 | -1 | skip | | | `nums[i]==nums[i-1]` |

**6. Complexity**
- Time: O(n log n) sort + O(n²) search = O(n²).
- Space: O(1) or O(log n) depending on sorting algorithm.

#### Pattern 2: Custom Comparator — String Concatenation

**1. Concept — What and Why**
Sometimes we need to arrange elements to form the maximum or minimum possible combination. Simply sorting them by value or lexically isn't enough. Instead, we write a comparator that asks: "If I put A before B, is the result better than putting B before A?"

**2. Trigger Words**
"largest number", "arrange", "concatenate", "order strings"

**3. Template Code**
```java
// Convert int to String, then sort using custom comparator
String[] strs = new String[nums.length];
for (int i = 0; i < nums.length; i++) strs[i] = String.valueOf(nums[i]);
Arrays.sort(strs, (a, b) -> (b + a).compareTo(a + b));
```

**4. Worked Example — Largest Number (LeetCode 179)**
```java
public String largestNumber(int[] nums) {
    String[] strs = new String[nums.length];
    for (int i = 0; i < nums.length; i++) strs[i] = String.valueOf(nums[i]);
    
    // Custom comparator: compare b+a vs a+b to decide which should come first
    Arrays.sort(strs, (a, b) -> (b + a).compareTo(a + b));
    
    if (strs[0].equals("0")) return "0"; // Edge case: all zeroes
    
    StringBuilder sb = new StringBuilder();
    for (String s : strs) sb.append(s);
    return sb.toString();
}
```
**Why `(b+a).compareTo(a+b)`?** Let's test "9" and "34".
`a = "9", b = "34"`.
`b+a = "349"`, `a+b = "934"`.
`"349".compareTo("934")` returns a negative number. This means `a+b` is lexicographically larger. So "9" should come before "34".

**5. Dry Run Table**
Input: `[3, 30, 34, 5, 9]`

| a | b | a+b | b+a | greater? | sorting decision |
|---|---|---|---|---|---|
| "3" | "30" | "330" | "303" | a+b | "3" comes before "30" |
| "3" | "34" | "334" | "343" | b+a | "34" comes before "3" |

Final Sorted Array: `["9", "5", "34", "3", "30"]`

**6. Complexity**
- Time: O(n log n * k) where k is max length of string (for concatenation and comparison).
- Space: O(n) to store strings.

#### Pattern 3: Merge Intervals

**1. Concept — What and Why**
When dealing with overlapping ranges or meeting times, the first step is almost always: **Sort by Start Time**. Once sorted, intervals can only overlap with their immediate neighbors. We can iterate greedily: if the next interval starts before the current one ends, we merge them by taking `max(current.end, next.end)`.

**2. Trigger Words**
"merge", "overlap", "intervals", "meeting rooms", "insert interval"

**3. Template Code**
```java
Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
List<int[]> merged = new ArrayList<>();
int[] current = intervals[0];
merged.add(current);

for (int[] next : intervals) {
    if (next[0] <= current[1]) {
        current[1] = Math.max(current[1], next[1]); // Merge
    } else {
        current = next; // No overlap, add to result
        merged.add(current);
    }
}
```

**4. Worked Example — Merge Intervals (LeetCode 56)**
```java
public int[][] merge(int[][] intervals) {
    if (intervals.length <= 1) return intervals;
    
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
    List<int[]> res = new ArrayList<>();
    
    int[] current = intervals[0];
    res.add(current);
    
    for (int[] next : intervals) {
        if (next[0] <= current[1]) {
            current[1] = Math.max(current[1], next[1]);
        } else {
            current = next;
            res.add(current);
        }
    }
    
    return res.toArray(new int[res.size()][]);
}
```

**5. Dry Run Table**
Input: `[[1,3], [8,10], [2,6], [15,18]]`
Sorted: `[[1,3], [2,6], [8,10], [15,18]]`

| Next Interval | overlaps with Current? | Action | `current` becomes | `res` |
|---|---|---|---|---|
| `[1,3]` | N/A (init) | Add to res | `[1,3]` | `[[1,3]]` |
| `[2,6]` | Yes (2 <= 3) | max(3, 6) | `[1,6]` | `[[1,6]]` |
| `[8,10]`| No (8 > 6) | New current, add | `[8,10]`| `[[1,6], [8,10]]` |

**6. Complexity**
- Time: O(n log n) to sort. Merging is O(n). Total O(n log n).
- Space: O(n) for the output array.

#### Pattern 4: Sort + Binary Search

**1. Concept — What and Why**
If a problem requires searching for elements or verifying existence, sorting the array first allows you to use Binary Search (O(log n)) instead of Linear Search (O(n)). For `m` queries on an array of size `n`, `Sort + BS` is `O(n log n + m log n)`, which is infinitely better than `O(m * n)`.

**2. Trigger Words**
"find target", "search", "kth element", "multiple queries"

**4. Worked Example — Two Sum II (Input array is sorted)**
*Note: Two Pointers is O(n) and optimal here, but BS is also viable O(n log n).*
```java
public int[] twoSum(int[] numbers, int target) {
    for (int i = 0; i < numbers.length; i++) {
        int complement = target - numbers[i];
        int idx = Arrays.binarySearch(numbers, i + 1, numbers.length, complement);
        if (idx >= 0) {
            return new int[]{i + 1, idx + 1};
        }
    }
    return new int[]{};
}
```

#### Pattern 5: Counting Sort for Frequency Problems

**1. Concept — What and Why**
When the input space is bounded (e.g. lowercase letters, integers 0-100), you don't need `Arrays.sort()` (O(n log n)). You can use an array as a frequency map and construct the sorted output directly in O(n) time.

**4. Worked Example — Sort Characters By Frequency (LeetCode 451)**
```java
public String frequencySort(String s) {
    int[] count = new int[256]; // ASCII
    for (char c : s.toCharArray()) count[c]++;
    
    // Find max frequency
    int maxFreq = 0;
    for (int c : count) maxFreq = Math.max(maxFreq, c);
    
    // Bucket sort: index is frequency
    List<Character>[] buckets = new List[maxFreq + 1];
    for (int i = 0; i < 256; i++) {
        if (count[i] > 0) {
            if (buckets[count[i]] == null) buckets[count[i]] = new ArrayList<>();
            buckets[count[i]].add((char) i);
        }
    }
    
    StringBuilder sb = new StringBuilder();
    for (int i = buckets.length - 1; i > 0; i--) {
        if (buckets[i] != null) {
            for (char c : buckets[i]) {
                for (int j = 0; j < i; j++) sb.append(c);
            }
        }
    }
    return sb.toString();
}
```

### Chapter 8: Mixed Patterns — Hashing + Sorting Combined

These problems test your ability to use both data structures in tandem.

**8.1 Top K Frequent Words (LeetCode 692)**
- **Problem:** Given an array of strings `words` and an integer `k`, return the `k` most frequent strings. Return the answer sorted by frequency from highest to lowest. Sort the words with the same frequency by their lexicographical order.
- **Intuition:** First, we need a HashMap to count frequencies. Second, we need to sort those keys. Because of the complex sorting rule (frequency descending, then alphabetical ascending), we need a custom Comparator.
- **Full Java Code:**
```java
public List<String> topKFrequent(String[] words, int k) {
    Map<String, Integer> freq = new HashMap<>();
    for (String w : words) {
        freq.put(w, freq.getOrDefault(w, 0) + 1);
    }
    
    List<String> candidates = new ArrayList<>(freq.keySet());
    candidates.sort((w1, w2) -> {
        int f1 = freq.get(w1);
        int f2 = freq.get(w2);
        if (f1 == f2) {
            return w1.compareTo(w2); // alphabetical asc
        }
        return Integer.compare(f2, f1); // freq desc
    });
    
    return candidates.subList(0, k);
}
```
- **Dry Run:** `words = ["i", "love", "leetcode", "i", "love", "coding"]`, `k = 2`
  - Freq Map: `{"i":2, "love":2, "leetcode":1, "coding":1}`
  - Sort Candidates: 
    - "i" vs "love" -> freq is same (2). "i".compareTo("love") -> "i" comes first.
    - Resulting order: `["i", "love", "coding", "leetcode"]`
  - Return top 2: `["i", "love"]`.
- **Complexity:** Time: O(n log n) for sorting all unique words. Space: O(n).

**8.2 Contains Duplicate II (LeetCode 219)**
- **Problem:** Given an integer array `nums` and an integer `k`, return `true` if there are two distinct indices `i` and `j` such that `nums[i] == nums[j]` and `abs(i - j) <= k`.
- **Intuition:** We can use a HashSet to maintain a "sliding window" of the most recently seen `k` elements. If the window ever contains the current element, we found a duplicate within distance `k`. If the window grows larger than `k`, we remove the oldest element.
- **Full Java Code:**
```java
public boolean containsNearbyDuplicate(int[] nums, int k) {
    Set<Integer> window = new HashSet<>();
    for (int i = 0; i < nums.length; i++) {
        if (window.contains(nums[i])) {
            return true;
        }
        window.add(nums[i]);
        if (window.size() > k) {
            window.remove(nums[i - k]); // Remove the element that fell out of window
        }
    }
    return false;
}
```
- **Dry Run:** `nums = [1,2,3,1]`, `k = 3`
  - i=0: add 1 -> set={1}
  - i=1: add 2 -> set={1,2}
  - i=2: add 3 -> set={1,2,3}
  - i=3: set contains 1! Return true.
- **Complexity:** Time: O(n). Space: O(k) for the set.

**8.3 Longest Substring with At Most K Distinct Characters (LeetCode 340)**
- **Problem:** Given a string `s` and an integer `k`, return the length of the longest substring of `s` that contains at most `k` distinct characters.
- **Intuition:** Sliding window. We use a `LinkedHashMap` to store the character and its rightmost index. `LinkedHashMap` maintains insertion order. When we exceed `k` distinct characters, we must evict the "oldest" character (the one whose rightmost appearance is furthest left). Since `LinkedHashMap` is ordered, the first entry is always the oldest!
- **Full Java Code:**
```java
public int lengthOfLongestSubstringKDistinct(String s, int k) {
    if (k == 0 || s.length() == 0) return 0;
    
    LinkedHashMap<Character, Integer> window = new LinkedHashMap<>(k + 1, 0.75f, true); // Access-order
    int left = 0, maxLength = 0;
    
    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        // Put updates access order. It puts this char at the end of the Linked list
        window.put(c, right); 
        
        if (window.size() == k + 1) {
            // Remove the least recently used character (which is at the head of the list)
            Map.Entry<Character, Integer> eldest = window.entrySet().iterator().next();
            window.remove(eldest.getKey());
            // Move left pointer past the rightmost index of the evicted char
            left = eldest.getValue() + 1;
        }
        maxLength = Math.max(maxLength, right - left + 1);
    }
    return maxLength;
}
```
- **Dry Run:** `s = "eceba"`, `k = 2`
  - r=0: 'e', map={'e':0}. len=1
  - r=1: 'c', map={'e':0, 'c':1}. len=2
  - r=2: 'e', map={'c':1, 'e':2} (access order updated e!). len=3
  - r=3: 'b', map={'c':1, 'e':2, 'b':3}. Size=3! Eldest is 'c' (idx 1). Remove 'c', left = 1+1 = 2. max=3.
- **Complexity:** Time: O(n). `put`, `remove`, and `next` are O(1). Space: O(k).

**8.4 Rank Transform of Array (LeetCode 1331)**
- **Problem:** Given an array of integers `arr`, replace each element with its rank. Rank starts at 1, larger elements have larger ranks. Equal elements have the same rank.
- **Intuition:** We need to know the sorted order of unique elements to assign ranks. We copy the array, sort it, assign ranks 1, 2, 3... in a HashMap, and finally map the original array using the HashMap.
- **Full Java Code:**
```java
public int[] arrayRankTransform(int[] arr) {
    int[] sorted = arr.clone();
    Arrays.sort(sorted);
    
    Map<Integer, Integer> ranks = new HashMap<>();
    int currentRank = 1;
    
    for (int num : sorted) {
        if (!ranks.containsKey(num)) {
            ranks.put(num, currentRank);
            currentRank++;
        }
    }
    
    int[] res = new int[arr.length];
    for (int i = 0; i < arr.length; i++) {
        res[i] = ranks.get(arr[i]);
    }
    return res;
}
```
- **Dry Run:** `arr = [40,10,20,30]`
  - sorted = `[10,20,30,40]`
  - map = `{10:1, 20:2, 30:3, 40:4}`
  - map back: 40->4, 10->1, 20->2, 30->3. Result: `[4,1,2,3]`
- **Complexity:** Time: O(n log n). Space: O(n).

---

### Chapter 5: Interview Cheat Sheet

| Problem Type | Pattern | Key Data Structure | Time | Space |
|---|---|---|---|---|
| Two elements summing to target | Two Sum (HashMap) | `HashMap<Integer,Integer>` | O(n) | O(n) |
| Count element frequencies | Frequency Map | `HashMap<T,Integer>` | O(n) | O(n) |
| Subarray sum equals k | Prefix Sum + HashMap | `HashMap<Integer,Integer>` | O(n) | O(n) |
| Longest run of consecutive ints | HashSet + greedy start | `HashSet<Integer>` | O(n) | O(n) |
| Group words by anagram | Sort key + HashMap | `HashMap<String,List>` | O(n·k·log k) | O(n·k) |
| Top K frequent elements | Freq map + bucket sort | `HashMap` + `List[]` | O(n) | O(n) |
| Window with all chars of pattern | Sliding window + freq map | `HashMap` x2 | O(n) | O(1) |
| Sort objects by field | `Comparator.comparing()` | `Comparator` | O(n log n) | O(log n) |
| Merge overlapping intervals | Sort by start + greedy | `List<int[]>` | O(n log n) | O(n) |
| Largest number from array | Custom concat comparator | `Comparator` | O(n log n) | O(n) |
| Contains duplicates within distance k | Sliding Window Set | `HashSet<Integer>` | O(n) | O(k) |
| Find if anagram exists in string | Sliding Window Anagram | `int[26]` array | O(n) | O(1) |
| Check if string has unique chars | Boolean array hash map | `boolean[128]` | O(n) | O(1) |
| Longest substring no repeats | Sliding Window + Map | `HashMap<Char,Int>` | O(n) | O(1) |
| LRU Cache implementation | Access-order Map | `LinkedHashMap` | O(1) | O(c) |
| First non-repeating character | Frequency Array | `int[26]` | O(n) | O(1) |
| Find closest value to target | TreeMap floor/ceiling | `TreeMap` | O(log n) | O(n) |
| Kth largest element | QuickSelect or Min-Heap | `PriorityQueue` | O(n) avg | O(k) |
| Triplet sum to zero | Sort + Two Pointers | Array | O(n²) | O(1) |
| Sort massive files on disk | External Merge Sort | File I/O | O(n log n) | O(M) |

---

### Appendix: Quick Reference Cards

**Card 1: HashMap Methods**
| Method | Signature | One-line purpose |
|---|---|---|
| Put | `V put(K key, V value)` | Inserts or updates a value. |
| Get | `V get(Object key)` | Retrieves value, null if missing. |
| GetOrDefault | `V getOrDefault(Object key, V def)` | Retrieves value, default if missing. |
| ContainsKey | `boolean containsKey(Object key)` | True if key exists (O(1)). |
| ContainsValue | `boolean containsValue(Object val)` | True if value exists (O(n)). |
| Remove | `V remove(Object key)` | Deletes key and returns old value. |
| Replace | `V replace(K key, V value)` | Updates ONLY if key exists. |
| PutIfAbsent | `V putIfAbsent(K key, V value)` | Inserts ONLY if key missing. |
| Merge | `V merge(K key, V val, BiFunction)` | Combines old and new value. |
| ComputeIfAbsent | `V computeIfAbsent(K key, Function)` | Computes value if missing. |
| KeySet | `Set<K> keySet()` | Returns view of all keys. |
| Values | `Collection<V> values()` | Returns view of all values. |
| EntrySet | `Set<Map.Entry<K,V>> entrySet()` | Returns view of key-value pairs. |
| Clear | `void clear()` | Empties the map. |

**Card 2: TreeMap Navigation Methods**
| Method | Signature | One-line purpose |
|---|---|---|
| FirstKey | `K firstKey()` | Smallest key in map. |
| LastKey | `K lastKey()` | Largest key in map. |
| FloorKey | `K floorKey(K key)` | Greatest key <= given key. |
| CeilingKey | `K ceilingKey(K key)` | Smallest key >= given key. |
| LowerKey | `K lowerKey(K key)` | Greatest key strictly < given key. |
| HigherKey | `K higherKey(K key)` | Smallest key strictly > given key. |
| PollFirstEntry | `Map.Entry<K,V> pollFirstEntry()` | Removes and returns smallest entry. |
| PollLastEntry | `Map.Entry<K,V> pollLastEntry()` | Removes and returns largest entry. |

**Card 3: Comparator Factory Methods**
| Method | Signature | One-line purpose |
|---|---|---|
| Comparing | `Comparator.comparing(Function)` | Sort by a specific object property. |
| Reversed | `comparator.reversed()` | Reverses the current comparator direction. |
| ThenComparing | `comparator.thenComparing(Other)` | Tie-breaker sort criteria. |
| NaturalOrder | `Comparator.naturalOrder()` | Standard ascending sort. |
| ReverseOrder | `Comparator.reverseOrder()` | Standard descending sort. |
| NullsLast | `Comparator.nullsLast(comparator)` | Puts null objects at the end. |
| ComparingInt | `Comparator.comparingInt(ToIntFunc)` | Sort by int property (avoids boxing). |

**Card 4: Sorting Algorithm Selector**
```text
Is the input range bounded (integers in [0,k])?
├── YES → Counting Sort O(n+k) or Radix Sort O(d·n)
└── NO  → Comparison sort needed
         ├── Need stability? → Merge Sort O(n log n) / TimSort
         ├── Nearly sorted? → Insertion Sort O(n) or TimSort
         ├── Memory tight? → Heap Sort O(n log n), O(1) space
         └── General case? → Quick Sort O(n log n) avg
```

**Card 5: Hashing Pattern Selector**
```text
Does order of elements matter?
├── YES → LinkedHashMap (insertion order) or TreeMap (sorted)
└── NO  → HashMap (fastest)
         ├── Counting occurrences? → freq.getOrDefault(key, 0) + 1
         ├── Pair with target sum? → Two Sum pattern
         ├── Subarray sum = k? → Prefix Sum + HashMap
         ├── Group equivalents? → Canonical key + computeIfAbsent
         └── Longest window with constraint? → Sliding Window + HashMap
```

