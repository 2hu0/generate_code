package com.model;

import java.sql.ResultSet;

/**
 * @author 2hu0
 */
public class ResponseBean {
    private Integer status;
    private String message;
    private Object obj;

    public static ResponseBean ok(String msg,Object obj) {
        return new ResponseBean(200,msg,obj);
    }
    public static ResponseBean ok(String msg) {
        return new ResponseBean(200,msg,null);
    }
    public static ResponseBean error(String msg) {
        return new ResponseBean(500,msg,null);
    }
    public static ResponseBean error(String msg,Object obj) {
        return new ResponseBean(500,msg,obj);
    }

    private ResponseBean(Integer status, String message, Object obj) {
        this.status = status;
        this.message = message;
        this.obj = obj;
    }
}
