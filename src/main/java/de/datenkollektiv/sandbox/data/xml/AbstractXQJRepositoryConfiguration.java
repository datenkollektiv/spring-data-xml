package de.datenkollektiv.sandbox.data.xml;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.xquery.XQDataSource;

@Configuration
public class AbstractXQJRepositoryConfiguration {

    @Bean
    XQJOperations xqjOperations(XQDataSource xqDataSource) {
        return new XQJTemplate(xqDataSource);
    }

}
