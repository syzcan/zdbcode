package com.zong.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zong.bean.ColumnField;
import com.zong.bean.TableEntity;
import com.zong.util.Properties;

/**
 * @desc
 * @author zong
 * @date 2016年3月23日
 */
public class OracleCodeDao extends BaseJdbcDao implements IJdbcDao {

	public OracleCodeDao(Properties props) {
		super(props);
	}

	public OracleCodeDao() {

	}

	/**
	 * 查询某个表的所有字段
	 */
	public List<ColumnField> showTableColumns(String tableName) {
		conn = getConnection(); // 同样先要获取连接，即连接到数据库.

		List<ColumnField> list = new ArrayList<ColumnField>();
		String result = "";
		try {
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量
			String sql = "select * from DBA_TAB_COLUMNS where Table_Name='" + tableName + "' and lower(OWNER)=lower('"+user+"') ORDER BY COLUMN_ID";
			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集

			StringBuffer sb = new StringBuffer();
			int i = 1;
			while (rs.next()) { // 判断是否还有下一个数据
				// 根据字段名获取相应的值
				String column = rs.getString("COLUMN_NAME");
				String columnType = rs.getString("DATA_TYPE");
				Integer dataLength = rs.getInt("DATA_LENGTH");
				Integer dataPrecision = rs.getInt("DATA_PRECISION");
				Integer dataScale = rs.getInt("DATA_SCALE");
				String defaultValue = rs.getString("DATA_DEFAULT");
				String canNull = rs.getString("NULLABLE").equals("Y") ? "YES" : "NO";
				ColumnField columnField = new ColumnField();
				columnField.setColumn(column);
				columnField.setField(transColumn(column));
				columnField.setColumnType(columnType);
				columnField.setDataLength(dataLength);
				columnField.setDataPrecision(dataPrecision);
				columnField.setDataScale(dataScale);
				columnField.setDefaultValue(defaultValue);
				columnField.setCanNull(canNull);
				// 设置jdbcType
				columnField.setType(columnType);
				list.add(columnField);
				if (i++ % 6 == 0) {
					sb.append("\r");
				}
				sb.append(column + " \"" + transColumn(column) + "\",");
			}
			// 查询字段备注
			sql = "select * from user_col_comments where Table_Name='" + tableName + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String column = rs.getString("COLUMN_NAME");
				String comment = rs.getString("COMMENTS");
				for (ColumnField columnField : list) {
					if (columnField.getColumn().equals(column)) {
						columnField.setRemark(comment);
					}
				}
			}
			// 查询表主键
			sql = "select cu.* from user_cons_columns cu, user_constraints au where cu.constraint_name = au.constraint_name and au.constraint_type = 'P' and au.table_name = '"
					+ tableName + "' order by position";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String column = rs.getString("COLUMN_NAME");
				for (ColumnField columnField : list) {
					if (columnField.getColumn().equals(column)) {
						columnField.setKey("PRI");
					}
				}
			}
			result = sb.toString();
			result = result.substring(0, result.length() - 1);
			// System.out.println(result);
			//conn.close(); // 关闭数据库连接
		} catch (SQLException e) {
			System.out.println("查询数据失败");
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取当前用户所有表名
	 */
	public List<TableEntity> showTables() {
		conn = getConnection(); // 同样先要获取连接，即连接到数据库.

		List<TableEntity> list = new ArrayList<TableEntity>();
		try {
			String sql = "select * from user_tab_comments ORDER BY table_name";
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量
			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集

			while (rs.next()) { // 判断是否还有下一个数据
				// 根据字段名获取相应的值
				String tableName = rs.getString("TABLE_NAME");
				String comment = rs.getString("COMMENTS");
				TableEntity tableEntity = new TableEntity();
				tableEntity.setTableName(tableName);
				tableEntity.setComment(comment);
				list.add(tableEntity);
			}
			sql = "select table_name,num_rows from user_tables ORDER BY num_rows desc";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				int numRows = rs.getInt("NUM_ROWS");
				for (TableEntity tableEntity : list) {
					if (tableEntity.getTableName().equals(tableName)) {
						tableEntity.setTotalResult(numRows);
					}
				}
			}
			//conn.close(); // 关闭数据库连接
		} catch (SQLException e) {
			System.out.println("查询数据失败");
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 字段名转换为属性名，首字母小写，下划线后一个单词大写开头，然后取消下划线
	 */
	public static String transColumn(String column) {
		String[] names = column.split("_");
		StringBuffer nameBuffer = new StringBuffer();
		for (int i = 0; i < names.length; i++) {
			String name = names[i].toLowerCase();
			if (i != 0) {
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
			}
			nameBuffer.append(name);
		}
		return nameBuffer.toString();
	}

	private static int count(String sql) throws SQLException {
		int count = 0;
		String countSql = "select count(*) " + sql.substring(sql.indexOf("from"));
		st = (Statement) conn.createStatement();
		ResultSet rs = st.executeQuery(countSql);
		while (rs.next()) {
			count = rs.getInt(1);
		}
		return count;
	}

	@Override
	public TableEntity showTable(String tableName) {
		conn = getConnection();

		TableEntity tableEntity = new TableEntity();
		try {
			String sql = "select * from user_tab_comments where table_name='" + tableName + "'";
			st = (Statement) conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				String comment = rs.getString("COMMENTS");
				tableEntity.setTableName(tableName);
				tableEntity.setComment(comment);
			}
			sql = "select num_rows from user_tables where table_name='" + tableName + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				int numRows = rs.getInt("NUM_ROWS");
				tableEntity.setTotalResult(numRows);
			}
			//conn.close();
		} catch (SQLException e) {
			System.out.println("查询数据失败");
			e.printStackTrace();
		}
		return tableEntity;
	}

}
