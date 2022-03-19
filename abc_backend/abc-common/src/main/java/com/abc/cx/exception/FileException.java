package com.abc.cx.exception;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:11 2021/11/6
 **/
public class FileException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    public FileException() {
        super();
    }

    public FileException(String message) {
        super(message);
    }

}