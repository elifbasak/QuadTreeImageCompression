

import java.util.ArrayList;

public class Node {

    public static final int NORTH_WEST = 0;
    public static final int NORTH_EAST = 1;
    public static final int SOUTH_WEST = 2;
    public static final int SOUTH_EAST = 3;

    public Color color;
    public int height;
    public int width;
    public int x;
    public int y;// origin (x,y)
    public Node parent;
    public ArrayList<Node> children;

    public Node() {
        children = new ArrayList(4);

    }

    public Node(Color color, int x, int y, int height, int width) {
        super();
        this.color = color;
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        this.children = new ArrayList(4);
    }

    public Node(int x, int y, int height, int width) {
        super();
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        this.children = new ArrayList(4);
        for (int i = 0; i < 4; i++) {
            children.add(null);
        }
    }

    public Color getAvarageChildren() {
        int r = 0;
        int g = 0;
        int b = 0;

        r = r + children.get(NORTH_WEST).color.r;
        g = g + children.get(NORTH_WEST).color.g;
        b = b + children.get(NORTH_WEST).color.b;

        r = r + children.get(NORTH_EAST).color.r;
        g = g + children.get(NORTH_EAST).color.g;
        b = b + children.get(NORTH_EAST).color.b;

        r = r + children.get(SOUTH_WEST).color.r;
        g = g + children.get(SOUTH_WEST).color.g;
        b = b + children.get(SOUTH_WEST).color.b;

        r = r + children.get(SOUTH_EAST).color.r;
        g = g + children.get(SOUTH_EAST).color.g;
        b = b + children.get(SOUTH_EAST).color.b;

        r = r / 4;
        g = g / 4;
        b = b / 4;
        return new Color(r, g, b);
    }

    public void setChildren(int child, Node node) {

        this.children.set(child, node);

    }

    public boolean isLeaf() {

        return (children.get(0) == null);

    }

}
