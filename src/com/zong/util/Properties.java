package com.zong.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc 配置文件加载工具
 * @author zong
 * @date 2016年11月27日 上午2:09:30
 */
public class Properties {
	private Map data = new HashMap();

	public Properties(String dbname) {
		data.put(Config.PACKAGE_NAME, Config.configData.get(Config.PACKAGE_NAME));
		data.put(Config.PACKAGE_BEAN, Config.configData.get(Config.PACKAGE_BEAN));
		data.put(Config.PACKAGE_MAPPER, Config.configData.get(Config.PACKAGE_MAPPER));
		data.put("jdbc.url", Config.configData.get("jdbc.url"));
		data.put("jdbc.username", Config.configData.get("jdbc.username"));
		data.put("jdbc.password", Config.configData.get("jdbc.password"));
		data.put("jdbc.driverClassName", Config.configData.get("jdbc.driverClassName"));
	}

	public String getProperty(String key) {
		return (String) data.get(key);
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

}
