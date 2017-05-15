package com.bidanet.bdcms.exception;

/**
 * 登陆失败，错误异常
 */
public class LoginErrorException extends BaseException {
    public LoginErrorException(String message) {
        super(message);
    }
}
