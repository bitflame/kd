import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KdTreeTest {
    KdTree kdtree = new KdTree();

    @ParameterizedTest
    @CsvFileSource(resources = "troubleshoot.txt", delimiterString = " ")
    void insertShouldWork(double x, double y) {
        Point2D p = new Point2D(x, y);
        kdtree.insert(p);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "troubleshoot.txt", delimiterString = " ")
    void containsShouldWork(double x, double y) {
        Point2D p = new Point2D(x, y);
        kdtree.insert(p);
        kdtree.contains(p);
    }

}