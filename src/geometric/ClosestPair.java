package geometric;

import java.util.*;
import java.lang.Math;

class Point {
    public long x;
    public long y;
    public Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("<%d,%d>", this.x, this.y);
    }
}

class ByX implements Comparator<Point> {
    public int compare(Point a, Point b) {return (int)a.x - (int)b.x;}
}

class ByY implements Comparator<Point> {
    public int compare(Point a, Point b) {return (int)a.y - (int)b.y;}
}


public class ClosestPair {

    private static double dist(Point a, Point b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }


    public static double closestPair(Point[] sx, Point[] sy) {

        double d = 2000000000;

        if (sx.length <= 3) {
            for (int i = 0; i < sx.length; i++)
                for (int j = i + 1; j < sx.length; j++)
                    d = Math.min(d, dist(sx[i], sx[j]));
        } else {

            // split point: len/2 (belongs to the rigtht)

            int split = sx.length / 2;
            while ((split + 1) < sx.length && sx[split].x == sx[split + 1].x) split++;

            // make sure the split point has a different x axis from the previous one.

            if ((split + 1) == sx.length) {
                while ((split - 1) > 0 && sx[split].x == sx[split - 1].x) split--;
                // all points have the same x axis.
                if ((split - 1) == 0) {
                    for(int i = 0; i < sy.length; i++) {
                        if ((i + 1) == sy.length) return d;
                        else
                            d = Math.min(d, sy[i+1].y - sy[i].y);
                    }
                }
            } else split++;



            Point[] newLeftSx = Arrays.copyOf(sx, split);
            Point[] newRightSx = Arrays.copyOfRange(sx, split, sx.length);

            Point[] newLeftSy = new Point[newLeftSx.length];
            Point[] newRightSy = new Point[newRightSx.length];

            int lidx = 0;
            int ridx = 0;
            for (int i = 0; i < sy.length; i++)
                // TODO: handle the case where two points have the same x axis?
                if (sy[i].x < sx[split].x)
                    newLeftSy[lidx++] = sy[i];
                else
                    newRightSy[ridx++] = sy[i];

            double dLeft = closestPair(newLeftSx, newLeftSy);
            double dRight = closestPair(newRightSx, newRightSy);

            d = Math.min(dLeft, dRight);

            double leftBoundary = sx[sx.length/2].x - d;
            double rightBoundary = sx[sx.length/2].x + d;

            Point[] boundary = new Point[sx.length];

            int bidx = 0;
            for (int i = 0; i < sy.length; i++)
                if (sy[i].x <= rightBoundary && sy[i].x >= leftBoundary)
                    boundary[bidx++] = sy[i];

            for (int i = 0; i < boundary.length; i++) {
                if (boundary[i] == null) break;
                else {
                    for (int j = 1; j <= 12 && (i+j) < boundary.length; j++)
                        if (boundary[i + j] == null) break;
                        else
                            d = Math.min(d, dist(boundary[i], boundary[i+j]));
                }

            }

        }
        return d;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numPoints = scanner.nextInt();

        Point[] points = new Point[numPoints];

        for (int i = 0; i < numPoints; i++) {
            points[i] = new Point(scanner.nextLong(), scanner.nextLong());
        }

        Point[] sx = Arrays.copyOf(points, points.length);
        Point[] sy = Arrays.copyOf(points, points.length);

        Arrays.sort(sx, new ByX());
        Arrays.sort(sy, new ByY());

        System.out.println(String.format("%.3f", closestPair(sx, sy)));
    }
}