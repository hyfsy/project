package com.hyf.ssm.pojo;

/**
 * @author baB_hyf
 * @date 2020/05/17
 */
public class Hello {

    private Integer id;
    private String name;

    public Hello() {
    }

    public Hello(Integer id, String name) {
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

    @Override
    public String toString() {
        return "Hello{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
