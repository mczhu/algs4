import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size;
    private Item[] item_array;
    private void resize(int capacity) {
        Item[] new_array = (Item[]) new Object[capacity];
        for (int i=0; i<size; i++) {
            new_array[i] = item_array[i];
        }
        item_array = new_array;
    }
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int itr_ind = 0;
        private Item[] shuffledItem;
        public RandomizedQueueIterator() {
            shuffledItem = (Item[]) new Object[size];
            for (int i=0; i<size; i++)  shuffledItem[i] = item_array[i];
            StdRandom.shuffle(shuffledItem);
        }
        public boolean hasNext() {
            return (itr_ind < size);
        }
        public Item next() {
            if (!hasNext())  throw new NoSuchElementException();
            Item current = shuffledItem[itr_ind++];
            return current;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    public RandomizedQueue() {
        // construct an empty randomized queue
        item_array = (Item[]) new Object[1];
        size = 0;
    }
    public boolean isEmpty() {
        // is the queue empty?
        return size == 0;
    }
    public int size()   {
        // return the number of items on the queue
        return size;
    }
    public void enqueue(Item item) {
        // add the item
        if (item == null) throw new NullPointerException();
        if (size == item_array.length) resize(2*item_array.length);
        // StdOut.println("item_array size: " +item_array.length);
        item_array[size++] = item;
    }
    public Item dequeue()   {
        // remove and return a random item
        // Constant amortized time: on average every 1 out of 4 access will
        // be none-null since the queue is at least 25% full.
        if (isEmpty()) throw new NoSuchElementException();
        int rand_idx = StdRandom.uniform(size);
        // Exchange with the last array element
        Item item = item_array[rand_idx];
        item_array[rand_idx] = item_array[size-1];
        item_array[size-1] = null;
        size--;
        if ((size > 0) && (size <= item_array.length/4)) resize(item_array.length/2);
        return item;
    }
    public Item sample()    {
        // return (but do not remove) a random item
        if (isEmpty()) throw new NoSuchElementException();
        int rand_idx = StdRandom.uniform(size);
        return item_array[rand_idx];
    }
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new RandomizedQueueIterator();
        // initialize a copy of the array and dequeue one by one
    }
    public static void main(String[] args) {
        // unit testing
        RandomizedQueue<String> rand_queue = new RandomizedQueue<String>();
        StdOut.println("empty? " +rand_queue.isEmpty());
        StdOut.println("size: " +rand_queue.size());
        rand_queue.enqueue("a");
        StdOut.println("empty? " +rand_queue.isEmpty());
        rand_queue.enqueue("b");
        rand_queue.enqueue("c");
        rand_queue.enqueue("d");
        rand_queue.enqueue("e");
        StdOut.println("size: " +rand_queue.size());
        for (String item : rand_queue)  StdOut.print(item+" ");
        StdOut.print("\n");
        for (String item : rand_queue)  StdOut.print(item+" ");
        StdOut.print("\n");
        StdOut.println(rand_queue.dequeue());
        StdOut.println("size: " +rand_queue.size());
        for (String item : rand_queue)  StdOut.print(item+" ");
        StdOut.print("\n");
        StdOut.println("sample: "+ rand_queue.sample());
        for (String item : rand_queue)  StdOut.print(item+" ");
        StdOut.print("\n");
    }
}
