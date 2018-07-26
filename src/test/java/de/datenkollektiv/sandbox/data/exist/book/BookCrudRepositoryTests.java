package de.datenkollektiv.sandbox.data.exist.book;

import de.datenkollektiv.sandbox.data.xml.SimpleXQJRepository;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Double.NaN;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookRepositoryConfiguration.class)
class BookCrudRepositoryTests {

    private static final String THE_OUTSIDER_ISBN_13 = "9781501180989";
    private static final String THE_GREAT_ALONE_ISBN_13 = "9780312577230";
    private static final String IN_THE_DARK_ISBN_13 = "9781501112331";
    private static final String THE_IMMORTALISTS_ISBN_13 = "9780735213180";

    @Inject
    private SimpleXQJRepository<Book> repository;

    @Test
    void shouldSaveBook() {
        repository.save(book("9780062678416", "The Woman in the Window", "A.J. Finn"));
    }

    @Test
    void shouldSaveAll() {
        Iterable<Book> books = repository.saveAll(Stream.of(
                book("9780312577230", "The Great Alone", "Kristin Hannah"),
                book("9781501112331", "In a Dark, Dark Wood", "Ruth Ware"),
                book("9780735213180", "The Immortalists", "Chloe Benjamin")
        ).collect(toList()));

        assertThat(books, iterableWithSize(3));

        assertThat(books, everyItem(
                hasProperty("year", equalTo(2018))
        ));
    }

    @Test
    void shouldNotFindBogusBookById() {
        Optional<Book> actual = repository.findById("non-existent-book");

        assertFalse(actual.isPresent());
    }

    @Test
    void shouldFindById() {
        Book book = book("9780316556347", "Circe", "Madeline Miller");
        repository.save(book);

        Optional<Book> actual = repository.findById(BookEntityInformation.asId(book));

        assertTrue(actual.isPresent());

        assertEquals("Madeline Miller", actual.get().getAuthor());
    }

    @Test
    void shouldFindWithFilter() {
        Book book = book("9780316556347", "Circe", "Madeline Miller");
        repository.save(book);

        Stream<Book> actual = repository.findWithFilter("[title='Circe']");

        Optional<Book> first = actual.findFirst();

        assertTrue(first.isPresent());
        assertEquals("Madeline Miller", first.get().getAuthor());
    }

    @Test
    void shouldCorrectlyDetectExistsById() {
        if (repository.existsById(THE_OUTSIDER_ISBN_13)) {
            repository.deleteById(THE_OUTSIDER_ISBN_13);
        }

        assertFalse(repository.existsById(THE_OUTSIDER_ISBN_13));

        repository.save(book(THE_OUTSIDER_ISBN_13, "The Outsider", "Stephen King"));

        assertTrue(repository.existsById(THE_OUTSIDER_ISBN_13));
    }

    @Test
    void shouldFindAll() {
        Iterable<Book> books = repository.findAll();

        assertThat(books, iterableWithSize((int) repository.count()));
    }

    @Test
    void findAllByIdShouldFailWithBogusId() {
        assertEquals(0, repository.findAllById(Collections.singleton("foo_bar_baz")).size());
    }

    @Test
    void shouldFindAllById() {
        repository.saveAll(Stream.of(
                book(THE_GREAT_ALONE_ISBN_13, "The Great Alone", "Kristin Hannah"),
                book(IN_THE_DARK_ISBN_13, "In a Dark, Dark Wood", "Ruth Ware"),
                book(THE_IMMORTALISTS_ISBN_13, "The Immortalists", "Chloe Benjamin")
        ).collect(toList()));

        assertThat(repository.findAllById(Collections.singleton(THE_GREAT_ALONE_ISBN_13)), iterableWithSize(1));
        assertThat(repository.findAllById(Arrays.asList(THE_GREAT_ALONE_ISBN_13, THE_IMMORTALISTS_ISBN_13)), iterableWithSize(2));
    }

    @Test
    void shouldDeleteById() {
        repository.save(book(THE_OUTSIDER_ISBN_13, "The Outsider", "Stephen King"));
        assertTrue(repository.existsById(THE_OUTSIDER_ISBN_13));

        repository.deleteById(THE_OUTSIDER_ISBN_13);
        assertFalse(repository.existsById(THE_OUTSIDER_ISBN_13));
    }

    @Test
    void shouldDeleteByEntity() {
        Book book = book(THE_OUTSIDER_ISBN_13, "The Outsider", "Stephen King");
        repository.save(book);
        assertTrue(repository.existsById(THE_OUTSIDER_ISBN_13));

        repository.delete(book);
        assertFalse(repository.existsById(THE_OUTSIDER_ISBN_13));
    }

    @Test
    void shouldDeleteAll() {
        repository.save(book(THE_OUTSIDER_ISBN_13, "The Outsider", "Stephen King"));
        assertTrue(repository.existsById(THE_OUTSIDER_ISBN_13));

        repository.deleteAll();

        assertEquals(0, repository.count());
    }

    @Test
    void shouldDeleteAllByEntity() {
        Book theOutsider = book(THE_OUTSIDER_ISBN_13, "The Outsider", "Stephen King");
        repository.save(theOutsider);
        Book inTheDark = book(IN_THE_DARK_ISBN_13, "In a Dark, Dark Wood", "Ruth Ware");
        repository.save(inTheDark);

        assertTrue(repository.existsById(THE_OUTSIDER_ISBN_13));
        assertTrue(repository.existsById(IN_THE_DARK_ISBN_13));

        repository.deleteAll(Arrays.asList(theOutsider, inTheDark));

        assertFalse(repository.existsById(THE_OUTSIDER_ISBN_13));
        assertFalse(repository.existsById(IN_THE_DARK_ISBN_13));

    }

    @Test
    void shouldCorrectlyCount() {
        if (repository.existsById(THE_OUTSIDER_ISBN_13)) {
            repository.deleteById(THE_OUTSIDER_ISBN_13);
        }

        long current = repository.count();

        repository.save(book(THE_OUTSIDER_ISBN_13, "The Outsider", "Stephen King"));

        assertEquals(current + 1, repository.count());
    }

    private static Book book(String isbn13, String title, String author) {
        Book book = new Book();
        book.setIsbn13(isbn13);
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory("unknown");
        book.setPrice(NaN);
        book.setYear(2018);
        return book;
    }

}
