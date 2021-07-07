
import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Comparator;


public class KdTree {
    private Node root;
    private Queue<Node> q = new Queue<>();
    private Queue<Point2D> pq = new Queue<>();
    private ArrayList<Point2D> points = new ArrayList<>();
    private BST<Double, Double> intTree = new BST<>();
    private MinPQ<Node> intersectingNodes = new MinPQ<>();
    private RectHV rHl = null;
    private RectHV rHr = null;

    private static class Node implements Comparable<Node> {
        Point2D p; // key
        Node left, right, parent; // subtrees
        int n; // # nodes in this subtree
        boolean coordinate; // 0 means horizontal
        RectHV nodeRect;
        double maximumX = 0.0;
        double maximumY = 0.0;

        public Node(Point2D p, int n, boolean coordinate, Node parent) {
            this.p = p;
            this.coordinate = coordinate;
            this.parent = parent;
            this.n = n;
            this.nodeRect = null;
        }

        @Override
        public int compareTo(Node h) {
            double thisX = this.p.x();
            double thisY = this.p.y();
            double hX = h.p.x();
            double hY = h.p.y();
            if (!this.coordinate) {
                if (thisX < hX) {
                    h.coordinate = true;
                    return -1;
                } else {
                    h.coordinate = true;
                    return 1;
                }
            }
            if (this.coordinate) {
                if (thisY < hY) {
                    h.coordinate = false;
                    return -1;
                } else {
                    h.coordinate = false;
                    return 1;
                }
            }
            return 1;
        }
    }

    public void draw() {
        RectHV rec = new RectHV(0.0, 0.0, 1.0, 1.0);
        if (root == null) return;
        draw(root, rec);
    }

    private void draw(Node h, RectHV rectHV) {
        RectHV tempRect;
        if (!h.coordinate) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.012);
            StdDraw.point(h.p.x(), h.p.y());
            StdDraw.point(h.p.x(), h.p.y());
            StdDraw.setPenRadius(0.003);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(h.p.x(), rectHV.ymin(), h.p.x(), rectHV.ymax());
            if (h.left != null) {
                tempRect = new RectHV(rectHV.xmin(), rectHV.ymin(), h.p.x(), rectHV.ymax());
                draw(h.left, tempRect);
            }
            if (h.right != null) {
                tempRect = new RectHV(h.p.x(), rectHV.ymin(), rectHV.xmax(), rectHV.ymax());
                draw(h.right, tempRect);
            }
        } else if (h.coordinate) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.012);
            StdDraw.point(h.p.x(), h.p.y());
            StdDraw.setPenRadius(0.003);
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rectHV.xmin(), h.p.y(), rectHV.xmax(), h.p.y());
            if (h.left != null) {
                // the sub rectangles are different depending on parent axis orientation
                tempRect = new RectHV(rectHV.xmin(), rectHV.ymin(), rectHV.xmax(), h.p.y());
                draw(h.left, tempRect);
            }
            if (h.right != null) {
                tempRect = new RectHV(rectHV.xmin(), h.p.y(), rectHV.xmax(), rectHV.ymax());
                draw(h.right, tempRect);
            }
        }

    }
private void sweepLineAlg(){
        /* insert y-coordinate into the bst, Horizontal-segment left endpoint insert y-coordinate into the BST. At
        Horizontal-segment right endpoint: remove y-coodinate from BST. When encountering a vertical-segment: range
         search for interface of y-endpoints*/
}
    private Point2D get(Point2D p) {
        return get(root, p);
    }

    public boolean isEmpty() {
        return keys() == null;
    }

    private Point2D get(Node h, Point2D p) {
        if (h == null) return null;
        int cmp = p.compareTo(h.p);
        if (cmp < 0) return get(h.left, h.p);
        else if (cmp > 0) return get(h.right, h.p);
        else return h.p;
    }

    private Iterable<Node> keys() {
        q = new Queue<>();
        return keys(root);
    }

    private Queue<Node> keys(Node h) {
        if (h == null) return null;
        if (h != null) q.enqueue(h);
        if (h.left != null) {
            keys(h.left);
        }
        if (h.right != null) {
            keys(h.right);
        }
        return q;
    }

    public boolean contains(Point2D p) {
        return get(p) != null;
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rectangle has to be a valid " +
                "object. ");
        root.nodeRect = new RectHV(0.0, 0.0, 1.0, 1.0);

        /* I may have to and be able to use these  now to make sure I do not look at rectangles I do not need to. Once
         * the rect is covered, I am done. Right now, it might have too many rectangles to count. It might! Might just
         * work. Also as you pull off nodes from intersectionRectangles, you can check to see if left and right intersect,
         * and if so, just ignore the parent and not check for points since it is redundant. You can save time and
         * processing. */
        for (Object o : range(root, rect)) {  // make sure the smallest rec is coming in first; if not, fix it
            Node node = (Node) o;
            for (Node n : keys(node)) {
                if (rect.contains(n.p) && (!points.contains(n.p))) points.add(n.p);

            }
        }
        return points;
    }

    private Iterable<Node> range(Node h, RectHV rect) {
        // for (double i = 0.0;i<1.0;i+=0.1){
        // if (h.nodeRect.xmin()<=i){
        // Problem - I can not use any other BST and I am using this one to store points
        // }

        /* only create rectangles if the point at the node is in the target rectangle */
        double x = h.p.x();
        double y = h.p.y();
        if (rect.contains(h.p)) {
            intersectingNodes.insert(h);
            // If rect contains the root node, it contains all the nodes underneath it
            return intersectingNodes;
        }
        /* I think I should use rank() function. rank() of a rectangle's xmin should give me the branch that I would then
         * have to check all the nodes if they are contained by the rectangle. All this in logarithmic time. I also have
         * wow it seems so much easier than what I did below
         * And I can start at the nodes which their rectangle intercepts with rect wow wow wow. Also use the interval search
         * tree algorithm and store the max of each subtree at the node so you do not have to traverse if entirely to see
         * if there is an overlap wow wow  In order to do SweepLine algorithm, create a loop that increases x coordinate
         * by .1 and checks to see if there are any rectangles with minx value smaller or equal to the current x i.e.
         * kdtree.floor(currentX). You have to check to see if  you need to update the maximum on the way up the tree. I
         * think I did it while going down. Test to make sure the maximum x propagates to the top correctly. Create a
         * tree like slide 24 with tailored coordinates and see if it behaves the same. I think one of the test files
         * already has them. See if size() gives you all the nodes below a node i.e. rank. Create a unit test for it,
         * and validate it. As you check to see which rectangles intersect with rect, you can create an interval symbol
         * table of a the points in the intersecting rectangles so you can later check to see if rect.contain()s them.
         * Instead of creating rectangles and saving them in the KdTree, save the interval; I think both x and y. Then
         * when you are looking for intersecting rectangles you can look for the ones that intersect the x interval of
         * your rectangle, and you have the y coordinate also. But most importantly you have the points as value.  How
         * the heck do i store more than one value? Maybe I can make the comparator so that two nodes are never the same?
         * Perhaps x+y+value? Slide 54 says exactly how to get the intersections with Sweep Line algorithm. I guess then
         * I can get the points for those intervals from an interval tree; kinda looks like the tree I implemented here.
         * check to see if the rect contains the node at the root, and so we are done. If it contains the node at the root
         * it contains all the nodes in that subtree. The knowledge of rect intersecting the spliting line at a node
         * will let us know which subtrees we need to search  */
        {
            // now create rHl
            if (!h.coordinate) {  // horizontal scenario

                if (h.left != null) {
                    rHl = new RectHV(h.nodeRect.xmin(), h.nodeRect.ymin(), h.p.x(), h.nodeRect.ymax());
                    h.left.parent = h;
                    h.left.nodeRect = rHl;
                }
                if (h.right != null) {
                    // rHr = new RectHV(h.p.x(), h.nodeRect.ymin(), h.nodeRect.xmax(), h.nodeRect.ymax());
                    rHr = new RectHV(x, h.nodeRect.ymin(), h.nodeRect.xmax(), h.nodeRect.ymax());
                    h.right.parent = h;
                    h.right.nodeRect = rHr;
                }
                if (h.left != null && h.nodeRect.xmin() < h.left.maximumX) {
                    h.left.nodeRect = rHl;
                    range(h.left, rect);
                    // if you run into nodes with null rectangle in points, you may have to set h.nodeRect to rHl here
                }

                if (h.right != null && h.nodeRect.xmax() > h.right.maximumX) {
                    h.right.nodeRect = rHr;
                    range(h.right, rect);
                    /* if you run into nodes with null rectangle in points, you may have to set h.nodeRect to hHr here */
                }
            }
            if (h.coordinate) {  // If h does not have a rectangle, recreate it. If it does use it.
                if (rect.contains(h.p)) {
                    intersectingNodes.insert(h);
                    return intersectingNodes;
                }
                if (h.left != null && h.nodeRect.xmin() < h.left.maximumX) {
                    // rHl = new RectHV(h.nodeRect.xmin(), h.nodeRect.ymin(), h.nodeRect.xmax(), h.p.y());
                    rHl = new RectHV(h.nodeRect.xmin(), h.nodeRect.ymin(), h.nodeRect.xmax(), y);
                    h.left.parent = h;
                    h.left.nodeRect = rHl;
                }
                if (h.right != null && h.nodeRect.xmax() > h.right.maximumX) {
                    // rHr = new RectHV(h.nodeRect.xmin(), h.p.y(), h.nodeRect.xmax(), h.nodeRect.ymax());
                    rHr = new RectHV(h.nodeRect.xmin(), y, h.nodeRect.xmax(), h.nodeRect.ymax());
                    h.right.parent = h;
                    h.right.nodeRect = rHr;
                }
                if (h.left != null) {
                    h.left.nodeRect = rHl;
                    range(h.left, rect);
                    // if you run into nodes with null rectangle in points, you may have to set h.nodeRect to rHl here
                }
                if (h.right != null) {
                    h.right.nodeRect = rHr;
                    range(h.right, rect);
                    /* if you run into nodes with null rectangle in points, you may have to set h.nodeRect to hHr here */
                }
            }
        }
        return intersectingNodes;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("You can not insert null object" +
                "into the tree");
        Node newNode = new Node(p, 1, false, null);
        newNode.maximumX = p.x();
        root = insert(root, newNode);
    }

    private Node insert(Node h, Node newNode) {
        if (h == null) {
            return newNode;
        } else {
            int cmp = h.compareTo(newNode);
            h.maximumX = Math.max(root.maximumX, newNode.maximumX);
            /* I think I just save maximum X to know which branches to visit, Y coordinate ranges are saved
             * in a separate tree to keep track of which intervals were covered. */
            if (cmp < 0) {
                newNode.parent = h;
                h.right = insert(h.right, newNode);
            } else if (cmp > 0) {
                newNode.parent = h;
                h.left = insert(h.left, newNode);
            }
        }
        int leftN = 0;
        if (h.left != null) {
            leftN = h.left.n;
        }
        int rightN = 0;
        if (h.right != null) {
            rightN = h.right.n;
        }
        h.n = leftN + rightN + 1;
        return h;
    }

    public int size() {
        if (root == null) return 0;
        else return root.n;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Data passed to nearest() can not be null.");
        if (root == null) throw new IllegalArgumentException("The tree is empty.");
        /* if the closest point discovered so far is closer than the distance between the query point and the rectangle
        corresponding to a node, there is no need to explore that node (or its subtrees). */
        RectHV initialRec = new RectHV(0.0, 0.0, 1.0, 1.0);
        Point2D nearestNeig = root.p;
        root.nodeRect = initialRec;
        return nearest(root, p, nearestNeig);
    }

    /* I may have to change this method also, and the fix might very well the fact that we have to go towards the query
     * point first. */
    private Point2D nearest(Node h, Point2D p, Point2D nearstP) {
        RectHV rHl = null;
        RectHV rHr = null;
        if (h == null) return nearstP;
        if (!h.coordinate) {
            if (h.parent == null) {
                rHl = new RectHV(0.0, 0.0, h.p.x(), 1.0);
                rHr = new RectHV(h.p.x(), 0.0, 1.0, 1.0);
            } else if (h.parent != null) {
                // I have to rebuild the h rectangle here or save it in the node from previous round.
                // How should I handle points like 0.0,0.5? there is no left rectangle if (h.x() == 0) do what?
                rHl = new RectHV(h.nodeRect.xmin(), h.nodeRect.ymin(), h.p.x(), h.nodeRect.ymax());
                rHr = new RectHV(h.p.x(), h.nodeRect.ymin(), h.nodeRect.xmax(), h.nodeRect.ymax());
            }
            if (h.left != null) {
                if (rHl.distanceSquaredTo(p) < p.distanceSquaredTo(nearstP)) {
                    if (h.left.p.distanceSquaredTo(p) < nearstP.distanceSquaredTo(p)) {
                        nearstP = h.left.p;
                    }
                }
                h.left.parent = h;
                h.left.nodeRect = rHl;
                nearstP = nearest(h.left, p, nearstP);
            }
            if (h.right != null) {
                if (rHr.distanceSquaredTo(p) < p.distanceSquaredTo(nearstP)) {
                    if (h.right.p.distanceSquaredTo(p) < nearstP.distanceSquaredTo(p)) {
                        nearstP = h.right.p;
                    }
                }
                h.right.parent = h;
                h.right.nodeRect = rHr;
                nearstP = nearest(h.right, p, nearstP);
            }

        }
        if (h.coordinate) {
            rHl = new RectHV(h.nodeRect.xmin(), h.nodeRect.ymin(), h.nodeRect.xmax(), h.p.y());
            rHr = new RectHV(h.nodeRect.xmin(), h.p.y(), h.nodeRect.xmax(), h.nodeRect.ymax());
            // rHr = new RectHV(h.p.x(),h.nodeRect.ymin(),h.nodeRect.xmax(),h.nodeRect.ymax());
            if (h.left != null) {
                if (rHl.distanceSquaredTo(p) < p.distanceSquaredTo(nearstP)) {
                    if (h.left.p.distanceSquaredTo(p) < nearstP.distanceSquaredTo(p)) {
                        nearstP = h.left.p;
                    }
                }
                h.left.parent = h;
                h.left.nodeRect = rHl;
                nearstP = nearest(h.left, p, nearstP);
            }
            if (h.right != null) {
                if (rHr.distanceSquaredTo(p) < p.distanceSquaredTo(nearstP)) {
                    if (h.right.p.distanceSquaredTo(p) < nearstP.distanceSquaredTo(p)) {
                        nearstP = h.right.p;
                    }
                }
            }
        }
        return nearstP;
    }

    private int height(Node root) {
        if (root == null)
            return 0;
        else {
            /* Compute the height of each subtree */
            int lheight = height(root.left);
            int rheight = height(root.right);
            /* use the larger one */
            if (lheight > rheight)
                return (lheight + 1);
            else return (rheight + 1);
        }
    }

    /* print the current level */
    private void printCurrentLevel(Node root, int level) {
        if (root == null) return;
        if (level == 1) StdOut.println(root.p);
        else if (level > 1) {
            printCurrentLevel(root.left, level - 1);
            printCurrentLevel(root.right, level - 1);
        }
    }

    private void ensureOrder(Node root, int level) {
        if (root == null || root.left == null || root.right == null) return;
        if (root.compareTo(root.left) < 0) StdOut.println("Need to fix the tree. " +
                root.left + "is on the left of its parent but it is larger, or comparator is " +
                "messed up");
        if (root.compareTo(root.right) > 0) StdOut.println("Need to fix the tree. " + root +
                "is the parent but it is larger than its right child, or comparator is " +
                "messed up");
        else {
            ensureOrder(root.left, level - 1);
            ensureOrder(root.right, level - 1);
        }
    }

    private void ensureOrder() {
        int h = height(root);
        int i;
        for (i = 1; i <= h; i++) {
            ensureOrder(root, i);
        }
    }

    private void printLevelOrder() {
        int h = height(root);
        int i;
        for (i = 1; i <= h; i++) {
            printCurrentLevel(root, i);
        }
    }

    public static void main(String[] args) {
//        int increment = 3;
//        Point2D p = null;
        KdTree kdtree = new KdTree();
//        for (int i = 0; i < 100; i++) {
//            p = new Point2D(StdRandom.uniform(0.0, 1.0), StdRandom.uniform(0.0, 1.0));
//            kdtree.insert(p);
//        }
//        RectHV r = new RectHV(0.1, 0.1, 0.8, 0.6);
//        StdOut.println("Rectangle: " + r + "Contains points: " + kdtree.range(r));

        String filename = args[0];
        In in = new In(filename);
//        PointSET brute = new PointSET();
//
//        int counter = 0;
//        for (int i = 0; i < 4; i++) {
//            double x = in.readDouble();
//            double y = in.readDouble();
//            Point2D p = new Point2D(x, y);
//            kdtree.insert(p);
//        }
//        for (Node node : kdtree.keys()) {
//            //StdOut.printf("%2.2f%n",node.maximumX);
//            StdOut.println("Here is the point: " + node.p + "Here is its maximum x: " + node.maximumX);
//        }
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            // StdOut.println(kdtree.root+""+kdtree.root.maximumX);
//            brute.insert(p);
            // counter++;
            // StdOut.println(counter+" th number was just inserted.");
//            StdOut.println();
//            kdtree.printLevelOrder();
        }

        // kdtree.ensureOrder();
        //RectHV r = new RectHV(0.1, 0.1, 0.5, 0.7);
        RectHV r = new RectHV(0.1, 0.5, 0.3, 0.7);
        // Stopwatch st = new Stopwatch();
        // kdtree.range(r);
        // double rangeElapsedTime = st.elapsedTime();
//        StdOut.println("Here are the points in rectangle " + r);
        StdOut.println("Here are the points in the above rectangle: ");
        for (Point2D p : kdtree.range(r)) {
            StdOut.println(" : " + p);
        }
        // StdOut.println("Kd range() took " + rangeElapsedTime + " seconds.");
        // StdOut.printf("Kd range() took %20.6f%n", rangeElapsedTime);
        // kdtree.draw();
        // StdOut.println("now we are going to test range.");
        //
        // StdOut.println("Rectangle: " + r + "Contains points: " + kdtree.range(r));
//        StdOut.println("Should be 10 " + kdtree.size());
//        StdOut.println("Should be 10 " + brute.size());
//        StdOut.println("Should be false " + kdtree.isEmpty());
//        StdOut.println("Should be false " + brute.isEmpty());
//        kdtree.draw();
//        KdTree kt = new KdTree();
//        Point2D p1 = new Point2D(0.5, 0.25);
//        kt.insert(p1);
//        Point2D p2 = new Point2D(0.0, 0.5);
//        kt.insert(p2);
//        Point2D p3 = new Point2D(0.5, 0.0);
//        kt.insert(p3);
//        Point2D p4 = new Point2D(0.25, 0.0);
//        kt.insert(p4);
//        Point2D p5 = new Point2D(0.0, 1.0);
//        kt.insert(p5);
//        Point2D p6 = new Point2D(1.0, 0.5);
//        kt.insert(p6);
//        Point2D p7 = new Point2D(0.25, 0.0);
//        kt.insert(p7);
//        Point2D p8 = new Point2D(0.0, 0.25);
//        kt.insert(p8);
//        Point2D p9 = new Point2D(0.25, 0.0);
//        kt.insert(p9);
//        Point2D p10 = new Point2D(0.25, 0.5);
//        kt.insert(p10);
        // Point2D queryPoint = new Point2D(0.75, 0.75);
        // kt.draw();
        // StdOut.println("Distance Squared to Query Point: " + kt.nearest(queryPoint).distanceSquaredTo(queryPoint));
        // StdOut.println(kt.nearest(queryPoint));
//        StdOut.println("Changed something for testing.");
        //KdTree k = new KdTree();
//        Queue<Point2D> s = new Queue<>();
//        Point2D p1 = new Point2D(0.7, 0.2);
//        s.enqueue(p1);
//        Point2D p2 = new Point2D(0.5, 0.4);
//        s.enqueue(p2);
//        Point2D p3 = new Point2D(0.2, 0.3);
//        s.enqueue(p3);
//        Point2D p4 = new Point2D(0.4, 0.7);
//        s.enqueue(p4);
//        Point2D p5 = new Point2D(0.9, 0.6);
//        s.enqueue(p5);
//        Point2D p6 = new Point2D(0.1, 0.9);
//        s.enqueue(p6);
//        Point2D p7 = new Point2D(0.2, 0.8);
//        s.enqueue(p7);
//        Point2D p8 = new Point2D(0.3, 0.7);
//        s.enqueue(p8);
//        Point2D p9 = new Point2D(0.4, 0.7);
//        s.enqueue(p9);
//        Point2D p10 = new Point2D(0.9, 0.6);
//        s.enqueue(p10);
//        for (Point2D p : s) {
//            k.insert(p);
//        }
        //Stack<RectHV> recs = new Stack<>();
        //RectHV r = new RectHV(0.8, 0.5, 1.0, 0.7);
        //recs.push(r);

        // recs.push(r);
        // r = new RectHV(0.0, 0.0, 1.0, 1.0);
        // recs.push(r);
        // r = new RectHV(0.7, 0.2, 1.0, 1.0);
        // recs.push(r);
//        for (RectHV rec : recs) {
//            StdOut.println("Rectangle: " + rec + "Contains points: " + k.range(rec));
//        }
        // StdOut.println("Does r contain the first node? " + r.contains(p1));
//        for (Point2D p : k.range(r)) {
//            StdOut.println("Here is the points in above rectangle: " + p);
//        }
//        StdOut.println("Here is the point in your rectangle : " + k.range(r));
//        StdOut.println("Rectangle " + r + "has the following points inside it: " + k.range(r));
//        Point2D p = k.nearest(queryPoint);
//        StdOut.println("Here is the nearest point to 0.75, 0.75: " + p);
//        k.draw();
//        for (int i = 0; i < 20; i++) {
//            Point2D p = new Point2D(StdRandom.uniform(0.0, 1.0), StdRandom.uniform(0.0, 1.0));
//            k.insert(p);
//        }
//        StdOut.println("Finished w/o errors.");
//        int index = 1;
//        for (Node n : k.keys()) {
//            if (n.coordinate == true) {
//                StdOut.println(index + "-" + n.p);
//                index++;
//            }
//        }
    }
}

