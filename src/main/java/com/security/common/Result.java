package com.security.common;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private Integer resultCode;
    private String resultMsg;
    private T data;

    public Result(ResultEnum resultEnum) {
        this.resultCode = resultEnum.getCode();
        this.resultMsg = resultEnum.getMsg();
    }

    @SuppressWarnings("unchecked")
    public Result(ResultEnum resultEnum, String resultMsg, T data) {
        this.resultCode = resultEnum.getCode();
        this.resultMsg = resultEnum.getMsg();
        this.data = data;
        if (!StringUtils.isEmpty(resultMsg)) {
            this.resultMsg = resultMsg;
        }
    }

    public static Result<Object> success(String resultMsg, Object data) {
        Result<Object> resultData = new Result<Object>(ResultEnum.SUCCESS);
        resultData.setResultMsg(resultMsg);
        resultData.setData(data);
        return resultData;
    }

    public static Result<Object> fail(String resultMsg) {
        Result<Object> resultData = new Result<Object>(ResultEnum.FAILED);
        resultData.setResultMsg(resultMsg);
        return resultData;
    }
}