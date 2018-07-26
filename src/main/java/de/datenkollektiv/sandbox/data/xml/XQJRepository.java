package de.datenkollektiv.sandbox.data.xml;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.stream.Stream;

@NoRepositoryBean
interface XQJRepository<T> extends CrudRepository<T, String> {

    Stream<T> execute(String xquery);

    Stream<T> execute(String xquery, VarBinder... varBinder);

    Stream<T> findWithFilter(String filter);

    <S> S queryForObject(String xquery, Class<S> clazz);

    <S> Stream<S> queryInCollectionForStream(String xquery, Class<S> clazz);

    <S> Stream<S> queryForStream(String xquery, Class<S> clazz);

}
