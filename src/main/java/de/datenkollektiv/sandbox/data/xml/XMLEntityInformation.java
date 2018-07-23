package de.datenkollektiv.sandbox.data.xml;

import org.springframework.data.repository.core.EntityInformation;

public abstract class XMLEntityInformation<T> implements EntityInformation<T, String> {

    public Class<String> getIdType() {
        return String.class;
    }
}
