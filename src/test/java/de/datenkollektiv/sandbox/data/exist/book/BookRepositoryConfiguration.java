package de.datenkollektiv.sandbox.data.exist.book;

import de.datenkollektiv.sandbox.data.exist.AbstractExistRepositoryConfiguration;
import de.datenkollektiv.sandbox.data.xml.SimpleXQJRepository;
import de.datenkollektiv.sandbox.data.xml.XQJOperations;
import de.datenkollektiv.sandbox.data.xml.util.XQItems;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.dom.DOMSource;
import javax.xml.xquery.XQItem;

public class BookRepositoryConfiguration extends AbstractExistRepositoryConfiguration {

    @Bean
    Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Book.class);
        return marshaller;
    }

    @Bean
    Converter<XQItem, Book> xqItemBookConverter(Jaxb2Marshaller marshaller) {
        return (xqItem -> Book.class.cast(marshaller.unmarshal(new DOMSource(XQItems.toNode(xqItem)))));
    }

    @Bean
    SimpleXQJRepository<Book> repository(XQJOperations xqjOperations,
                                         Marshaller marshaller,
                                         Converter<XQItem, Book> toEntityConverter) {
        return new SimpleXQJRepository<>(new BookEntityInformation(), xqjOperations, marshaller, toEntityConverter);
    }
}
