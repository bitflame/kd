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
    /*  final static File inputsFolder = new File("src/main/resources");
     final static File inputsFullPath = new File("/home/sansari/IdeaProjects/kd/src/main/resources");

     private static String fileName = "";

     The filter works with Linux format full path, but not with root directory path and escape characters. I wonder if
      * Linux format would work for a root directory relative path format; will have to test later.
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

    @Disabled
    @BeforeAll
    void setup() {
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
*/
    /* Create another parameterized test for setting the left and right rectangles to see if they work */
    KdTree kdtree = new KdTree();

    /* using reflection to test IntervalST ref:
    https://stackoverflow.com/questions/14112166/instantiate-private-inner-class-with-java-reflection#14112262 */
    public static Object giveMeInnerInstance() throws Exception {
        KdTree kdtree = new KdTree();
        Class<?> intervalST = KdTree.class.getDeclaredClasses()[0];
        /* make sure there are not more than one constructors below. Put a break point and check it. */
        Constructor<?> constructor = intervalST.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        return constructor.newInstance(kdtree);
    }

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

    @Test
    public void intervalSearchTreeShouldWork() throws Exception{
        KdTree kdtree = new KdTree();
        Class<?> intervalST = KdTree.class.getDeclaredClasses()[0];
        Constructor<?> constructor = intervalST.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Object innerObject = constructor.newInstance(kdtree);
        Method method = intervalST.getDeclaredMethod("put",Object.class,Object.class,Object.class);
        method.setAccessible(true);
        method.invoke(intervalST,0.0,1.0,1.0);
        StdOut.println("it worked ");
    }
}