package pl.wydzials.virtualcamera.bsp;

public class TreeNode<T> {

    public T key;
    public TreeNode<T> left;
    public TreeNode<T> right;

    public TreeNode() {
    }

    public TreeNode(T key) {
        this.key = key;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "");
        return buffer.toString();
    }

    // modified from source: https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(key);
        buffer.append('\n');

        if (left != null) {
            if (right != null) {
                left.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                left.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
        if (right != null) {
            right.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
        }
    }
}
