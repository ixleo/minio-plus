package org.liuxp.minioplus.core.engine;

import cn.hutool.core.lang.Pair;
import org.liuxp.minioplus.model.dto.FileCheckDTO;
import org.liuxp.minioplus.model.dto.FileMetadataInfoSaveDTO;
import org.liuxp.minioplus.model.vo.CompleteResultVo;
import org.liuxp.minioplus.model.vo.FileCheckResultVo;
import org.liuxp.minioplus.model.vo.FileMetadataInfoVo;

import java.util.List;

/**
 * 存储引擎Service接口定义
 *
 * @author contact@liuxp.me
 * @since 2023/06/26
 */
public interface StorageEngineService {

    /**
     * 上传任务初始化
     *
     * @param dto dto
     * @param userId  用户编号
     * @return {@link FileCheckResultVo}
     */
    FileCheckResultVo init(FileCheckDTO dto,String userId);


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
     * 写入文件流
     * @param saveDTO 文件元数据信息保存入参
     * @param fileBytes 文件流
     * @return 是否成功
     */
    Boolean createFile(FileMetadataInfoSaveDTO saveDTO, byte[] fileBytes);

    /**
     * 读取文件流
     * @param fileKey 文件KEY
     * @return 文件流
     */
    Pair<FileMetadataInfoVo,byte[]> read(String fileKey);

    /**
     * 删除文件
     * @param fileKey 文件KEY
     * @return 是否成功
     */
    Boolean remove(String fileKey);

    /**
     * 删除文件
     * @param fileKey 文件KEY
     * @param userId  用户编号
     * @return 是否成功
     */
    Boolean remove(String fileKey, String userId);

}
