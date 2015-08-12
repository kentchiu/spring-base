package com.kentchiu.spring.base.service.query;

import com.kentchiu.spring.attribute.AttributeInfo;
import org.springframework.data.domain.PageRequest;

import javax.validation.constraints.NotNull;

abstract public class PageableQuery<T> implements Queryable<T> {
    private Integer page = 0;
    private Integer size = 100;

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
        return new PageRequest(page, size);
    }

}
