package com.zong.dao;

import java.util.List;

import com.zong.bean.ColumnField;
import com.zong.bean.TableEntity;

public interface IJdbcDao {
	/**
	 * 获取当前用户【数据库】所有表名和描述
	 */
	public List<TableEntity> showTables();

	/**
	 * 获取某个表字段
	 */
	public List<ColumnField> showTableColumns(String tableName);

	public TableEntity showTable(String tableName);
}
