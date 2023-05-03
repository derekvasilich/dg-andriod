package com.example.dg_andriod.data.model;

import java.util.ArrayList;
import java.util.List;

public class PagedList<T> {

    private int page = 0;
    private List<T> content = new ArrayList<>();
    private int totalElements = 0;
    private int totalPages = 0;
    public boolean last = false;
    public int numberOfElements = 0;
    public Object sort;
    public Object pageable;
    public int number = 0;
    public boolean first = false;
    public int size = 0;
    public boolean empty = false;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> results) {
        this.content = content;
    }

    public int getTotalResults() {
        return totalElements;
    }

    public void setTotalResults(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}