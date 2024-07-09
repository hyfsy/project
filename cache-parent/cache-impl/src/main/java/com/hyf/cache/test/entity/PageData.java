package com.hyf.cache.test.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2022/01/18
 */
public class PageData<T> implements Serializable
{

    private int count = 0;

    private List<T> list = null;

    public PageData(int count, List<T> list) {
        this.count = count;
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public List<T> getList() {
        return list;
    }
}
