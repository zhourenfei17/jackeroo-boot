package cn.hub.jackeroo.utils;

import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.IOException;

public class Global {

	/**
	 * 获取工程路径
	 * 
	 * @return
	 */
	public static String getProjectPath() {
		// 如果配置了工程路径，则直接返回，否则自动获取。
		/*
		 * String projectPath = Global.getConfig("projectPath"); if
		 * (StringUtils.isNotBlank(projectPath)){ return projectPath; }
		 */
		String projectPath = null;
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null) {
				while (true) {
					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
					if (f == null || f.exists()) {
						break;
					}
					if (file.getParentFile() != null) {
						file = file.getParentFile();
					} else {
						break;
					}
				}
				projectPath = file.toString();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
	}
}
