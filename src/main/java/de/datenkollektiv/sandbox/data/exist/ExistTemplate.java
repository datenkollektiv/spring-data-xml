package de.datenkollektiv.sandbox.data.exist;

import de.datenkollektiv.sandbox.data.xml.XQJTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQItem;
import java.util.stream.Stream;

public class ExistTemplate extends XQJTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(ExistTemplate.class);

    private static final String EXIST_NAMESPACE = "xmldb";

    public ExistTemplate(XQDataSource dataSource) {
        super(dataSource);
    }

    public boolean collectionAvailable(String collectionName) {
        String xquery = EXIST_NAMESPACE + ":collection-available('" + collectionName + "')";
        Stream<XQItem> result = execute(xquery);
        try {
            return result.findFirst().orElseThrow(() -> new RuntimeException("Failed to xquery collection-available."))
                    .getBoolean();
        } catch (XQException e) {
            throw new RuntimeException("Failed to query collection-available.", e);
        }
    }

    public void createCollection(String targetCollectionUri, String collectionName) {
        String xquery = EXIST_NAMESPACE + ":create-collection('" + targetCollectionUri + "', '" + collectionName + "')";
        execute(xquery);
    }

    public void removeCollection(String collectionName) {
        String xquery = EXIST_NAMESPACE + ":remove('" + collectionName + "')";
        execute(xquery);
    }

    @Override
    public void store(String collectionName, String documentName, Document documentToSave) {
        String xquery = "declare variable $id external; " +
                "declare variable $doc external; " +
                EXIST_NAMESPACE + ":store('" + collectionName + "', $id, $doc)";
        execute(xquery,
                expression -> expression.bindString(QName.valueOf("id"), documentName + ".xml", null),
                expression -> expression.bindNode(QName.valueOf("doc"), documentToSave.getDocumentElement(), null));
    }

    @Override
    public void delete(String collectionName, String documentName) {
        LOG.debug("Deleting '" + documentName + "' from '" + collectionName + "'.");
        String xquery = EXIST_NAMESPACE + ":remove('" + collectionName + "', '" + documentName + ".xml')";
        execute(xquery);
    }

}
