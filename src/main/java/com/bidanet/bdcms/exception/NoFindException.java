package com.bidanet.bdcms.exception;

/**
 * 没有查找到 数据异常
 */
public class NoFindException extends DaoException {
    public NoFindException(String message) {
        super(message);
    }
}
