import java.util.Arrays;

public class Fast {

    private static void print_points(Point[] points) {
        StdOut.print(points[0].toString());
        for (int i = 1; i < points.length; i++) {
            StdOut.print(" -> ");
            StdOut.print(points[i].toString());
        }
        StdOut.print("\n");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int nPoints = in.readInt();
        // Assign memory based on the first line
        Point[] points = new Point[nPoints];
        Point[] auxPoints = new Point[nPoints];
        for (int i = 0; i < nPoints; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
            auxPoints[i] = points[i];
        }

        // Read in all the points.
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (int i = 0; i < points.length; i++) {
            points[i].draw();
        }

        Arrays.sort(points);

        for (int i = 0; i < nPoints - 3; i++) {
            Arrays.sort(auxPoints, points[i].SLOPE_ORDER);
            // print_points(auxPoints);

            // maintain two pointers, one the first collinear point, the other
            // the index of the last collinear point.
            int first = 1; // the 0th element is itself
            while (first < nPoints - 2) {
                int last = first + 1;
                boolean isRepeat = false;
                while (points[i].SLOPE_ORDER.compare(auxPoints[first], auxPoints[last]) == 0) {
                    if (points[i].compareTo(auxPoints[last]) == 1 || points[i].compareTo(auxPoints[first]) == 1) isRepeat = true;
                    if (++last == nPoints) break;
                }
                if ((last - first) > 2 && !isRepeat) {
                    Point[] collinearPoints = new Point[last-first+1];
                    collinearPoints[0] = points[i];
                    int counter = 1;
                    for (int cpIdx = first; cpIdx < last; cpIdx++) 
                        collinearPoints[counter++] = auxPoints[cpIdx];
                    Arrays.sort(collinearPoints);
                    print_points(collinearPoints);
                    collinearPoints[0].drawTo(collinearPoints[collinearPoints.length-1]);
                }
                first = last;
            }

        }
    }
}
