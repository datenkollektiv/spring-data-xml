package de.datenkollektiv.sandbox.data.exist;

import de.datenkollektiv.sandbox.data.exist.DummyRepositoryConfiguration.Dummy;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DummyRepositoryConfiguration.class)
class SimpleExistXQJRepositoryTests {

    @Inject
    private SimpleXQJRepository<Dummy> existRepository;

    @Test
    void emptySequence() {
        Stream<Integer> result = existRepository.queryForStream("()", Integer.class);

        assertThat("Expected empty sequence.", result.count(), is(0L));
    }

    @Test
    void oneIntegerInSequence() {
        Stream<Integer> result = existRepository.queryForStream("(1)", Integer.class);

        assertThat(result.toArray(), arrayContaining(1));
    }

    @Test
    void booleanSequence() {
        Stream<Boolean> result = existRepository.queryForStream("true(), false()", Boolean.class);

        assertThat(result.toArray(), arrayContaining(true, false));
    }

    @Test
    void oneToThreeIntegersInSequence() {
        Stream<Integer> result = existRepository.queryForStream("1 to 3", Integer.class);

        assertThat(result.toArray(), arrayContaining(1,2,3));
    }

    @Test
    void findMinMaxInSquence() {
        try (Stream<Integer> result = existRepository.queryForStream("(1 to 10)", Integer.class)) {
            IntSummaryStatistics intSummary = result.collect(summarizingInt(Integer::intValue));

            assertThat("Expected 1 to be the minimum of the sequence", intSummary.getMin(), is(1));
            assertThat("Expected 10 to be the maximum of the sequence", intSummary.getMax(), is(10));
        }
    }

    @Test
    void twoStringsInSequence() {
        // same as ('foo', 'bar')
        Stream<String> result = existRepository.queryForStream("'foo', 'bar'", String.class);

        assertThat(result.toArray(), arrayContaining("foo", "bar"));
    }

    @Test
    void calculationWithAtomicIntegerResult() {
        Integer result = existRepository.queryForObject("2 * 3", Integer.class);

        assertThat(result, is(6));
    }

    @Test
    void operationWithAtomicStringResult() {
        String result = existRepository.queryForObject("concat('foo', 'bar')", String.class);

        assertThat(result, is("foobar"));
    }

}
