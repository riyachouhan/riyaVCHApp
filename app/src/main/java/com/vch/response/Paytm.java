package com.vch.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Paytm implements Serializable {

    @SerializedName("foo")
    @Expose
    private String foo;
    @SerializedName("bar")
    @Expose
    private Integer bar;
    @SerializedName("baz")
    @Expose
    private Boolean baz;
    private final static long serialVersionUID = -1775509128031814981L;

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public Integer getBar() {
        return bar;
    }

    public void setBar(Integer bar) {
        this.bar = bar;
    }

    public Boolean getBaz() {
        return baz;
    }

    public void setBaz(Boolean baz) {
        this.baz = baz;
    }
}