import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node{
        Item item;
        Node next;
        Node previous;
    }
    private Node first;
    private Node last;
    private int size;
    private class DequeIterator implements Iterator<Item>{
        private Node current = first;
        public boolean hasNext() {
            // StdOut.println(node.next != null);
            return current != null;
        }
        public Item next() {
            if (!hasNext()){
                // StdOut.println(null);
                throw new NoSuchElementException();
            }
            else{
                Item item = current.item;
                current = current.next;
                return item;
            }
        }
        public void remove() {throw new UnsupportedOperationException();}
    }
    public Deque(){
        // construct an empty deque
        first = null;
        last = null;        
        size = 0;
    }
    public boolean isEmpty(){
        // is the deque empty?
        return (first == null);
    }
    public int size(){
        // return the number of items on the deque
        return size;
    }
    public void addFirst(Item item){
        // add the item to the front
        if (item == null) throw new NullPointerException();
        else{
            Node new_first = new Node();
            new_first.item = item;
            new_first.next = null;
            new_first.previous = null;
            if (first == null){
                first = new_first;
                last = first;
            }
            else{
                new_first.next = first;
                first.previous = new_first;
                first = new_first;                
            }
            size++;
        }
    }
    public void addLast(Item item){
        // add the item to the end
        if (item == null) throw new NullPointerException();
        else{
            Node new_last = new Node();
            new_last.item = item;
            new_last.next = null;
            new_last.previous = null;
            if (last == null){
                last = new_last;
                first = last;
            }
            else{
                last.next = new_last;
                new_last.previous = last;
                last = new_last;                
            }
            size++;
        }
    }
    public Item removeFirst(){
        // remove and return the item from the front
        if (isEmpty()) throw new NoSuchElementException();
        else{
            Item item = first.item;
            first = first.next;
            if (first == null) last = null;
            else first.previous = null;
            size--;
            return item;
        }
    }
    public Item removeLast(){
        // remove and return the item from the end
        if (isEmpty()) throw new NoSuchElementException();
        else{
            Item item = last.item;
            last = last.previous;
            if (last == null) first = null;
            else last.next = null;
            size--;
            return item;
        }
       // NoSuchElementException 
    }
    public Iterator<Item> iterator(){
        // return an iterator over items in order from front to end
        return new DequeIterator();
    }
   public static void main(String[] args){
   // unit testing
       Deque<String> s_deque = new Deque<String>();
       StdOut.println("empty? "+s_deque.isEmpty());
       s_deque.addLast("a");
       StdOut.println("empty? "+s_deque.isEmpty());
       for (String item : s_deque)  StdOut.println(item);
       s_deque.addLast("b");
       s_deque.addFirst("c");
       s_deque.addFirst("d");
       StdOut.println("size? "+s_deque.size());
       for (String item : s_deque)  StdOut.println(item);

       String s_first = s_deque.removeFirst();
       StdOut.println("first item: "+s_first);
       String s_last = s_deque.removeLast();
       StdOut.println("last item: "+s_last);
       for (String item : s_deque)  StdOut.println(item);
       StdOut.println("size? "+s_deque.size());
       s_last = s_deque.removeLast();
       s_last = s_deque.removeLast();
       StdOut.println("size? "+s_deque.size());
       // Should be empty
       StdOut.println("empty? "+s_deque.isEmpty());
       for (String item : s_deque)  StdOut.println(item);
       // s_last = s_deque.removeFirst();
       // s_last = s_deque.removeLast();
       s_deque.addFirst("e");
       for (String item : s_deque)  StdOut.println(item);
   }
}
