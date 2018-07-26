package de.datenkollektiv.sandbox.data.exist.book;

import de.datenkollektiv.sandbox.data.exist.ExistTemplate;
import de.datenkollektiv.sandbox.data.exist.ExistTemplateConfiguration;
import de.datenkollektiv.sandbox.data.exist.ExistXQJRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class BookRepositoryConfiguration extends ExistTemplateConfiguration {

    @Bean
    Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Book.class);
        return marshaller;
    }

    @Bean
    ExistXQJRepository<Book> repository(ExistTemplate existTemplate,
                                        Marshaller marshaller,
                                        Unmarshaller unmarshaller) {
        return new ExistXQJRepository<>(new BookEntityInformation(), existTemplate, marshaller, unmarshaller);
    }
}
