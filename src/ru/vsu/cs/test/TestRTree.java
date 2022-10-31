import ru.vsu.cs.course1.tree.RTree;

public class TestRTree {

    @org.junit.Test
    public void testDelete1() throws Exception {
        RTree<Integer> tree;
        {
            try {
                tree = new RTree<>(2, 4);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        tree.insert(new float[]{2, 4, 4, 6}, 0);
        tree.insert(new float[]{3, 4, 4, 6}, 1);
        tree.insert(new float[]{6, 4, 8, 6}, 2);
        tree.insert(new float[]{6, 4, 7, 6}, 3);
        tree.insert(new float[]{9, 4, 10, 6}, 4);
        tree.delete(new float[]{9, 4, 10, 6}, 4);
        assert tree.search(new float[]{9, 4, 10, 6}).size() == 0;
    }

    @org.junit.Test
    public void testDelete2() throws Exception {
        RTree<Integer> tree;
        {
            try {
                tree = new RTree<>(2, 4);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        tree.insert(new float[]{1, 2, 2, 2}, 0);
        tree.insert(new float[]{1.5f, 2, 2, 2}, 1);
        tree.insert(new float[]{6, 4, 8, 6}, 2);
        tree.insert(new float[]{6, 4, 7, 6}, 3);
        tree.insert(new float[]{9, 4, 10, 6}, 4);
        tree.delete(new float[]{1, 2, 2, 2}, 0);
        tree.delete(new float[]{1.5f, 2, 2, 2}, 1);
        assert tree.search(new float[]{1, 2, 2, 2}).size() == 0;
    }

    @org.junit.Test
    public void testSearch1() throws Exception {
        RTree<Integer> tree;
        {
            try {
                tree = new RTree<>(2, 4);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        tree.insert(new float[]{1, 2, 2, 2}, 0);
        tree.insert(new float[]{1.5f, 2, 2, 2}, 1);
        tree.insert(new float[]{6, 4, 8, 6}, 2);
        tree.insert(new float[]{6, 4, 7, 6}, 3);
        tree.insert(new float[]{9, 4, 10, 6}, 4);
        assert tree.search(new float[]{1, 2, 2, 2}).size() == 2;
    }

    @org.junit.Test
    public void testSearch2() throws Exception {
        RTree<Integer> tree;
        {
            try {
                tree = new RTree<>(2, 4);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        tree.insert(new float[]{1, 2, 2, 2}, 0);
        tree.insert(new float[]{1.5f, 2, 2, 2}, 1);
        tree.insert(new float[]{6, 4, 8, 6}, 2);
        tree.insert(new float[]{6, 4, 7, 6}, 3);
        tree.insert(new float[]{9, 4, 10, 6}, 4);
        tree.delete(new float[]{1, 2, 2, 2}, 0);
        assert tree.search(new float[]{1, 2, 2, 2}).size() == 1;
    }

    @org.junit.Test
    public void testSearch3() throws Exception {
        RTree<Integer> tree;
        {
            try {
                tree = new RTree<>(2, 4);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        tree.insert(new float[]{1, 2, 2, 2}, 0);
        tree.insert(new float[]{1.5f, 2, 2, 2}, 1);
        tree.insert(new float[]{6, 4, 8, 6}, 2);
        tree.insert(new float[]{6, 4, 7, 6}, 3);
        tree.insert(new float[]{9, 4, 10, 6}, 4);
        tree.insert(new float[]{12, 7, 16, 6}, 4);
        tree.delete(new float[]{1, 2, 2, 2}, 0);
        assert tree.search(new float[]{9, 4, 12, 8}).size() == 2;
    }

    @org.junit.Test
    public void testSearch4() throws Exception {
        RTree<Integer> tree;
        {
            try {
                tree = new RTree<>(2, 4);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        tree.insert(new float[]{1, 2, 2, 2}, 0);
        tree.insert(new float[]{1.5f, 2, 2, 2}, 1);
        tree.insert(new float[]{6, 4, 8, 6}, 2);
        tree.insert(new float[]{6, 4, 7, 6}, 3);
        tree.insert(new float[]{9, 4, 10, 6}, 4);
        tree.insert(new float[]{12, 7, 16, 6}, 4);
        tree.insert(new float[]{12, 7, 16, 6}, 4);
        tree.delete(new float[]{1, 2, 2, 2}, 0);
        assert tree.search(new float[]{3, 2, 13, 8}).size() == 5;
    }

    @org.junit.Test
    public void testSearch5() throws Exception {
        RTree<Integer> tree;
        {
            try {
                tree = new RTree<>(2, 4);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        tree.insert(new float[]{1, 2, 2, 2}, 0);
        tree.insert(new float[]{1.5f, 2, 2, 2}, 1);
        tree.insert(new float[]{6, 4, 8, 6}, 2);
        tree.insert(new float[]{6, 4, 7, 6}, 3);
        tree.insert(new float[]{9, 4, 10, 6}, 4);
        tree.insert(new float[]{12, 7, 16, 6}, 4);
        tree.insert(new float[]{12, 7, 16, 6}, 4);
        tree.delete(new float[]{1, 2, 2, 2}, 0);
        assert tree.search(new float[]{2, 2, 13, 8}).size() == 6;
    }
}
