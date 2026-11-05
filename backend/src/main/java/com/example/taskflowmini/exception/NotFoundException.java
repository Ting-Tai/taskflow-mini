package com.example.taskflowmini.exception;

/**
 * 资源不存在。全局异常处理会把它翻译成 HTTP 404 + {"code":404,...}。
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
