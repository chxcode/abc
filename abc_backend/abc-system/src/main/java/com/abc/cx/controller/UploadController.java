package com.abc.cx.controller;

import com.abc.cx.service.impl.UploadServiceImpl;
import com.abc.cx.vo.Ret;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ChangXuan
 * @Decription: 上传管理
 * @Date: 11:20 2021/11/7
 **/
@Api(tags = "上传管理")
@RestController
@Slf4j
@RequestMapping(value = "/api/upload")
public class UploadController {

    UploadServiceImpl uploadService;

    @Autowired
    public void setUploadServiceImpl(UploadServiceImpl uploadService) {
        this.uploadService = uploadService;
    }

    @ApiOperation("上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "图片文件", required = true, dataType = "File", paramType = "form")
    })
    @PostMapping(value = "/image")
    public Ret uploadImage(@RequestParam MultipartFile file) {
        return uploadService.uploadImage(file);
    }

    @ApiOperation("上传Markdown图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "图片文件", required = true, dataType = "File", paramType = "form")
    })
    @PostMapping(value = "/image/markdown")
    public Map uploadMarkdownImage(@RequestParam("editormd-image-file") MultipartFile file) {
        Ret result = uploadService.uploadImage(file);
        Map res = new HashMap();
        if (result.isOk()){
            res.put("success", 1);
            res.put("message","上传成功");
            res.put("url", "/api/download/image?url="+result.get("url"));
        }else {
            res.put("success", 0);
            res.put("message",result.get("msg"));
        }
        return res;
    }


    @ApiOperation("上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "上传文件", required = true, dataType = "File", paramType = "form")
    })
    @PostMapping(value = "/file")
    public Ret uploadFile(@RequestParam MultipartFile file) {
        return uploadService.uploadFile(file);
    }

    @ApiOperation("上传视频")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "视频文件", required = true, dataType = "File", paramType = "form")
    })
    @PostMapping(value = "/video")
    public Ret uploadVideo(@RequestParam MultipartFile file) {
        return uploadService.uploadVideo(file);
    }

}
