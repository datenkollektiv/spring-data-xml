package de.datenkollektiv.sandbox.data.xml;

import org.w3c.dom.Document;

import javax.xml.xquery.XQItem;
import java.util.stream.Stream;

public interface XQJOperations {

    Stream<XQItem> execute(String xquery, VarBinder... varBinder);

    void store(String collectionName, String documentName, Document documentToSave);

    Stream<XQItem> load(String collectionName, String documentName, String rootElementName);

    Stream<XQItem> loadAll(String collectionName, String rootElementName);

    long count(String collectionName, String rootElementName);

    void delete(String collectionName, String documentName);

}
