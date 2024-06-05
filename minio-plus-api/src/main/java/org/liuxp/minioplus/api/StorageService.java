package org.liuxp.minioplus.api;

import cn.hutool.core.lang.Pair;
import org.liuxp.minioplus.api.model.dto.FileCheckDTO;
import org.liuxp.minioplus.api.model.dto.FileMetadataInfoDTO;
import org.liuxp.minioplus.api.model.dto.FileSaveDTO;
import org.liuxp.minioplus.api.model.vo.CompleteResultVo;
import org.liuxp.minioplus.api.model.vo.FileCheckResultVo;
import org.liuxp.minioplus.api.model.vo.FileMetadataInfoVo;

import java.io.InputStream;
import java.util.List;

/**
 * MinIO Plus 接口定义
 * @author contact@liuxp.me
 * @since  2024/06/05
 */
public interface StorageService {

    /**
     * 上传任务初始化
     *
     * @param dto dto
     * @param userId  用户编号
     * @return {@link FileCheckResultVo}
     */
    FileCheckResultVo init(FileCheckDTO dto, String userId);


    /**
     * 合并已分块的文件
     *
     * @param fileKey 文件关键
     * @param partMd5List 文件分块md5列表
     * @param userId  用户编号
     *
     * @return {@link CompleteResultVo}
     */
    CompleteResultVo complete(String fileKey, List<String> partMd5List,String userId);

    /**
     * 上传图片
     * @param fileKey 文件KEY
     * @param file 文件
     * @return 是否成功
     */
    Boolean uploadImage(String fileKey, byte[] file);

    /**
     * 取得文件下载地址
     *
     * @param fileKey 文件KEY
     * @param userId  用户编号
     * @return 地址
     */
    String download(String fileKey, String userId);

    /**
     * 取得原图地址
     *
     * @param fileKey 文件KEY
     * @param userId  用户编号
     * @return 地址
     */
    String image(String fileKey, String userId);

    /**
     * 取得缩略图地址
     *
     * @param fileKey 文件KEY
     * @param userId  用户编号
     * @return 地址
     */
    String preview(String fileKey, String userId);

    /**
     * 查询元数据信息
     * @param key 文件key
     * @return 文件元数据信息
     */
    FileMetadataInfoVo one(String key);

    /**
     * 查询元数据信息
     * @param fileMetadataInfo 查询入参
     * @return 文件元数据信息集合
     */
    List<FileMetadataInfoVo> list(FileMetadataInfoDTO fileMetadataInfo);

    /**
     * 创建文件
     * 尽量不要用本方法处理大文件，大文件建议使用前端直传
     * @param fileSaveDTO 文件保存入参
     * @param fileBytes 文件字节流
     * @return 文件元数据信息
     */
    FileMetadataInfoVo createFile(FileSaveDTO fileSaveDTO, byte[] fileBytes);

    /**
     * 创建文件
     * 尽量不要用本方法处理大文件，大文件建议使用前端直传
     * @param fileSaveDTO 文件保存入参
     * @param inputStream 文件输入字节流
     * @return 文件元数据信息
     */
    FileMetadataInfoVo createFile(FileSaveDTO fileSaveDTO, InputStream inputStream);

    /**
     * 创建文件
     * 尽量不要用本方法处理大文件，大文件建议使用前端直传
     * @param fileSaveDTO 文件保存入参
     * @param url 文件地址
     * @return 文件元数据信息
     */
    FileMetadataInfoVo createFile(FileSaveDTO fileSaveDTO, String url);

    /**
     * 根据文件key读取文件字节流
     * @param fileKey 文件key
     * @return 文件字节流
     */
    Pair<FileMetadataInfoVo,byte[]> read(String fileKey);

    /**
     * 根据文件key删除文件
     * @param fileKey 文件key
     * @return 是否成功
     */
    Boolean remove(String fileKey);

}