package com.abc.cx.service;

import com.abc.cx.vo.Ret;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:45 2021/11/6
 **/
public interface IUploadService {

    /**
     * 上传图片
     *
     * @param uploadFile 图片
     * @return 上传结果
     * @throws IOException 异常
     */
    Ret uploadImage(MultipartFile uploadFile) throws IOException;

    /**
     * 上传文件
     *
     * @param uploadFile 文件
     * @return 上传结果
     * @throws IOException 异常
     */
    Ret uploadFile(MultipartFile uploadFile) throws IOException;

    /**
     * 上传视频
     *
     * @param uploadFile 视频
     * @return 上传结果
     * @throws IOException 异常
     */
    Ret uploadVideo(MultipartFile uploadFile) throws IOException;

}
