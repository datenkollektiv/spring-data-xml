package de.datenkollektiv.sandbox.data.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.oxm.Marshaller;
import org.springframework.util.Assert;
import org.w3c.dom.Document;

import javax.xml.transform.dom.DOMResult;
import javax.xml.xquery.XQItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class SimpleXQJRepository<T> implements XQJRepository<T> {

    private static Logger LOG = LoggerFactory.getLogger(SimpleXQJRepository.class);

    protected static final String DATA = "data";

    private final XMLEntityInformation<T> entityInformation;
    private final XQJOperations xqjOperations;
    private final Marshaller marshaller;
    private final Converter<T, Document> toDocumentConverter;

    private Converter<XQItem, T> toEntityConverter;

    protected String rootElementName;
    protected String collectionName;

    public void setToEntityConverter(Converter<XQItem, T> toEntityConverter) {
        this.toEntityConverter = toEntityConverter;
    }

    public SimpleXQJRepository(XMLEntityInformation<T> entityInformation,
                               XQJOperations existOperations,
                               Marshaller marshaller,
                               Converter<XQItem, T> toEntityConverter) {

        Assert.notNull(entityInformation, "XMLEntityInformation must not be null!");
        Assert.notNull(existOperations, "ExistOperations must not be null!");
        Assert.notNull(marshaller, "Marshaller must not be null!");
        Assert.notNull(toEntityConverter, "to entity Converter must not be null!");

        this.entityInformation = entityInformation;
        this.xqjOperations = existOperations;
        this.marshaller = marshaller;
        this.toEntityConverter = toEntityConverter;

        this.rootElementName = entityInformation.getJavaType().getSimpleName().toLowerCase();
        this.collectionName = DATA + "/" + rootElementName;

        this.toDocumentConverter = entity -> {
            DOMResult result = new DOMResult();
            try {
                this.marshaller.marshal(entity, result);
            } catch (IOException e) {
                throw new DataAccessResourceFailureException("Failed to marshal the given entity.", e);
            }
            return (Document) result.getNode();
        };
    }

    @Override
    public Stream<T> execute(String xquery) {
        return execute(xquery, new VarBinder[]{});
    }

    @Override
    public Stream<T> execute(String xquery, VarBinder... varBinder) {
        return xqjOperations.execute(xquery, varBinder).map(this::toEntity);
    }

    @Override
    public <S extends T> S save(S entity) {
        String id = this.entityInformation.getId(entity);
        Assert.notNull(id, "ID must be non null.");
        LOG.debug("Saving '" + id + "'...");
        xqjOperations.store(collectionName, id, toDocumentConverter.convert(entity));
        LOG.debug("done.");
        return entity;
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach((S entity) -> result.add(save(entity)));
        return result;
    }

    @Override
    public Optional<T> findById(String id) {
        return xqjOperations.load(collectionName, id, rootElementName).findFirst().map(this::toEntity);
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    @Override
    public List<T> findAll() {
        return xqjOperations.loadAll(collectionName, rootElementName).map(this::toEntity).collect(toList());
    }

    @Override
    public List<T> findAllById(Iterable<String> ids) {
        List<T> result = new ArrayList<>();
        ids.forEach((String id) -> {
            findById(id).ifPresent(result::add);
        });
        return result;
    }

    @Override
    public long count() {
        return xqjOperations.count(collectionName, rootElementName);
    }

    @Override
    public void deleteById(String id) {
        Assert.notNull(id, "Cannot delete entity. Given id was 'null'.");

        xqjOperations.delete(collectionName, id);
    }

    @Override
    public void delete(T entity) {
        deleteById(entityInformation.getId(entity));
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        findAll().forEach(this::delete);
    }

    private T toEntity(XQItem xqItem) {
        return toEntityConverter.convert(xqItem);
    }

}
