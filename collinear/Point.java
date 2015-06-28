/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();       // YOUR DEFINITION HERE

    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point v, Point w) {
            if (slopeTo(v) < slopeTo(w)) return -1;
            if (slopeTo(v) > slopeTo(w)) return +1;
            return 0;
        }
    }

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y == that.y && this.x != that.x ) return 0.0/1.0; // positive zero
        if (this.x == that.x && this.y != that.y) return 1.0/0.0; // positive infinity
        if (this.x == that.x && this.y == that.y) return -1.0/0.0; // negative infinity
        return ((double)(this.y-that.y))/(this.x-that.x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y > that.y) return +1;
        if (this.y < that.y) return -1;
        if (this.y == that.y){
            if (this.x > that.x) return +1;
            if (this.x < that.x) return -1;            
        }
        return 0;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point p1 = new Point(1,1);
        Point p2 = new Point(1,2);
        assert p1.compareTo(p2) < 0;
        Point p3 = new Point(2,1);
        assert p1.compareTo(p3) < 0;
        double slope1 = p1.slopeTo(p2);
        assert slope1 == 1.0/0.0;
        double slope2 = p1.slopeTo(p3);
        assert slope2 == 0.0/1.0;
        double slope3 = p2.slopeTo(p3);
        assert slope3 == -1.0;
        // assert p1.compare() 
    }
}
