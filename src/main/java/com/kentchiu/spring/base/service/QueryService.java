package com.kentchiu.spring.base.service;

import com.kentchiu.spring.base.service.query.PageableQuery;
import com.kentchiu.spring.base.service.query.Queryable;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface QueryService<T> {

    Logger logger = LoggerFactory.getLogger(CrudService.class);

    static Long count(EntityManager em, String sql, Map<String, Object> params) {
        TypedQuery<Long> countQuery = em.createQuery(QueryUtils.createCountQueryFor(sql), Long.class);
        params.forEach((k, v) -> countQuery.setParameter(k, v));
        return countQuery.getSingleResult();
    }

    JpaRepository<T, String> getRepository();

    EntityManager getEntityManager();

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

    @Transactional(readOnly = true)
    default Page<T> findAll(PageableQuery query) {
        PageRequest pageable = query.getPageRequest();
        Query<T> q = query.buildQuery();

        logger.debug("JPQL: {} with parameter {}", q.getQuery(), q.getParameters());

        EntityManager entityManager = getEntityManager();
        Long total = count(entityManager, q.getQuery(), q.getParameters());
        if (total <= query.getPageRequest().getOffset()) {
            return new PageImpl<>(Collections.emptyList(), pageable, total);
        }
        q.setFirstResult(pageable.getOffset());
        q.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(q.list(entityManager), pageable, total);
    }

    @Transactional(readOnly = true)
    default List<T> findAll(Queryable query) {
        Query<T> q = query.buildQuery();
        logger.debug("JPQL: {} with parameter {}", q.getQuery(), q.getParameters());
        return q.list(getEntityManager());
    }

}
