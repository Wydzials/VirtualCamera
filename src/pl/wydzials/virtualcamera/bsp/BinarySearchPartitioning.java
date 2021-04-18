package pl.wydzials.virtualcamera.bsp;

import pl.wydzials.virtualcamera.model.Face;
import pl.wydzials.virtualcamera.model.Point;

import java.util.*;

public class BinarySearchPartitioning {
    private final static Random random = new Random();
    private final static Point relativePoint = new Point(0, 0, 0);

    public static TreeNode<Face> buildTree(Set<Face> faces) {
        TreeNode<Face> root = new TreeNode<>();
        recursiveBuild(root, new HashSet<>(faces));
        return root;
    }

    private static void recursiveBuild(TreeNode<Face> tree, Set<Face> faces) {
        Face key = faces.iterator().next();
        tree.key = key;
        faces.remove(key);

        List<Point> facePoints = Arrays.asList(key.getPoints());
        double[] planeIndexes = Geometry.calcThePlane(facePoints);

        Set<Face> left = new HashSet<>(); //Po lewej znajdują się pkt po przeciwnej stronie do punktu relatywnego, a po prawej pkt po tej samej stronie
        Set<Face> right = new HashSet<>();
        for (Face face : faces) {
            int sameSide = Geometry.areFaceAndPointSameSide(face, relativePoint, planeIndexes);

            if (sameSide == 1) {
                right.add(face);
            } else if (sameSide == -1) {
                left.add(face);
            } else if (sameSide == 0) {
                right.add(face);
            } else {
                Point[] firstSidePoint = new Point[2];
                Point[] secSidePoint = new Point[2];
                int firstCount = 0;
                int secCount = 0;
                for (Point singlePoint : face.getPoints()) {
                    int side = Geometry.areTwoPointsSameSide(relativePoint, singlePoint, planeIndexes);
                    if (side == 1)
                        firstSidePoint[firstCount++] = singlePoint;
                    else
                        secSidePoint[secCount++] = singlePoint;
                }
                Face[] dividedFace = Geometry.divideFace(firstSidePoint, secSidePoint, planeIndexes);
                right.add(dividedFace[0]);
                left.add(dividedFace[1]);
            }
        }

        if (!left.isEmpty()) {
            tree.left = new TreeNode<>();
            recursiveBuild(tree.left, left);
        }
        if (!right.isEmpty()) {
            tree.right = new TreeNode<>();
            recursiveBuild(tree.right, right);
        }
    }
}
