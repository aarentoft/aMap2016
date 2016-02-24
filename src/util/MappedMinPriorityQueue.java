package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Minimal implementation of an mapped minimum priority queue (ie. smallest element will always be in front)
 * needed by the Dijkstra and A* path finding algorithms.
 *
 * @param <Item> Type of the elements, the priority queue will contain.
 */
public class MappedMinPriorityQueue<Key, Item extends Comparable<Item>> {

    // This is the actual heap structure. Keeps the elements of itemValues in order
    // by storing and ordering the Keys of itemValues.
    private Key[]             heap;
    // itemPositionInHeap.get(k)] is the position of itemValues.get(k) in the heap
    private Map<Key, Integer> itemPositionInHeap;
    // NOTE: The position of an item in itemValues is final as long as the value is part of the Priority Queue
    private Map<Key, Item>    itemValues;
    // The number of elements in the Priority Queue
    private int               size;

    /**
     * Instantiate an MappedMinPriorityQueue with a specified maximum size.
     *
     * @param maxSize Maximum size of the priority queue.
     */
    public MappedMinPriorityQueue(int maxSize) {
        // The index 0 remains unused
        heap = (Key[]) new Object[maxSize + 1];
        itemPositionInHeap = new HashMap<>(maxSize + 1);
        itemValues = new HashMap<>(maxSize + 1);
        size = 0;
    }

    /**
     * Insert a item with specified index into the priority queue.
     *
     * @param i   Index value, the item will get.
     * @param item Item to insert.
     */
    public void insert(Key i, Item item) {
        size++;
        itemPositionInHeap.put(i, size);
        heap[size] = i;           // Insert i at bottom of heap
        itemValues.put(i, item);
        heapifyUp(size);           // Restore heap order
    }

    /**
     * Removes the min Item from the Priority Queue.
     *
     * @return The min Item
     */
    public Key popMin() {
        Key min = heap[1];
        swap(1, size);      // Swap top element with the last element (which is also one of the smallest)
        size--;             // NOTE: size needs to be updated here, otherwise heapifyDown will fail
        heapifyDown(1);     // Restore heap order after moving one of the smallest elements to the top

        // Clean-up. Remove the previous top element
        itemPositionInHeap.remove(min);
        itemValues.remove(heap[size + 1]);
        heap[size + 1] = null;
        return min;
    }

    /**
     * Decrease the value of the Item associated with index i.
     *
     * @param i Index of the Item value to decrease
     * @param item Decrease the value to this Item value.
     */
    public void decreaseItemValue(Key i, Item item) {
        // if itemValues[i] is less than item, the value will INcrease instead of DEcrease.
        if (itemValues.get(i).compareTo(item) <= 0)
            return;

        itemValues.put(i, item);
        heapifyUp(itemPositionInHeap.get(i));
    }

    /**
     * @param i Index to test.
     * @return Whether index i is associated with some Item.
     */
    public boolean contains(Key i) {
        return itemPositionInHeap.get(i) != null;
    }

    /**
     * @return Whether the Priority Queue is empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Moves the element at index i up the heap until proper heap order has been restored.
     *
     * @param i Heap index to heapify up.
     */
    private void heapifyUp(int i) {
        // When i is 1, the top has been reached.
        // i/2 is the parent of i, so i is swapped with its parent as long
        // the parent of i is greater than i itself (ie. the minimum heap order is violated)
        while (i > 1 && greater(i / 2, i)) {
            swap(i / 2, i);
            i = i / 2;
        }
    }

    /**
     * Moves the element at index i down the heap until proper heap order has been restored.
     *
     * @param i Heap index to heapify down.
     */
    private void heapifyDown(int i) {
        while (2 * i <= size) {
            // child1 = 2*i, child2 = child1+1
            int largestChild = greater(2 * i, 2 * i + 1) && 2 * i + 1 < size ? 2 * i + 1 : 2 * i;

            // if i is larger than its largest child, heap order has been restored
            if (!greater(i, largestChild))
                break;

            swap(i, largestChild);
            i = largestChild;
        }
    }

    /**
     * @param i1 First index.
     * @param i2 Second index.
     * @return Whether the Item at i1 is greater than the Item at i2.
     */
    private boolean greater(int i1, int i2) {
        Item v1 = itemValues.get(heap[i1]);
        Item v2 = itemValues.get(heap[i2]);
        if (v1 != null && v2 != null)
            return v1.compareTo( v2 ) > 0;
        else
            return false;
    }

    /**
     * Swap the Item at index i1 in the heap with the Item at index i2 in the heap.
     *
     * @param i1 Index of first Item to swap.
     * @param i2 Index of second Item to swap.
     */
    private void swap(int i1, int i2) {
        Key i1temp = heap[i1];
        heap[i1] = heap[i2];
        heap[i2] = i1temp;
        itemPositionInHeap.put(heap[i1], i1);
        itemPositionInHeap.put(heap[i2], i2);
    }
}