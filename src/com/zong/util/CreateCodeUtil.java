package com.zong.util;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zong.bean.TableEntity;
import com.zong.service.JdbcCodeService;

/**
 * @desc 代码生成类， ftl文件按关键字归类： bean，controller，mapperjava，mapperxml， service，jsp_
 * @author zong
 * @date 2016年12月2日 上午1:07:55
 */
public class CreateCodeUtil {
	private final static Logger LOGGER = Logger.getLogger(CreateCodeUtil.class);

	private static Map<String, Object> loadConfig(String dbname, TableEntity tableEntity, String objectName,
			String className, String packageName) {
		Properties props = new Properties(dbname);
		if (packageName == null || packageName.trim().equals("")) {
			packageName = props.getProperty("packageName");
		} else {
			packageName = packageName.trim();
		}
		String packageBean = props.getProperty("packageBean");
		String packageMapper = props.getProperty("packageMapper");

		tableEntity.setPackageBean(packageName + "." + packageBean);
		tableEntity.setPackageMapper(packageName + "." + packageMapper);

		tableEntity.setObjectName(objectName);
		tableEntity.setClassName(className);

		Map<String, Object> root = new HashMap<String, Object>(); // 创建数据模型
		root.put("tableEntity", tableEntity);
		root.put("columnFields", tableEntity.getColumnFields());
		root.put("className", tableEntity.getClassName()); // 类名
		root.put("objectName", tableEntity.getObjectName()); // 实体名
		root.put("packageBean", tableEntity.getPackageBean()); // 包名
		root.put("packageMapper", tableEntity.getPackageMapper());
		root.put("nowDate", new Date()); // 当前日期
		return root;
	}

	/**
	 * 生成代码
	 * 
	 * @param tableEntity
	 * @param type
	 *            bean、mapperJava、mapperXml 表结构模型
	 */
	public static String createCode(String dbname, TableEntity tableEntity, String type, String objectName,
			String className, String packageName) {
		String result = "";
		try {
			Map<String, Object> root = loadConfig(dbname, tableEntity, objectName, className, packageName); // 创建数据模型

			String ftlPath = FileUtils.getClassResources() + "ftl";
			System.out.println("======生成" + tableEntity.getTableName() + "代码start======");
			result = Freemarker.printString(type + ".ftl", ftlPath, root);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.toString();
		}
		return result;
	}

	public static String downCode(String dbname, TableEntity tableEntity, String objectName, String className,
			String packageName) {
		String result = "";
		try {
			Map<String, Object> root = loadConfig(dbname, tableEntity, objectName, className, packageName); // 创建数据模型

			String parentPath = FileUtils.getClassResources();
			String codePath = parentPath + "code";// 生成代码存放路径
			String ftlPath = FileUtils.getClassResources() + "ftl/";
			// 先删除原来的
			FileUtils.removeAllFile(codePath);
			// 获取所有模板
			List<File> ftls = FileUtils.listFile(ftlPath, "ftl");
			for (File ftl : ftls) {
				// 根据模板名称归类分别处理
				String ftlName = ftl.getName().toLowerCase();
				String fileName = "";
				String filePath = "";
				if (ftlName.indexOf("bean") > -1) {
					// 生成实体bean
					fileName = tableEntity.getClassName() + ".java";
					filePath = tableEntity.getPackageBeanPath();
				} else if (ftlName.indexOf("mapperjava") > -1) {
					// 生成mapper.java
					fileName = tableEntity.getClassName() + "Mapper.java";
					filePath = tableEntity.getPackageMapperPath();
				} else if (ftlName.indexOf("mapperxml") > -1) {
					// 生成mapper.xml
					fileName = tableEntity.getClassName() + "Mapper.xml";
					filePath = tableEntity.getPackageMapperPath();
				}
				if (!fileName.equals("")) {
					Freemarker.printFile(ftl.getName(), ftlPath, root, codePath + "/" + filePath + fileName);
					LOGGER.info("create：" + codePath + "/" + filePath + fileName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = e.toString();
		}
		return result;
	}

	public static void main(String[] args) {
		try {
			LOGGER.info("读取配置文件");
			Config.readConfig();
			LOGGER.info(Config.configData.toString());
			JdbcCodeService codeService = new JdbcCodeService();
			LOGGER.info("查询表结构：" + Config.configData.get("tableName").toString());
			TableEntity tableEntity = codeService.showTable("", Config.configData.get("tableName").toString());
			LOGGER.info("生成文件start");
			downCode("", tableEntity, "",
					Config.configData.get("className") == null ? "" : Config.configData.get("className").toString(),
					Config.configData.get("packageName").toString());
			LOGGER.info("生成文件end");
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
}
