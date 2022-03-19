package com.abc.cx.upload;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 本地上传文件服务
 *
 */
@Service
@Slf4j
public class LocalUploadServiceImpl implements ICommonUploadService {

    private LocalUploadConfig uploadConfig;

    @Autowired
    public void setUploadConfig(LocalUploadConfig uploadConfig) {
        this.uploadConfig = uploadConfig;
    }

    /**
     * 上传图片
     *
     * @param uploadFile 图片
     * @return 上传结果
     */
    @Override
    public UploadResult uploadImage(MultipartFile uploadFile) {
        try {
            String fileType = UploadFileUtil.getFileExt(uploadFile);
            if (!IMAGE_FORMAT_ACCEPT_ARRAY.contains(fileType.trim().toLowerCase())) {
                return UploadResult.fail(FILE_FORMAT_DANGER);
            }
            String uploadRelativePath = getUploadPath("images");
            return doUpload(uploadFile, uploadRelativePath);
        } catch (IOException e) {
            e.printStackTrace();
            return UploadResult.fail("上传图片失败");
        }
    }

    /**
     * 上传文件
     *
     * @param uploadFile 文件
     * @return 上传结果
     */
    @Override
    public UploadResult uploadFile(MultipartFile uploadFile) {
        try {
            String fileType = FileUtil.extName(uploadFile.getOriginalFilename());
            if (!CHALLENGE_FORMAT_ACCEPT_ARRAY.contains(fileType.trim().toLowerCase())) {
                return UploadResult.fail(FILE_FORMAT_DANGER);
            }

            String uploadRelativePath = getUploadPath("document");
            return doUpload(uploadFile, uploadRelativePath);
        } catch (IOException e) {
            e.printStackTrace();
            return UploadResult.fail("上传文件失败");
        }
    }

    /**
     * 上传视频
     *
     * @param uploadFile 视频文件
     * @return 上传结果
     */
    @Override
    public UploadResult uploadVideo(MultipartFile uploadFile) {
        try {
            String fileType = UploadFileUtil.getFileExt(uploadFile);
            if (!VIDEO_FORMAT_ACCEPT_ARRAY.contains(fileType.trim().toLowerCase())) {
                return UploadResult.fail(FILE_FORMAT_DANGER);
            }

            String uploadRelativePath = getUploadPath("video");
            return doUpload(uploadFile, uploadRelativePath);
        } catch (IOException e) {
            e.printStackTrace();
            return UploadResult.fail("上传视频失败");
        }
    }


    private UploadResult doUpload(MultipartFile uploadFile, String uploadRelativePath) throws IOException {
        String saveName = UploadFileUtil.generateUploadFileName(uploadFile);
        String savePath = uploadConfig.getBasePath() + uploadRelativePath + saveName;
        String saveUrl = uploadConfig.getBaseUrl() + uploadRelativePath + saveName;
        String md5 = UploadFileUtil.getMd5(uploadFile);
        File savedFile = FileUtil.file(savePath);
        FileUtil.writeFromStream(uploadFile.getInputStream(), savedFile);
        return UploadResult.ok(saveName, savePath, saveUrl, md5);
    }

    private String getUploadPath(String subDirectory) {
        DateTime now = DateUtil.date();
        return subDirectory + "/" + DateUtil.year(now) + "/" + (DateUtil.month(now) + 1) + "/" + DateUtil.dayOfMonth(now) + "/";
    }

}
