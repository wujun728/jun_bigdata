package com.shujia.common;

public class Result<T> {
    private String code;
    private String msg;
    private T data;

    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 请求成功 不返回数据
    public static <T> Result<T> success() {
        Result rs = new Result<>();
        rs.setCode("200");
        rs.setMsg("ok");
        return rs;
    }

    // 请求成功 返回数据
    public static <T> Result<T> success(T data) {
        Result<T> rs = new Result<T>(data);
        rs.setCode("200");
        rs.setMsg("ok");
        return rs;
    }

    // 请求失败
    public static <T> Result<T> error(String code, String msg) {
        Result rs = new Result<>();
        rs.setCode(code);
        rs.setMsg(msg);
        return rs;
    }
}
