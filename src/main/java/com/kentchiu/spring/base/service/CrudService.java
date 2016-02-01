package com.kentchiu.spring.base.service;

import com.google.common.base.Preconditions;
import com.kentchiu.spring.base.domain.IdentifiableObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface CrudService<T> {

    Logger logger = LoggerFactory.getLogger(CrudService.class);

    JpaRepository<T, String> getRepository();

    default <S extends T> S add(S entity) {
        IdentifiableObject io = (IdentifiableObject) entity;
        if (StringUtils.isBlank(io.getUuid())) {
            String uuid = UUID.randomUUID().toString();
            io.setUuid(uuid);
        }
        logger.debug("add entity: {}", entity);
        return getRepository().save(entity);
    }

    default <S extends T> S update(S entity) {
        IdentifiableObject io = (IdentifiableObject) entity;
        Preconditions.checkState(StringUtils.isNotBlank(io.getUuid()), "uuid should not be blank when update");
        logger.debug("update entity: {}", entity);
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


    default void delete(String id) {
        logger.info("delete entity by id: {}", id);
        getRepository().delete(id);
    }

    default void delete(T entity) {
        logger.info("delete entity : {}", entity);
        getRepository().delete(entity);
    }

    default void delete(Iterable<? extends T> entities) {
        logger.info("delete entities : {}", entities);
        getRepository().delete(entities);
    }


    default void deleteInBatch(Iterable<T> entities) {
        logger.info("delete entities : {}", entities);
        getRepository().deleteInBatch(entities);
    }

    @Transactional
    default void deleteByIds(Iterable<String> ids) {
        logger.info("delete by ids : {}", ids);
        for (String id : ids) {
            getRepository().delete(id);
        }
    }

    default void deleteAll() {
        logger.warn("delete all entities");
        getRepository().deleteAll();
    }

    default void deleteAllInBatch() {
        logger.warn("delete all entities in batch");
        getRepository().deleteAllInBatch();
    }


}
