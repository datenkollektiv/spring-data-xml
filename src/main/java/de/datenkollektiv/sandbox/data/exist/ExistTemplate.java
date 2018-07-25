package de.datenkollektiv.sandbox.data.exist;

import de.datenkollektiv.sandbox.data.xml.XQJTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQDataSource;

public class ExistTemplate extends XQJTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(ExistTemplate.class);

    private static final String EXIST_NAMESPACE = "xmldb";

    public ExistTemplate(XQDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void store(String collectionName, String documentName, Document documentToSave) {
        String query = "declare variable $id external; " +
                "declare variable $doc external; " +
                EXIST_NAMESPACE + ":store('" + collectionName + "', $id, $doc)";
        execute(query,
                expression -> expression.bindString(QName.valueOf("id"), documentName + ".xml", null),
                expression -> expression.bindNode(QName.valueOf("doc"), documentToSave.getDocumentElement(), null));
    }

    @Override
    public void delete(String collectionName, String documentName) {
        LOG.debug("Deleting '" + documentName + "' from '" + collectionName + "'.");
        execute(EXIST_NAMESPACE + ":remove('" + collectionName + "', '" + documentName + ".xml')");
    }

}
