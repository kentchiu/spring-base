package com.kentchiu.spring.base.service;

import com.kentchiu.spring.base.service.query.Queryable;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.torpedoquery.jpa.Query;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class QueryServiceTest {
    @Test
    public void testName() throws Exception {
        EntityManager mockEntityManger = Mockito.mock(EntityManager.class);
        JpaRepository mockRepository = Mockito.mock(JpaRepository.class);

        QueryService service = new QueryService() {
            @Override
            public JpaRepository getRepository() {
                return mockRepository;
            }

            @Override
            public EntityManager getEntityManager() {
                return mockEntityManger;
            }
        };

        Page all = service.findAll(new PageRequest(1, 10));

    }


    /*
        totalElements : 總筆數
        last : 是否為最後一頁
        totalPages : 總頁數
        size : 每頁筆數
        number : 頁碼 (從0算起)
        first : 是否為第一頁
        sort : 排序(目前無效)
        numberOfElements : 本頁筆數
     */

    @Test
    public void testPaging_first_page() throws Exception {
        List<String> content = IntStream.range(1, 100).mapToObj(i -> Integer.toString(i)).collect(Collectors.toList());
        Pageable pageable = new PageRequest(0, 10);
        PageImpl<String> pages = new PageImpl<>(content, pageable, content.size());

        assertThat(pages.getTotalElements(), is(99L));
        assertThat(pages.isLast(), is(false));
        assertThat(pages.getTotalPages(), is(10));
        assertThat(pages.getNumber(), is(0));
        assertThat(pages.isFirst(), is(true));
        assertThat(pages.getSize(), is(10));
        assertThat(pages.getSort(), nullValue());
        assertThat(pages.getNumberOfElements(),is(99)); // not 10 ?


        assertThat(pages.getContent().size(), is(99));
        assertThat(pages.hasContent(), is(true));
        assertThat(pages.hasPrevious(), is(false));
        assertThat(pages.hasNext(), is(true));
    }

    @Test
    public void testPaging_last_page() throws Exception {
        List<String> content = IntStream.range(1, 101).mapToObj(i -> Integer.toString(i)).collect(Collectors.toList());
        Pageable pageable = new PageRequest(0, 10);
        PageImpl<String> pages = new PageImpl<>(content, pageable, content.size());

        assertThat(pages.getTotalElements(), is(100L));
        assertThat(pages.isLast(), is(false));
        assertThat(pages.getTotalPages(), is(10));
        assertThat(pages.getNumber(), is(0));
        assertThat(pages.isFirst(), is(true));
        assertThat(pages.getSize(), is(10));
        assertThat(pages.getSort(), nullValue());
        assertThat(pages.getNumberOfElements(),is(100));


        assertThat(pages.getContent().size(), is(100));
        assertThat(pages.hasContent(), is(true));
        assertThat(pages.hasPrevious(), is(false));
        assertThat(pages.hasNext(), is(true));
    }
}