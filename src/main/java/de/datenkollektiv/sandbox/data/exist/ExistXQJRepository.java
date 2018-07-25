package de.datenkollektiv.sandbox.data.exist;

import de.datenkollektiv.sandbox.data.xml.SimpleXQJRepository;
import de.datenkollektiv.sandbox.data.xml.XMLEntityInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.oxm.Marshaller;

import javax.xml.xquery.XQItem;

public class ExistXQJRepository<T> extends SimpleXQJRepository<T> {

    private static Logger LOG = LoggerFactory.getLogger(ExistXQJRepository.class);

    public ExistXQJRepository(XMLEntityInformation<T> entityInformation,
                              ExistTemplate existTemplate,
                              Marshaller marshaller,
                              Converter<XQItem, T> toEntityConverter) {
        super(entityInformation, existTemplate, marshaller, toEntityConverter);

        if (!existTemplate.collectionAvailable(collectionName)) {
            LOG.info("Collection '" + collectionName + "' not found.");
            existTemplate.createCollection(DATA, rootElementName);
        }
    }
}
