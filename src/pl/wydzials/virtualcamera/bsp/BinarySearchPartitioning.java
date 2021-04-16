package pl.wydzials.virtualcamera.bsp;

import pl.wydzials.virtualcamera.model.Face;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BinarySearchPartitioning {
    private final static Random random = new Random();

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
}
