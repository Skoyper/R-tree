import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        RTree<Integer> tree = new RTree<>(2, 4);
        tree.insert(new float[]{1, 1, 3, 3}, 8);
        tree.insert(new float[]{2, 1, 5, 4}, 10);
        tree.insert(new float[]{100, 2, 102, 4}, 13);
        tree.insert(new float[]{22, 4, 27, 8}, 17);
        tree.delete(new float[]{22, 4, 27, 8}, 17);
        List<Integer> s = tree.search(new float[]{2, 2, 16, 3});
    }
}