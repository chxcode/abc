package com.abc.cx.upload;

import lombok.Getter;


public class UploadResult {
    private final boolean state;
    @Getter
    private String failMessage;
    @Getter
    private String name;
    @Getter
    private String path;
    @Getter
    private String url;
    @Getter
    private String md5;

    public static UploadResult fail(String failMessage) {
        return new UploadResult(failMessage);
    }

    private UploadResult(String failMessage) {
        this.failMessage = failMessage;
        this.state = false;
    }

    public static UploadResult ok(String name, String path, String url) {
        return new UploadResult(name, path, url);
    }

    public static UploadResult ok(String name, String path, String url, String md5) {
        return new UploadResult(name, path, url, md5);
    }

    private UploadResult(String name, String path, String url) {
        this.name = name;
        this.path = path;
        this.url = url;
        this.state = true;
    }
    private UploadResult(String name, String path, String url, String md5) {
        this.name = name;
        this.path = path;
        this.url = url;
        this.state = true;
        this.md5 = md5;
    }

    public boolean isOk() {
        return this.state;
    }
}
