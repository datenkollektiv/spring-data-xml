package de.datenkollektiv.sandbox.data.exist;

import de.datenkollektiv.sandbox.data.xml.SimpleXQJRepository;
import de.datenkollektiv.sandbox.data.xml.XMLEntityInformation;
import de.datenkollektiv.sandbox.data.xml.XQJOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

class DummyRepositoryConfiguration extends ExistTemplateConfiguration {

    static class Dummy {
    }

    @Bean
    Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Dummy.class);
        return marshaller;
    }

    @Bean
    SimpleXQJRepository<Dummy> repository(XQJOperations xqjOperations,
                                          Marshaller marshaller,
                                          Unmarshaller unmarshaller) {
        XMLEntityInformation<Dummy> entityInformation = new XMLEntityInformation<Dummy>() {
            @Override
            public Class<Dummy> getJavaType() {
                return Dummy.class;
            }

            @Override
            public boolean isNew(Dummy entity) {
                return false;
            }

            @Override
            public String getId(Dummy entity) {
                return entity.toString();
            }
        };
        return new SimpleXQJRepository<>(entityInformation, xqjOperations, marshaller, unmarshaller);
    }
}
