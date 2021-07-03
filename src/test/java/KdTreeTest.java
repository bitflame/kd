import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KdTreeTest {

    @BeforeEach
    void setUp() {
        Point2D p1;
        KdTree kt = new KdTree();
        for (int i = 0; i < 10; i++) {
            p1 = new Point2D(StdRandom.uniform(0.0, 1.0), StdRandom.uniform(0.0, 1.0));
            kt.insert(p1);
        }

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void contains() {
    }

    @Test
    void range() {
    }

    @Test
    void insert() {
    }

    @Test
    void nearest() {
    }
}