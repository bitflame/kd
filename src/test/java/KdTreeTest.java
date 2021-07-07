import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PointInfoAggregator implements ArgumentsAggregator {
    @Override
    public Point2D aggregateArguments(ArgumentsAccessor arguments, ParameterContext context) {
        return new Point2D(arguments.getDouble(0),
                arguments.getDouble(1));
    }
}

class RectangleInfoAggregator implements ArgumentsAggregator {
    @Override
    public RectHV aggregateArguments(ArgumentsAccessor arguments, ParameterContext context) {
        return new RectHV(arguments.getDouble(0), arguments.getDouble(0), arguments.getDouble(0), arguments.getDouble(0));
    }
}

class KdTreeTest {
    @ParameterizedTest
    @BeforeEach
    @CsvSource({"0.45,0.50", "0.40,0.20", "0.50,0.20", "0.40,0.10", "0.40,0.30", "0.50,0.10", "0.60,0.30", "0.65,0.50", "0.70,0.20"
            , "0.80,0.20", "0.85,0.10", "0.90,0.30", "0.96,0.10", "0.61,0.30", "0.44,0.51", "0.42,0.22", "0.51,0.20", "0.40,0.12", "0.49,0.30"
            , "0.50,0.18", "0.67,0.30", "0.65,0.56", "0.75,0.20", "0.80,0.24", "0.84,0.13", "0.90,0.32", "0.96,0.11", "0.61,0.37"})
    void setUp(@AggregateWith(PointInfoAggregator.class) Point2D point) {
        KdTree kt = new KdTree();
        for (int i = 0; i < 29; i++) {
            kt.insert(point);
        }
        /* Point2D p1;
        for (int i = 0; i < 10; i++) {
            p1 = new Point2D(StdRandom.uniform(0.0, 1.0), StdRandom.uniform(0.0, 1.0));
            kt.insert(p1);
        } */

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void contains() {
    }
@ParameterizedTest
    @CsvSource({})
    void range() {
    }

    @Test
    void insert() {
    }

    @Test
    void nearest() {
    }
}