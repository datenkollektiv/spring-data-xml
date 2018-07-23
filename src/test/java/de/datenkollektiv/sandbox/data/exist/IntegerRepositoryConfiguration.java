package de.datenkollektiv.sandbox.data.exist;

import de.datenkollektiv.sandbox.data.exist.book.AbstractExistRepositoryConfiguration;
import de.datenkollektiv.sandbox.data.xml.SimpleXQJRepository;
import de.datenkollektiv.sandbox.data.xml.XMLEntityInformation;
import de.datenkollektiv.sandbox.data.xml.XQJOperations;
import de.datenkollektiv.sandbox.data.xml.util.XQItems;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.xquery.XQItem;

public class IntegerRepositoryConfiguration extends AbstractExistRepositoryConfiguration {

    @Bean
    Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Integer.class);
        return marshaller;
    }

    @Bean
    Converter<XQItem, Integer> xqItemIntegerConverter() {
        return XQItems::toInt;
    }

    @Bean
    SimpleXQJRepository<Integer> repository(XQJOperations xqjOperations,
                                            Marshaller marshaller,
                                            Converter<XQItem, Integer> toEntityConverter) {
        XMLEntityInformation<Integer> entityInformation = new XMLEntityInformation<Integer>() {
            @Override
            public Class<Integer> getJavaType() {
                return Integer.class;
            }

            @Override
            public boolean isNew(Integer entity) {
                return false;
            }

            @Override
            public String getId(Integer entity) {
                return entity.toString();
            }
        };
        return new SimpleXQJRepository<>(entityInformation, xqjOperations, marshaller, toEntityConverter);
    }
}
