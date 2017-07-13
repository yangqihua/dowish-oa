package net.dowish.common.utils;

/**
 * Created by yangqihua on 2017/7/13.
 */
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @description 得到当前应用的系统路径
 */
public class SystemPathUtils {

	public static String getSysPath() {
		String path = Thread.currentThread().getContextClassLoader()
							.getResource("").toString();
		String temp = path.replaceFirst("file:/", "").replaceFirst(
				"WEB-INF/classes/", "");
		String separator = System.getProperty("file.separator");
		String resultPath = temp.replaceAll("/", separator + separator);
		return resultPath;
	}

	public static String getClassPath() {
		String path = Thread.currentThread().getContextClassLoader()
							.getResource("").toString();
		String temp = path.replaceFirst("file:/", "");
		String separator = System.getProperty("file.separator");
		String resultPath = temp.replaceAll("/", separator + separator);
		return resultPath;
	}

	public static String getSystempPath() {
		return System.getProperty("java.io.tmpdir");
	}

	public static String getSeparator() {
		return System.getProperty("file.separator");
	}

	public static String getServerProject() {
		String mainPath = "";
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null) {
				while (true) {
					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
					if (f.exists()) {
						break;
					}
					if (file.getParentFile() != null) {
						file = file.getParentFile();
					} else {
						break;
					}
				}
				mainPath = file.getAbsolutePath()+System.getProperty("file.separator");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mainPath;
	}

	public static String getServerMainDir(){
		return getServerProject()+ "src" + File.separator;
	}

	public static String getProjectPath() {
		String projectPath = "";
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null) {
				File projectFile = file.getParentFile().getParentFile().getParentFile();
				if(null==projectFile){
					throw new FileNotFoundException("找不到项目根目录啊啊啊啊");
				}
				projectPath = projectFile.getAbsolutePath()+System.getProperty("file.separator");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
	}

	public static void main(String[] args) {
		System.out.println(getSysPath());
		System.out.println(getSystempPath());
		System.out.println(getSeparator());
		System.out.println(getClassPath());
		System.out.println(getServerProject());
		System.out.println(getProjectPath());
	}
}

