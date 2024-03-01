package com.wcwy.common.web.exception;


import com.wcwy.common.base.result.IResultCode;
import lombok.Getter;

/**
 * @author： 乐哥聊编程(全平台同号)
 */
@Getter
public class BizException extends RuntimeException {

    public IResultCode resultCode;

    public BizException(IResultCode errorCode) {
        super(errorCode.getMsg());
        this.resultCode = errorCode;
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }
}
