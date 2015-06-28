import java.util.Arrays;

public class Brute {
    
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
        // Read in all the points.
        for (int i = 0; i < nPoints; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
        }
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (int i = 0; i < points.length; i++) {
            points[i].draw();
        }
        
        for (int i = 0; i < nPoints - 3; i++)
            for (int j = i + 1; j < nPoints - 2; j++)
                for (int k = j + 1; k < nPoints - 1; k++)
                    for (int l = k + 1; l < nPoints; l++) {
                        if ((points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]))
                            && (points[i].slopeTo(points[j]) == points[i].slopeTo(points[l]))){
                            Point[] collinearPoints = {points[i], points[j], points[k], points[l]};
                            Arrays.sort(collinearPoints);
                            print_points(collinearPoints);
                            // draw_points(collinearPoints);                          
                            collinearPoints[0].drawTo(collinearPoints[collinearPoints.length-1]);
                        }
                    }

    }

}
