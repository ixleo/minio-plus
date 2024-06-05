package org.liuxp.minioplus.api;

import cn.hutool.core.lang.Pair;
import org.liuxp.minioplus.api.model.dto.FileMetadataInfoDTO;
import org.liuxp.minioplus.api.model.dto.FileSaveDTO;
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
     * 根据文件key查询
     * @param key 文件key
     * @return 文件元数据信息
     */
    FileMetadataInfoVo one(String key);

    /**
     * 列表数据查询
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