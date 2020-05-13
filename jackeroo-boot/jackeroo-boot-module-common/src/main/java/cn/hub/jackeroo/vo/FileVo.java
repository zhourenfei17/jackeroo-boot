package cn.hub.jackeroo.vo;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Description
 * @Date 2018/3/15
 * @Author
 */
public class FileVo {

	private MultipartFile file; // 上传的文件

	private File localFile; // 即将保存的本地文件

	private String fileName; // 文件名

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public File getLocalFile() {
		return localFile;
	}

	public void setLocalFile(File localFile) {
		this.localFile = localFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
