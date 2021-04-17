package pl.wydzials.virtualcamera.bsp;

import pl.wydzials.virtualcamera.Obscure;
import pl.wydzials.virtualcamera.model.Face;
import pl.wydzials.virtualcamera.model.Point;

import java.util.*;

public class BinarySearchPartitioning {
    private final static Random random = new Random();
    private final static Point relativePoint = new Point(0,0,0);

    public static TreeNode<Face> buildTree(Set<Face> faces) {
        TreeNode<Face> root = new TreeNode<>();
        build(root, new HashSet<>(faces));
        return root;
    }

    // builds random test tree
    private static void build(TreeNode<Face> tree, Set<Face> faces) {
        Face key = faces.iterator().next();
        tree.key = key;
        faces.remove(key);

        if (faces.isEmpty()) {
            return;
        }

        Set<Face> left = new HashSet<>();
        Set<Face> right = new HashSet<>();

        for (Face face : faces) {
            if (random.nextBoolean()) {
                left.add(face);
            } else {
                right.add(face);
            }
        }

        if (!left.isEmpty()) {
            tree.left = new TreeNode<>();
            build(tree.left, left);
        }
        if (!right.isEmpty()) {
            tree.right = new TreeNode<>();
            build(tree.right, right);
        }
    }

    private static void createTree(TreeNode<Face> tree, Set<Face> faces) {
        Face key = faces.iterator().next();
        tree.key = key;
        faces.remove(key);

        List<Point> facePoints = Arrays.asList(key.getPoints());
        double[] planeIndexes = Obscure.calcThePlane(facePoints);

        Set<Face> left = new HashSet<>(); //Po lewej znajdują się pkt po przeciwnej stronie do punktu relatywnego, a po prawej pkt po tej samej stronie
        Set<Face> right = new HashSet<>();
        for (Face face : faces) {
            HashMap<Integer, Point> pointsSides = new HashMap<>();
            int numOfZeros = 0;
            for(Point singlePoint: face.getPoints()) {
                int side = Obscure.areTwoPointsSameSide(relativePoint, singlePoint, planeIndexes);
                if(side != 0)
                    pointsSides.put(side, singlePoint);
                else
                    numOfZeros++;
            }
            if (pointsSides.size() == 1) {
                if (pointsSides.keySet().iterator().next() == -1) {
                    left.add(face);
                } else {
                    right.add(face);
                }
            }
            else if(numOfZeros == 4){//Dla ścian o takich samych płaszczyznach zakładam że są przed ścianą???
                right.add(face);
            }else{
                Point[] firstSidePoint = new Point[2];
                Point[] secSidePoint = new Point[2];
                int firstCount=0;
                int secCount=0;
                for(Point singlePoint: face.getPoints()) {
                    int side = Obscure.areTwoPointsSameSide(relativePoint, singlePoint, planeIndexes);
                    if (side == 1)
                        firstSidePoint[firstCount++] = singlePoint; //Na przybliżeniu się wywala z powodu relatywnych wielkości
                    else
                        secSidePoint[secCount++] = singlePoint;
                }
                Face[] dividedFace = Obscure.divideFace(firstSidePoint, secSidePoint, planeIndexes);
                right.add(dividedFace[0]);
                left.add(dividedFace[1]);
            }
        }

        if (!left.isEmpty()) {
            tree.left = new TreeNode<>();
            createTree(tree.left, left);
        }
        if (!right.isEmpty()) {
            tree.right = new TreeNode<>();
            createTree(tree.right, right);
        }
    }

}
