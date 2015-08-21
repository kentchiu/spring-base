package com.kentchiu.spring.base.service;

import com.google.common.base.Preconditions;
import com.kentchiu.spring.base.domain.IdentifiableObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface CrudService<T> {

    JpaRepository<T, String> getRepository();

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


}
