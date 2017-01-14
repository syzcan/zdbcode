package com.zong.service;

import com.zong.bean.TableEntity;
import com.zong.dao.IJdbcDao;
import com.zong.dao.MysqlCodeDao;
import com.zong.dao.OracleCodeDao;
import com.zong.util.Properties;

public class JdbcCodeService {
	private static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	private static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";

	private IJdbcDao makeJdbcDao(String dbname) {
		Properties props = new Properties(dbname);
		String driverClassName = props.getProperty("jdbc.driverClassName");
		if (driverClassName.equals(DRIVER_MYSQL)) {
			return new MysqlCodeDao(props);
		} else if (driverClassName.equals(DRIVER_ORACLE)) {
			return new OracleCodeDao(props);
		}
		return null;
	}

	public TableEntity showTable(String dbname, String tableName) {
		IJdbcDao jdbcDao = makeJdbcDao(dbname);
		TableEntity tableEntity = jdbcDao.showTable(tableName);
		tableEntity.setColumnFields(jdbcDao.showTableColumns(tableName));
		return tableEntity;
	}

}
