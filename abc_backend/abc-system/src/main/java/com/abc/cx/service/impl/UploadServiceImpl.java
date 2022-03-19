package com.abc.cx.service.impl;

import com.abc.cx.service.IUploadService;
import com.abc.cx.upload.LocalUploadServiceImpl;
import com.abc.cx.upload.UploadResult;
import com.abc.cx.vo.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:48 2021/11/6
 **/
@Service
public class UploadServiceImpl implements IUploadService {


    // 本地存储服务
    @Autowired
    private LocalUploadServiceImpl uploadService;

    /**
     * 上传图片
     *
     * @param uploadFile 图片
     * @return 上传结果
     */
    @Override
    public Ret uploadImage(MultipartFile uploadFile) {
        UploadResult uploadResult = uploadService.uploadImage(uploadFile);
        if (uploadResult.isOk()) {
            return Ret.ok("url", uploadResult.getUrl());
        }
        return Ret.failMsg(uploadResult.getFailMessage());
    }

    /**
     * 上传文件
     *
     * @param uploadFile 文件
     * @return 上传结果
     */
    @Override
    public Ret uploadFile(MultipartFile uploadFile) {
        UploadResult uploadResult = uploadService.uploadFile(uploadFile);
        if (uploadResult.isOk()) {
            return Ret.ok("url", uploadResult.getUrl()).set("md5",uploadResult.getMd5()).set("name",uploadFile.getOriginalFilename());
        }
        return Ret.failMsg(uploadResult.getFailMessage());
    }

    /**
     * 上传视频
     *
     * @param uploadFile 视频文件
     * @return 上传结果
     */
    @Override
    public Ret uploadVideo(MultipartFile uploadFile) {
        UploadResult uploadResult = uploadService.uploadVideo(uploadFile);
        if (uploadResult.isOk()) {
            return Ret.ok("url", uploadResult.getUrl());
        }
        return Ret.failMsg(uploadResult.getFailMessage());
    }

}
