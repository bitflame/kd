import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class IntervalST<Key extends Comparable<Key>, Value> extends BST {
    Node root;
    Queue<Value> q = new Queue<>();

    private class Node {
        Key lo;
        Key hi;
        Key maximum;
        Value val;
        Node left, right;
        int N;

        public Node(Key low, Key high, Value value,int N) {
            lo = low;
            hi = high;
            val = value;
            this.N=N;
        }
    }

    void delete(Key low, Key high) {
        delete(low);
    }

    void put(Key low, Key high, Value value) {
// val is the max value
        if (low == null) throw new IllegalArgumentException("calls put(0 with a null key");
        if (value == null) {
            delete(low);
            return;
        }
        root = put(root, low, high, value);
    }

    private Node put(Node h, Key low, Key high, Value value) {
        if (h == null) {
            Node n = new Node(low, high, value,1);
            n.maximum = high;
            return n;
        }
        int cmp = low.compareTo(h.lo);
        if (cmp < 0) {
             h.left = put(h.left, low, high, value);
            //h.left = new Node(low, high, value);
            h.left.maximum=high;
            h.maximum = (h.maximum.compareTo(high) > 0) ? h.maximum : high;

        } else if (cmp > 0) {
             h.right = put(h.right, low, high, value);
            //h.right = new Node(low, high, value);
            h.right.maximum=high;
            h.maximum = (h.maximum.compareTo(high) > 0) ? h.maximum : high;
        } else h.val = value;
        h.N = 1 + size(h.left) + size(h.right);
        return h;
    }

    Key getMaximum() {
        return root.maximum;
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }

    Iterable<Value> intersects(Key low, Key high) {
        intersects(root, low, high);
        return q;
    }

    private Node intersects(Node h, Key low, Key high) {
        while (h != null) {
            if (((h.lo.compareTo(low) <= 0 && (h.hi.compareTo(low) >= 0))) ||
                    ((h.hi.compareTo(high) >= 0 && h.lo.compareTo(low) >= 0)) ||
                    ((h.lo.compareTo(low) >= 0) && (h.hi.compareTo(high) <= 0)) || ((h.lo.compareTo(low) <= 0) &&
                    (h.hi.compareTo(high) >= 0))) q.enqueue(h.val);
            if (h.left == null) {
                h.right = intersects(h.right, low, high);
                return h.right;
            }
            if (h.left.maximum.compareTo(low) < 0) {
                h.right = intersects(h.right, low, high);
                return h.right;
            }
            h.left = intersects(h.left, low, high);
            h.right = intersects(h.right, low, high);
        }
        return h;
    }

    public static void main(String[] args) {
        StdOut.println("Hi");
        IntervalST iST = new IntervalST();

    }

}
