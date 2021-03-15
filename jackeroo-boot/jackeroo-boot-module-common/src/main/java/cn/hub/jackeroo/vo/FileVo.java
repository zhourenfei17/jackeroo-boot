package cn.hub.jackeroo.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

/**
 * @Description
 * @Date 2018/3/15
 * @Author
 */
@Data
public class FileVo {

	private MultipartFile file; // 上传的文件

	private Path localPath; // 即将保存的本地文件

	private String fileName; // 文件名

}
