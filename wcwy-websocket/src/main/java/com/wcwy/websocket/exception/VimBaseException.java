package com.wcwy.websocket.exception;

/**
 * vim 自定义类
 * @author 乐天kp
 */
public class VimBaseException extends BaseException {

    private static final long serialVersionUID = 1L;

    public VimBaseException(String code, Object[] args) {
        super("V-IM", code, args, null);
    }
}
