public class Subset {
    public static void main(String[] args) {
        RandomizedQueue<String> rand_queue = new RandomizedQueue<String>();
        int num_outs = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty())   rand_queue.enqueue(StdIn.readString());
        // for (String item : rand_queue)  StdOut.println(item);
        for (int k=0; k<num_outs; k++) StdOut.println(rand_queue.dequeue());
    }
}
