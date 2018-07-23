package de.datenkollektiv.sandbox.data.exist;

import de.datenkollektiv.sandbox.data.xml.SimpleXQJRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.stream.Stream;

import static java.util.stream.Collectors.summarizingInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = IntegerRepositoryConfiguration.class)
class SimpleExistXQJRepositoryTests {

    @Inject
    private SimpleXQJRepository<Integer> existRepository;

    @Test
    void findFirstShouldBeEmpty() {
        Stream<Integer> result = existRepository.execute("()");

        assertEquals(0, result.count());
    }

    @Test
    void findMinMaxInSquence() {
        try (Stream<Integer> result = existRepository.execute("(1 to 10)")) {
            IntSummaryStatistics intSummary = result.collect(summarizingInt(Integer::intValue));

            assertEquals(1, intSummary.getMin(), "Expected 1 to be the minimum of the sequence");
            assertEquals(10, intSummary.getMax(), "Expected 10 to be the maximum of the sequence");
        }
    }

    @Test
    void empty() {

        try (Stream<Integer> result = existRepository.execute("()")) {

            Iterator<Integer> iterator = result.iterator();

            assertFalse(iterator.hasNext(), "Expected empty sequence.");
        }
    }

    @Test
    void oneInteger() {

        Stream<Integer> result = existRepository.execute("(1)");

        Iterator<Integer> iterator = result.iterator();

        assertEquals(1, iterator.next().intValue(), "Expected first item to be '1'");

        assertFalse(iterator.hasNext(), "Expected empty sequence.");
    }

    @Test
    void oneToThreeIntegers() {

        Stream<Integer> result = existRepository.execute("1 to 3");

        Iterator<Integer> iterator = result.iterator();

        assertEquals(1, iterator.next().intValue(), "Expected first item to be '1'");
        assertEquals(2, iterator.next().intValue(), "Expected first item to be '2'");
        assertEquals(3, iterator.next().intValue(), "Expected first item to be '3'");
        assertFalse(iterator.hasNext(), "Expected three items only.");
    }

}
