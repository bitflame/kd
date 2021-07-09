import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KdTreeTest {
    final static File inputsFolder = new File("src\\main\\resources");
    final static File inputsFullPath = new File("/home/sansari/IdeaProjects/kd/src/main/resources");

    private static String fileName = "";
    KdTree kdtree = new KdTree();

    /* The filter works with Linux format full path, but not with root directory path and escape characters. I wonder if
     * Linux format would work for a root directory relative path format; will have to test later. */
    @Test
    void insertShouldWork() {
        FilenameFilter textFilefilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".txt")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        String[] testFilesList = inputsFullPath.list(textFilefilter);
        for (final String fileEntry : testFilesList) {
            In in = new In(fileEntry);
            while (!in.isEmpty()) {
                double x = in.readDouble();
                double y = in.readDouble();
                Point2D p = new Point2D(x, y);
                kdtree.insert(p);
                // brute.insert(p);
            }
        }
    }
}