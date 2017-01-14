package com.zong.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Freemarker 模版引擎类
 */
public class Freemarker {
	/**
	 * 打印到控制台(测试用)
	 * 
	 * @param ftlName
	 */
	public static void print(String ftlName, Map<String, Object> root, String ftlPath) throws Exception {
		try {
			Template temp = getTemplate(ftlName, ftlPath); // 通过Template可以将模板文件输出到相应的流
			temp.process(root, new PrintWriter(System.out));
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出到输出到文件
	 * 
	 * @param ftlName
	 *            ftl文件名
	 * @param ftlPath
	 *            模板所在文件夹全路径
	 * @param root
	 *            传入的map
	 * @param outFile
	 *            输出后的文件全部路径
	 * @param filePath
	 *            输出前的文件上部路径
	 */
	public static void printFile(String ftlName, String ftlPath, Map<String, Object> root, String outFile, String filePath) throws Exception {
		try {
			Template template = getTemplate(ftlName, ftlPath);
			// 没有模板直接返回
			if (template == null) {
				return;
			}
			File file = new File(getClassResources() + "/" + filePath + "/" + outFile);
			if (!file.getParentFile().exists()) { // 判断有没有父路径，就是判断文件整个路径是否存在
				file.getParentFile().mkdirs(); // 不存在就全部创建
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			// 如果template不存在才执行渲染，防止空指针异常影响其他操作
			template.process(root, out); // 模版输出
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出模板渲染后的字符串
	 *
	 * @param ftlName
	 * @param ftlPath
	 * @param root
	 * @return
	 * @throws Exception
	 */
	public static String printString(String ftlName, String ftlPath, Map<String, Object> root) throws Exception {
		String result = "";
		try {
			Template template = getTemplate(ftlName, ftlPath);
			// 没有模板直接返回
			if (template == null) {
				result = "没有找到模板：" + ftlPath + "==>" + ftlName;
				return result;
			}
			StringWriter out = new StringWriter();
			template.process(root, out); // 模版输出到字符
			result = out.toString();
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param ftlName
	 *            模板名称
	 * @param ftlPath
	 *            模板所在文件夹全路径
	 * @param root
	 *            传入的map
	 * @param outFile
	 *            输出文件全路径
	 */
	public static void printFile(String ftlName, String ftlPath, Map<String, Object> root, String outFile) throws Exception {
		try {
			File file = new File(outFile);
			if (!file.getParentFile().exists()) { // 判断有没有父路径，就是判断文件整个路径是否存在
				file.getParentFile().mkdirs(); // 不存在就全部创建
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			Template template = getTemplate(ftlName, ftlPath);
			template.process(root, out); // 模版输出
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过文件名加载模版
	 * @param ftlName 模板名称bean.ftl
	 * @param ftlPath 模板所在文件夹全路径
	 * @return
	 * @throws Exception
	 */
	public static Template getTemplate(String ftlName, String ftlPath) throws Exception {
		try {
			File file = new File(ftlPath + "/" + ftlName);
			// 不存在文件直接返回空对象，防止报空指针异常
			if (!file.exists()) {
				return null;
			}
			Configuration cfg = new Configuration(); // 通过Freemaker的Configuration读取相应的ftl
			cfg.setEncoding(Locale.CHINA, "utf-8");
			cfg.setDirectoryForTemplateLoading(new File(ftlPath)); // 设定去哪里读取相应的ftl模板文件
			Template temp = cfg.getTemplate(ftlName); // 在模板文件目录中找到名称为name的文件
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getClassResources() {
		// String path =
		// String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")).replace("file:/",
		// "");
		String path = new File(Freemarker.class.getResource("/").getPath()).toString();
		return path;
	}

	public static void main(String[] args) {
		System.out.println(getClassResources());
		System.out.println(Freemarker.class.getResource("/").getPath());
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
		// 第一种：获取类加载的根路径 D:\git\daotie\daotie\target\classes
		File f = new File(Freemarker.class.getResource("/").getPath());
		System.out.println(f);
		// 获取当前类的所在工程路径; 如果不加“/” 获取当前类的加载目录
		// D:\git\daotie\daotie\target\classes\my
		File f2 = new File(Freemarker.class.getResource("").getPath());
		System.out.println(f2);
		// 第二种：获取项目路径 D:\git\daotie\daotie
		File directory = new File("");// 参数为空
		String courseFile;
		try {
			courseFile = directory.getCanonicalPath();
			System.out.println(courseFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 第三种： file:/D:/git/daotie/daotie/target/classes/
		URL xmlpath = Freemarker.class.getClassLoader().getResource("");
		System.out.println(xmlpath);
		// 第四种： D:\git\daotie\daotie
		System.out.println(System.getProperty("user.dir"));
	}
}
