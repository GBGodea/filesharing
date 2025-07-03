package org.godea.models.dto;

import java.util.List;

public class PageDTO<T> {
    private List<T> items;
    private long total;

    public PageDTO(List<T> items, long total) {
        this.items = items;
        this.total = total;
    }
}
