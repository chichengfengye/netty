package gateway.common;

public class ReturnResult {
    private Integer code;
    private String msg;
    private Object data;

    public ReturnResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static boolean isSuccess(ReturnResult returnResult) {
        return returnResult.getCode().equals(200);
    }

    public static ReturnResult success() {
        return new ReturnResult(200,"success");
    }

    public static ReturnResult exception() {
        return new ReturnResult(500,"exception");
    }

    public static ReturnResult failure(Integer code, String msg) {
        return new ReturnResult(code,msg);
    }

    public static ReturnResult failure(Integer code, String msg, Object data) {
        return new ReturnResult(code,msg, data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
