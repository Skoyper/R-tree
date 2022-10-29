import java.util.*;

public class RTree<T> {

    private static class Rectangle {

        private final float[] minCoords;
        private final float[] maxCoords;

        public Rectangle(float minY, float minX, float maxY, float maxX) {
            minCoords = new float[]{minX, minY};
            maxCoords = new float[]{maxX, maxY};
        }

        public Rectangle(float[] coordinates) {
            minCoords = new float[]{coordinates[0], coordinates[1]};
            maxCoords = new float[]{coordinates[2], coordinates[3]};
        }

        public Rectangle(Rectangle rectangle) {
            minCoords = new float[]{rectangle.getMinX(), rectangle.getMinY()};
            maxCoords = new float[]{rectangle.getMaxX(), rectangle.getMaxY()};
        }

        public Rectangle() {
            minCoords = new float[DIEMENSION];
            maxCoords = new float[DIEMENSION];
        }

        public float calcArea() {
            return (this.getMaxX() - this.getMinX()) * (this.getMaxY() - this.getMinY());
        }

        public boolean isOverlap(Rectangle rect) {
            return (this.getMinX() <= rect.getMinX()
                    && this.getMaxX() >= rect.getMinX()
                    && this.getMinY() <= rect.getMinY()
                    && this.getMaxY() >= rect.getMinY())
                    || (this.getMinX() <= rect.getMaxX()
                    && this.getMaxX() >= rect.getMaxX()
                    && this.getMinY() <= rect.getMaxY()
                    && this.getMaxY() >= rect.getMaxY());
        }

        public float getMaxX() {
            return maxCoords[0];
        }

        public float getMaxY() {
            return maxCoords[1];
        }

        public float getMinX() {
            return minCoords[0];
        }

        public void setAllCoords(Rectangle rectangle) {
            this.setMinX(rectangle.getMinX());
            this.setMaxX(rectangle.getMaxX());
            this.setMinY(rectangle.getMinY());
            this.setMaxY(rectangle.getMaxY());
        }
        public float getMinY() {
            return minCoords[1];
        }
        public void setMaxX(float maxX) {
            maxCoords[0] = maxX;
        }
        public void setMaxY(float maxY) {
            maxCoords[1] = maxY;
        }
        public void setMinX(float minX) {
            minCoords[0] = minX;
        }
        public void setMinY(float minY) {
            minCoords[1] = minY;
        }

        public boolean isRectInside(Rectangle rectangle) {
            return this.getMaxX() >= rectangle.getMaxX()
                    && this.getMinX() <= rectangle.getMinX()
                    && this.getMaxY() >= rectangle.getMaxY()
                    && this.getMinY() <= rectangle.getMinY();
        }

        @Override
        public boolean equals(Object o) {
            Rectangle rectangle = (Rectangle) o;
            return Arrays.equals(minCoords, rectangle.minCoords) && Arrays.equals(maxCoords, rectangle.maxCoords);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(minCoords);
            result = 31 * result + Arrays.hashCode(maxCoords);
            return result;
        }
    }

    private class RTreeNode<T> {

        //        private final float[] coordinates;
        private final Rectangle rectangle;
        private final LinkedList<RTreeNode<T>> childrens;
        private boolean asLeaf;
        private RTreeNode<T> parent;
        private T object;

        public RTreeNode(float[] coordinates, boolean asLeaf) {
            this.parent = null;
            this.rectangle = new Rectangle(coordinates);
//            this.coordinates = coordinates;
            this.childrens = new LinkedList<>();
            this.object = null;
            this.asLeaf = asLeaf;
        }

        private RTreeNode(Rectangle rect, boolean asLeaf) {
            this.parent = null;
            this.rectangle = rect;
//            this.coordinates = coordinates;
            this.childrens = new LinkedList<>();
            this.object = null;
            this.asLeaf = asLeaf;
        }

        public RTreeNode(float[] coordinates, T object, RTreeNode<T> parent) {
            this.parent = parent;
            this.rectangle = new Rectangle(coordinates);
//            this.coordinates = coordinates;
            this.childrens = new LinkedList<>();
            this.asLeaf = true;
            this.object = object;
        }

        @Override
        public boolean equals(Object o) {
            RTreeNode<?> rTreeNode = (RTreeNode<?>) o;
            return asLeaf == rTreeNode.asLeaf
                    && rectangle.equals(rTreeNode.rectangle)
                    && object.equals(rTreeNode.object);
        }

        @Override
        public int hashCode() {
            return Objects.hash(rectangle, childrens, asLeaf, parent, object);
        }

    }

    private final int maxEntries;
    private final int minEntries;
    private RTreeNode<T> root;
    private final static int DIEMENSION = 2;


    private RTreeNode<T> buildRoot(boolean asLeaf) {
        return new RTreeNode<T>(new float[]{Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE}, asLeaf);
    }

    public RTree(int minEntries, int maxEntries) throws Exception {
        if (maxEntries < 4) {
            throw new Exception("Max entries smaller than 4");
        }
        if (minEntries > maxEntries / 2) {
            throw new Exception("Min entries bigger than half of max entries");
        }
        this.minEntries = minEntries;
        this.maxEntries = maxEntries;
        this.root = buildRoot(true);
    }

    public void insert(float[] coordinates, T object) throws Exception {
        if (coordinates.length / 2 > DIEMENSION) {
            throw new Exception("Wrong coordinates for 2D");
        }
        RTreeNode<T> entry = new RTreeNode<T>(coordinates, object, null);
        RTreeNode<T> choosenLeaf = chooseLeaf(root, entry);
        choosenLeaf.childrens.add(entry);
        entry.parent = choosenLeaf;
        if (choosenLeaf.childrens.size() > maxEntries) {
            RTreeNode<T>[] splits = splitNode(choosenLeaf);
            adjustTree(splits[0], splits[1]);
        } else {
            adjustTree(choosenLeaf, null);
        }
    }

    private void adjustTree(RTreeNode<T> firstSplit, RTreeNode<T> secondSplit) {
        if (firstSplit == root) {
            if (secondSplit != null) {
                this.root = buildRoot(false);
                root.childrens.add(firstSplit);
                root.childrens.add(secondSplit);
                firstSplit.parent = root;
                secondSplit.parent = root;
            }
            tighten(root);
            return;
        }
        tighten(firstSplit);
        if (secondSplit != null) {
            tighten(secondSplit);
            if (firstSplit.parent.childrens.size() > maxEntries) {
                RTreeNode<T>[] splits = splitNode(firstSplit.parent);
                adjustTree(splits[0], splits[1]);
            }
        }
        if (firstSplit.parent != null) {
            adjustTree(firstSplit.parent, null);
        }
    }

    private RTreeNode<T>[] splitNode(RTreeNode<T> node) {
        RTreeNode<T>[] newNodes = new RTreeNode[]{node, new RTreeNode<T>(new Rectangle(node.rectangle), node.asLeaf)};
        newNodes[1].parent = node.parent;
        if (newNodes[1].parent != null) {
            newNodes[1].parent.childrens.add(newNodes[1]);
        }
        LinkedList<RTreeNode<T>> currChildrens = new LinkedList<>(node.childrens);
        node.childrens.clear();
        RTreeNode<T>[] maxLeftMaxRight = linearSplit(currChildrens);
        newNodes[0].childrens.add(maxLeftMaxRight[0]);
        newNodes[0].rectangle.setAllCoords(maxLeftMaxRight[0].rectangle);
        newNodes[1].childrens.add(maxLeftMaxRight[1]);
        newNodes[1].rectangle.setAllCoords(maxLeftMaxRight[1].rectangle);
        while (!currChildrens.isEmpty()) {
            if ((newNodes[0].childrens.size() >= minEntries)
                && (newNodes[1].childrens.size() + currChildrens.size() == minEntries)) {
                newNodes[1].childrens.addAll(currChildrens);
                currChildrens.clear();
            } else if ((newNodes[1].childrens.size() >= minEntries)
                    && (newNodes[0].childrens.size() + currChildrens.size() == minEntries)) {
                newNodes[0].childrens.addAll(currChildrens);
                currChildrens.clear();
            }
            if (currChildrens.isEmpty()) {
                tighten(newNodes);
                return newNodes;
            }
            RTreeNode<T> children = currChildrens.pop();
            float firstIncreasedArea = calcIncreasedArea(newNodes[0], children);
            float secondIncreasedArea = calcIncreasedArea(newNodes[1], children);
            RTreeNode<T> prefNode;
            if (firstIncreasedArea < secondIncreasedArea) {
                prefNode = newNodes[0];
            } else if (firstIncreasedArea > secondIncreasedArea) {
                prefNode = newNodes[1];
            } else {
                float firstArea = newNodes[0].rectangle.calcArea();
                float secondArea = newNodes[1].rectangle.calcArea();
                if (firstArea < secondArea) {
                    prefNode = newNodes[0];
                } else if (firstArea > secondArea) {
                    prefNode = newNodes[1];
                } else {
                    prefNode = newNodes[(int) (Math.random() * 2)];
                }
            }
            prefNode.childrens.add(children);
            tighten(prefNode);
        }
        return newNodes;
    }

    private RTreeNode<T>[] linearSplit(LinkedList<RTreeNode<T>> nodes) {
        RTreeNode<T>[] maxLeftMaxRight = new RTreeNode[2];
        boolean foundLeftRight = false;
        float minLow = Float.MAX_VALUE;
        float maxLow = -Float.MAX_VALUE;
        float minHigh = Float.MAX_VALUE;
        float maxHigh = -Float.MAX_VALUE;
        RTreeNode<T> nodeMaxLower = null;
        RTreeNode<T> nodeMinUpper = null;
        for (RTreeNode<T> n : nodes) {
            minLow = Math.min(minLow, n.rectangle.getMinX());
            maxHigh = Math.max(maxHigh, n.rectangle.getMaxX());
            if (n.rectangle.getMinX() > maxLow) {
                maxLow = n.rectangle.getMinX();
                nodeMaxLower = n;
            }
            if (n.rectangle.getMaxX() < minHigh) {
                minHigh = n.rectangle.getMaxX();
                nodeMinUpper = n;
            }
        }
        float normalize = Math.abs(minHigh - maxLow) / (maxHigh - minLow);
        if (normalize >= 0.0f) {
            maxLeftMaxRight[0] = nodeMinUpper;
            maxLeftMaxRight[1] = nodeMaxLower;
        }
        nodes.remove(nodeMaxLower);
        nodes.remove(nodeMinUpper);
        return maxLeftMaxRight;
    }

    private void tighten(RTreeNode<T>... nodes) {
        for (RTreeNode<T> node : nodes) {
            float maxX = -Float.MAX_VALUE;
            float minX = Float.MAX_VALUE;
            float maxY = -Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            for (RTreeNode<T> curr : node.childrens) {
                curr.parent = node;
                maxX = Float.max(maxX, curr.rectangle.getMaxX());
                maxY = Float.max(maxY, curr.rectangle.getMaxY());
                minX = Float.min(minX, curr.rectangle.getMinX());
                minY = Float.min(minY, curr.rectangle.getMinY());
            }
            node.rectangle.setMinX(minX);
            node.rectangle.setMaxX(maxX);
            node.rectangle.setMinY(minY);
            node.rectangle.setMaxY(maxY);
        }
    }

    private RTreeNode<T> chooseLeaf(RTreeNode<T> node, RTreeNode<T> entry) {
        if (node.asLeaf) {
            return node;
        }
        float minIncrease = Float.MAX_VALUE;
        RTreeNode<T> next = null;
        for (RTreeNode<T> child : node.childrens) {
            float increase = calcIncreasedArea(child, entry);
            if (increase < minIncrease) {
                minIncrease = increase;
                next = child;
            } else if (increase == minIncrease) {
                if (child.rectangle.calcArea() < next.rectangle.calcArea()) {
                    next = child;
                }
            }
        }
        return chooseLeaf(next, entry);
    }

    private float calcIncreasedArea(RTreeNode<T> node, RTreeNode<T> entry) {
        float lenght = Math.max(node.rectangle.getMaxX(), entry.rectangle.getMaxX())
                - Math.min(node.rectangle.getMinX(), entry.rectangle.getMinX());
        float height = Math.max(node.rectangle.getMaxY(), entry.rectangle.getMaxY())
                - Math.min(node.rectangle.getMinY(), entry.rectangle.getMinY());
        return lenght * height - node.rectangle.calcArea();
    }

    public boolean delete(float[] coordinate, T object) throws Exception {
        RTreeNode<T> leaf = findLeaf(root, new Rectangle(coordinate), object);
        if (leaf == null) {
            throw new RuntimeException("wrong object to delete");
        }
        RTreeNode<T> delNode = new RTreeNode<>(coordinate, object, leaf.parent);
        for (RTreeNode<T> child : leaf.parent.childrens) {
            if (child.equals(delNode)) {
                leaf.parent.childrens.remove(child);
                break;
            }
        }
        condenseTree(leaf.parent);
        return true;
    }

    private RTreeNode<T> findLeaf(RTreeNode<T> node, Rectangle rect, T object) {
        if (node.asLeaf) {
            for (RTreeNode<T> child : node.childrens) {
                if (child.object.equals(object)) {
                    return child;
                }
            }
        } else {
            for (RTreeNode<T> child : node.childrens) {
                if (child.rectangle.isOverlap(rect)) {
                    RTreeNode<T> leaf = findLeaf(child, rect, object);
                    if (leaf != null) {
                        return leaf;
                    }
                }
            }
        }
        return null;
    }

    private void condenseTree(RTreeNode<T> childrens) throws Exception {
        RTreeNode<T> node = childrens;
        Set<RTreeNode<T>> q = new HashSet<>();
        while (node != root) {
            if (node.asLeaf && (node.childrens.size() < minEntries)) {
                q.addAll(node.childrens);
                node.parent.childrens.remove(node);
            } else if (!node.asLeaf && (node.childrens.size() < minEntries)) {
                LinkedList<RTreeNode<T>> toVisit = new LinkedList<>(node.childrens);
                while (!toVisit.isEmpty()) {
                    RTreeNode<T> child = toVisit.pop();
                    if (child.asLeaf) {
                        q.addAll(child.childrens);
                    } else {
                        toVisit.addAll(child.childrens);
                    }
                }
                node.parent.childrens.remove(node);
            } else {
                tighten(node);
            }
            node = node.parent;
        }
        if (root.childrens.size() == 0) {
            root = buildRoot(true);
        } else if ((root.childrens.size() == 1) && (!root.asLeaf)) {
            root = root.childrens.get(0);
            root.parent = null;
        } else {
            tighten(root);
        }
        for (RTreeNode<T> ne : q) {
            insert(new float[]{ne.rectangle.getMinX(), ne.rectangle.getMinY(),
                    ne.rectangle.getMaxX(), ne.rectangle.getMaxY()}, ne.object);
        }
    }

    private void search(Rectangle rect, RTreeNode<T> currentRoot, ArrayList<T> results) {
        if (currentRoot.asLeaf) {
            for (RTreeNode<T> entry : currentRoot.childrens) {
                if (entry.rectangle.isOverlap(rect) || rect.isOverlap(entry.rectangle)) {
                    results.add(entry.object);
                }
            }
        } else {
            for (RTreeNode<T> entry : currentRoot.childrens) {
                if (entry.rectangle.isOverlap(rect) || rect.isOverlap(entry.rectangle)) {
                    search(rect, entry, results);
                }
            }
        }
    }

    public List<T> search(float[] coordinates) {
        ArrayList<T> result = new ArrayList<>();
        Rectangle rect = new Rectangle(coordinates);
        search(rect, root, result);
        return result;
    }
}
