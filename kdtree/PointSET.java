public class PointSET {
    private SET<Point2D> pointSet;

    public         PointSET() {
        // construct an empty set of points 
        pointSet = new SET<Point2D>();
    }

    public           boolean isEmpty() {
        // is the set empty? 
        return pointSet.isEmpty();
    }

    public               int size() {
        // number of points in the set 
        return pointSet.size();
    }

    public              void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new NullPointerException();
        else pointSet.add(p);
    }

    public           boolean contains(Point2D p) {
            // does the set contain point p? 
        if (p == null) throw new NullPointerException();
        return pointSet.contains(p);
    }

    public              void draw() {
        // draw all points to standard draw 
        for (Point2D point : pointSet)
            point.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle 
        Queue<Point2D> pointsInRange = new Queue<Point2D>();
        for (Point2D point : pointSet) {
            if (rect.contains(point)) pointsInRange.enqueue(point);
        }
        return pointsInRange;
    }

    public           Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (isEmpty()) return null;
        if (p == null) throw new NullPointerException();
        double minDist = Math.sqrt(2);
        Point2D nearest = null;
        for (Point2D point : pointSet) {
            if (point.distanceTo(p) < minDist){
                minDist = point.distanceTo(p);
                nearest = point;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional) 
        Point2D p1 = new Point2D(0.6, 0.5);
        Point2D p2 = new Point2D(0.5, 0.5);
        Point2D p3 = new Point2D(0.2, 0.5);
        PointSET pSet = new PointSET();
        pSet.insert(p1);
        pSet.insert(p2);
        pSet.insert(p3);
        // pSet.draw();
        Point2D p4 = new Point2D(0.1, 0.5);
        StdOut.println(pSet.nearest(p4).toString());
    }
}
