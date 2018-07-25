package de.datenkollektiv.sandbox.data.exist;

import net.xqj.exist.ExistXQDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;

@Configuration
public class ExistTemplateConfiguration {

    @Value("${exist.datasource.server-name:localhost}")
    private String serverName;

    @Value("${exist.datasource.port:8080}")
    private String port;

    @Value("${exist.datasource.user:admin}")
    private String user;

    @Value("${exist.datasource.password:}")
    private String password;

    @Bean
    XQDataSource existXqDataSource() throws XQException {
        XQDataSource xqDataSource = new ExistXQDataSource();
        xqDataSource.setProperty("serverName", serverName);
        xqDataSource.setProperty("port", port);
        ((ExistXQDataSource) xqDataSource).setUser(user);
        ((ExistXQDataSource) xqDataSource).setPassword(password);
        return xqDataSource;
    }

    @Bean
    ExistTemplate existTemplate(XQDataSource xqDataSource) {
        return new ExistTemplate(xqDataSource);
    }

}
