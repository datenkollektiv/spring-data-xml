package de.datenkollektiv.sandbox.data.exist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ExistTemplateConfiguration.class)
class ExistTemplateTest {

    @Inject
    private ExistTemplate existTemplate;

    @Test
    void bogusCollectionNotAvailable() {
        assertFalse(existTemplate.collectionAvailable("bogus"));
    }

    @Test
    void shouldCreateFreshCollection() {
        String collectionName = "junit-test-collection";
        try {
            existTemplate.removeCollection(collectionName);
        } catch (Exception ignore) {
        }

        assertFalse(existTemplate.collectionAvailable(collectionName));

        existTemplate.createCollection("data", collectionName);
        assertTrue(existTemplate.collectionAvailable("/data/" + collectionName));
    }

}