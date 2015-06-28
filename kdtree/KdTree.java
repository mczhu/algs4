public class KdTree {

    private static class Node {
        private Point2D point;
        private Node left;
        private Node right;
        private RectHV rect;

        private Node(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
        }
    }

    private Node root;
    private int size;

    private void insertLeftRight(Point2D p, Node node) {
        if (p.x() < node.point.x()) {
            if (node.left == null) {
                RectHV newRec = new RectHV(node.rect.xmin(), node.rect.ymin(),
                                           node.point.x(), node.rect.ymax());
                node.left = new Node(p, newRec);
                // StdOut.println("parent:"+node.point.toString()+"; child:"+p.toString());
            }
            else insertBelowAbove(p, node.left);
        }
        else {
            if (node.right == null) {
                RectHV newRec = new RectHV(node.point.x(), node.rect.ymin(),
                                           node.rect.xmax(), node.rect.ymax());
                node.right = new Node(p, newRec);
            }
            else insertBelowAbove(p, node.right);
        }
    }

    private void insertBelowAbove(Point2D p, Node node) {
        if (p.y() < node.point.y()) {
            if (node.left == null) {
                RectHV newRec = new RectHV(node.rect.xmin(), node.rect.ymin(),
                                           node.rect.xmax(), node.point.y());
                node.left = new Node(p, newRec);
            }
            else insertLeftRight(p, node.left);
        }
        else {
            if (node.right == null) {
                RectHV newRec = new RectHV(node.rect.xmin(), node.point.y(),
                                           node.rect.xmax(), node.rect.ymax());
                node.right = new Node(p, newRec);
            }
            else insertLeftRight(p, node.right);
        }
    }


    private boolean containsLeftRight(Point2D p, Node node) {
        if (!node.point.equals(p)) {
            if (p.x() < node.point.x()) {
                // StdOut.println(p.toString() + " is to the left of" + node.point.toString());
                if (node.left == null) return false;
                else return containsBelowAbove(p, node.left);
            }
            else {
                if (node.right == null) return false;
                else return containsBelowAbove(p, node.right);
            }
        }
        return true;
    }

    private boolean containsBelowAbove(Point2D p, Node node) {
        if (!node.point.equals(p)) {
            if (p.y() < node.point.y()) {
                if (node.left == null) return false;
                else return containsLeftRight(p, node.left);
            }
            else {
                // StdOut.println(p.toString() + " is not above " + node.point.toString());
                if (node.right == null) return false;
                else return containsLeftRight(p, node.right);
            }
        }
        return true;
    }

    private void draw(Node node, boolean isVertical) {
        // Draw the point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        node.point.draw();
        StdDraw.setPenRadius();
        if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
            if (node.left != null) draw(node.left, false);
            if (node.right != null) draw(node.right, false);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
            if (node.left != null) draw(node.left, true);
            if (node.right != null) draw(node.right, true);
        }
    }

    private void addToRange(Node node, RectHV rect, Queue<Point2D> pointsInRange) {
        if (node.rect.intersects(rect)) {
            if (rect.contains(node.point)) pointsInRange.enqueue(node.point);
            if (node.left != null) addToRange(node.left, rect, pointsInRange);
            if (node.right != null) addToRange(node.right, rect, pointsInRange);
        }
    }

    private boolean isOnLeft(Node node, Point2D p, boolean isVertical) {
        if (isVertical) {
            if (p.x() < node.point.x()) return true;
        }
        else {
            if (p.y() < node.point.y()) return true;
        }
        return false;        
    }

    private double distToRect(Node node, Point2D p, boolean isVertical) {
        if (isVertical) {
            return Math.abs(p.x() - node.point.x());
        }
        else return Math.abs(p.y() - node.point.y());
    }

    private Point2D nn(Node node, Point2D np, Point2D p, boolean isVertical) {
        // StdOut.println(node.point.toString());
        if (node.point.distanceSquaredTo(p) <= np.distanceSquaredTo(p)) np = node.point;
        // StdOut.println("np: "+np.toString());
        // Decide which side of the node is p on 
        if (isOnLeft(node, p, isVertical)) {
            if (node.left != null)
                np = nn(node.left, np, p, !isVertical);
            if (np.distanceTo(p) >= distToRect(node, p, isVertical)) {
                if (node.right != null)
                    np = nn(node.right, np, p, !isVertical);
            }
        }
        else {
            // StdOut.println(p.toString() + " is on the right side of "+ node.point.toString());
            if (node.right != null)
                np = nn(node.right, np, p, !isVertical);
            if (np.distanceTo(p) >= distToRect(node, p, isVertical)) {
                if (node.left != null)
                    np = nn(node.left, np, p, !isVertical);
            }
        }
        return np;
    }

    public           boolean isEmpty() {
        // is the set empty? 
        return (root == null);
    }

    public               int size() {
        // number of points in the set 
        return size;
    }

    public              void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new NullPointerException();
        if (!contains(p)) {
            if (root == null) root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
            else insertLeftRight(p, root);
            size++;
        }
    }

    public           boolean contains(Point2D p) {
        // does the set contain point p? 
        if (p == null) throw new NullPointerException();
        if (root == null) return false;
        // Keep going down as if inserting the node. return false if null 
        return containsLeftRight(p, root);
    }

    public              void draw() {
        // draw all points to standard draw 
        draw(root, true);
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle 
        Queue<Point2D> pointsInRange = new Queue<Point2D>();
        addToRange(root, rect, pointsInRange);
        return pointsInRange;
    }

    public           Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (isEmpty()) return null;
        if (p == null) throw new NullPointerException();
        // double minDist = Math.sqrt(2);
        Point2D nearest = nn(root, root.point, p, true);
        return nearest;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            Point2D newPoint = new Point2D(in.readFloat(), in.readFloat());
            kdtree.insert(newPoint);
        }
        // kdtree.draw();
        StdOut.println(kdtree.nearest(new Point2D(.81, .30)).toString());
        // // unit testing of the methods (optional) 
        // Point2D p1 = new Point2D(0.7, 0.2);
        // Point2D p2 = new Point2D(0.5, 0.4);
        // Point2D p3 = new Point2D(0.2, 0.3);
        // Point2D p4 = new Point2D(0.4, 0.7);
        // KdTree kdtree = new KdTree();
        // StdOut.println("Empty: " + kdtree.isEmpty());
        // kdtree.insert(p1);
        // StdOut.println("size: " + kdtree.size());
        // // kdtree.draw();
        // kdtree.insert(p2);
        // StdOut.println("size: " + kdtree.size());
        // StdOut.println("contains? " + kdtree.contains(p3));
        // kdtree.insert(p3);
        // StdOut.println("size: " + kdtree.size());
        // kdtree.insert(p4);
        // kdtree.draw();
        // RectHV rect = new RectHV(0.1, 0.1, 0.4, 0.7);
        // for (Point2D point : kdtree.range(rect)) StdOut.println(point.toString());
        // StdOut.println(kdtree.nearest(new Point2D(0.7, 0.1)).toString());
        // // Point2D p4 = new Point2D(0.1, 0.5);
        // // StdOut.println(pSet.nearest(p4).toString());
    }
}
