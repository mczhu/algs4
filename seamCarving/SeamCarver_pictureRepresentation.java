// Already a DAG in topological order; simply iterate through in topological order
import java.util.Arrays;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;

    public SeamCarver(Picture picture) {
        // create a seam carver object based on the given picture
        this.picture = new Picture(picture);
        // distTo = new double[picture.height()][picture.width()];
        // edgeTo = new int[picture.height()][picture.width()];
        energy = new double[picture.height()][picture.width()];

        for (int y = 0; y < (picture.height()); y++)
            for (int x = 0; x < (picture.width()); x++)
                energy[y][x] = energy(x, y);
    }

    public Picture picture() {
        // current picture
        return picture;
    }

    public     int width() {
        // width of current picture
        return picture.width();
    }

    public     int height() {
        // height of current picture
        return picture.height();
    }

    public  double energy(int x, int y) {
        // energy of pixel at column x and row y
        if (x < 0 || y < 0 || x > picture.width()-1 || y > picture.height()-1) throw new IndexOutOfBoundsException();
        if (x == 0 || y == 0 || x == picture.width()-1 || y == picture.height()-1)
            return 195075;
        return (picture.get(x+1, y).getRed() - picture.get(x-1, y).getRed()) * 
            (picture.get(x+1, y).getRed() - picture.get(x-1, y).getRed()) +
            (picture.get(x+1, y).getGreen() - picture.get(x-1, y).getGreen()) * 
            (picture.get(x+1, y).getGreen() - picture.get(x-1, y).getGreen()) +
            (picture.get(x+1, y).getBlue() - picture.get(x-1, y).getBlue()) * 
            (picture.get(x+1, y).getBlue() - picture.get(x-1, y).getBlue()) + 
            (picture.get(x, y+1).getRed() - picture.get(x, y-1).getRed()) * 
            (picture.get(x, y+1).getRed() - picture.get(x, y-1).getRed()) +
            (picture.get(x, y+1).getGreen() - picture.get(x, y-1).getGreen()) * 
            (picture.get(x, y+1).getGreen() - picture.get(x, y-1).getGreen()) +
            (picture.get(x, y+1).getBlue() - picture.get(x, y-1).getBlue()) * 
            (picture.get(x, y+1).getBlue() - picture.get(x, y-1).getBlue());
    }

    public   int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam
        double[][] distTo = new double[picture.height()][picture.width()];
        int[][] edgeTo = new int[picture.height()][picture.width()];

        for (int y = 0; y < picture.height(); y++) {
            distTo[y][0] = 0;
            for (int x = 1; x < picture.width(); x++) {
                distTo[y][x] = Double.POSITIVE_INFINITY;
            }
        }

        for (int x = 1; x < (picture.width() - 1); x++)
            for (int y = 1; y < (picture.height() - 1); y++) {
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
        for (int y = 1; y < (picture.height() - 1); y++) {
            if (distTo[y][picture.width() - 2] < minY) {
                minYIdx = y;
                minY = distTo[y][picture.width() - 2];
            }
        }

        int[] horizontalSeam = new int[picture.width()];
        horizontalSeam[picture.width() - 1] = minYIdx - 1;
        horizontalSeam[picture.width() - 2] = minYIdx;

        for (int x = picture.width() - 3; x >= 0; x--) {
            horizontalSeam[x] = edgeTo[horizontalSeam[x+1]][x+1];
        }

        return horizontalSeam;

    }

    public   int[] findVerticalSeam() {
        double[][] distTo = new double[picture.height()][picture.width()];
        int[][] edgeTo = new int[picture.height()][picture.width()];

        for (int x = 0; x < picture.width(); x++) {
            distTo[0][x] = 0;
            // energy[0][x] = energy(x, 0);
            for (int y = 1; y < picture.height(); y++) {
                distTo[y][x] = Double.POSITIVE_INFINITY;
                // energy[y][x] = energy(x, y);
            }
        }
         
        // System.out.println("true energy:");
        // for (int y = 0; y < (picture.height()); y++) {
        //     for (int x = 0; x < (picture.width()); x++) {
        //         System.out.print(String.format("%8d", energy(x,y)));
        //     }
        //     System.out.print("\n");
        // }

        // sequence of indices for vertical seam
        for (int y = 1; y < (picture.height() - 1); y++)
            for (int x = 1; x < (picture.width() - 1); x++) {
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
        for (int x = 1; x < (picture.width() - 1); x++) {
            if (distTo[picture.height() - 2][x] < minX) {
                minXIdx = x;
                minX = distTo[picture.height() - 2][x];
            }
        }

        int[] verticalSeam = new int[picture.height()];
        verticalSeam[picture.height() - 1] = minXIdx - 1;
        verticalSeam[picture.height() - 2] = minXIdx;

        for (int y = picture.height() - 3; y >= 0; y--) {
            verticalSeam[y] = edgeTo[y+1][verticalSeam[y+1]];            
        }

        return verticalSeam;
    }

    public    void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from current picture

        if (seam == null) throw new NullPointerException();
        
        if (picture.height() <= 1) throw new IllegalArgumentException();

        if (seam.length != picture.width()) throw new IllegalArgumentException();

        Picture newPic = new Picture(picture.width(), picture.height() - 1);
        
        for (int x = 0; x < picture.width(); x++) {
            if (seam[x] < 0 || seam[x] > picture.height()-1)
                throw new IllegalArgumentException();
        }

        for (int x = 0; x < picture.width() - 1; x++) {
            if ((seam[x] - seam[x+1]) != 1 && (seam[x] - seam[x+1]) != 0 && (seam[x] - seam[x+1]) != -1) {
                throw new IllegalArgumentException();
            }
        }
        

        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height() - 1; y++) 
                if (y < seam[x]) newPic.set(x, y, picture.get(x, y));
                else {
                    newPic.set(x, y, picture.get(x, y + 1));
                    energy[y][x] = energy[y+1][x];
                }
            }
        picture = newPic;

        for (int x = 0; x < picture.width(); x++) {
            //  only update the energy around the seam
            if (seam[x] > 0)
                energy[seam[x]-1][x] = energy(x, seam[x]-1);
            energy[seam[x]][x] = energy(x, seam[x]);
        }
   }

    public    void removeVerticalSeam(int[] seam) {
        // System.out.println("energy:");
        // for (int y = 0; y < (picture.height()); y++) {
        //     for (int x = 0; x < (picture.width()); x++) {
        //         System.out.print(String.format("%8d", energy[y][x]));
        //     }
        //     System.out.print("\n");
        // }


        // remove vertical seam from current picture
        if (seam == null) throw new NullPointerException();

        if (picture.width() <= 1) throw new IllegalArgumentException();

        if (seam.length != picture.height()) throw new IllegalArgumentException();

        for (int y = 0; y < picture.height(); y++) {
            if (seam[y] < 0 || seam[y] > picture.width()-1)
                throw new IllegalArgumentException();
        }

        for (int y = 0; y < picture.height() - 1; y++) {
            if ((seam[y] - seam[y+1]) != 1 && (seam[y] - seam[y+1]) != 0 && (seam[y] - seam[y+1]) != -1)
                throw new IllegalArgumentException();
        }

        Picture newPic = new Picture(picture.width() - 1, picture.height());

        for (int y = 0; y < picture.height(); y++) 
            for (int x = 0; x < picture.width() - 1; x++) {
                if (x < seam[y]) newPic.set(x, y, picture.get(x, y));
                else {
                    newPic.set(x, y, picture.get(x + 1, y));
                    energy[y][x] = energy[y][x+1];
                }
            }
        picture = newPic;

        for (int y = 0; y < picture.height(); y++) {
            //  only update the energy around the seam
            if (seam[y] > 0)
                energy[y][seam[y]-1] = energy(seam[y]-1, y);
            energy[y][seam[y]] = energy(seam[y], y);
        }
    }

    public static void main(String args[]) {
        SeamCarver sc = new SeamCarver(new Picture(args[0]));
        // sc.picture().show();
        // System.out.println(sc.energy(0,2));
        // System.out.println(sc.width());
        // System.out.println(Arrays.toString(sc.findHorizontalSeam()));
        // for (int i = 0; i < 50; i++)
        //     sc.removeVerticalSeam(sc.findVerticalSeam());
        for (int i = 0; i < 100; i++)
            sc.removeHorizontalSeam(sc.findHorizontalSeam());
        // for (int i = 0; i < 4; i++) {
        //     System.out.println(Arrays.toString(sc.findVerticalSeam()));
        //     sc.removeVerticalSeam(sc.findVerticalSeam());
        // }
        // System.out.println(sc.width());
        sc.picture().show();
        // TODO: debug; print picture and energy matrix after each iteration
        
    }
}
