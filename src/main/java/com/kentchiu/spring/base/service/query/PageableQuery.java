package com.kentchiu.spring.base.service.query;

import com.google.common.base.Splitter;
import com.kentchiu.spring.attribute.AttributeInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

abstract public class PageableQuery<T> implements Queryable<T> {
    private Integer page = 0;
    private Integer size = 100;
    private String sort;

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PageableQuery{");
        sb.append("page=").append(page);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }

    @NotNull
    @AttributeInfo(description = "分頁頁碼(0-base)", defaultValue = "0")
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @NotNull
    @AttributeInfo(description = "每頁筆數", defaultValue = "100")
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public PageRequest getPageRequest() {
        if (StringUtils.isBlank(sort)) {
            return new PageRequest(page, size);
        } else {
            Sort sort = createSort(this.sort);
            return new PageRequest(page, size, sort);
        }
    }

    public Sort createSort(String sort) {
        List<String> tokens = Splitter.on(",").trimResults().splitToList(sort);
        List<Sort.Order> sorts = tokens.stream().map(token -> {
            if (StringUtils.startsWith(token, "-")) {
                return new Sort.Order(Sort.Direction.DESC, StringUtils.substringAfter(token, "-"));
            } else if (StringUtils.startsWith(token, "+")) {
                return new Sort.Order(Sort.Direction.ASC, StringUtils.substringAfter(token, "+"));
            } else {
                return new Sort.Order(Sort.Direction.ASC, token);
            }
        }).collect(Collectors.toList());
        return new Sort(sorts);
    }


}

