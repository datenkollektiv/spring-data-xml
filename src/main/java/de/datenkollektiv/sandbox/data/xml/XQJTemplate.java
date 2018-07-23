package de.datenkollektiv.sandbox.data.xml;

import de.datenkollektiv.sandbox.data.xml.util.CloseableXQConnection;
import de.datenkollektiv.sandbox.data.xml.util.XQItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQItem;
import java.util.stream.Stream;

public class XQJTemplate implements XQJOperations {

    private static final Logger LOG = LoggerFactory.getLogger(XQJTemplate.class);

    private final XQDataSource dataSource;

    public XQJTemplate(XQDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private CloseableXQConnection createClosableXQConnection() {
        try {
            return new CloseableXQConnection(dataSource.getConnection());
        } catch (XQException e) {
            throw new DataRetrievalFailureException("Failed to create connection.", e);
        }
    }

    @Override
    public Stream<XQItem> execute(String xquery, VarBinder... varBinder) {
        CloseableXQConnection xqConnection = createClosableXQConnection();
        try {
            try {
                return xqConnection.items(xquery, varBinder).onClose(xqConnection::close);
            } catch (XQException e) {
                throw new DataRetrievalFailureException("Failed to execute query '"
                        + xquery + "'.", e);
            }
        } catch (Error | RuntimeException e) {
            xqConnection.close();
            throw new DataRetrievalFailureException("Failed to execute query '"
                    + xquery + "'.", e);
        }
    }

    @Override
    public void store(String collectionName, String documentName, Document documentToSave) {
        String query = "declare variable $id external; " +
                "declare variable $doc external; " +
                "xmldb:store('" + collectionName + "', $id, $doc)";
        execute(query,
                expression -> expression.bindString(QName.valueOf("id"), documentName + ".xml", null),
                expression -> expression.bindNode(QName.valueOf("doc"), documentToSave.getDocumentElement(), null));
    }

    @Override
    public Stream<XQItem> load(String collectionName, String documentName, String rootElementName) {
        return execute("doc(xs:anyURI('" + collectionName + "/" + documentName + ".xml'))//" + rootElementName);
    }

    @Override
    public Stream<XQItem> loadAll(String collectionName, String rootElementName) {
        return execute("collection('" + collectionName + "/')//" + rootElementName);
    }

    @Override
    public long count(String collectionName, String rootElementName) {
        return execute("count(collection('" + collectionName + "')//" + rootElementName + ")")
                .map(XQItems::toLong).findFirst()
                .orElseThrow(() -> new DataRetrievalFailureException("Failed to count items in collection '" + collectionName + "'."));
    }

    @Override
    public void delete(String collectionName, String documentName) {
        LOG.debug("Deleting '" + documentName + "' from '" + collectionName + "'.");
        execute("xmldb:remove('" + collectionName + "', '" + documentName + ".xml')");
    }

}
