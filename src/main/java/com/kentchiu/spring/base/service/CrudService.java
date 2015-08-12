package com.kentchiu.spring.base.service;

import com.google.common.base.Preconditions;
import com.kentchiu.spring.base.domain.IdentifiableObject;
import com.kentchiu.spring.base.service.query.PageableQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.transaction.annotation.Transactional;
import org.torpedoquery.jpa.Query;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

public interface CrudService<T> {

    Logger logger = LoggerFactory.getLogger(CrudService.class);


    static Long count(EntityManager em, String sql, Map<String, Object> params) {
        TypedQuery<Long> countQuery = em.createQuery(QueryUtils.createCountQueryFor(sql), Long.class);
        params.forEach((k, v) -> countQuery.setParameter(k, v));
        return countQuery.getSingleResult();
    }

    default <S extends T> S add(S entity) {
        IdentifiableObject io = (IdentifiableObject) entity;
        if (StringUtils.isBlank(io.getUuid())) {
            String uuid = UUID.randomUUID().toString();
            io.setUuid(uuid);
        }
        return getRepository().save(entity);
    }

    default <S extends T> S update(S entity) {
        IdentifiableObject io = (IdentifiableObject) entity;
        Preconditions.checkState(StringUtils.isNotBlank(io.getUuid()), "uuid should not be blank when update");
        return getRepository().save(entity);
    }


    @Transactional(readOnly = true)
    default Optional<T> findOne(String id) {
        return Optional.ofNullable(getRepository().findOne(id));
    }

    @Transactional(readOnly = true)
    default boolean exists(String id) {
        return getRepository().equals(id);
    }

    @Transactional(readOnly = true)
    default List<T> findAll() {
        return getRepository().findAll();
    }

    @Transactional(readOnly = true)
    default Page<T> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    @Transactional(readOnly = true)
    default List<T> findAll(Iterable<String> ids) {
        return getRepository().findAll(ids);
    }

    @Transactional(readOnly = true)
    default long count() {
        return getRepository().count();
    }

    default void delete(String id) {
        getRepository().delete(id);
    }

    default void delete(T entity) {
        getRepository().delete(entity);
    }

    default void delete(Iterable<? extends T> entities) {
        getRepository().delete(entities);
    }

    default void deleteAll() {
        getRepository().deleteAll();
    }

    JpaRepository<T, String> getRepository();

    EntityManager getEntityManager();

    default Page<T> findAll(PageableQuery query) {
        return findAll(getEntityManager(), query);
    }

    @Transactional(readOnly = true)
    default Page<T> findAll(EntityManager entityManager, PageableQuery query) {
        PageRequest pageable = query.getPageRequest();
        Query<T> q = query.buildQuery();

        logger.debug("JPQL: {} with parameter {}", q.getQuery(), q.getParameters());

        Long total = CrudService.count(entityManager, q.getQuery(), q.getParameters());
        if (total <= query.getPageRequest().getOffset()) {
            return new PageImpl<>(Collections.emptyList(), pageable, total);
        }
        q.setFirstResult(pageable.getOffset());
        q.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(q.list(entityManager), pageable, total);
    }

}
