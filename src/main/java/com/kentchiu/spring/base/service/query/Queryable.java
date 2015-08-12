package com.kentchiu.spring.base.service.query;

import org.torpedoquery.jpa.Query;

public interface Queryable<T> {

    Query<T> buildQuery();

}
