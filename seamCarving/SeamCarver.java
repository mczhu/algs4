// Already a DAG in topological order; simply iterate through in topological order
import java.util.Arrays;
import java.awt.Color;

public class SeamCarver {
    // private Picture picture;
    private double[][] energy;
    
    private int height;
    private int width;
    private int[][] color;

    public SeamCarver(Picture picture) {
        // create a seam carver object based on the given picture
        height = picture.height();
        width = picture.width();

        energy = new double[height][width];
        
        color = new int[height][width];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                color[y][x] = picture.get(x, y).getRGB();
            }

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) 
                energy[y][x] = energy(x, y);
    }

    public Picture picture() {
        // current picture
        Picture picture = new Picture(width, height);
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                picture.set(x, y, new Color(color[y][x]));
            }
        
        return picture;
    }

    public     int width() {
        // width of current picture
        return width;
    }

    public     int height() {
        // height of current picture
        return height;
    }

    public  double energy(int x, int y) {
        // energy of pixel at column x and row y
        if (x < 0 || y < 0 || x > width-1 || y > height-1) throw new IndexOutOfBoundsException();
        if (x == 0 || y == 0 || x == width-1 || y == height-1)
            return 195075;
        Color colorRight = new Color(color[y][x+1]);
        Color colorLeft = new Color(color[y][x-1]);
        Color colorDown = new Color(color[y+1][x]);
        Color colorUp = new Color(color[y-1][x]);

        return (colorRight.getRed() - colorLeft.getRed()) * (colorRight.getRed() - colorLeft.getRed()) +
            (colorRight.getGreen() - colorLeft.getGreen()) * (colorRight.getGreen() - colorLeft.getGreen()) +
            (colorRight.getBlue() - colorLeft.getBlue()) * (colorRight.getBlue() - colorLeft.getBlue()) +
            (colorDown.getRed() - colorUp.getRed()) * (colorDown.getRed() - colorUp.getRed()) +
            (colorDown.getGreen() - colorUp.getGreen()) * (colorDown.getGreen() - colorUp.getGreen()) +
            (colorDown.getBlue() - colorUp.getBlue()) * (colorDown.getBlue() - colorUp.getBlue());
    }

    public   int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam
        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        for (int y = 0; y < height; y++) {
            distTo[y][0] = 0;
            for (int x = 1; x < width; x++) {
                distTo[y][x] = Double.POSITIVE_INFINITY;
            }
        }

        for (int x = 1; x < (width - 1); x++)
            for (int y = 1; y < (height - 1); y++) {
                distTo[y][x] = distTo[y-1][x-1] + energy[y][x];
                edgeTo[y][x] = y - 1;
                if (distTo[y][x-1] + energy[y][x] < distTo[y][x]) {
                    distTo[y][x] = distTo[y][x-1] + energy[y][x];
                    edgeTo[y][x] = y;
                }
                if (distTo[y+1][x-1] + energy[y][x] < distTo[y][x]) {
                    distTo[y][x] = distTo[y+1][x-1] + energy[y][x];
                    edgeTo[y][x] = y + 1;
                }
            }

        // Find the min total energy
        int minYIdx = 1;
        double minY = Double.POSITIVE_INFINITY;
        for (int y = 1; y < (height - 1); y++) {
            // System.out.println(distTo[y][0]);
            // System.out.println(energy[y][1]);
            // System.out.println(energy(1, y));
            if (distTo[y][width - 2] < minY) {
                minYIdx = y;
                minY = distTo[y][width - 2];
            }
        }
        // System.out.println(minYIdx);

        int[] horizontalSeam = new int[width];
        horizontalSeam[width - 1] = minYIdx - 1;
        horizontalSeam[width - 2] = minYIdx;

        for (int x = width - 3; x >= 0; x--) {
            horizontalSeam[x] = edgeTo[horizontalSeam[x+1]][x+1];
        }

        return horizontalSeam;

    }

    public   int[] findVerticalSeam() {
        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        for (int x = 0; x < width; x++) {
            distTo[0][x] = 0;
            // energy[0][x] = energy(x, 0);
            for (int y = 1; y < height; y++) {
                distTo[y][x] = Double.POSITIVE_INFINITY;
                // energy[y][x] = energy(x, y);
            }
        }

        // sequence of indices for vertical seam
        for (int y = 1; y < (height - 1); y++)
            for (int x = 1; x < (width - 1); x++) {
                distTo[y][x] = distTo[y-1][x-1] + energy[y][x];
                edgeTo[y][x] = x - 1;
                if (distTo[y-1][x] + energy[y][x] < distTo[y][x]) {
                    distTo[y][x] = distTo[y-1][x] + energy[y][x];
                    edgeTo[y][x] = x;
                }
                if (distTo[y-1][x+1] + energy[y][x] < distTo[y][x]) {
                    distTo[y][x] = distTo[y-1][x+1] + energy[y][x];
                    edgeTo[y][x] = x + 1;
                }
            }

        // Find the min total energy
        int minXIdx = 1;
        double minX = Double.POSITIVE_INFINITY;
        for (int x = 1; x < (width - 1); x++) {
            if (distTo[height - 2][x] < minX) {
                minXIdx = x;
                minX = distTo[height - 2][x];
            }
        }

        int[] verticalSeam = new int[height];
        verticalSeam[height - 1] = minXIdx - 1;
        verticalSeam[height - 2] = minXIdx;

        for (int y = height - 3; y >= 0; y--) {
            verticalSeam[y] = edgeTo[y+1][verticalSeam[y+1]];            
        }

        return verticalSeam;
    }

    public    void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from current picture

        if (seam == null) throw new NullPointerException();
        
        if (height <= 1) throw new IllegalArgumentException();

        if (seam.length != width)
            throw new IllegalArgumentException();

        int[][] newColor = new  int[height - 1][width];

        for (int x = 0; x < width; x++) {
            if (seam[x] < 0 || seam[x] > height-1)
                throw new IllegalArgumentException();
        }

        for (int x = 0; x < width - 1; x++) {
            if ((seam[x] - seam[x+1]) != 1 && (seam[x] - seam[x+1]) != 0 && (seam[x] - seam[x+1]) != -1) {
                throw new IllegalArgumentException();
            }
        }
        

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height - 1; y++) 
                if (y < seam[x]) {
                    newColor[y][x] = color[y][x];
                }
                else {
                    newColor[y][x] = color[y+1][x];
                    energy[y][x] = energy[y+1][x];
                }
            }
        color = newColor;
        height = height - 1;

        for (int x = 0; x < width; x++) {
            //  only update the energy around the seam
            if (seam[x] > 0)
                energy[seam[x]-1][x] = energy(x, seam[x]-1);
            if (seam[x] < height)
                energy[seam[x]][x] = energy(x, seam[x]);
        }
   }

    public    void removeVerticalSeam(int[] seam) {

        // remove vertical seam from current picture
        if (seam == null) throw new NullPointerException();

        if (width <= 1) throw new IllegalArgumentException();

        if (seam.length != height) throw new IllegalArgumentException();

        for (int y = 0; y < height; y++) {
            if (seam[y] < 0 || seam[y] > width-1)
                throw new IllegalArgumentException();
        }

        for (int y = 0; y < height - 1; y++) {
            if ((seam[y] - seam[y+1]) != 1 && (seam[y] - seam[y+1]) != 0 && (seam[y] - seam[y+1]) != -1)
                throw new IllegalArgumentException();
        }


        int[][] newColor = new int[height][width - 1];

        for (int y = 0; y < height; y++) 
            for (int x = 0; x < width - 1; x++) {
                if (x < seam[y]) {
                    newColor[y][x] = color[y][x];
                }
                else {
                    newColor[y][x] = color[y][x+1];
                    energy[y][x] = energy[y][x+1];
                }
            }
        color = newColor;
        width = width - 1;

        for (int y = 0; y < height; y++) {
            //  only update the energy around the seam
            if (seam[y] > 0)
                energy[y][seam[y]-1] = energy(seam[y]-1, y);
            if (seam[y] < width)
                energy[y][seam[y]] = energy(seam[y], y);
        }
    }

    public static void main(String args[]) {
        SeamCarver sc = new SeamCarver(new Picture(args[0]));
        // sc.picture().show();
        // System.out.println(sc.energy(0,2));
        // System.out.println(sc.width());
        // System.out.println(Arrays.toString(sc.findHorizontalSeam()));
        for (int i = 0; i < 250; i++)
            sc.removeVerticalSeam(sc.findVerticalSeam());
        for (int i = 0; i < 250; i++)
            sc.removeHorizontalSeam(sc.findHorizontalSeam());
        // for (int i = 0; i < 4; i++) {
        //     System.out.println(Arrays.toString(sc.findVerticalSeam()));
        //     sc.removeVerticalSeam(sc.findVerticalSeam());
        // }
        // System.out.println(sc.width());
        sc.picture().show();
        // int[] newSeam = {6,6,6};
        // sc.removeVerticalSeam(newSeam);
    }
}
