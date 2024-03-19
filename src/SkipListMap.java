import java.util.Random;
import java.util.function.BiConsumer;

public class SkipListMap<K extends Comparable<? super K>, V> {

    private static final int MAX_LEVEL = 32; // Maximum level of the skip list
    private int size; // Number of elements in the skip list
    private Node<K, V> head; // Head node of the skip list
    private FakeRandHeight random; // Fake random number generator for generating node levels

    // Constructor
    public SkipListMap(FakeRandHeight random) {
        size = 0;
        head = new Node<>(null, null, MAX_LEVEL);
        this.random = random;
    }

    // Node class for storing key-value pairs
    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V>[] next;

        @SuppressWarnings("unchecked")
        public Node(K key, V value, int level) {
            this.key = key;
            this.value = value;
            this.next = new Node[level];
        }
    }

    // Put method to insert a key-value pair into the skip list
    public void put(K key, V value) {
        Node<K, V>[] update = new Node[MAX_LEVEL]; // Array to store nodes to update
        Node<K, V> current = head; // Start from the head node

        // Traverse the skip list to find the insertion position
        for (int i = MAX_LEVEL - 1; i >= 0; i--) {
            while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                current = current.next[i]; // Move forward
            }
            update[i] = current; // Store the node to update
        }

        current = current.next[0]; // Move to the next node at the lowest level

        // If the node with the same key exists, update its value
        if (current != null && current.key.compareTo(key) == 0) {
            current.value = value;
        } else {
            int newLevel = randomLevel(); // Generate a random level for the new node
            Node<K, V> newNode = new Node<>(key, value, newLevel); // Create a new node

            // Update pointers to include the new node
            for (int i = 0; i < newLevel; i++) {
                newNode.next[i] = update[i].next[i];
                update[i].next[i] = newNode;
            }

            size++; // Increment the size of the skip list
        }
    }

    // Get method to retrieve the value associated with a given key
    public V get(K key) {
        Node<K, V> current = head; // Start from the head node

        // Traverse the skip list level by level
        for (int i = MAX_LEVEL - 1; i >= 0; i--) {
            while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                current = current.next[i]; // Move forward
            }
        }

        current = current.next[0]; // Move to the next node at the lowest level

        // If the node with the given key is found, return its value
        if (current != null && current.key.compareTo(key) == 0) {
            return current.value;
        }

        return null; // Key not found, return null
    }

    // Remove method to remove the key-value pair associated with a given key
public void remove(K key) {
    Node<K, V>[] update = new Node[MAX_LEVEL]; // Array to store nodes to update
    Node<K, V> current = head; // Start from the head node
    
    // Traverse the skip list to find the node to remove
    for (int i = MAX_LEVEL - 1; i >= 0; i--) {
        while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
            current = current.next[i]; // Move forward
        }
        update[i] = current; // Store the node to update
    }
    
    current = current.next[0]; // Move to the next node at the lowest level
    
    // If the node with the given key is found, remove it
    if (current != null && current.key.compareTo(key) == 0) {
        // Update pointers to bypass the node to remove
        for (int i = 0; i < MAX_LEVEL; i++) {
            if (update[i].next[i] == current) {
                update[i].next[i] = current.next[i];
            }
        }
        size--; // Decrement the size of the skip list
    } else {
        // Print message and return early if the node to remove doesn't exist
        System.out.println("RemoveActivity " + key + " none");
        return;
    }
}

    // Check if the skip list map is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Perform an action on each key-value pair in the skip list map
    public void forEach(BiConsumer<K, V> action) {
        Node<K, V> current = head.next[0]; // Start from the first node

        // Traverse the skip list and apply the action to each node
        while (current != null) {
            action.accept(current.key, current.value); // Perform the action
            current = current.next[0]; // Move to the next node
        }
    }

    // SubMap method to retrieve a sub-map of key-value pairs between two keys
    public SkipListMap<K, V> subMap(K fromKey, K toKey) {
        SkipListMap<K, V> subMap = new SkipListMap<>(new FakeRandHeight()); // Adjusted to use generic types K, V
        Node<K, V> current = head; // Start from the head node
    
        // Traverse the skip list to find the starting node
        for (int i = MAX_LEVEL - 1; i >= 0; i--) {
            while (current.next[i] != null && current.next[i].key.compareTo(fromKey) < 0) {
                current = current.next[i]; // Move forward
            }
        }
    
        current = current.next[0]; // Move to the next node at the lowest level
    
        // Collect key-value pairs within the range [fromKey, toKey]
        while (current != null && current.key.compareTo(toKey) <= 0) {
            subMap.put(current.key, current.value); // Use generic types K, V
            current = current.next[0]; // Move to the next node
        }
    
        return subMap; // Return the sub-map
    }
    

    // Generate a random level for a new node
    private int randomLevel() {
        return random.get();
    }

    // Print the contents of the skip list map
    public void printSkipList() {
        boolean isEmpty = true; // Flag to track if the skip list is empty
        for (int i = MAX_LEVEL - 1; i >= 0; i--) {
            System.out.print("(S" + i + ") ");
            Node<K, V> current = head.next[i]; // Start from the first node at the current level
            if (current != null) {
                isEmpty = false; // Skip list is not empty
                while (current != null) {
                    System.out.print(String.format("%08d", current.key) + ":" + current.value + " ");
                    current = current.next[i]; // Move to the next node at the current level
                }
            } else {
                System.out.print("empty"); // Print "empty" if the level is empty
            }
            System.out.println(); // Move to the next line for the next level
        }
        if (isEmpty) {
            System.out.println("Skip list is empty");
        }
    }
}
