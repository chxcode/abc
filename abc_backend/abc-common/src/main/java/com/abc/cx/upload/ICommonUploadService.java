package com.abc.cx.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 文件上传服务接口
 *
 */
public interface ICommonUploadService {

    /**
     * 上传允许的文件类型
     */
    List<String> CHALLENGE_FORMAT_ACCEPT_ARRAY =
            Arrays.asList("jpg", "jpeg", "bmp", "png", "gif","txt","rar","zip","doc","docx","ini","conf","eml","xlsx","xls","pdf");

    /**
     * 不安全文件提示信息
     */
    String FILE_FORMAT_DANGER = "文件格式不允许上传";

    /**
     * 图片类型
     */
    List<String> IMAGE_FORMAT_ACCEPT_ARRAY = Arrays.asList("jpg", "jpeg", "bmp", "png", "gif");

    /**
     * 文件类型
     */
    List<String> FILE_FORMAT_ACCEPT_ARRAY = Arrays.asList("doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "txt");

    /**
     * 视频类型
     */
    List<String> VIDEO_FORMAT_ACCEPT_ARRAY = Arrays
            .asList("mp4", "mpeg");


    /**
     * 上传图片
     *
     * @param uploadFile 图片
     * @return 上传结果
     * @throws IOException 异常
     */
    UploadResult uploadImage(MultipartFile uploadFile) throws IOException;

    /**
     * 上传文件
     *
     * @param uploadFile 文件
     * @return 上传结果
     * @throws IOException 异常
     */
    UploadResult uploadFile(MultipartFile uploadFile) throws IOException;

    /**
     * 上传视频
     *
     * @param uploadFile 视频
     * @return 上传结果
     * @throws IOException 异常
     */
    UploadResult uploadVideo(MultipartFile uploadFile) throws IOException;

}
