package com.kentchiu.spring.base.service.query;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.torpedoquery.jpa.Query;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PageableQueryTest {

    private PageableQuery query;

    @Before
    public void setUp() throws Exception {
        query = new PageableQuery() {
            @Override
            public Query buildQuery() {
                return null;
            }
        };
    }

    @Test
    public void sort() throws Exception {
        Sort sort = query.createSort("foo");
        assertThat(sort.getOrderFor("foo").getDirection(), is(Sort.Direction.ASC));
        assertThat(sort.getOrderFor("foo").getProperty(), is("foo"));
    }

    @Test
    public void sort_asc() throws Exception {
        Sort sort = query.createSort("+foo");
        assertThat(sort.getOrderFor("foo").getDirection(), is(Sort.Direction.ASC));
        assertThat(sort.getOrderFor("foo").getProperty(), is("foo"));
    }


    @Test
    public void sort_desc() throws Exception {
        Sort sort = query.createSort("-foo");
        assertThat(sort.getOrderFor("foo").getDirection(), is(Sort.Direction.DESC));
        assertThat(sort.getOrderFor("foo").getProperty(), is("foo"));
    }


    @Test
    public void sort_asc_and_desc() throws Exception {
        Sort sort = query.createSort("+foo,-bar");
        assertThat(sort.getOrderFor("foo").getDirection(), is(Sort.Direction.ASC));
        assertThat(sort.getOrderFor("foo").getProperty(), is("foo"));
        assertThat(sort.getOrderFor("bar").getDirection(), is(Sort.Direction.DESC));
        assertThat(sort.getOrderFor("bar").getProperty(), is("bar"));
    }

    @Test
    public void sort_asc_and_desc_2() throws Exception {
        Sort sort = query.createSort("foo,-bar");
        assertThat(sort.getOrderFor("foo").getDirection(), is(Sort.Direction.ASC));
        assertThat(sort.getOrderFor("foo").getProperty(), is("foo"));
        assertThat(sort.getOrderFor("bar").getDirection(), is(Sort.Direction.DESC));
        assertThat(sort.getOrderFor("bar").getProperty(), is("bar"));
    }


    @Test
    public void sort_asc_and_desc_3() throws Exception {
        Sort sort = query.createSort("-foo,bar");
        assertThat(sort.getOrderFor("foo").getDirection(), is(Sort.Direction.DESC));
        assertThat(sort.getOrderFor("foo").getProperty(), is("foo"));
        assertThat(sort.getOrderFor("bar").getDirection(), is(Sort.Direction.ASC));
        assertThat(sort.getOrderFor("bar").getProperty(), is("bar"));
    }
}