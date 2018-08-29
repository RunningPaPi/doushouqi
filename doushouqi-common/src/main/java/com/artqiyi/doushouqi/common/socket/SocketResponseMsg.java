package com.artqiyi.doushouqi.common.socket;

/**
 * socket服务端响应信息载体
 */
public class SocketResponseMsg {
    private String code;      //操作
    private String msg;           //响应提示信息
    private Object data;          //响应数据
    private boolean isSuccess;  //是否成功

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
