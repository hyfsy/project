package com.hyf.cache.test.test.origin;

import java.io.Serializable;

/**
 * @author baB_hyf
 * @date 2022/02/15
 */
public class Person implements Serializable {

    private Integer id;
    private String name;

    public Person(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
