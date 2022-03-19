package com.abc.cx.upload;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @Author: ChangXuan
 * @Decription: 上传文件工具类
 * @Date: 9:34 2020/6/4
 **/
@Slf4j
public class UploadFileUtil {
    /**
     * 生成上传文件新名称
     *
     * @param uploadFile 上传文件
     * @return 新名称
     */
    public static String generateUploadFileName(MultipartFile uploadFile) {
        String originFileName = uploadFile.getOriginalFilename();
        String fileExt = FileUtil.extName(originFileName);
        return StrUtil.uuid() + "." + fileExt;
    }

    public static String getFileExt(MultipartFile uploadFile) throws IOException {
        String fileType = FileTypeUtil.getType(uploadFile.getInputStream());
        if (StrUtil.isNotBlank(fileType)) {
            return fileType;
        }
        return FileUtil.extName(uploadFile.getOriginalFilename());
    }

    public static String getMd5(MultipartFile file) {
        try {
            byte[] uploadBytes = file.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            String hashString = new BigInteger(1, digest).toString(16);
            return hashString;
        } catch (Exception e) {
            log.error("计算文件md5出错",e);
        }
        return null;
    }

    /**
     * InputStream 转换成byte[]
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

}
