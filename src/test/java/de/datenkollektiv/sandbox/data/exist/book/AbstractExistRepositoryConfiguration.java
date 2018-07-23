package de.datenkollektiv.sandbox.data.exist.book;

import de.datenkollektiv.sandbox.data.xml.AbstractXQJRepositoryConfiguration;
import net.xqj.exist.ExistXQDataSource;
import org.springframework.context.annotation.Bean;

import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;

public class AbstractExistRepositoryConfiguration extends AbstractXQJRepositoryConfiguration {

    @Bean
    XQDataSource existXqDataSource() throws XQException {
        XQDataSource xqDataSource = new ExistXQDataSource();
        xqDataSource.setProperty("serverName", "localhost");
        xqDataSource.setProperty("port", "8080");
        ((ExistXQDataSource) xqDataSource).setUser("admin");
        ((ExistXQDataSource) xqDataSource).setPassword("");
        return xqDataSource;
    }

}
